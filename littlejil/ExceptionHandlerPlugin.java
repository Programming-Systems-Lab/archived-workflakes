package psl.workflakes.littlejil;

import java.util.*;

import org.apache.log4j.Logger;
import org.cougaar.core.blackboard.IncrementalSubscription;
import org.cougaar.core.blackboard.PrivilegedClaimant;
import org.cougaar.core.plugin.ComponentPlugin;
import org.cougaar.core.plugin.util.PluginHelper;
import org.cougaar.core.service.DomainService;
import org.cougaar.core.domain.RootFactory;
import org.cougaar.planning.ldm.plan.*;
import org.cougaar.util.UnaryPredicate;

import laser.littlejil.HandlerBinding;
import laser.littlejil.Step;

/**
 * This class handles exceptions thrown when executing a task.
 *
 * Exceptions are thrown by publishing a LittleJILException object, to which this plugin subscribes.
 * When an exception is received:
 *  - if it was thrown by a child task, any other child tasks that depended on that one are removed
 *      NOTE that this means that if a task has parallel sequencing, then the other substeps will still
 *      run (i.e., will not be removed). This seems to follow the LittleJIL language report v1.0
 *  - the exception handler is retrieved. at the moment only the first one is considered
 *  - the rest corresponds to the behavior of the exception handler (restarting, continuing, completing, rethrowing)
 *
 * Implementing the PrivilegedClaimant interface allows this plugin to remove expansion that were
 * posted by other plugins
 *
 * @author matias
 */
public class ExceptionHandlerPlugin extends ComponentPlugin implements PrivilegedClaimant {

    private static final Logger logger = Logger.getLogger(ExceptionHandlerPlugin.class);
    private DomainService domainService;
    private RootFactory factory;

    private IncrementalSubscription exceptionsSubscription;
    private IncrementalSubscription stepsTableSubscription;

    private LittleJILStepsTable stepsTable;

    /**
     * Used by the binding utility through reflection to set DomainService
     */
    public void setDomainService(DomainService domainService) {
        this.domainService = domainService;
        factory = domainService.getFactory();
    }

    private static class ExceptionsPredicate implements UnaryPredicate {
        public boolean execute(Object o) {
            return (o instanceof LittleJILException);
        }
    }

    private static class LittleJILStepsTablePredicate implements UnaryPredicate {
        public boolean execute(Object o) {
            return (o instanceof LittleJILStepsTable);
        }
    }

    public void setupSubscriptions() {

        exceptionsSubscription = (IncrementalSubscription) blackboard.subscribe(new ExceptionsPredicate());
        stepsTableSubscription = (IncrementalSubscription) blackboard.subscribe(new LittleJILStepsTablePredicate());

    }

