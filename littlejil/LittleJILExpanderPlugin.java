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

    private static double endTime = 1.0;
        // the "end time" for the tasks, which keeps increasing...
        // TODO: make sure there are no issues with sharing this among different workflows
        // (intuitively there shouldn't be since later tasks will always have a later end time)

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

            makeTask(diagram.getRootStep());

        }

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

        // TODO: should I use different END_TIME prefs? mhmm...
        ScoringFunction scorefcn = ScoringFunction.createStrictlyAtValue
                (new AspectValue(AspectType.END_TIME, endTime));
        Preference pref = factory.newPreference(AspectType.END_TIME, scorefcn);
        parentTask.setPreference(pref);

        logger.debug("created task " + parentTask.getVerb() + " with end_time" + endTime);
        endTime++;

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

            if (substep.getPrerequisite() != null) {
                // have to create a task for this pre-req and add a constraint to it goes before

                final Step preReqStep = ((Reference) substep.getPrerequisite().getTarget()).refersTo();
                logger.info("step has prerequisite " + preReqStep.getName() + ", adding constraint");
                NewTask preReqTask = makeTask(preReqStep);
                preReqTask.setPreference(pref);

                logger.debug("adding pre-req task " + preReqTask.getVerb() + " to workflow of task " + parentTask.getVerb());
                workflow.addTask(preReqTask);
                preReqTask.setWorkflow(workflow);

                // add constraint so that this task gets done before the task of which it is a pre-req
                NewConstraint constraint = factory.newConstraint();
                constraint.setConstrainingTask(preReqTask);
                constraint.setConstrainingAspect(AspectType.END_TIME);

                constraint.setConstrainedTask(task);
                constraint.setConstrainedAspect(AspectType.START_TIME);

                constraint.setConstraintOrder(Constraint.BEFORE);

                workflow.addConstraint(constraint);

            }

            if (substep.getPostrequisite() != null) {
                // have to create a task for this post-req and add a constraint to it goes after

                final Step postReqStep = ((Reference) substep.getPostrequisite().getTarget()).refersTo();
                logger.info("step has postrequisite " + postReqStep.getName() + ", adding constraint");
                NewTask postReqTask = makeTask(postReqStep);
                postReqTask.setPreference(pref);

                logger.debug("adding post-req task " + postReqTask.getVerb() + " to workflow of task " + parentTask.getVerb());
                workflow.addTask(postReqTask);
                postReqTask.setWorkflow(workflow);

                // add constraint so that this task gets done after the task of which it is a post-req
                NewConstraint constraint = factory.newConstraint();
                constraint.setConstrainingTask(task);
                constraint.setConstrainingAspect(AspectType.END_TIME);

                constraint.setConstrainedTask(postReqTask);
                constraint.setConstrainedAspect(AspectType.START_TIME);

                constraint.setConstraintOrder(Constraint.BEFORE);

                workflow.addConstraint(constraint);

            }

            // TODO: choice and try

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
































