package psl.workflakes.littlejil;

import org.cougaar.core.blackboard.IncrementalSubscription;
import org.cougaar.core.blackboard.SubscriberException;
import org.cougaar.core.domain.RootFactory;
import org.cougaar.core.plugin.ComponentPlugin;
import org.cougaar.core.service.BlackboardService;
import org.cougaar.core.service.DomainService;
import org.cougaar.planning.ldm.asset.Asset;
import org.cougaar.planning.ldm.plan.*;
import org.cougaar.util.UnaryPredicate;

import laser.littlejil.ParameterBinding;
import laser.littlejil.ParameterDeclaration;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * The base class for Executor Plugins. This class allows for Executor plugins to be easily written
 * for any desired type of effectors (worklets, class invokation, SOAP, etc).
 *
 * Plugins extending this class specify in their constructor super() call the class of Assets to
 * subscribe to. They also implement the executeTask() method which is used to get and set
 * the in and out parameters for a task.
 *
 * @author matias
 */
public abstract class AbstractTaskExecutorPlugin extends ComponentPlugin {

    protected static final Logger logger = Logger.getLogger(AbstractTaskExecutorPlugin.class);
    private static RootFactory factory;
    private static BlackboardService blackboard;
    private static LittleJILStepsTable stepsTable; // used to keep a mapping of task->step

    private IncrementalSubscription littleJILStepsTableSubscription;
    private IncrementalSubscription allocationsSubscription;
    private DomainService domainService;

    private Class assetClass;   // the asset class to match for this plugin instance

    /**
     * Subclasses need to implement this constructor, where they should call super() with the class
     * of ExecAgentAssets they want to subscribe to.
     * @param assetClass
     */
    public AbstractTaskExecutorPlugin(Class assetClass) {
        this.assetClass = assetClass;
    }

    /**
     * Matches allocations that have a specified class of assets only
     */
    private class AllocationPredicate implements UnaryPredicate {

        public boolean execute(Object o) {
            if (o instanceof Allocation) {
                Allocation allocation = (Allocation) o;
                Asset asset = allocation.getAsset();
                return (asset != null && assetClass.isInstance(asset));
            }
            else {
                return false;
            }

        }
    }

    private static class LittleJILStepsTablePredicate implements UnaryPredicate {
        public boolean execute(Object o) {
            return (o instanceof LittleJILStepsTable);
        }
    }

    /**
     * Used by the binding utility through reflection to set my DomainService
     */
    public void setDomainService(DomainService domainService) {
        this.domainService = domainService;
        factory = this.domainService.getFactory();
    }

    public void setupSubscriptions() {

        blackboard = getBlackboardService();

        // now set up the subscription to get leaf tasks
        allocationsSubscription = (IncrementalSubscription) blackboard.subscribe(new AllocationPredicate());
        littleJILStepsTableSubscription = (IncrementalSubscription) blackboard.subscribe(new LittleJILStepsTablePredicate());

    }

    /**
     * The execute method for Cougaar plugins. The given implemention goes through the added matching
     * allocations, and for the task in each allocation it gathers any "in" params into a hashtable,
     * calls the plugin implementation's executeTask() method.
     */
    public void execute() {

        // assumming that the LittleJILExpanderPlugin has already published a stepsTable
        stepsTable = (LittleJILStepsTable) littleJILStepsTableSubscription.first();
        assert(stepsTable != null);

        for (Enumeration allocations = allocationsSubscription.getAddedList(); allocations.hasMoreElements();) {

            Allocation allocation = (Allocation) allocations.nextElement();
            Task task = allocation.getTask();

            // first get all "in" parameters for this task
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

            // call implemented excuteTask method
            executeTask(allocation, inParams);


        }

    }

    /**
     * Subclass of AbstractTaskExecutorPlugin need to implement this method to actually execute the
     * given task allocation with the appropriate facility (eg worklets, direct class invokation, SOAP, etc).
     *
     * NOTE: because tasks may be executed asynchronously, implementations of this method
     * <b>MUST</b> call the static method <code>processAllocation</code> when they complete, so that
     * the task can be set as having completed successfully or not and the out parameters can be
     * properly set.
     *
     * @param allocation the allocation for the task to be executed
     * @param inParams a Hashtable of "in" parameters for the task
     */
    protected abstract void executeTask(Allocation allocation, Hashtable inParams);

    /**
     * <b>Either taskSucceeded or taskFailed() MUST be called after executeTask() completes.</b>
     *
     * Given an allocation for the task that was executed successfully, it updates the
     * allocation results. It also copies out any out parameters with the values given in outParams.
     *
     * @param allocation the task allocation that has completed successfully
     * @param outParams a Hashtable containing the name and value of any out parameters for this task
     */
    protected static void taskSucceeded(Allocation allocation, Hashtable outParams) {
        processAllocation(allocation, true, outParams);
    }

    /**
     * <b>Either taskSucceeded or taskFailed() MUST be called after executeTask() completes.</b>
     *
     * Given an allocation for the task that failed execution, it updates the allocation results,
     * and publishes a LittleJILException for this failed task.
     *
     * @param allocation the task allocation that has failed execution
     */
    protected static void taskFailed(Allocation allocation) {
        processAllocation(allocation, false, null);
    }

    /**
     * This method MUST be called after executeTask() completes.
     * Given an allocation and a success flag for the task in this allocation, it updates the
     * allocation results and if success is false publishes an exception. It also copies out
     * any out parameters with the values given in outParams.
     *
     * @param allocation the task allocation that has completed
     * @param success true if the task has completed successfully, false otherwise
     * @param outParams a Hashtable containing the name and value of any out parameters for this task
     */
    private static void processAllocation(Allocation allocation, boolean success, Hashtable outParams) {
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

            blackboard.openTransaction();   // because we not calling this method from Plugin.execute();

            if (success) {

                // set out parameter values to the ones in the outParams table, and copy them
                // as specified by the parameter bindings
                if (outParams != null) {
                    LittleJILStepsTable.Entry entry = stepsTable.getEntry(task);
                    assert(task != null);

                    Collection paramBindings = entry.getParameterBindings();
                    for (Iterator i = paramBindings.iterator(); i.hasNext();) {
                        ParameterBinding binding = (ParameterBinding) i.next();
                        ParameterDeclaration childDeclaration = binding.getDeclarationInChild();
                        ParameterDeclaration parentDeclaration = binding.getDeclarationInParent();

                        if (childDeclaration.getMode() == ParameterDeclaration.COPY_OUT ||
                            childDeclaration.getMode() == ParameterDeclaration.COPY_IN_AND_OUT) {
                            Object value = outParams.get(childDeclaration.getName());
                            if (value != null) {
                                //logger.debug("setting parent param " + parentDeclaration.getName() + " to " + value);
                                childDeclaration.setParameterValue(value);
                                parentDeclaration.setParameterValue(value);
                            } else {
                                logger.warn("out parameter " + childDeclaration.getName() +
                                            " not found in outParams table for task " + task.getVerb());
                            }
                        }
                    }
                }
                else {
                    logger.warn("outParams table is null for task " + task.getVerb());
                }


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

            blackboard.closeTransaction();

        } catch (SubscriberException e) {
            logger.error("could not publish change: " + e);
        }
    }



}
