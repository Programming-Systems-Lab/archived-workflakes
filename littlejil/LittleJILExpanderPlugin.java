package psl.workflakes.littlejil;

import org.apache.log4j.Logger;
import org.cougaar.core.blackboard.IncrementalSubscription;
import org.cougaar.core.plugin.ComponentPlugin;
import org.cougaar.core.service.BlackboardService;
import org.cougaar.core.service.DomainService;
import org.cougaar.core.domain.RootFactory;
import org.cougaar.util.UnaryPredicate;
import org.cougaar.planning.ldm.plan.*;
//import psl.workflakes.littlejil.xmlschema.*;
//import psl.workflakes.littlejil.xmlschema.types.*;

import laser.littlejil.*;

import java.util.*;
import java.io.ObjectInputStream;
import java.io.BufferedInputStream;
import java.io.FileInputStream;

/**
 * This plugin parses a LittleJIL diagram and publishes the root task to be expanded
 * by the ExpanderPlugin
 * @author matias
 */

public class LittleJILExpanderPlugin extends ComponentPlugin {
    private static final String DIAGRAM_FILENAME = "test-diagram";

    private static final Logger logger = Logger.getLogger(LittleJILExpanderPlugin.class);
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

       // set up the subscription to get diagrams
        diagramSubscription = (IncrementalSubscription) blackboard.subscribe(new UnaryPredicate () {
            public boolean execute(Object o) {
                return (o instanceof Diagram);
            }
        });

        logger.info("ready.");

    }

    public void execute() {

        for (Enumeration e = diagramSubscription.getAddedList();e.hasMoreElements();) {

            Diagram diagram = (Diagram) e.nextElement();
            logger.info("found diagram in blackboard: " + diagram.getName());

            logger.debug("creating tasks...");
            Task task = makeTasks(diagram);

        }

    }

    /**
     * This method makes the root Task for a LittleJIL diagram, simply by calling makeTask on the
     * root Step
     * @param diagram the LittleJIL diagram
     * @return the root Task for the diagram, as per makeTask()
     */
    public Task makeTasks(Diagram diagram) {

        Task rootTask = makeTask(diagram.getRootStep());

        return rootTask;
    }


    /**
     * This method makes a Task from a LittleJIL step, setting the necessary properties and creating
     * a workflow with the step's substeps (if any).
     *
     * WARNING: this method calls itself recursively for each subtask. It will go into an infinite loop
     * if there's some kind of circular reference... TODO: might want to check for that.
     *
     * @param step the LittleJIL step to convert
     * @return a Cougaar Task, with an associated workflow (if necessary)
     */
    private NewTask makeTask(Step step) {

        NewTask parentTask = factory.newTask();
        parentTask.setVerb(new Verb(step.getName()));

        logger.debug("created task " + parentTask.getVerb());

        // get subsets of this step and create tasks for those, and a workflow to put them in
        NewWorkflow workflow = factory.newWorkflow();

        // NOTE: tasks are returned in the correct order (according to Little-JIL API docs)
        Task lastTask = null;
        for (Enumeration substepsEnum = step.substeps(); substepsEnum.hasMoreElements();) {
            Step substep = (Step) ((SubstepBinding) substepsEnum.nextElement()).getTarget();
            NewTask task = makeTask(substep);

            logger.debug("adding task " + task.getVerb() + " to workflow of task " + parentTask.getVerb());
            task.setParentTask(parentTask);
            workflow.addTask(task);
            task.setWorkflow(workflow);

            // TODO: should I use different END_TIME prefs? mhmm...
            ScoringFunction scorefcn = ScoringFunction.createStrictlyAtValue
                    (new AspectValue(AspectType.END_TIME, 1.0));
            Preference pref = factory.newPreference(AspectType.END_TIME, scorefcn);
            task.setPreference(pref);

            // if the parent step is sequential, set this task so it starts after the previous ends
            if (step.getStepKind() == Step.SEQUENTIAL && lastTask != null) {
                logger.info("making constraint so that task " + task.getVerb() + " goes after " + lastTask.getVerb());

                NewConstraint constraint = factory.newConstraint();
                constraint.setConstrainingTask(lastTask);
                constraint.setConstrainingAspect(AspectType.END_TIME);

                constraint.setConstrainedTask(task);
                constraint.setConstrainedAspect(AspectType.START_TIME);

                constraint.setConstraintOrder(Constraint.BEFORE);

                workflow.addConstraint(constraint);

            }

            // TODO: choice and try
            if (substep.getPrerequisite() != null) {
                final Step preReqStep = ((Reference) substep.getPrerequisite().getTarget()).refersTo();
                logger.info("step has prerequisite " + preReqStep.getName() + " adding constraint");
                NewTask preReqTask = makeTask(preReqStep);
                preReqTask.setPreference(pref);

                workflow.addTask(preReqTask);
                preReqTask.setWorkflow(workflow);

                // add constraint so that this task gets done before
                NewConstraint constraint = factory.newConstraint();
                constraint.setConstrainingTask(preReqTask);
                constraint.setConstrainingAspect(AspectType.END_TIME);

                constraint.setConstrainedTask(task);
                constraint.setConstrainedAspect(AspectType.START_TIME);

                constraint.setConstraintOrder(Constraint.BEFORE);

                workflow.addConstraint(constraint);

            }

            lastTask = task;
        }

        if (lastTask != null) {
            workflow.setParentTask(parentTask);
            NewExpansion expansion = (NewExpansion) factory.createExpansion(parentTask.getPlan(), parentTask, workflow, null);

            logger.debug("publishing parent task " + parentTask);
            blackboard.publishAdd(parentTask);

            logger.debug("publishing expansion " + expansion);
            blackboard.publishAdd(expansion);

        }

        return parentTask;
    }



}
































