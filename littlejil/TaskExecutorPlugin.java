package psl.workflakes.littlejil;

import org.apache.log4j.Logger;
import org.cougaar.core.blackboard.IncrementalSubscription;
import org.cougaar.core.blackboard.SubscriberException;
import org.cougaar.core.plugin.ComponentPlugin;
import org.cougaar.core.service.*;
import org.cougaar.core.domain.RootFactory;
import org.cougaar.util.UnaryPredicate;
import org.cougaar.planning.ldm.plan.*;
import org.cougaar.planning.ldm.asset.Asset;
import psl.workflakes.littlejil.xmlschema.*;
import psl.workflakes.littlejil.xmlschema.types.*;

import java.util.*;

import psl.workflakes.littlejil.assets.*;

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

            int waitTime = random.nextInt(3)+2;
            logger.info("executing task " + task.getVerb() + ", will be done in " + waitTime + " seconds.");

            // "wait for task to be done"
            try {
                Thread.sleep((waitTime)*1000);
            } catch (InterruptedException e) {

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
                    true, // success or not
                    aspect_types,
                    results);

            // have to do this inside a transaction, since we are not in the execute() method
            try {
                logger.info("task " + task.getVerb() + " finished. updating allocation result");
                blackboard.openTransaction();

                allocation.setEstimatedResult(result);
                blackboard.publishChange(allocation);

                blackboard.publishChange(task);

                blackboard.closeTransaction();
            } catch (SubscriberException e) {
                logger.error("could not publish change: " + e);
            }


        }
    }

}
