    public void execute() {

        stepsTable = (LittleJILStepsTable) stepsTableSubscription.first();

        for (Enumeration exceptions = exceptionsSubscription.getAddedList(); exceptions.hasMoreElements();) {

            LittleJILException exception = (LittleJILException) exceptions.nextElement();
            Task task = exception.getTask();
            logger.debug("got exception for task " + task.getVerb());

            Task failedTask = null;
            if (exception.getNestedException() != null) {
                failedTask = exception.getNestedException().getTask();
            }


            // look for any handlers defined for this task
            // in general, tasks that have pre or post-reqs will have the handlers at the task level,
            // and not in the parent task (ie the stepsTable.get(parent task) will be null)
            // however, there is a 'corner case' with RESTART handlers, where we have to handler it at the
            // "parent task" level so that the pre-req will be executed.
            Step step = stepsTable.get(task);
            Enumeration handlers = (step == null ? null : step.handlers());
            if (handlers != null && handlers.hasMoreElements()) {

                // TODO: look at multiple handlers (for now, just looking at first one)
                // TODO: match exception types
                HandlerBinding handlerBinding = (HandlerBinding) handlers.nextElement();
                logger.debug("found handler of type " + handlerBinding.getControlFlow());

                Task handlerTask = null;
                if (handlerBinding.getTarget() != null && handlerBinding.getTarget() instanceof Step) {
                    handlerTask = stepsTable.get((Step) handlerBinding.getTarget());
                    logger.debug("found handler task: " + handlerTask.getVerb());
                }

                /////////////////////////////////////////////////////////////////////////////////////////////
                // CONTINUE handler
                if (handlerBinding.getControlFlow() == HandlerBinding.CONTINUE) {

                    if (failedTask == null) {
                        // trying to continue a leaf task -- not valid
                        logger.info("cannot continue a leaf task -- rethrowing exception");
                        if (task.getWorkflow() != null) {
                            blackboard.publishAdd(new LittleJILException(exception));
                        }

                        logger.info(">>> task " + task.getVerb() + " FAILED <<<");
                        continue;   // with for (Enumeration exceptions...)
                    }

                    // get a workflow for the remaining tasks
                    NewWorkflow remainingWorkflow = factory.newWorkflow();
                    remainingWorkflow.setParentTask(task);
                    extractDependentTasks(failedTask, failedTask.getWorkflow(), remainingWorkflow);

                    // now remove the original workflow for this task, since we'll post a new one
                    if (task.getPlanElement() != null) {
                        logger.debug("removing expansion for task " + task.getVerb());
                        blackboard.publishRemove(task.getPlanElement());
                    }

                    if (handlerTask != null) {
                        remainingWorkflow.addTask(handlerTask);
                        ((NewTask) handlerTask).setWorkflow(remainingWorkflow);

                        // TODO: assuming that the first task returned by workflow.getTasks() is indeed the first one in order
                        if (remainingWorkflow.getTasks().hasMoreElements()) {
                            Task firstTask = (Task) remainingWorkflow.getTasks().nextElement();

                            // add constraint so that the handler task gets done before this task
                            NewConstraint constraint = factory.newConstraint();
                            constraint.setConstrainingTask(handlerTask);
                            constraint.setConstrainingAspect(AspectType.END_TIME);

                            constraint.setConstrainedTask(firstTask);
                            constraint.setConstrainedAspect(AspectType.START_TIME);

                            constraint.setConstraintOrder(Constraint.BEFORE);

                            remainingWorkflow.addConstraint(constraint);
                        }
                    }

                    logger.debug("re-publishing expansion for continuing task");
                    NewExpansion expansion = (NewExpansion) factory.createExpansion(task.getPlan(), task, remainingWorkflow, null);

                    blackboard.publishAdd(expansion);

                }
                /////////////////////////////////////////////////////////////////////////////////////////////
                // COMPLETE handler
                else if (handlerBinding.getControlFlow() == HandlerBinding.COMPLETE) {

                    // first remove the existing expansion for this task, since we won't run any more tasks
                    if (task.getPlanElement() != null) {
                        logger.debug("removing expansion for task " + task.getVerb());
                        blackboard.publishRemove(task.getPlanElement());
                    }

                    // now just add the handler task (if any)
                    NewWorkflow workflow = factory.newWorkflow();
                    workflow.setParentTask(task);

                    if (handlerTask != null) {
                        workflow.addTask(handlerTask);
                        ((NewTask) handlerTask).setWorkflow(workflow);
                        ((NewTask) handlerTask).setParentTask(task);
                    } else {
                        // TODO: need to add a dummy task?? otherwise there's nothing to do for this task and it never completes
                    }

                    NewExpansion expansion = (NewExpansion) factory.createExpansion(task.getPlan(), task, workflow, null);

                    blackboard.publishAdd(task);
                    blackboard.publishAdd(expansion);
                }
                /////////////////////////////////////////////////////////////////////////////////////////////
                // RESTART handler
                else if (handlerBinding.getControlFlow() == HandlerBinding.RESTART) {

                    // first check that this came from a failed task below us
                    if (failedTask == null) {
                        // trying to restart a leaf task -- not valid
                        logger.info("cannot restart on a leaf task -- rethrowing exception");
                        if (task.getWorkflow() != null) {
                            blackboard.publishAdd(new LittleJILException(exception));
                        }

                        logger.info(">>> task " + task.getVerb() + " FAILED <<<");
                        continue;   // with for (Enumeration exceptions...)
                    }

                    if (step.getPrerequisite() != null && !task.getVerb().toString().endsWith("Parent")) {
                        // throw it up to the parent
                        logger.debug("deferring restart handler to parent");

                        blackboard.publishAdd(new LittleJILException(exception));
                        continue;   // with the for (Enumeration exceptions...)
                    }

                    // make a copy of this workflow, which we will post to restart the task
                    Expansion originalExpansion = (Expansion) task.getPlanElement();
                    Workflow originalWorkflow = originalExpansion.getWorkflow();
                    NewWorkflow copiedWorkflow = copyWorkflow(originalWorkflow);
                    copiedWorkflow.setParentTask(task);

                    // remove the old expansion - but have to manually remove tasks from workflow first
                    // otherwise, the tasks associated with this expansion will also be publishRemove()'d
                    clearWorkflow((NewWorkflow) originalWorkflow);
                    blackboard.publishRemove(originalExpansion);

                    // if there is a handler task, and it's not already in the workflow, we want to insert that first....
                    // (it could be already if this is the second time we are restarting the task)
                    if (handlerTask != null && !containsTask(copiedWorkflow, handlerTask)) {

                        copiedWorkflow.addTask(handlerTask);
                        ((NewTask) handlerTask).setWorkflow(copiedWorkflow);

                        // TODO: I'm assuming that the first task returned by workflow.getTasks() is indeed the first one in order
                        if (copiedWorkflow.getTasks().hasMoreElements()) {
                            Task firstTask = (Task) copiedWorkflow.getTasks().nextElement();

                            logger.debug("adding constraint so that " + handlerTask.getVerb() + " goes before " +
                                    firstTask.getVerb());


                            // add constraint so that the handler task gets done before this task
                            NewConstraint constraint = factory.newConstraint();
                            constraint.setConstrainingTask(handlerTask);
                            constraint.setConstrainingAspect(AspectType.END_TIME);

                            constraint.setConstrainedTask(firstTask);
                            constraint.setConstrainedAspect(AspectType.START_TIME);

                            constraint.setConstraintOrder(Constraint.BEFORE);

                            copiedWorkflow.addConstraint(constraint);
                        }
                    }

                    NewExpansion expansion = (NewExpansion) factory.createExpansion(task.getPlan(), task, copiedWorkflow, null);

                    blackboard.publishChange(task);

                    blackboard.publishAdd(expansion);

                }
                /////////////////////////////////////////////////////////////////////////////////////////////
                // RETHROW handler (default)
                else /*if (handlerBinding.getControlFlow() == HandlerBinding.RETHROW)*/ {

                    // remove the current expansion, if any
                    if (task.getPlanElement() != null) {
                        logger.debug("removing expansion for task " + task.getVerb());
                        blackboard.publishRemove(task.getPlanElement());
                    }

                    NewWorkflow workflow = factory.newWorkflow();
                    workflow.setParentTask(task);

                    if (handlerTask != null) {
                        workflow.addTask(handlerTask);
                        ((NewTask) handlerTask).setWorkflow(workflow);
                        ((NewTask) handlerTask).setParentTask(task);

                        NewExpansion expansion = (NewExpansion) factory.createExpansion(task.getPlan(), task, workflow, null);

                        blackboard.publishAdd(task);
                        blackboard.publishAdd(expansion);
                    }

                    logger.info("rethrowing exception");
                    // and re-throw the exception, too
                    if (task.getWorkflow() != null) {
                        blackboard.publishAdd(new LittleJILException(exception));
                    }

                    logger.info(">>> task " + task.getVerb() + " FAILED <<<");
                }

            }
            /////////////////////////////////////////////////////////////////////////////////////////////
            // no handler found
            else {

                // remove the current expansion, if any
                if (task.getPlanElement() != null) {
                    logger.debug("removing expansion for task " + task.getVerb());
                    blackboard.publishRemove(task.getPlanElement());
                }

                // exception is not handled, re-throw to the parent (if the task has a parent (ie, if task has workflow))
                if (task.getWorkflow() != null) {
                    blackboard.publishAdd(new LittleJILException(exception));
                }

                logger.info(">>> task " + task.getVerb() + " FAILED <<<");
            }

        }

    }

