package psl.workflakes.littlejil;

import java.util.*;

import org.apache.log4j.Logger;
import org.cougaar.core.blackboard.IncrementalSubscription;
import org.cougaar.core.plugin.ComponentPlugin;
import org.cougaar.core.plugin.util.PluginHelper;
import org.cougaar.core.service.DomainService;
import org.cougaar.planning.ldm.plan.*;
import org.cougaar.util.UnaryPredicate;
import laser.littlejil.Step;
import laser.littlejil.ParameterBinding;
import laser.littlejil.ParameterDeclaration;

/**
 * This plugin is in charge of processing the Task expansions that the
 * <code>LittleJILExpanderPlugin</code> posts. It subscribes to expandable (i.e.
 * non-leaf) tasks. When executed, it does two main things: for all the
 * expansions, it checks if a constraint has been satisfied (for example, after
 * the first of a sequential set of leaf tasks has completed), and if so it
 * posts the next available leaf task for that expansion. It also checks to see
 * if expansions that have not been started yet have any more constraints
 * imposed on them (for example, a parent task that comes sequentially after
 * another parent task). If there are no more constraints, it publishes the
 * child tasks of the expansion as necessary.
 *
 *  The <code>TaskExpanderPlugin</code> also sets the in and out parameters of
 *  leaf tasks as necessary, and provides methods for listeners to subscribe to
 *  events such as "task started" or "task completed".
 *
 * @author matias
 */

public class TaskExpanderPlugin extends ComponentPlugin {

    private static final Logger logger = Logger.getLogger(TaskExpanderPlugin.class);

    private static Vector listeners = new Vector();

    private DomainService domainService;

    private IncrementalSubscription expansionsSubscription;
    private IncrementalSubscription tasksSubscription;
    private IncrementalSubscription littleJILStepsTableSubscription;

    private LittleJILStepsTable stepsTable; // used to keep a mapping of task->step

    /**
     * Used by the binding utility through reflection to set my DomainService
     */
    public void setDomainService(DomainService domainService) {
        this.domainService = domainService;
    }

    private static class ExpansionsPredicate implements UnaryPredicate {
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

    private static class LittleJILStepsTablePredicate implements UnaryPredicate {
        public boolean execute(Object o) {
            return (o instanceof LittleJILStepsTable);
        }
    }

    public void setupSubscriptions() {

        expansionsSubscription = (IncrementalSubscription) blackboard.subscribe(new ExpansionsPredicate());
        tasksSubscription = (IncrementalSubscription) blackboard.subscribe(new ExpandableTasksPredicate());
        littleJILStepsTableSubscription = (IncrementalSubscription) blackboard.subscribe(new LittleJILStepsTablePredicate());

    }

    public static void addListener(Listener listener) {
        listeners.add(listener);
    }

    public static void removeListener(Listener listener) {
        listeners.remove(listener);
    }

    public void execute() {

        // assumming that the LittleJILExpanderPlugin has already published a stepsTable
        stepsTable = (LittleJILStepsTable) littleJILStepsTableSubscription.first();

        // look for changed expansions that may be done
        for (Enumeration expansions = expansionsSubscription.getChangedList(); expansions.hasMoreElements();) {
            Expansion expansion = (Expansion) expansions.nextElement();

            if (expansion.getEstimatedResult() != null) {
                logger.info(">>>> parent task " + expansion.getTask().getVerb() + " done <<<<");

                // copy out any out parameters of this task
                LittleJILStepsTable.Entry entry = stepsTable.getEntry(expansion.getTask());
                if (entry != null) {
                    Collection paramBindings = entry.getParameterBindings();

                    for (Iterator i = paramBindings.iterator(); i.hasNext();) {
                        ParameterBinding binding = (ParameterBinding) i.next();
                        ParameterDeclaration childDeclaration = binding.getDeclarationInChild();
                        ParameterDeclaration parentDeclaration = binding.getDeclarationInParent();

                        if (childDeclaration.getMode() == ParameterDeclaration.COPY_OUT ||
                                childDeclaration.getMode() == ParameterDeclaration.COPY_IN_AND_OUT) {

                            if (childDeclaration.getParameterValue() == null) {
                                logger.warn("value is null for out param " + childDeclaration.getName());
                            } else {
                                logger.info("copying out param " + childDeclaration.getName() + ", value=" +
                                        childDeclaration.getParameterValue());
                                parentDeclaration.setParameterValue(childDeclaration.getParameterValue());
                            }

                        }
                    }
                }

                // NOTE: should we remove the used expansions here? - could make things more efficient,
                // but would lose history? (would also require implementing PrivilegedClaimant)
                //blackboard.publishRemove(expansion);

                // notify listeners of task ending
                for (Iterator iterator = listeners.iterator(); iterator.hasNext();) {
                    Listener listener = (Listener) iterator.next();
                    listener.taskFinished(expansion.getTask().getVerb().toString());
                }

            }

        }

        // check all expansions for satisfied constraints
        for (Enumeration expansions = expansionsSubscription.elements(); expansions.hasMoreElements();) {

            Expansion expansion = (Expansion) expansions.nextElement();
            Workflow workflow = expansion.getWorkflow();

            processExpansion(expansion);

            // check for pending constraints
            Constraint constraint;
            while ((constraint = workflow.getNextPendingConstraint()) != null) {
                logger.debug("found a pending constraint: " + PluginUtil.constraintToString(constraint));
                ConstraintEvent ced = constraint.getConstrainedEventObject();
                if (ced instanceof SettableConstraintEvent) {
                    ((SettableConstraintEvent) ced).setValue(constraint.computeValidConstrainedValue());

                    Task constrainedTask = constraint.getConstrainedTask();


                    // we've resolved this constraint, but before we continue, we should check if
                    // there aren't more constraints for this task that have not been resolved
                    if (hasMoreConstraints(workflow, constrainedTask, false)) {
                        logger.debug("task " + constrainedTask.getVerb() + " has more constraints");
                        continue;
                    }
                    else {
                        publishTask(constrainedTask);
                    }
                }
            }
        }

        PluginHelper.updateAllocationResult(expansionsSubscription);

    }


