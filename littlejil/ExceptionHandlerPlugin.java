package psl.workflakes.littlejil;

import java.util.*;

import org.apache.log4j.Logger;
import org.cougaar.core.blackboard.IncrementalSubscription;
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
 * Notes: (newest to oldest)
 *  12/12/02: corner cases to take care of: when there's a post-requisiste and the task has a RESTART handler,
 *            we need to restart at the "parent task" level so that the pre-req gets ran.
 *            that will actually have to be a special case in LittleJILExpanderPlugin
 *
 *            Also, not really dealing with paralell substasks now... will have to somehow wait for the
 *            subtasks to be done before doing whatever the handler is.... that could be hard!
 *
 *            Finally, still getting some complains and warnings from Cougaar when tasks and/or
 *            expansions are removed... Need to look into how to do that "properly"
 *
 *  12/12/02 (later): now all four cases work for simple cases. Still a problem with the restart task:
 *            if the task had a pre-requisite, it will not be ran before the tasks's workflow. this is
 *            because of the "parent task" folding
 *
 *  12/12/02: complete and rethrow are working, at least for simple cases. however, cougaar complains
 *            when tasks are removed (in continue case) and when a task is completed but the parent tasks
 *            fails (complains of updating a non-existant allocation)
 *
 *
 * @author matias
 */

public class ExceptionHandlerPlugin extends ComponentPlugin {

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
        private final Logger logger = Logger.getLogger(ExceptionsPredicate.class);

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