    private boolean containsTask(Workflow workflow, Task task) {

        for (Enumeration tasks = workflow.getTasks(); tasks.hasMoreElements();) {
            Task t = (Task) tasks.nextElement();
            if (t.getVerb().equals(task.getVerb())) {
                return true;
            }
        }

        return false;
    }


    private NewWorkflow copyWorkflow(Workflow workflow) {

        NewWorkflow newWorkflow = factory.newWorkflow();
        newWorkflow.setParentTask(workflow.getParentTask());

        for (Enumeration tasks = workflow.getTasks(); tasks.hasMoreElements();) {
            Task task = (Task) tasks.nextElement();

            // remove the allocation from this task... otherwise it won't be re-published by the
            // task expander
            logger.debug("copying task " + task.getVerb() + ",planelement=" + task.getPlanElement());
            if (task.getPlanElement() != null && task.getPlanElement() instanceof Allocation) {
                logger.debug("removing allocation for task " + task.getVerb());
                blackboard.publishRemove(task.getPlanElement());
            } else if (task.getPlanElement() != null && task.getPlanElement() instanceof Expansion) {
                /*task.getPlanElement().setObservedResult(null);
                task.getPlanElement().setEstimatedResult(null);
                ((PlanElementForAssessor)task.getPlanElement()).setReceivedResult(null);*/
                // copy this tasks's workflow, publish a new expansion
                Expansion expansion = (Expansion) task.getPlanElement();

                NewWorkflow originalWorkflow = (NewWorkflow) expansion.getWorkflow();
                Workflow subWorkflow = copyWorkflow(originalWorkflow);

                // remove expansion (have to remove tasks from workflow first)
                clearWorkflow(originalWorkflow);
                blackboard.publishRemove(expansion);

                logger.debug("re-publishing expansion for task " + task.getVerb());
                Expansion newExpansion = factory.createExpansion(task.getPlan(), task, subWorkflow, null);
                blackboard.publishChange(task);
                blackboard.publishAdd(newExpansion);

            }

            // if this task was constrained and it ran, it's start_time preference will be set.
            // in that case, we need to remove that preference
            if (task.getPreference(AspectType.START_TIME) != null && task.getPreference(AspectType.END_TIME) != null) {
                logger.debug("resetting preferences of task " + task.getVerb());
                Vector v = new Vector();
                v.add(task.getPreference(AspectType.END_TIME));
                ((NewTask) task).setPreferences(v.elements());
            }

            ((NewTask) task).setVerb(new Verb(task.getVerb().toString() + "2"));
            ((NewTask) task).setWorkflow(newWorkflow);
            newWorkflow.addTask(task);

        }

        // we need to copy the constraints, because the ones in the workflow might already be satisified
        for (Enumeration constraints = workflow.getConstraints(); constraints.hasMoreElements();) {
            Constraint constraint = (Constraint) constraints.nextElement();

            NewConstraint newConstraint = factory.newConstraint();
            newConstraint.setConstrainingTask(constraint.getConstrainingTask());
            newConstraint.setConstrainingAspect(AspectType.END_TIME);

            newConstraint.setConstrainedTask(constraint.getConstrainedTask());
            newConstraint.setConstrainedAspect(AspectType.START_TIME);

            newConstraint.setConstraintOrder(Constraint.BEFORE);

            newWorkflow.addConstraint(newConstraint);
        }

        return newWorkflow;
    }

