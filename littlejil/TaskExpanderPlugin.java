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

        /*for (Enumeration expansions = expansionsSubscription.getAddedList(); expansions.hasMoreElements();) {
            Expansion expansion = (Expansion) expansions.nextElement();
            //logger.debug("got new expansion " + expansion);

            processExpansion(expansion, true);

        }*/

        // look for changed expansions that may be done
        for (Enumeration expansions = expansionsSubscription.getChangedList(); expansions.hasMoreElements();) {
            Expansion expansion = (Expansion) expansions.nextElement();

            //logger.debug("got changed expansion: " + expansion);

            if (expansion.getEstimatedResult() != null) {
                logger.info(">>>> parent task " + expansion.getTask().getVerb() + " is done <<<<");
            }/* else if (expansion.getReportedResult() == null) {
                processExpansion(expansion, false);
            }*/

        }

        // check all expansions for satisfied constraints
        for (Enumeration expansions = expansionsSubscription.elements(); expansions.hasMoreElements();) {

            Expansion expansion = (Expansion) expansions.nextElement();
            Workflow wf = expansion.getWorkflow();

            processExpansion(expansion, true);

            Constraint constraint;
            while ((constraint = wf.getNextPendingConstraint()) != null) {
                logger.info("found a pending constraint: " + PluginUtil.constraintToString(constraint));
                ConstraintEvent ced = constraint.getConstrainedEventObject();
                if (ced instanceof SettableConstraintEvent) {
                    ((SettableConstraintEvent) ced).setValue(constraint.computeValidConstrainedValue());

                    Task constrainedTask = constraint.getConstrainedTask();

                    // we've resolved this constraint, but before we continue, we should check if
                    // there aren't more constraints for this task that have not been resolved
                    if (hasMoreConstraints(wf, constrainedTask, false)) {
                        logger.debug("task " + constrainedTask.getVerb() + " has more constraints");
                        continue;
                    }

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

    /**
     * Used to see if there are more unresolved constraints on this particular task
     * @param workflow the workflow of which this task is part of
     * @param constrainedTask the task to check
     * @return true if there are any unresolved constraints that constrain the given task
     */
    private boolean hasMoreConstraints(Workflow workflow, Task constrainedTask, boolean checkParent) {

        if (constrainedTask.getWorkflow() == null) {
            return false;
        }

        boolean constrained = false;
        for (Enumeration e = workflow.getTaskConstraints(constrainedTask);e.hasMoreElements();) {
            Constraint constraint = (Constraint) e.nextElement();

            try {
                if (constraint.getConstrainedTask() == constrainedTask &&
                        /*constraint.getConstrainedEventObject().isConstraining() &&*/
                        Double.isNaN(constraint.getConstrainedEventObject().getResultValue())) {
                    constrained = true;
                    break;
                }
            } catch (Exception e1) {
                continue;
            }
        }

        if (!constrained && checkParent) {
            Task parent = constrainedTask.getWorkflow().getParentTask();
            if (parent != null) {
                //logger.debug("[" + constrainedTask.getVerb() + ": checking parent for constraints]");
                constrained = hasMoreConstraints(parent.getWorkflow(), parent, true);
            }
        }

        return constrained;

    }

    private void processExpansion(Expansion expansion, boolean checkParentConstraints) {
        // first check that the parent task for this expansion can be executed
        Task parentTask = expansion.getTask();
        Workflow parentWorkflow = parentTask.getWorkflow();


        //logger.debug("expansion is for task " + parentTask + ", which has workflow " + parentWorkflow);
        /*if (!checkParentConstraints)
            logger.debug("[not checking parent task constraints]");
*/
        /*if (parentWorkflow != null && checkParentConstraints) {

            Enumeration constraints = parentWorkflow.getTaskConstraints(parentTask);
            while (constraints.hasMoreElements()) {
                Constraint c = (Constraint) constraints.nextElement();
                if (c.getConstrainedTask() == parentTask) {
                    logger.debug("task has existing constraints, not expanding");
                    return;
                }
            }
        }*/
        if (parentWorkflow != null) {
            if (hasMoreConstraints(parentWorkflow, parentTask, true)) {
                logger.debug("parent task has existing constraints, not expanding");
                return;
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
                //logger.debug("task " + task.getVerb() + " is a leaf task");

                // have to check each constraint, because this task may just be constraining
                // some other task. if we find that this task is indeed being constrained, then
                // we stop and don't publish it yet
                boolean constrained = false;
                for (Enumeration constraints = workflow.getTaskConstraints(task); constraints.hasMoreElements();) {
                    Constraint c = (Constraint) constraints.nextElement();
                    //logger.debug("  found a constraint: " + PluginUtil.constraintToString(c));

                    if (c.getConstrainedTask() == task) {
                        constrained = true;
                        //logger.debug("  constraint exists, cannot publish this task at this time");
                        break;
                    }
                }
                if (!constrained) {
                    logger.debug("task " + task.getVerb() + " is not constrained, publishing it");
                    blackboard.publishAdd(task);
                }


            }
        }
    }


}
































