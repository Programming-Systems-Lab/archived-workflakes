package psl.workflakes.littlejil;

import psl.workflakes.littlejil.assets.ExecAgentAsset;

import org.cougaar.planning.ldm.plan.Allocation;
import org.cougaar.planning.ldm.plan.Task;

import org.apache.log4j.Logger;

import java.util.Hashtable;

/**
 * This plugin simply simulates executing a task -- kind of like a "Hello World" executor plugin.
 *
 * It "executes" the tasks asynchronously, waiting for each one for a small period and then
 * determining success or failure of each task based on the name of the task -- all tasks succeed
 * except ones whose names end in "Fail".
 *
 * @author matias
 */

public class TaskExecutorSimulatePlugin extends AbstractTaskExecutorPlugin {

    private static final Logger logger = Logger.getLogger(TaskExecutorSimulatePlugin.class);

    public TaskExecutorSimulatePlugin() {
        super(ExecAgentAsset.class);
    }

    protected void executeTask(Allocation allocation, Hashtable inParams) {

         (new ExecutionThread(allocation)).start();

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
            if (success) {
                Hashtable outParams = new Hashtable();
                taskSucceeded(allocation, outParams);
            }
            else {
                taskFailed(allocation);
            }

        }
    }

}
















