package psl.workflakes.littlejil;

import java.util.Enumeration;

import org.apache.log4j.Logger;
import org.cougaar.core.blackboard.IncrementalSubscription;
import org.cougaar.core.blackboard.Subscription;
import org.cougaar.core.domain.RootFactory;
import org.cougaar.core.plugin.ComponentPlugin;
import org.cougaar.core.service.*;
import org.cougaar.planning.ldm.plan.*;
import org.cougaar.planning.ldm.asset.Asset;
import org.cougaar.util.UnaryPredicate;
import psl.workflakes.littlejil.assets.*;

/**
 * This class should get tasks posted on a blackboard and allocate them according to
 * the given workflow
 * @author matias
 */

public class TaskAllocatorPlugin extends ComponentPlugin {

    private static final Logger logger = Logger.getLogger(TaskAllocatorPlugin.class);
    private IncrementalSubscription leafTasksSubscription;
    private IncrementalSubscription execAgentAssetsSubscription;

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

    public void setPrototypeRegistryService(PrototypeRegistryService prototypeRegistryService) {
        this.prototypeRegistry = prototypeRegistryService;

        // create asset prototype
        factory.addPropertyGroupFactory(new PropertyGroupFactory());
        {
            ExecClassAgentAsset prototype = (ExecClassAgentAsset)
                    factory.createPrototype(ExecClassAgentAsset.class, "ExecClassAgentProto");

            prototypeRegistry.cachePrototype("ExecClassAgent", prototype);
        }

        {
            ExecWorkletAgentAsset prototype = (ExecWorkletAgentAsset)
                    factory.createPrototype(ExecWorkletAgentAsset.class, "ExecWorkletAgentProto");

            prototypeRegistry.cachePrototype("ExecWorkletAgent", prototype);
        }


    }

    private class LeafTaskPredicate implements UnaryPredicate {
        public boolean execute(Object o) {
            boolean ret = false;
            if (o instanceof Task) {
                Task task = (Task) o;
                ret = (task.getPlanElement() == null);     // leaf tasks have no PlanElement (expansion)
            }
            return ret;
        }
    }

    private class ExecAgentAssetPredicate implements UnaryPredicate {

        public boolean execute(Object o) {
            return (o instanceof ExecAgentAsset);
        }
    }

    public void setupSubscriptions() {

        leafTasksSubscription = (IncrementalSubscription) blackboard.subscribe(new LeafTaskPredicate());
        execAgentAssetsSubscription = (IncrementalSubscription) blackboard.subscribe(new ExecAgentAssetPredicate());

        // TEMPORARY: create exec agents
        {
            ExecClassAgentAsset asset = (ExecClassAgentAsset) factory.createInstance("ExecClassAgent");
            NewExecutorPG executorPG = (NewExecutorPG) factory.createPropertyGroup("ExecutorPG");
            executorPG.setCapabilities("any");

            NewClassPG classPG = (NewClassPG) factory.createPropertyGroup("ClassPG");
            classPG.setClassName("psl.workflakes.littlejil.TaskExecutorInternalPlugin$DummyExecutableTask");
            asset.setExecutorPG(executorPG);
            asset.setClassPG(classPG);

            blackboard.publishAdd(asset);
        }

        /*{
            ExecWorkletAgentAsset asset = (ExecWorkletAgentAsset) factory.createInstance("ExecWorkletAgent");
            NewExecutorPG executorPG = (NewExecutorPG) factory.createPropertyGroup("ExecutorPG");
            executorPG.setCapabilities("any");
            asset.setExecutorPG(executorPG);

            blackboard.publishAdd(asset);
        }*/

    }

    public void execute() {

        for (Enumeration tasks = leafTasksSubscription.getAddedList(); tasks.hasMoreElements();) {
            Task task = (Task) tasks.nextElement();

            // find an execution asset for this task.
            ExecAgentAsset asset = null;
            try {
                asset = findExecAgent(task);
                Allocation allocation = factory.createAllocation(task.getPlan(), task, asset, null, Role.ASSIGNED);

                logger.info("publishing allocation for task " + task.getVerb());
                blackboard.publishAdd(allocation);
                blackboard.publishChange(task);

            } catch (PluginException e) {
                logger.warn("Could not find asset for task " + task.getVerb() + ": " + e);
                logger.warn("Not publishing task");
            }


        }


    }

    /**
     * Returns an appropriate execution agent asset for this task.
     * This implementation simply finds the first matching asset.
     * It can be overriden by a subclass as necessary
     *
     * TODO: specify agent asset as part of Step def?
     * @param task the task to find the Execution Agent asset for
     * @return an asset representing the Execution Agent
     * @throws PluginException if an appropriate asset cannot be found
     */
    protected ExecAgentAsset findExecAgent(Task task) throws PluginException {

        // iterate through available assets, return first that matches
        for (Enumeration assets = execAgentAssetsSubscription.elements(); assets.hasMoreElements();) {
            ExecAgentAsset asset = (ExecAgentAsset) assets.nextElement();
            String capabilities = asset.getExecutorPG().getCapabilities();
            if (capabilities.equals("any") ||
                capabilities.equals(task.getVerb())) {
                return asset;
            }
        }

        throw new PluginException("No matching ExecAgent assets found");
    }


}
