        for (Enumeration exceptions = exceptionsSubscription.getAddedList();exceptions.hasMoreElements();) {

            LittleJILException exception = (LittleJILException) exceptions.nextElement();
            Task task = exception.getTask();
            logger.debug("got exception for task " + task.getVerb());

            // if this has been re-thrown from a child task, first remove any child steps that haven't run yet...
            // these would be any sequential steps that came after the task that threw the exception
            NewWorkflow newWorkflow = null;         // will be used if there's a continuation handler
            NewWorkflow originalWorkflow = null;    // will be used if we need to restart the task

            if (exception.getNestedException() != null) {
                Task failedTask = exception.getNestedException().getTask();
                Workflow workflow = failedTask.getWorkflow();

                // make a "backup" of this workflow, in case we need to restart
                // TODO: this could be a time-consuming operation... only do it if we need to
                originalWorkflow = copyWorkflow(workflow);
                originalWorkflow.setParentTask(task);

                newWorkflow = factory.newWorkflow();
                newWorkflow.setParentTask(task);

                removeDependentTasks(failedTask, workflow, newWorkflow);

                logger.debug("after removeDependentTasks, got workflow " + newWorkflow);

                blackboard.publishRemove(task.getPlanElement());
            }


            // look for any handlers defined for this task
            // unfortunately we have a special case with tasks that have pre or post-reqs, since they get
            // expanded into a "parent task" that does not correspond to a little-jil task
            Step step = stepsTable.get(task);
            Enumeration handlers = (step == null ? null : step.handlers());
            if (handlers != null && handlers.hasMoreElements()) {

                // TODO: look at multiple handlers (for now, just looking at first one)
                // TODO: match exception types
                HandlerBinding handlerBinding = (HandlerBinding) handlers.nextElement();
                logger.debug("found handler: " + handlerBinding);

                Task handlerTask = null;
                if (handlerBinding.getTarget() != null && handlerBinding.getTarget() instanceof Step) {
                    handlerTask = stepsTable.get((Step)handlerBinding.getTarget());
                    logger.debug("found handler task: " + handlerTask.getVerb());
                }


                if (handlerBinding.getControlFlow() == HandlerBinding.CONTINUE && newWorkflow != null) {

                    // newWorkflow will have the tasks that remained to be executed for this workflow
                    // if there is a handler task, we want to insert that first....

                    if (handlerTask != null) {
                        newWorkflow.addTask(handlerTask);
                        ((NewTask)handlerTask).setWorkflow(newWorkflow);

                        // TODO: assuming that the first task returned by workflow.getTasks() is indeed the first one in order
                        if (newWorkflow.getTasks().hasMoreElements()) {
                            Task firstTask = (Task) newWorkflow.getTasks().nextElement();

                            // add constraint so that the handler task gets done before this task
                            NewConstraint constraint = factory.newConstraint();
                            constraint.setConstrainingTask(handlerTask);
                            constraint.setConstrainingAspect(AspectType.END_TIME);

                            constraint.setConstrainedTask(firstTask);
                            constraint.setConstrainedAspect(AspectType.START_TIME);

                            constraint.setConstraintOrder(Constraint.BEFORE);

                            newWorkflow.addConstraint(constraint);
                        }
                    }

                    logger.debug("re-publishing expansion for continuing task");
                    NewExpansion expansion = (NewExpansion) factory.createExpansion(task.getPlan(), task, newWorkflow, null);

                    blackboard.publishAdd(expansion);
                }
                else if (handlerBinding.getControlFlow() == HandlerBinding.COMPLETE) {

                    // we already deleted the remaining tasks from this workflow, just add the handler task (if any)

                    NewWorkflow workflow = factory.newWorkflow();
                    workflow.setParentTask(task);

                    if (handlerTask != null) {
                        workflow.addTask(handlerTask);
                        ((NewTask)handlerTask).setWorkflow(workflow);
                        ((NewTask)handlerTask).setParentTask(task);
                    }
                    else {
                        // TODO: need to add a dummy task?? otherwise there's nothing to do for this task and it never completes
                    }

                    NewExpansion expansion = (NewExpansion) factory.createExpansion(task.getPlan(), task, workflow, null);

                    blackboard.publishAdd(task);
                    blackboard.publishAdd(expansion);
                }
                else if (handlerBinding.getControlFlow() == HandlerBinding.RESTART && originalWorkflow != null) {

                    // if there is a handler task, and it's not already in the workflow, we want to insert that first....
                    // (it could be already if this is the second time we are restarting the task)
                    if (handlerTask != null && !containsTask(originalWorkflow,handlerTask)) {

                        originalWorkflow.addTask(handlerTask);
                        ((NewTask)handlerTask).setWorkflow(originalWorkflow);

                        // TODO: I'm assuming that the first task returned by workflow.getTasks() is indeed the first one in order
                        if (originalWorkflow.getTasks().hasMoreElements()) {
                            Task firstTask = (Task) originalWorkflow.getTasks().nextElement();

                            // add constraint so that the handler task gets done before this task
                            NewConstraint constraint = factory.newConstraint();
                            constraint.setConstrainingTask(handlerTask);
                            constraint.setConstrainingAspect(AspectType.END_TIME);

                            constraint.setConstrainedTask(firstTask);
                            constraint.setConstrainedAspect(AspectType.START_TIME);

                            constraint.setConstraintOrder(Constraint.BEFORE);

                            originalWorkflow.addConstraint(constraint);
                        }
                    }

                    logger.debug("re-publishing original workflow for restarting task: " + originalWorkflow);
                    NewExpansion expansion = (NewExpansion) factory.createExpansion(task.getPlan(), task, originalWorkflow, null);

                    blackboard.publishChange(task);

                    blackboard.publishAdd(expansion);
                    logger.debug("published expansion " + expansion);

                }
                else /*if (handlerBinding.getControlFlow() == HandlerBinding.RETHROW)*/ {
                    // default action is to RETHROW the exception

                    NewWorkflow workflow = factory.newWorkflow();
                    workflow.setParentTask(task);

                    if (handlerTask != null) {
                        workflow.addTask(handlerTask);
                        ((NewTask)handlerTask).setWorkflow(workflow);
                        ((NewTask)handlerTask).setParentTask(task);

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
            else {
                // exception is not handled, re-throw to the parent (if the task has a parent (ie, if task has workflow))
                if (task.getWorkflow() != null) {
                    blackboard.publishAdd(new LittleJILException(exception));
                }

                logger.info(">>> task " + task.getVerb() + " FAILED <<<");
            }

        }

    }

    private boolean containsTask(Workflow workflow, Task task) {

        for (Enumeration tasks = workflow.getTasks();tasks.hasMoreElements();) {
            Task t = (Task) tasks.nextElement();
            if (t.getVerb().equals(task.getVerb())) {
                return true;
            }
        }

        return false;
    }

    /**
     * Copies all the tasks and constraints from the given workflow to a new one,
     * instantiating new tasks and constraints in the process
     * @param workflow
     * @return a copy of the given workflow
     */
    private NewWorkflow copyWorkflow(Workflow workflow) {

        NewWorkflow newWorkflow = factory.newWorkflow();

        Hashtable tasksTable = new Hashtable(); // need to keep track of new tasks, to set constraints properly

        for (Enumeration tasks = workflow.getTasks();tasks.hasMoreElements();) {
            Task task = (Task) tasks.nextElement();

            NewTask newTask = factory.newTask();
            newTask.setVerb(task.getVerb());

            ScoringFunction scorefcn = ScoringFunction.createStrictlyAtValue
                    (new AspectValue(AspectType.END_TIME, LittleJILExpanderPlugin.getNextEndTime()));
            Preference pref = factory.newPreference(AspectType.END_TIME, scorefcn);
            newTask.setPreference(pref);

            // need to get the step associated with the old task and associate it with this one too
            Step step = stepsTable.get(task);
            if (step != null) {
                stepsTable.put(newTask, step);
            }


            newTask.setWorkflow(newWorkflow);
            newWorkflow.addTask(newTask);
            tasksTable.put(task, newTask);
        }

        for (Enumeration constraints = workflow.getConstraints();constraints.hasMoreElements();) {
            Constraint constraint = (Constraint) constraints.nextElement();

            Task newConstrainedTask = (Task) tasksTable.get(constraint.getConstrainedTask());
            Task newConstrainingTask = (Task) tasksTable.get(constraint.getConstrainingTask());

            NewConstraint newConstraint = factory.newConstraint();
            newConstraint.setConstrainedTask(newConstrainedTask);
            newConstraint.setConstrainedAspect(constraint.getConstrainedAspect());

            newConstraint.setConstrainingTask(newConstrainingTask);
            newConstraint.setConstrainingAspect(constraint.getConstrainingAspect());

            newConstraint.setConstraintOrder(constraint.getConstraintOrder());

            newWorkflow.addConstraint(newConstraint);
        }


        return newWorkflow;
    }

    /**
     * Removes any tasks that depended on this task to complete, and the given task.
     * It also places the tasks it removes into a new workflow that can be executed to continue the parent task
     * @param task the task on which the tasks to be remove depend on
     * @param workflow the workflow where these tasks and constraints are
     * @param newWorkflow the workflow to place the removed tasks and constraints in
     */
    private void removeDependentTasks(Task task, Workflow workflow, NewWorkflow newWorkflow) {
        for (Enumeration e = workflow.getTaskConstraints(task);e.hasMoreElements();) {

            Constraint c = (Constraint) e.nextElement();
            if (c.getConstrainingTask() == task) {
                Task toRemove = c.getConstrainedTask();
                logger.debug("removing task " + toRemove.getVerb());

                // add this task and the constraints it imposes to the new workflow
                newWorkflow.addTask(toRemove);
                for (Enumeration constraints = workflow.getTaskConstraints(toRemove);constraints.hasMoreElements();) {
                    Constraint con = (Constraint) constraints.nextElement();
                    if (con.getConstrainingTask() == toRemove) {
                        newWorkflow.addConstraint(con);
                    }
                }

                removeDependentTasks(toRemove, workflow, newWorkflow);
                blackboard.publishRemove(toRemove);

                ((NewTask)toRemove).setWorkflow(newWorkflow);
                //blackboard.publishAdd(toRemove);
            }

        }

    }


}
































