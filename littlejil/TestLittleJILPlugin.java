package psl.workflakes.littlejil;

import org.apache.log4j.Logger;
import org.cougaar.core.blackboard.IncrementalSubscription;
import org.cougaar.core.plugin.SimplePlugin;
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
 * This class is just to test posting and getting LittleJIL castor objects
 * from a cougaar blackboard
 * @author matias
 */

public class TestLittleJILPlugin extends ComponentPlugin {
    private static final String DIAGRAM_FILENAME = "test-diagram.xml";

    private static final Logger logger = Logger.getLogger(TestLittleJILPlugin.class);
    private IncrementalSubscription diagramSubscription;
    private DomainService domainService;
    private RootFactory factory;

    /**
     * Used by the binding utility through reflection to set my DomainService
     */
    public void setDomainService(DomainService aDomainService) {
        domainService = aDomainService;
        factory = domainService.getFactory();
    }

    public void setupSubscriptions() {

        BlackboardService blackboardService = getBlackboardService();

        Diagram diagram = null;
        try {
            diagram = LittleJILXMLParser.loadDiagram(DIAGRAM_FILENAME);
        } catch (Exception e) {
            logger.fatal("Could not load diagram: " + e);
            return;
        }

        blackboardService.publishAdd(diagram);
        logger.debug("published diagram");

        // now set up the subscription to get diagrams
        diagramSubscription = (IncrementalSubscription) blackboardService.subscribe(new UnaryPredicate () {
            public boolean execute(Object o) {
                return (o instanceof Diagram);
            }
        });


    }

    public void execute() {

        for (Enumeration e = diagramSubscription.getAddedList();e.hasMoreElements();) {
            logger.debug("found diagram in blackboard:");
            Diagram diagram = (Diagram) e.nextElement();
            //LittleJILXMLParser.outputDiagram(diagram);

            logger.debug("creating workflow...");
            Workflow workflow = makeWorkflowFromLittleJIL(diagram);

            AllocationResult ar = null;
            Task task = workflow.getParentTask();
            Expansion expansion = factory.createExpansion(task.getPlan(), task, workflow, ar);

            blackboard.publishAdd(expansion);
            blackboard.publishAdd(task);

        }

    }

    /**
     * Makes a Cougaar worflow, with its associated tasks, subtasks, and constraints, from a Little-JIL diagram
     * @param diagram the given Little-JIL diagram
     * @return a Cougaar workflow
     */
    public Workflow makeWorkflowFromLittleJIL(Diagram diagram) {

        NewWorkflow workflow = factory.newWorkflow();

        // we need to enumerate through the steps twice, because the steps are not necessarily
        // in any order, and they may refer to substeps that we don't know exist yet

        // first we go convert them into Cougaar Task objects, and hash them by step-id
        Hashtable tasks = new Hashtable(diagram.getStepCount());
        for (Enumeration steps = diagram.enumerateStep(); steps.hasMoreElements();) {
            Step step = (Step) steps.nextElement();
            NewTask task = makeTaskFromStep(step);

            logger.debug("adding task " + task.getVerb() + " with id " + step.getId());
            tasks.put(step.getId(), task);
        }

        // now we go through the steps again, this time setting the task parentTask property
        // to the correct value and the correct Constraints between subtasks
        for (Enumeration steps = diagram.enumerateStep(); steps.hasMoreElements();) {
            Step step = (Step) steps.nextElement();
            if (step.getSubsteps() == null)
                continue;

            Task parentTask = (Task) tasks.get(step.getId());
            if (parentTask == null) {
                logger.warn("task for step id " + step.getId() + " not found in tasks table!");
                continue;
            }

            Task lastTask = null;
            for (Enumeration substeps = step.getSubsteps().enumerateSubstepBinding(); substeps.hasMoreElements();) {
                Step substep = (Step) ((SubstepBinding) substeps.nextElement()).getTarget();
                NewTask task = (NewTask) tasks.get(substep.getId());

                if (task == null) {
                    logger.warn("task for substep id " + substep.getId() + " not found in tasks table!");
                    continue;
                }

                logger.debug("adding task " + task.getVerb() + " to workflow, and setting task " + parentTask.getVerb() + " as parent");
                task.setParentTask(parentTask);
                task.setWorkflow(workflow);
                workflow.addTask(task);

                // if the parent step is sequential, set this task to go after the previous one
                if (step.getKind().getType() == StepKindType.SEQUENTIAL_TYPE && lastTask != null) {
                    logger.debug("making constraint so that task " + task.getVerb() + " goes after " + lastTask.getVerb());

                    NewConstraint constraint = factory.newConstraint();
                    constraint.setConstrainedTask(task);
                    constraint.setConstrainingTask(lastTask);
                    constraint.setConstrainingAspect(Constraint.AFTER);
                }

                // TODO: what do we do for choice and try??? can't really add them here, have to add them later?

                lastTask = task;
            }


        }

        // finally, set the parent step (referenced by the diagram) as the parent of the workflow
        Task task = (Task) tasks.get(((Step)diagram.getRoot()).getId());
        if (task != null) {
            workflow.setParentTask(task);
        }
        else {
            logger.warn("parent task (id " + diagram.getRoot() + " not found in tasks table!");
        }

        return workflow;
    }

    private NewTask makeTaskFromStep(Step step) {

        NewTask task = factory.newTask();
        task.setVerb(new Verb(step.getName()));

        // TODO set other task properties here (like direct object from resource)

        return task;
    }

}
































