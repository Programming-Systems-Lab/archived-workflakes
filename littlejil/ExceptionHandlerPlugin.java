package psl.workflakes.littlejil;

import java.util.*;

import org.apache.log4j.Logger;
import org.cougaar.core.blackboard.IncrementalSubscription;
import org.cougaar.core.plugin.ComponentPlugin;
import org.cougaar.core.plugin.util.PluginHelper;
import org.cougaar.core.service.DomainService;
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

    private IncrementalSubscription exceptionsSubscription;
    private IncrementalSubscription stepsTableSubscription;

    /**
     * Used by the binding utility through reflection to set DomainService
     */
    public void setDomainService(DomainService domainService) {
        this.domainService = domainService;
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

            // first look for any handlers defined for this task's parent
            Step step = stepsTable.get(task);
            Enumeration handlers = step.handlers();
            if (handlers.hasMoreElements()) {

                for (;handlers.hasMoreElements();) {
                    HandlerBinding handlerBinding = (HandlerBinding) handlers.nextElement();
                }

            }
            else {
                // exception is not handled, re-throw to the parent

                if (task.getWorkflow() != null) {
                    blackboard.publishAdd(new LittleJILException(task.getWorkflow().getParentTask(), exception.getException()));
                }

                logger.info(">>> task " + task.getVerb() + " FAILED <<<");
            }

        }

    }


}
