    /**
     * Empties this workflow of all the tasks and constraints, in preparation
     * for removing the expansion of this workflow.
     * @param originalWorkflow
     */
    private static void clearWorkflow(NewWorkflow originalWorkflow) {

        // we use a vector to store the tasks, and then remove them. otherwise,
        // as we remove them we modify the source of the enumerator, with unpredicatble results

        Vector v = new Vector();
        for (Enumeration e = originalWorkflow.getTasks(); e.hasMoreElements();) {
            v.add(e.nextElement());
        }

        for (Enumeration e = originalWorkflow.getConstraints(); e.hasMoreElements();) {
            v.add(e.nextElement());
        }


        for (Enumeration e = v.elements(); e.hasMoreElements();) {
            Object o = e.nextElement();
            if (o instanceof Task) {
                originalWorkflow.removeTask((Task) o);
            } else {
                originalWorkflow.removeConstraint((Constraint) o);
            }

        }

    }

    /**
     * Removes any tasks that depended on this task to complete, and the given task.
     * It also places the tasks it removes into a new workflow that can be executed to continue the parent task
     * @param task the task on which the tasks to be remove depend on
     * @param workflow the workflow where these tasks and constraints are
     * @param newWorkflow the workflow to place the removed tasks and constraints in
     */
    private void extractDependentTasks(Task task, Workflow workflow, NewWorkflow newWorkflow) {
        for (Enumeration e = workflow.getTaskConstraints(task); e.hasMoreElements();) {

            Constraint c = (Constraint) e.nextElement();
            if (c.getConstrainingTask() == task) {
                Task toRemove = c.getConstrainedTask();
                logger.debug("extracting task " + toRemove.getVerb());

                // add this task and the constraints it imposes to the new workflow
                newWorkflow.addTask(toRemove);
                for (Enumeration constraints = workflow.getTaskConstraints(toRemove); constraints.hasMoreElements();) {
                    Constraint con = (Constraint) constraints.nextElement();
                    if (con.getConstrainingTask() == toRemove) {
                        newWorkflow.addConstraint(con);
                    }
                }

                extractDependentTasks(toRemove, workflow, newWorkflow);
                ((NewWorkflow) workflow).removeTask(toRemove);

                ((NewTask) toRemove).setWorkflow(newWorkflow);
            }

        }

    }


}