    /**
     * Looks for tasks in the expansion that are not constrainted, and publishes them
     * @param expansion
     */
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
                    publishTask(task);
                }


            }
        }
    }

    private void publishTask(Task task) {

        // if this a task is a CHOICE task, choose one of the substeps and ask the
        // LittleJILExpanderPlugin to expand it
        if (task.getAnnotation() != null && task.getAnnotation() instanceof ChoiceAnnotation) {

            logger.debug("task " + task.getVerb() + " is a CHOICE task");
            Step step = ((ChoiceAnnotation) task.getAnnotation()).chooseSubstep();

            MakeTaskRequest request = new MakeTaskRequest(task, step);
            blackboard.publishAdd(request);

        } else {
            logger.debug("task " + task.getVerb() + " is not constrained, publishing it");

            setInParams(task);
            blackboard.publishAdd(task);

            PlanElement planElement = task.getPlanElement();
            if (planElement != null && planElement instanceof Expansion) {
                //logger.debug("publishing tasks's expansion:" + planElement);
                blackboard.publishChange(planElement);

                // notify listeners of task being published
                for (Iterator iterator = listeners.iterator(); iterator.hasNext();) {
                    Listener listener = (Listener) iterator.next();
                    listener.taskPublished(task.getVerb().toString());
                }
            } else {

                // notify listeners of task being published
                for (Iterator iterator = listeners.iterator(); iterator.hasNext();) {
                    Listener listener = (Listener) iterator.next();
                    listener.leafTaskPublished(task.getVerb().toString());
                }
            }
        }
    }

    /**
     * Gets this tasks's LittleJIL parameter bindings and sets the ones that are COPY_IN
     * to the value of the parent task's parameter
     * @param task
     */
    private void setInParams(Task task) {
        LittleJILStepsTable.Entry entry = stepsTable.getEntry(task);

        if (entry != null) {
            Collection paramBindings = entry.getParameterBindings();

            for (Iterator i = paramBindings.iterator(); i.hasNext();) {
                ParameterBinding binding = (ParameterBinding) i.next();
                if (binding.getBindingMode() == ParameterBinding.COPY_IN ||
                        binding.getBindingMode() == ParameterBinding.COPY_IN_AND_OUT) {

                    ParameterDeclaration childDeclaration = binding.getDeclarationInChild();
                    ParameterDeclaration parentDeclaration = binding.getDeclarationInParent();

                    assert(parentDeclaration.getParameterValue() != null);

                    if (parentDeclaration.getParameterValue() != childDeclaration.getParameterValue()) {
                        childDeclaration.setParameterValue(parentDeclaration.getParameterValue());

                        logger.debug("in param " + childDeclaration.getName() +
                                " set to " + childDeclaration.getParameterValue());
                    }
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


    /**
     * This class can be subclassed and used to subscribe to events from the TaskExpanderPlugin.
     *
     */
    public static abstract class Listener {

        public abstract void taskPublished(String taskName);

        public abstract void leafTaskPublished(String taskName);

        public abstract void taskFinished(String taskName);

    }

}
































