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
 * This class should get tasks posted on a blackboard and expand their workflows as necessary
 * @author matias
 */

public class TaskExpanderPlugin extends ComponentPlugin {

    private static final Logger logger = Logger.getLogger(TaskExpanderPlugin.class);
    private DomainService domainService;
    private RootFactory factory;

    private IncrementalSubscription expansionsSubscriptions;
    private IncrementalSubscription tasksSubscriptions;


    /**
     * Used by the binding utility through reflection to set my DomainService
     */
    public void setDomainService(DomainService aDomainService) {
        domainService = aDomainService;
        factory = domainService.getFactory();
    }

    private class ExpansionsPredicate implements UnaryPredicate {
        public boolean execute(Object o) {
            return (o instanceof Expansion);
        }
    }

    /**
     * This predicate only matches tasks that have a workflow (ie that are expandable)
     */
    private class ExpandableTasksPredicate implements UnaryPredicate {
        public boolean execute(Object o) {
            boolean ret = false;
            if (o instanceof Task) {
                Task task = (Task) o;
                ret = (task.getWorkflow() != null);
            }
            return ret;
        }
    }

    public void setupSubscriptions() {

        BlackboardService blackboardService = getBlackboardService();

        // now set up the subscription to get diagrams
        expansionsSubscriptions = (IncrementalSubscription) blackboardService.subscribe(new ExpansionsPredicate());
        tasksSubscriptions = (IncrementalSubscription) blackboardService.subscribe(new ExpandableTasksPredicate());


    }

    public void execute() {

        for (Enumeration tasks = tasksSubscriptions.getAddedList(); tasks.hasMoreElements();) {
            Task task = (Task) tasks.nextElement();
            logger.debug("posting expansion for task " + task.getVerb());

            // post the expansion for this task
            Expansion expansion = factory.createExpansion(task.getPlan(), task, task.getWorkflow(), null);
            blackboard.publishAdd(expansion);

        }

        for (Enumeration expansions = expansionsSubscriptions.getAddedList(); expansions.hasMoreElements();) {
            Expansion expansion = (Expansion) expansions.nextElement();
            logger.debug("looking at expansion " + expansion);

            Workflow workflow = expansion.getWorkflow();
            for (Enumeration subtasks = workflow.getTasks(); subtasks.hasMoreElements();) {
                Task task = (Task) subtasks.nextElement();
                logger.debug(" looking at task " + task.getVerb());

                Enumeration constraints = workflow.getTaskConstraints(task);
                if (!constraints.hasMoreElements()) {
                    logger.debug("\tthis task has no constraints, publishing it");
                    blackboard.publishAdd(task);
                }
                else {
                    for (; constraints.hasMoreElements();) {
                        Constraint c = (Constraint) constraints.nextElement();
                        logger.debug("\tfound a constraint: " + constraintToString(c));

                        if (c.getConstrainedTask() != task) {
                            logger.debug("\tthis task is not constrained, publishing it");
                            blackboard.publishAdd(task);
                        }
                        else {
                            logger.debug("\tconstraint exists, cannot publish this task at this time");
                        }

                    }
                }
            }
        }

        // TODO: need to look for changed expansions were the constraints were satisfied...?

    }

    private String constraintToString(Constraint c) {

        return "[Constraint: " + c.getConstrainedTask().getVerb() + " constrained by " +
                c.getConstrainingTask().getVerb() +
                ", aspect=" + AspectType.ASPECT_STRINGS[c.getConstrainingAspect()] + "]";

    }


}
































