package psl.workflakes.littlejil;

import java.util.Enumeration;
import java.util.Random;

import org.apache.log4j.Logger;
import org.cougaar.core.blackboard.IncrementalSubscription;
import org.cougaar.core.blackboard.SubscriberException;
import org.cougaar.core.domain.RootFactory;
import org.cougaar.core.plugin.ComponentPlugin;
import org.cougaar.core.service.*;
import org.cougaar.planning.ldm.asset.Asset;
import org.cougaar.planning.ldm.plan.*;
import org.cougaar.util.UnaryPredicate;

/**
 * This plugin simulates executing a task
 * @author matias
 */

public class TaskExecutorPlugin extends ComponentPlugin {

    private static final Logger logger = Logger.getLogger(TaskExecutorPlugin.class);

    private static Random random = new Random();

    private IncrementalSubscription allocationsSubscription;
    private DomainService domainService;
    private RootFactory factory;
    private PrototypeRegistryService prototypeRegistry;



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

    public void setupSubscriptions() {

        BlackboardService blackboardService = getBlackboardService();

        // now set up the subscription to get leaf tasks
        allocationsSubscription = (IncrementalSubscription) blackboardService.subscribe(new AllocationPredicate());


    }

    public void execute() {

        for (Enumeration allocations = allocationsSubscription.getAddedList(); allocations.hasMoreElements();) {

            Allocation allocation = (Allocation) allocations.nextElement();
            (new ExecutionThread(allocation)).start();
        }

    }


    /**
     * Simulates an execution by simply pausing for a while... When it's done,
     * it sets the allocation results and updates the allocation
     */
    private class ExecutionThread extends Thread {

        Allocation allocation;

        public ExecutionThread(Allocation allocation) {
            this.allocation = allocation;
        }

        public void run() {

            Task task = allocation.getTask();
            Asset asset = allocation.getAsset();

            int waitTime = random.nextInt(1)+2;
            logger.info(">>> executing task " + task.getVerb() + ", will be done in " + waitTime + " seconds.");

            // "wait for task to be done"
            try {
                Thread.sleep((waitTime)*1000);
            } catch (InterruptedException e) {

            }

            boolean success;
            LittleJILException exception = null;
            // for TESTING. tasks that contain "Fail" in the name will fail
            if (task.getVerb().toString().indexOf("Fail") != -1) {
                logger.debug("setting success to false");
                success = false;

                // exception to be published
                exception = new LittleJILException(task, new Exception("test exception"));
            }
            else {
                success = true;
            }

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

            // have to do this inside a transaction, since we are not in the execute() method
            try {
                logger.info(">>> task " + task.getVerb() + " finished. updating allocation result");
                blackboard.openTransaction();

                if (success) {
                    allocation.setEstimatedResult(result);
                } else {
                    ((PlanElementForAssessor)allocation).setReceivedResult(result);
                }


                blackboard.publishChange(allocation);
                blackboard.publishChange(task);

                if (exception != null) {
                    blackboard.publishAdd(exception);
                }

                blackboard.closeTransaction();
            } catch (SubscriberException e) {
                logger.error("could not publish change: " + e);
            }


        }
    }

}
































