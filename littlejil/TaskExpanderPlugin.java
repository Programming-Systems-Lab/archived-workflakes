package psl.workflakes.littlejil;

import org.apache.log4j.Logger;
import org.cougaar.core.blackboard.IncrementalSubscription;
import org.cougaar.core.blackboard.Subscription;
import org.cougaar.core.blackboard.CollectionSubscription;
import org.cougaar.core.plugin.ComponentPlugin;
import org.cougaar.core.plugin.util.PluginHelper;
import org.cougaar.core.service.BlackboardService;
import org.cougaar.core.service.DomainService;
import org.cougaar.core.domain.RootFactory;
import org.cougaar.util.UnaryPredicate;
import org.cougaar.planning.ldm.plan.*;
import psl.workflakes.littlejil.xmlschema.*;
import psl.workflakes.littlejil.xmlschema.types.*;

import java.util.*;

/**
 * This class should get tasks posted on a blackboard and expand their workflows as necessary
 * @author matias
 */

public class TaskExpanderPlugin extends ComponentPlugin {

    private static final Logger logger = Logger.getLogger(TaskExpanderPlugin.class);
    private DomainService domainService;

    private IncrementalSubscription expansionsSubscription;
    private IncrementalSubscription tasksSubscription;

    /**
     * Used by the binding utility through reflection to set my DomainService
     */
    public void setDomainService(DomainService domainService) {
        this.domainService = domainService;
    }

    private static class ExpansionsPredicate implements UnaryPredicate {
        private final Logger logger = Logger.getLogger(ExpansionsPredicate.class);

        public boolean execute(Object o) {
            return (o instanceof Expansion);
        }
    }

    /**
     * This predicate only matches tasks that have a workflow (ie that are expandable)
     */
    private static class ExpandableTasksPredicate implements UnaryPredicate {
        public boolean execute(Object o) {
            boolean ret = false;
            if (o instanceof Task) {
                Task task = (Task) o;
                ret = (task.getPlanElement() != null);
            }
            return ret;
        }
    }

    public void setupSubscriptions() {

        // now set up the subscription to get diagrams
        expansionsSubscription = (IncrementalSubscription) blackboard.subscribe(new ExpansionsPredicate());
        tasksSubscription = (IncrementalSubscription) blackboard.subscribe(new ExpandableTasksPredicate());


    }

    Vector exps = new Vector();
    public void execute() {

        /*for (Enumeration tasks = tasksSubscriptions.getAddedList(); tasks.hasMoreElements();) {
            Task task = (Task) tasks.nextElement();
            logger.debug("posting expansion for task " + task.getVerb());

            // post the expansion for this task
            //Expansion expansion = factory.createExpansion(task.getPlan(), task, task.getWorkflow(), null);
            blackboard.publishAdd(task.getPlanElement());

        }*/

        for (Enumeration expansions = expansionsSubscription.getAddedList(); expansions.hasMoreElements();) {
            Expansion expansion = (Expansion) expansions.nextElement();
            logger.debug("got new expansion " + expansion);

            processExpansion(expansion, true);

        }

        // look for changed expansions where the constraints were satisfied...
        for (Enumeration expansions = expansionsSubscription.getChangedList(); expansions.hasMoreElements();) {
            Expansion expansion = (Expansion) expansions.nextElement();

            logger.debug("got changed expansion: " + expansion);

            if (expansion.getEstimatedResult() != null) {
                logger.info(">>>> task " + expansion.getTask().getVerb() + " is done <<<<");
            }
            else if (expansion.getReportedResult() == null) {
                processExpansion(expansion, false);
            }

        }

        for (Enumeration expansions = expansionsSubscription.elements(); expansions.hasMoreElements();) {

            Expansion expansion = (Expansion) expansions.nextElement();
            Workflow wf = expansion.getWorkflow();
            Constraint constraint = wf.getNextPendingConstraint();

            if (constraint != null) {
                logger.info("found a pending constraint: " + PluginUtil.constraintToString(constraint));
                ConstraintEvent ced = constraint.getConstrainedEventObject();
                if (ced instanceof SettableConstraintEvent)
                {
                    ((SettableConstraintEvent)ced).setValue(constraint.computeValidConstrainedValue());

                    Task constrainedTask = constraint.getConstrainedTask();

                    // there could still have other constraints pending! (like prerequisistes)
                    // FIX: this seems to work now, but when the second constraint is resolved we don't get it

                    boolean remainingConstraints = false;
                    for (Enumeration constraints = wf.getTaskConstraints(constrainedTask);constraints.hasMoreElements();) {
                        Constraint c = (Constraint) constraints.nextElement();
                        if (c.getConstrainedTask() == constrainedTask) {
                            final double resultValue = c.getConstrainedEventObject().getResultValue();
                            logger.debug("resultvalue=" + resultValue + ", value=" + c.getConstrainedEventObject().getValue());
                            if (Double.isNaN(resultValue)) {
                                logger.debug("one constraint satisfied, but there are still others not satisfied.");
                                remainingConstraints = true;
                                break;
                            }
                        }
                    }

                    if (remainingConstraints)
                        continue;

                    logger.info("publishing task " + constrainedTask.getVerb());
                    blackboard.publishAdd(constrainedTask);

                    PlanElement planElement = constrainedTask.getPlanElement();
                    if (planElement != null && planElement instanceof Expansion) {
                        logger.debug("publishing tasks's expansion:" + planElement);
                        blackboard.publishChange(planElement);
                    }
                }
            }
        }

        PluginHelper.updateAllocationResult(expansionsSubscription);

    }

    private void processExpansion(Expansion expansion, boolean checkParentConstraints) {
        // first check that the parent task for this expansion can be executed
        Task parentTask = expansion.getTask();
        Workflow parentWorkflow = parentTask.getWorkflow();


        //logger.debug("expansion is for task " + parentTask + ", which has workflow " + parentWorkflow);
        if (!checkParentConstraints)
            logger.debug("[not checking parent task constraints]");

        if (parentWorkflow != null && checkParentConstraints) {

            Enumeration constraints = parentWorkflow.getTaskConstraints(parentTask);
            while (constraints.hasMoreElements()) {
                Constraint c = (Constraint) constraints.nextElement();
                if (c.getConstrainedTask() == parentTask) {
                    logger.debug("task has existing constraints, not expanding");
                    return;
                }
            }
        }

        Workflow workflow = expansion.getWorkflow();
        if (!workflow.getTasks().hasMoreElements()) {
            logger.info("expansion " + expansion + " has no more tasks!");
            return;
        }

        for (Enumeration subtasks = workflow.getTasks(); subtasks.hasMoreElements();) {
            Task task = (Task) subtasks.nextElement();
            //logger.debug("looking at task " + task.getVerb());

            if (task.getPlanElement() == null) {
                logger.debug("task " + task.getVerb() + " is a leaf task");

                // have to check each constraint, because this task may just be constraining
                // some other task. if we find that this task is indeed being constrained, then
                // we stop and don't publish it yet
                boolean constrained = false;
                for (Enumeration constraints = workflow.getTaskConstraints(task); constraints.hasMoreElements();) {
                    Constraint c = (Constraint) constraints.nextElement();
                    logger.debug("  found a constraint: " + PluginUtil.constraintToString(c));

                    if (c.getConstrainedTask() == task) {
                        constrained = true;
                        logger.debug("  constraint exists, cannot publish this task at this time");
                        break;
                    }
                }
                if (!constrained) {
                    logger.debug("  this task is not constrained, publishing it");
                    blackboard.publishAdd(task);
                }


            }
        }
    }


}
































