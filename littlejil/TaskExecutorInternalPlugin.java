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

import java.util.*;

import psl.workflakes.littlejil.assets.ExecAgentAsset;

/**
 * This plugin executes tasks by invoking a method on a class.
 * Classes that are used to execute tasks need to implement the ExecutableTask interface.
 * Invokations are executed asynchronously.
 *
 * // TODO: get class name based on some configuration or parameter in little-jil diagram?
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
            ExecutableTaskThread thread = new ExecutableTaskThread(allocation);

            logger.debug("starting execute thread for task " + allocation.getTask().getVerb() + "...");
            thread.start();
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

            blackboard.openTransaction();   // because we not calling this method from Plugin.execute();

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
     * This thread class is used to execute tasks asynchronously
     */
    private class ExecutableTaskThread extends Thread {

        private Allocation allocation;

        public ExecutableTaskThread(Allocation allocation) {
            this.allocation = allocation;
        }

        public void run() {

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
            try {

                ExecAgentAsset execAgent = (ExecAgentAsset) allocation.getAsset();
                String className = execAgent.getExecutorPG().getJunction();
                Class execClass = Class.forName(className);
                ExecutableTask executable = (ExecutableTask) execClass.newInstance(); //new DummyExecutableTask();
                executable.execute(task.getVerb().toString(), inParams, outParams);

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

                // task executed successfully
                logger.debug("executable for task " + task.getVerb() + " executed successfully");
                processAllocation(allocation, true);

            }
            catch (Exception e) {
                // an error occurred
                logger.debug("executable for task " + task.getVerb() + " failed with exception " + e);
                processAllocation(allocation, false);   // TODO: pass exception along
                return;
            }



        }

    }

    public static class DummyExecutableTask implements ExecutableTask {

        private static Random random = new Random();

        public void execute(String method, Hashtable inParams, Hashtable outParams) throws Exception {

            logger.info("executing method " + method);

            // for TESTING
            /*laser.littlejil.types.SimpleDuration v = new laser.littlejil.types.SimpleDuration();
            v.setSeconds((int)(Math.random()*100));*/

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
            else {  // for TESTING

                // expect a List named "in", add something and put the list as "result"
                List in = (List) inParams.get("in");
                if (in == null) {
                    throw new Exception("expected 'in' parameter");
                }

                in.add(new Integer(random.nextInt(100)));

                outParams.put("result", in);
            }

        }

    }
}