package psl.workflakes.littlejil;

import laser.littlejil.ParameterBinding;
import laser.littlejil.ParameterDeclaration;
import org.apache.log4j.Logger;
import org.cougaar.core.blackboard.IncrementalSubscription;
import org.cougaar.core.blackboard.SubscriberException;
import org.cougaar.core.domain.RootFactory;
import org.cougaar.core.plugin.ComponentPlugin;
import org.cougaar.core.service.BlackboardService;
import org.cougaar.core.service.DomainService;
import org.cougaar.planning.ldm.plan.*;
import org.cougaar.util.UnaryPredicate;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;

/**
 * This plugin executes task by invoking a method on a class.
 * Currently the method is executed synchronously
 *
 * @author matias
 */

public class TaskExecutorInternalPlugin extends ComponentPlugin {

    private static final Logger logger = Logger.getLogger(TaskExecutorInternalPlugin.class);

    private IncrementalSubscription allocationsSubscription;
    private DomainService domainService;
    private static RootFactory factory;

    private static BlackboardService blackboard;

    private IncrementalSubscription littleJILStepsTableSubscription;
    private LittleJILStepsTable stepsTable; // used to keep a mapping of task->step

    /**
     * Used by the binding utility through reflection to set my DomainService
     */
    public void setDomainService(DomainService domainService) {
        this.domainService = domainService;
        factory = this.domainService.getFactory();
    }

    private class AllocationPredicate implements UnaryPredicate {
        public boolean execute(Object o) {
            return (o instanceof Allocation);
        }
    }

    private static class LittleJILStepsTablePredicate implements UnaryPredicate {
        public boolean execute(Object o) {
            return (o instanceof LittleJILStepsTable);
        }
    }


    public void setupSubscriptions() {

        blackboard = getBlackboardService();

        // now set up the subscription to get leaf tasks
        allocationsSubscription = (IncrementalSubscription) blackboard.subscribe(new AllocationPredicate());
        littleJILStepsTableSubscription = (IncrementalSubscription) blackboard.subscribe(new LittleJILStepsTablePredicate());

    }

    public void execute() {

        // assumming that the LittleJILExpanderPlugin has already published a stepsTable
        stepsTable = (LittleJILStepsTable) littleJILStepsTableSubscription.first();

        for (Enumeration allocations = allocationsSubscription.getAddedList(); allocations.hasMoreElements();) {

            Allocation allocation = (Allocation) allocations.nextElement();
            Task task = allocation.getTask();

            // fill parameter table from parameter bindings
            // the "in" parameters should have been filled when this task was posted,
            // by the ExpanderPlugin
            Hashtable inParams = new Hashtable();

            LittleJILStepsTable.Entry entry = stepsTable.getEntry(task);
            assert(task != null);

            Collection paramBindings = entry.getParameterBindings();
            for (Iterator i = paramBindings.iterator(); i.hasNext();) {
                ParameterBinding binding = (ParameterBinding) i.next();
                ParameterDeclaration declaration = binding.getDeclarationInChild();
                if (declaration.getMode() != ParameterDeclaration.COPY_OUT) {
                    inParams.put(declaration.getName(), declaration.getParameterValue());
                }
            }

            Hashtable outParams = new Hashtable();
            /*try {
                // TODO invoke task! -- pass in and outParams tables (outParams to be filled in)
            }
            catch () {

            }*/


            // for TESTING
            laser.littlejil.types.SimpleDuration v = new laser.littlejil.types.SimpleDuration();
            v.setSeconds((int)(Math.random()*100));
            outParams.put("result", v);

            // set out parameter values to the ones in the outParams table, and copy them
            // as specified by the parameter bindings
            for (Iterator i = paramBindings.iterator(); i.hasNext();) {
                ParameterBinding binding = (ParameterBinding) i.next();
                ParameterDeclaration childDeclaration = binding.getDeclarationInChild();
                ParameterDeclaration parentDeclaration = binding.getDeclarationInParent();

                if (childDeclaration.getMode() == ParameterDeclaration.COPY_OUT ||
                    childDeclaration.getMode() == ParameterDeclaration.COPY_IN_AND_OUT) {
                    Object value = outParams.get(childDeclaration.getName());
                    if (value != null) {
                        logger.debug("setting parent param " + parentDeclaration.getName() +
                                " to " + value);
                        childDeclaration.setParameterValue(value);
                        parentDeclaration.setParameterValue(value);
                    } else {
                        logger.warn("out parameter " + childDeclaration.getName() + " not found in outParams table");
                    }
                }
            }

            processAllocation(allocation, true);
        }

    }

     /**
     * Given an allocation and a success flag for the task in this allocation, we update the
     * allocation results and if success is false publish an exception
     * @param allocation
     * @param success
     */
    private static void processAllocation(Allocation allocation, boolean success) {
        Task task = allocation.getTask();

        LittleJILException exception = null;

        Preference end_time_pref = task.getPreference(AspectType.END_TIME);
        if (end_time_pref == null) {
            logger.warn("task has no end_time_pref!");
            return;
        }

        int end = (int) end_time_pref.getScoringFunction().getBest().getValue();
        int[] aspect_types = {AspectType.END_TIME};
        double[] results = {end};

        AllocationResult result = factory.newAllocationResult(1.0, //rating
                success, // success or not
                aspect_types,
                results);

        try {
            if (success) {
                logger.info(">>> task " + task.getVerb() + " finished. updating allocation result");
                allocation.setEstimatedResult(result);
            } else {
                logger.info(">>> task " + task.getVerb() + " failed. updating allocation result");
                ((PlanElementForAssessor) allocation).setReceivedResult(result);

                exception = new LittleJILException(task, new Exception("test exception"));
                blackboard.publishAdd(exception);
            }

            blackboard.publishChange(allocation);
            blackboard.publishChange(task);
        } catch (SubscriberException e) {
            logger.error("could not publish change: " + e);
        }
    }

}