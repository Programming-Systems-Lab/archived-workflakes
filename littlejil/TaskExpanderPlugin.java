package psl.workflakes.littlejil;

import java.util.Enumeration;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.cougaar.core.blackboard.IncrementalSubscription;
import org.cougaar.core.plugin.ComponentPlugin;
import org.cougaar.core.plugin.util.PluginHelper;
import org.cougaar.core.service.DomainService;
import org.cougaar.planning.ldm.plan.*;
import org.cougaar.util.UnaryPredicate;

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

    public void execute() {

        // look for changed expansions that may be done
        for (Enumeration expansions = expansionsSubscription.getChangedList(); expansions.hasMoreElements();) {
            Expansion expansion = (Expansion) expansions.nextElement();

            if (expansion.getEstimatedResult() != null) {
                logger.info(">>>> parent task " + expansion.getTask().getVerb() + " done <<<<");

            }

        }

        // check all expansions for satisfied constraints
        for (Enumeration expansions = expansionsSubscription.elements(); expansions.hasMoreElements();) {

            Expansion expansion = (Expansion) expansions.nextElement();
            Workflow wf = expansion.getWorkflow();

            processExpansion(expansion);

            Constraint constraint;
            while ((constraint = wf.getNextPendingConstraint()) != null) {
                logger.debug("found a pending constraint: " + PluginUtil.constraintToString(constraint));
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
                        //logger.debug("publishing tasks's expansion:" + planElement);
                        blackboard.publishChange(planElement);
                    }
                }
            }
        }

        PluginHelper.updateAllocationResult(expansionsSubscription);

    }


    private void processExpansion(Expansion expansion) {
        // first check that the parent task for this expansion can be executed
        Task parentTask = expansion.getTask();
        Workflow parentWorkflow = parentTask.getWorkflow();

        //logger.debug("processing expansion " + expansion);

        if (parentWorkflow != null) {
            if (hasMoreConstraints(parentWorkflow, parentTask, true)) {
                //logger.debug("parent task " + parentTask.getVerb() + " has existing constraints, not expanding");
                return;
            }
        }

        Workflow workflow = expansion.getWorkflow();
        if (!workflow.getTasks().hasMoreElements()) {
            //logger.info("expansion " + expansion + " has no more tasks!");
            return;
        }

        //logger.debug("looking for leaf tasks in expansion " + expansion.getUID() + " of task " + expansion.getTask().getVerb());
        for (Enumeration subtasks = workflow.getTasks(); subtasks.hasMoreElements();) {
            Task task = (Task) subtasks.nextElement();
            //logger.debug("looking at task " + task.getVerb());

            if (task.getPlanElement() == null) {
                // task is a leaf task and has not been yet allocated

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
        for (Enumeration e = workflow.getTaskConstraints(constrainedTask); e.hasMoreElements();) {
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

}
































