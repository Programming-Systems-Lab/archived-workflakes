package psl.workflakes.littlejil;

import org.apache.log4j.Logger;
import org.cougaar.core.blackboard.IncrementalSubscription;
import org.cougaar.core.plugin.ComponentPlugin;
import org.cougaar.core.service.BlackboardService;
import org.cougaar.core.service.DomainService;
import org.cougaar.core.domain.RootFactory;
import org.cougaar.util.UnaryPredicate;
import org.cougaar.planning.ldm.plan.*;
import psl.workflakes.littlejil.xmlschema.*;
import psl.workflakes.littlejil.xmlschema.types.*;

import java.util.*;

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

    /**
     * Used by the binding utility through reflection to set my DomainService
     */
    public void setDomainService(DomainService aDomainService) {
        domainService = aDomainService;
        factory = domainService.getFactory();
    }

    private class LeafTaskPredicate implements UnaryPredicate {
        public boolean execute(Object o) {
            boolean ret = false;
            if (o instanceof Task) {
                Task task = (Task) o;
                ret = (task.getWorkflow() == null);     // leaf tasks have no workflow
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

            // TODO: allocate, simulate result...

        }

    }


}
































