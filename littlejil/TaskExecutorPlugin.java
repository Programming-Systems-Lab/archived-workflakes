package psl.workflakes.littlejil;

import java.util.*;
import java.io.Serializable;

import org.apache.log4j.Logger;
import org.cougaar.core.blackboard.IncrementalSubscription;
import org.cougaar.core.blackboard.SubscriberException;
import org.cougaar.core.domain.RootFactory;
import org.cougaar.core.plugin.ComponentPlugin;
import org.cougaar.core.service.*;
import org.cougaar.planning.ldm.asset.Asset;
import org.cougaar.planning.ldm.plan.*;
import org.cougaar.util.UnaryPredicate;
import psl.worklets.WVM;
import psl.worklets.WorkletJunction;
import psl.worklets.Worklet;

/**
 * This plugin simulates executing a task.
 * Currently it can use either a thread that waits, or a worklet that goes to a TaskMonitor instance.
 * The following system properties should be set:
 *     psl.workflakes.littlejil.useWorklets: true if worklets should be used
 *     psl.workflakes.littlejil.WVMHostname: if using worklets, the hostname to bind to
 *     psl.workflakes.littlejil.MonitorWVMHostname: if using worklets, the monitor's hostname
 *
 * @author matias
 */

public class TaskExecutorPlugin extends ComponentPlugin {

    private static final Logger logger = Logger.getLogger(TaskExecutorPlugin.class);

    private IncrementalSubscription allocationsSubscription;
    private DomainService domainService;
    private static RootFactory factory;

    private static BlackboardService blackboard;

    private static Hashtable allocationTable;   // used to keep track of allocations for tasks being sent to the TaskMonitor

    private static final int WVM_PORT = 9101;
    private static final String RMINAME = "TaskExecutorPluginWVM";

    private static final String MONITOR_RMINAME = "TaskMonitor";
    private static final int MONITOR_PORT = 9101;

    private static final String USE_WORKLETS_KEY = "psl.workflakes.littlejil.useWorklets";
    private static final String WVM_HOSTNAME_KEY = "psl.workflakes.littlejil.WVMHostname";
    private static final String MONITOR_WVM_HOSTNAME_KEY = "psl.workflakes.littlejil.MonitorWVMHostname";

    private String wvmHostname;
    private String monitorHostname;
    private boolean useWorklets;
    private WVM wvm;        // the worklet VM to use for dispatching worklets

    public TaskExecutorPlugin() {

        useWorklets = (System.getProperty(USE_WORKLETS_KEY) != null && System.getProperty(USE_WORKLETS_KEY).equals("true"));

        if (useWorklets) {

            wvmHostname = System.getProperty(WVM_HOSTNAME_KEY);
            monitorHostname = System.getProperty(MONITOR_WVM_HOSTNAME_KEY);

            logger.debug("instantiating WVM");
            wvm = new WVM(new WorkletListener(), wvmHostname, RMINAME);
        }

        if (allocationTable == null) {
            allocationTable = new Hashtable();
        }


    }

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

        blackboard = getBlackboardService();

        // now set up the subscription to get leaf tasks
        allocationsSubscription = (IncrementalSubscription) blackboard.subscribe(new AllocationPredicate());


    }

    public void execute() {

        for (Enumeration allocations = allocationsSubscription.getAddedList(); allocations.hasMoreElements();) {

            Allocation allocation = (Allocation) allocations.nextElement();

            allocation.getAsset();

            allocationTable.put(allocation.getUID().toString(), allocation);

            if (!useWorklets || allocation.getTask().getVerb() == LittleJILExpanderPlugin.DUMMY_TASK) {
                // if this is a "dummy" task (used for COMPLETE handlers), just set it as success

                (new ExecutionThread(allocation)).start();
            }
            else {
                // create a worklet junction that will "perform this task"
                ReturnJunction returnJunction = new ReturnJunction(allocation.getUID().toString(),wvmHostname, RMINAME, WVM_PORT);
                Worklet worklet = new Worklet(returnJunction);

                worklet.addJunction(new TaskMonitorJunction(allocation.getTask().getVerb().toString(),
                        returnJunction, monitorHostname, MONITOR_RMINAME, MONITOR_PORT));

                logger.info(">>>> launching worklet for task " + allocation.getTask().getVerb());

                worklet.deployWorklet(wvm);
            }

        }

    }

    protected static void taskComplete(String allocationID, boolean success) {

        Allocation allocation = (Allocation) allocationTable.get(allocationID);
        if (allocation == null) {
            logger.warn("got taskComplete with unknown allocation id " + allocationID);
            return;
        }

        processAllocation(allocation, success);


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

        // have to do this inside a transaction, since we are not in the execute() method
        try {

            blackboard.openTransaction();

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

            blackboard.closeTransaction();
        } catch (SubscriberException e) {
            logger.error("could not publish change: " + e);
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

            int waitTime = 1;
            logger.info(">>> executing task " + task.getVerb() + ", will be done in " + waitTime + " seconds.");

            // "wait for task to be done"
            try {
                Thread.sleep((waitTime)*1000);
            } catch (InterruptedException e) {

            }

            boolean success = !(task.getVerb().toString().endsWith("Fail"));
            processAllocation(allocation, success);

        }
    }

}


/**
 * This junction simply tells a TaskMonitor what's happening
 */
class TaskMonitorJunction extends WorkletJunction {

    TaskMonitor monitor;

    String taskName;
    ReturnJunction returnJunction;

    public TaskMonitorJunction(String taskName, ReturnJunction returnJunction, String host, String name, int port) {

        super(host, name, port);
        this.taskName = taskName;
        this.returnJunction = returnJunction;
    }

    protected void init(Object system, WVM wvm) {
        monitor = (TaskMonitor) system;
    }

    protected void execute() {
        boolean success = monitor.executing(taskName);
        returnJunction.setSuccess(success);
    }
}

/**
 * This junction is used as the "origin" junction. It brings back the result of executing the
 * task in the given allocation.
 */
class ReturnJunction extends WorkletJunction {

    private String allocationID;
    private boolean success;

    public ReturnJunction(String allocationID, String host, String name, int port) {
        super(host, name, port);

        this.allocationID = allocationID;
    }

    protected void init(Object system, WVM wvm) {
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    protected void execute() {
        ((WorkletListener) _system).taskComplete(allocationID, success);
    }

}

/**
 * This class is used to give as the "system" for the WVM and worklet... so that the whole plugin doesn't
 * need to be sent
 */
class WorkletListener implements Serializable {

    public void taskComplete(String allocationID, boolean success) {
        TaskExecutorPlugin.taskComplete(allocationID, success);
    }

}















