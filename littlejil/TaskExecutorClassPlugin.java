package psl.workflakes.littlejil;

import psl.workflakes.littlejil.assets.ExecClassAgentAsset;

import org.cougaar.planning.ldm.plan.Allocation;
import org.cougaar.planning.ldm.plan.Task;

import java.util.*;

/**
 * This plugin extends the <code>AbstractTaskExecutorPlugin</code> and
 * subscribes to <code>ExecClassAssetAsset</code>s. It implements the
 * <code>executeTask</code> method so that it invokes the <code>execute()</code>
 * method on an instance of the <code>ExecutableTask</code>-implementing class
 * specified by the <code>ExecAgentAsset</code>. The invokation is done by a
 * separate thread, asynchronously, so that tasks can be run in parallel.
 *
 * @author matias
 */

public class TaskExecutorClassPlugin extends AbstractTaskExecutorPlugin {

    private Hashtable instances;    // instances of classes ready to use
                                    // NOTE: these classes must be threadsafe!!

    public TaskExecutorClassPlugin() {
        super(ExecClassAgentAsset.class);

        instances = new Hashtable();
    }

    public void setupSubscriptions() {
        super.setupSubscriptions();
    }


    public void executeTask(Allocation allocation, Hashtable inParams) {

        ExecutableTaskThread thread = new ExecutableTaskThread(allocation, inParams);

        logger.debug("starting execute thread for task " + allocation.getTask().getVerb() + "...");
        thread.start();

    }

    /**
     * This thread class is used to execute tasks asynchronously
     */
    private class ExecutableTaskThread extends Thread {

        private Allocation allocation;
        private Hashtable inParams, outParams;

        public ExecutableTaskThread(Allocation allocation, Hashtable inParams) {
            super("ExecutableTaskThread");
            this.allocation = allocation;
            this.inParams = inParams;

        }

        public void run() {

            Task task = allocation.getTask();

            try {

                ExecClassAgentAsset execAgent = (ExecClassAgentAsset) allocation.getAsset();
                String className = execAgent.getClassPG().getClassName();

                ExecutableTask executable = (ExecutableTask) instances.get(className);
                if (executable == null) {
                    logger.debug("instantiating class " + className);
                    Class execClass = Class.forName(className);
                    executable = (ExecutableTask) execClass.newInstance(); //new DummyExecutableTask();
                    instances.put(className, executable);
                }

                Hashtable outParams = new Hashtable();

                String name = task.getVerb().toString();
                PluginUtil.Timing.addTimestamp("EXEC " + name);
                executable.execute(name, inParams, outParams);
                PluginUtil.Timing.addTimestamp("END " + name);

                // task executed successfully
                logger.debug("executable for task " + task.getVerb() + " executed successfully");
                taskSucceeded(allocation, outParams);

            }
            catch (Throwable e) {
                // an error occurred
                logger.warn("executable for task " + task.getVerb() + " failed with exception " + e);
                taskFailed(allocation);
                return;
            }

        }

    }

    public static class DummyExecutableTask implements ExecutableTask {

        private static Random random = new Random();

        public void execute(String method, Hashtable inParams, Hashtable outParams) throws Exception {

            logger.info("executing method " + method);

            if (method.equals("FindBase")) {

                // set the "base"
                outParams.put("base", String.valueOf(random.nextInt(100)));

            }
            else if (method.equals("EvaluateClient")) {

                String base = (String) inParams.get("base");
                Vector clientInfo = (Vector) inParams.get("clientInfo");

                if (base == null || clientInfo == null ) {
                    throw new Exception("expected 'base' and 'clientInfo' parameters");
                }

                logger.debug("EvaluateClient got base=" + base);

                // add to the clientInfo list
                clientInfo.add(new Integer(random.nextInt(100)));
                outParams.put("clientInfo", clientInfo);
            }
            else if (method.equals("AdaptClients")) {

                Vector clientInfo = (Vector) inParams.get("clientInfo");
                if (clientInfo == null)  {
                    throw new Exception("expected 'clientInfo' parameter");
                }

                logger.debug("AdaptClients got clientInfo vector: " + clientInfo);

            }
            else {
                // do nothing
            }

        }

    }
}