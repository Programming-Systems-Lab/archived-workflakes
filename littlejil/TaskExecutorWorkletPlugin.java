package psl.workflakes.littlejil;

import psl.workflakes.littlejil.assets.ExecWorkletAgentAsset;
import psl.workflakes.littlejil.assets.WVMPG;
import psl.worklets.WVM;
import psl.worklets.Worklet;
import psl.worklets.WorkletJunction;

import org.cougaar.planning.ldm.plan.Allocation;

import org.apache.log4j.Logger;

import java.io.Serializable;
import java.util.Hashtable;

/**
 *
 * This plugin extends the <code>AbstractTaskExecutorPlugin</code> and
 * subscribes to <code>ExecWorkletAssetAsset</code>s. It implements the
 * <code>executeTask</code> method so that a <a class="ref"
 * href="ref">Worklet</a> is launched to execute the given task.
 *
 * At the moment this plugin is used mostly for testing purposes. For example,
 * it always uses a <code>TaskMonitorJunction</code> worklet junction that is
 * intended to go to a running instance of a <code>TaskMonitor</code> or
 * <code>TaskMonitorGUI</code>. In reality it should use the junction that is
 * specified as part of the <code>ExecWorkletAssetAsset</code> for the task.
 *
 *
 * The following system properties should be set:
 *     psl.workflakes.littlejil.useWorklets: true if worklets should be used
 *     psl.workflakes.littlejil.WVMHostname: if using worklets, the hostname to bind to
 *     psl.workflakes.littlejil.MonitorWVMHostname: if using worklets, the monitor's hostname
 *
 * @author matias
 */

public class TaskExecutorWorkletPlugin extends AbstractTaskExecutorPlugin {

    private static final Logger logger = Logger.getLogger(TaskExecutorWorkletPlugin.class);

    private static final int WVM_PORT = 9101;
    private static final String RMINAME = "TaskExecutorPluginWVM";

    private static final String MONITOR_RMINAME = "TaskMonitor";
    private static final int MONITOR_PORT = 9101;

    private static final String WVM_HOSTNAME_KEY = "psl.workflakes.littlejil.WVMHostname";
    private static final String MONITOR_WVM_HOSTNAME_KEY = "psl.workflakes.littlejil.MonitorWVMHostname";

    private static Hashtable allocationTable;   // used to keep track of allocations for tasks being sent to the TaskMonitor

    private String wvmHostname;
    private String monitorHostname;
    private WVM wvm;        // the worklet VM to use for dispatching worklets

    public TaskExecutorWorkletPlugin() {
        super(ExecWorkletAgentAsset.class);

        wvmHostname = System.getProperty(WVM_HOSTNAME_KEY);
        monitorHostname = System.getProperty(MONITOR_WVM_HOSTNAME_KEY);

        logger.debug("instantiating WVM");
        wvm = new WVM(new WorkletListener(), wvmHostname, RMINAME);

        if (allocationTable == null) {
            allocationTable = new Hashtable();
        }

    }

    public void setupSubscriptions() {
        super.setupSubscriptions();
    }

    protected void executeTask(Allocation allocation, Hashtable inParams) {

        ExecWorkletAgentAsset execAgent = (ExecWorkletAgentAsset) allocation.getAsset();
        WVMPG wvmPG = execAgent.getWVMPG();

        allocationTable.put(allocation.getUID().toString(), allocation);

        // create a worklet junction that will "perform this task"
        ReturnJunction returnJunction = new ReturnJunction(allocation.getUID().toString(), wvmHostname, RMINAME, WVM_PORT);
        Worklet worklet = new Worklet(returnJunction);

        // TODO: use information in asset to create worklet

        worklet.addJunction(new TaskMonitorJunction(allocation.getTask().getVerb().toString(),
                returnJunction, monitorHostname, MONITOR_RMINAME, MONITOR_PORT));

        logger.info(">>>> launching worklet for task " + allocation.getTask().getVerb());

        worklet.deployWorklet(wvm);


    }

    protected static void taskComplete(String allocationID, boolean success) {

        Allocation allocation = (Allocation) allocationTable.get(allocationID);
        if (allocation == null) {
            logger.warn("got taskComplete with unknown allocation id " + allocationID);
            return;
        }

        if (success) {
            Hashtable outParams = new Hashtable();  // TODO: actually get this from the incoming worklet!
            taskSucceeded(allocation, outParams);
        }
        else {
            taskFailed(allocation);
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
        TaskExecutorWorkletPlugin.taskComplete(allocationID, success);
    }

}















