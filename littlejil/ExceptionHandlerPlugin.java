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
 *
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
            Step failedStep = null;
            if (exception.getNestedException() != null) {
                failedTask = exception.getNestedException().getTask();
                failedStep = stepsTable.get(failedTask);
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

                    // create a Request to ask the LittleJILExpander to re-post the following tasks.
                    // even though this might seem inefficient, it is cleaner than attempting to modify the
                    // existing workflow to continue after a task failed.
                    int type;
                    if (step.getStepKind() == Step.TRY)
                        type = ExceptionHandlerRequest.TRY;
                    else
                        type = ExceptionHandlerRequest.CONTINUE;

                    ExceptionHandlerRequest request = new ExceptionHandlerRequest(type, step, task, failedStep);
                    blackboard.publishAdd(request);

                    // post a PostHandlerTaskRequest so that the handler task gets posted by LittleJILExpanderPlugin
                    if (handlerBinding.getTarget() != null) {
                        PostHandlerTaskRequest handlerRequest = new PostHandlerTaskRequest(handlerBinding, task);
                        blackboard.publishAdd(handlerRequest);
                    }


                }
                /////////////////////////////////////////////////////////////////////////////////////////////
                // COMPLETE handler
                else if (handlerBinding.getControlFlow() == HandlerBinding.COMPLETE) {

                    // create a RestartRequest to "send" to the LittleJILExpanderPlugin
                    ExceptionHandlerRequest request =
                            new ExceptionHandlerRequest(ExceptionHandlerRequest.COMPLETE, step, task, failedStep);
                    blackboard.publishAdd(request);

                    // post a PostHandlerTaskRequest so that the handler task gets posted by LittleJILExpanderPlugin
                    if (handlerBinding.getTarget() != null) {
                        PostHandlerTaskRequest handlerRequest = new PostHandlerTaskRequest(handlerBinding, task);
                        blackboard.publishAdd(handlerRequest);
                    }

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

                    // create a RestartRequest to "send" to the LittleJILExpanderPlugin
                    ExceptionHandlerRequest request =
                            new ExceptionHandlerRequest(ExceptionHandlerRequest.RESTART, step, task, failedStep);
                    blackboard.publishAdd(request);

                    // post a PostHandlerTaskRequest so that the handler task gets posted by LittleJILExpanderPlugin
                    if (handlerBinding.getTarget() != null) {
                        PostHandlerTaskRequest handlerRequest = new PostHandlerTaskRequest(handlerBinding, task);
                        blackboard.publishAdd(handlerRequest);
                    }




                }
                /////////////////////////////////////////////////////////////////////////////////////////////
                // RETHROW handler (default)
                else /*if (handlerBinding.getControlFlow() == HandlerBinding.RETHROW)*/ {

                    /*// remove the current expansion, if any
                    if (task.getPlanElement() != null) {
                        logger.debug("removing expansion for task " + task.getVerb());
                        blackboard.publishRemove(task.getPlanElement());
                    }*/

                    // create a RestartRequest to "send" to the LittleJILExpanderPlugin
                    ExceptionHandlerRequest request =
                            new ExceptionHandlerRequest(ExceptionHandlerRequest.COMPLETE, step, task, failedStep);
                    blackboard.publishAdd(request);

                    // post a PostHandlerTaskRequest so that the handler task gets posted by LittleJILExpanderPlugin
                    if (handlerBinding.getTarget() != null) {
                        PostHandlerTaskRequest handlerRequest = new PostHandlerTaskRequest(handlerBinding, task);
                        blackboard.publishAdd(handlerRequest);
                    }

                    logger.info("rethrowing exception");
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

}

