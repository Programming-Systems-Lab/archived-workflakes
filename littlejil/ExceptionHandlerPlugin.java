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
 * This class handles exceptions thrown when executing a task
 * @author matias
 */

public class ExceptionHandlerPlugin extends ComponentPlugin {

    private static final Logger logger = Logger.getLogger(ExceptionHandlerPlugin.class);
    private DomainService domainService;
    private RootFactory factory;

    private IncrementalSubscription exceptionsSubscription;
    private IncrementalSubscription stepsTableSubscription;

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

        LittleJILStepsTable stepsTable = (LittleJILStepsTable) stepsTableSubscription.first();

        for (Enumeration exceptions = exceptionsSubscription.getAddedList();exceptions.hasMoreElements();) {

            LittleJILException exception = (LittleJILException) exceptions.nextElement();
            Task task = exception.getTask();
            logger.debug("got exception for task " + task.getVerb());

            // if this has been re-thrown from a child task, first remove any child steps that haven't run yet...
            // these would be any sequential steps that came after the task that threw the exception
            if (exception.getNestedException() != null) {
                Task failedTask = exception.getNestedException().getTask();
                Workflow workflow = failedTask.getWorkflow();

                removeDependentTasks(failedTask, workflow);

                // remove the current Expansion for this task. we will create a new one below
                // if we need to continue, complete, or restart
                blackboard.publishRemove(task.getPlanElement());
            }


            // look for any handlers defined for this task
            Step step = stepsTable.get(task);
            Enumeration handlers = step.handlers();
            if (handlers.hasMoreElements()) {

                // TODO: look at multiple handlers (for now, just looking at first one)
                // TODO: match exception types
                HandlerBinding handlerBinding = (HandlerBinding) handlers.nextElement();
                logger.debug("found handler: " + handlerBinding);

                // TODO: run handler step!!
                Task handlerTask = null;
                if (handlerBinding.getTarget() != null && handlerBinding.getTarget() instanceof Step) {
                    handlerTask = stepsTable.get((Step)handlerBinding.getTarget());
                    logger.debug("found handler task: " + handlerTask);
                }


                // TODO: for now, only handling continue and re-throw
                if (handlerBinding.getControlFlow() == HandlerBinding.CONTINUE) {

                }
                else if (handlerBinding.getControlFlow() == HandlerBinding.COMPLETE) {
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
                    logger.debug("published new expansion: " + expansion);
                }

            }
            else {
                // exception is not handled, re-throw to the parent

                if (task.getWorkflow() != null) {
                    blackboard.publishAdd(new LittleJILException(exception));
                }

                logger.info(">>> task " + task.getVerb() + " FAILED <<<");
            }

        }

    }

    /**
     * Removes any tasks that depended on this task to complete, and the given task
     * @param task
     * @param workflow
     */
    private void removeDependentTasks(Task task, Workflow workflow) {
        for (Enumeration e = workflow.getTaskConstraints(task);e.hasMoreElements();) {

            Constraint c = (Constraint) e.nextElement();
            if (c.getConstrainingTask() == task) {
                Task toRemove = c.getConstrainedTask();
                removeDependentTasks(toRemove, workflow);
                logger.debug("removing task " + toRemove);
                ((NewTask)toRemove).setWorkflow(null);
                blackboard.publishRemove(toRemove);
            }

        }

    }


}
































