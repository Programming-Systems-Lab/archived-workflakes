package psl.workflakes.littlejil;

import org.apache.log4j.Logger;
import org.cougaar.core.blackboard.IncrementalSubscription;
import org.cougaar.core.plugin.ComponentPlugin;
import org.cougaar.core.service.*;
import org.cougaar.core.domain.RootFactory;
import org.cougaar.util.UnaryPredicate;
import org.cougaar.planning.ldm.plan.*;
import psl.workflakes.littlejil.xmlschema.*;
import psl.workflakes.littlejil.xmlschema.types.*;

import java.util.*;

import psl.workflakes.littlejil.assets.*;

/**
 * This class should get tasks posted on a blackboard and allocate them according to
 * the given workflow
 * @author matias
 */

public class TaskAllocatorPlugin extends ComponentPlugin {

    private static final Logger logger = Logger.getLogger(TaskAllocatorPlugin.class);
    private IncrementalSubscription leafTasksSubscription;
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
        ExecAgentAsset prototype = (ExecAgentAsset) factory.createPrototype(ExecAgentAsset.class, "ExecAgentProto");

        prototypeRegistry.cachePrototype("ExecAgent", prototype);

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

    public void setupSubscriptions() {

        BlackboardService blackboardService = getBlackboardService();

        // now set up the subscription to get leaf tasks
        leafTasksSubscription = (IncrementalSubscription) blackboardService.subscribe(new LeafTaskPredicate());


    }

    public void execute() {

        for (Enumeration tasks = leafTasksSubscription.getAddedList(); tasks.hasMoreElements();) {
            Task task = (Task) tasks.nextElement();

            logger.debug("got task " + task.getVerb() + ", will allocate...");

            /*Preference end_time_pref = task.getPreference(AspectType.END_TIME);
            if (end_time_pref == null) {
                logger.debug("task has no end_time_pref!");
                continue;
            }

            int end = (int) end_time_pref.getScoringFunction().getBest().getValue();

            AllocationResult estAR = null;

            // Create an estimate that reports that we did just what we were asked to do

            boolean onTime = true;
            int[] aspect_types = {AspectType.END_TIME};
            double[] results = {end};
            estAR = factory.newAllocationResult(1.0, //rating
                    onTime, // success or not
                    aspect_types,
                    results);
*/
            ExecAgentAsset asset = (ExecAgentAsset) factory.createInstance("ExecAgent");
            Allocation allocation = factory.createAllocation(task.getPlan(), task, asset, null, Role.ASSIGNED);

            logger.info("publishing allocation for task " + task.getVerb());
            blackboard.publishAdd(allocation);
            blackboard.publishChange(task);

        }


    }


}
































