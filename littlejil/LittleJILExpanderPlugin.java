package psl.workflakes.littlejil;

import java.util.Enumeration;
import java.util.Vector;

import laser.littlejil.*;
import org.apache.log4j.Logger;
import org.cougaar.core.blackboard.IncrementalSubscription;
import org.cougaar.core.domain.RootFactory;
import org.cougaar.core.plugin.ComponentPlugin;
import org.cougaar.core.service.DomainService;
import org.cougaar.planning.ldm.plan.*;
import org.cougaar.util.UnaryPredicate;

/**
 * This plugin parses a LittleJIL diagram and publishes the root task to be expanded
 * by the ExpanderPlugin.
 *
 * Design change 12/06/02
 *  Peppo and I talked about handling pre and post-reqs. He came up with a better idea than the
 *  current solution: encapsulate a task and its pre- and post-reqs into a parent task. This makes
 *  it simple to process the post and pre-reqs, since we don't have to worry about moving over constraints
 *  and such.
 *
 * @author matias
 */

public class LittleJILExpanderPlugin extends ComponentPlugin {

    private static final Logger logger = Logger.getLogger(LittleJILExpanderPlugin.class);
    private IncrementalSubscription diagramSubscription;
    private IncrementalSubscription stepSubscription;
    private IncrementalSubscription littleJILStepsTableSubscription;
    private IncrementalSubscription exceptionHandlerRequestSubscription;

    private DomainService domainService;
    private RootFactory factory;

    private LittleJILStepsTable stepsTable; // used to keep a mapping of task->step

    // the "end time" for new tasks, which keeps increasing...
    private static double endTime = 1.0;



    /**
     * Used by the binding utility through reflection to set my DomainService
     */
    public void setDomainService(DomainService aDomainService) {
        domainService = aDomainService;
        factory = domainService.getFactory();
    }

    private static class DiagramPredicate implements UnaryPredicate {
        public boolean execute(Object o) {
            return (o instanceof Diagram);
        }
    }

    private static class StepPredicate implements UnaryPredicate {
        public boolean execute(Object o) {
            return (o instanceof Step);
        }
    }

    private static class LittleJILStepsTablePredicate implements UnaryPredicate {
        public boolean execute(Object o) {
            return (o instanceof LittleJILStepsTable);
        }
    }

    private static class ExceptionHandlerRequestPredicate implements UnaryPredicate {
        public boolean execute(Object o) {
            return (o instanceof ExceptionHandlerRequest);
        }
    }

    public void setupSubscriptions() {

        // set up the subscription to get diagrams
        diagramSubscription = (IncrementalSubscription) blackboard.subscribe(new DiagramPredicate());
        stepSubscription = (IncrementalSubscription) blackboard.subscribe(new StepPredicate());
        littleJILStepsTableSubscription = (IncrementalSubscription) blackboard.subscribe(new LittleJILStepsTablePredicate());
        exceptionHandlerRequestSubscription = (IncrementalSubscription) blackboard.subscribe(new ExceptionHandlerRequestPredicate());

        logger.info("ready.");

    }

    public void execute() {

        // if there isn't a steps table in the blackboard yet, create one. otherwise, we will
        // use the one that is there.
        if (littleJILStepsTableSubscription.size() == 0) {
            stepsTable = new LittleJILStepsTable();
            blackboard.publishAdd(stepsTable);
        } else {
            stepsTable = (LittleJILStepsTable) littleJILStepsTableSubscription.first();
        }

        // process any diagrams found
        for (Enumeration e = diagramSubscription.getAddedList(); e.hasMoreElements();) {

            Diagram diagram = (Diagram) e.nextElement();
            logger.info("found diagram in blackboard: " + diagram.getName());

            logger.debug("creating tasks...");

            // make a "Root" task under which the diagram's root task will go.
            // the reason to do this is that if the diagram's root task has a restart handler,
            // we need to be able to add the restart handler task *before* the diagram's root task,
            // and for that we need the diagram's root task to belong to a Cougaar Workflow

            NewTask rootTask = factory.newTask();
            rootTask.setVerb(new Verb("ROOT"));

            NewTask task = (NewTask) makeTask(diagram.getRootStep());

            NewWorkflow workflow = factory.newWorkflow();
            workflow.setParentTask(rootTask);
            workflow.addTask(task);
            task.setParentTask(rootTask);
            task.setWorkflow(workflow);

            blackboard.publishAdd(rootTask);
            blackboard.publishAdd(factory.createExpansion(rootTask.getPlan(), rootTask, workflow, null));

        }

        // process any steps that need to be re-posted
        // (as of now, used only in RESTART exception handlers)
        for (Enumeration e = stepSubscription.getAddedList(); e.hasMoreElements();) {

            Step step = (Step) e.nextElement();
            logger.info("found step in blackboard: " + step.getName());

            // TODO: if this step already has a task associated with it, then modify that task
            /*Task task = stepsTable.get(step);
            if (task != null) {
                makeTask(step, task, true);
            }
            else {
                makeTask(step);
            }*/

        }

        for (Enumeration e = exceptionHandlerRequestSubscription.getAddedList(); e.hasMoreElements();) {

            ExceptionHandlerRequest request = (ExceptionHandlerRequest) e.nextElement();
            makeTask(request.getStep(), request, true);

        }

    }


    /**
     * This method makes a Task from a LittleJIL step, setting the necessary properties and creating
     * a workflow with the step's substeps (if any).
     *
     */
    private Task makeTask(Step step) {
        return makeTask(step, true);
    }


    private Task makeTask(Step step, boolean checkRequisites) {
        return makeTask(step, null, checkRequisites);
    }

    /**
     * This method makes a Task from a LittleJIL step, setting the necessary properties and creating
     * a workflow with the step's substeps (if any).
     *
     * WARNING: this method calls itself recursively for each subtask. It will go into an infinite loop
     * if there's some kind of circular reference... TODO: might want to check for that.
     *
     * @param step the LittleJIL step to convert
     * @param checkRequisites true if makeTaskWithRequisites should be called if the step has pre or
     *        post-requisites. This is used mostly so makeTask() can be called from makeTaskWithRequisistes
     *        without going into an infinite loop
     * @param request not null iff makeTask is being called as a cause of a request by the ExceptionHandlerPlugin,
     * @return a Cougaar Task. If the task has subtasks, the task and its expansion are published
     */
    private Task makeTask(Step step, ExceptionHandlerRequest request, boolean checkRequisites) {

        Task task = (request == null ? null : request.getTask());
        if (task == null) {
            logger.info("creating new task for step " + step.getName());

            if (checkRequisites && (step.getPrerequisite() != null || step.getPostrequisite() != null)) {
                logger.debug("step " + step.getName() + " has pre- or post-requisites");
                return makeTaskWithRequisites(step);
            }

            task = factory.newTask();
            ((NewTask) task).setVerb(new Verb(step.getName()));


            // make tasks for any handlers that this step has, and put them in the steps table
            // (they will be used in the ExceptionHandlerPlugin)
            for (Enumeration handlers = step.handlers(); handlers.hasMoreElements();) {
                HandlerBinding handlerBinding = (HandlerBinding) handlers.nextElement();
                if (handlerBinding.getTarget() != null && handlerBinding.getTarget() instanceof Step) {
                    Step handlerStep = (Step) handlerBinding.getTarget();
                    if (handlerStep != null) {
                        stepsTable.put(handlerStep, makeTask(handlerStep));
                    }
                }
            }

            ScoringFunction scorefcn = ScoringFunction.createStrictlyAtValue
                    (new AspectValue(AspectType.END_TIME, getNextEndTime()));
            Preference pref = factory.newPreference(AspectType.END_TIME, scorefcn);
            ((NewTask) task).setPreference(pref);


            // add this task to the task->steps table
            stepsTable.put(task, step);
            stepsTable.put(step, task);
        }
        else {
            logger.info("processing Request for task " + task.getVerb());

            // when we are retrying or restarting multiple times, we need to
            // reset the START_TIME preference for the task, so that handler constraints are handled properly
           /* Preference pref = task.getPreference(AspectType.END_TIME);
            Vector v = new Vector();
            v.add(pref);
            ((NewTask) task).setPreferences(v.elements());*/

        }

        NewWorkflow workflow = factory.newWorkflow();

        // now get subsets of this step and create tasks for those, and put them in the workflow
        // NOTE: tasks are returned in the correct order (according to Little-JIL API docs)
        Task lastTask = null;
        boolean continueFlag = false;   // (only if request is ContinueRequest) indicates that tasks should be now added
        for (Enumeration substepsEnum = step.substeps(); substepsEnum.hasMoreElements();) {
            Step substep = (Step) ((SubstepBinding) substepsEnum.nextElement()).getTarget();

            if (request != null && !continueFlag) {
                if (request.getType() == ExceptionHandlerRequest.CONTINUE ||
                    request.getType() == ExceptionHandlerRequest.TRY) {
                    if (request.getFailedStep() == substep) {
                        continueFlag = true;    // after this we can start adding tasks again
                    }
                    continue;
                }
            }

            // recursive call to create this task -- this will create the task's subtasks, etc
            NewTask subtask = (NewTask) makeTask(substep);

            logger.debug("adding task " + subtask.getVerb() + " to workflow of task " + task.getVerb());
            subtask.setParentTask(task);
            workflow.addTask(subtask);
            subtask.setWorkflow(workflow);

            if (step.getStepKind() == Step.TRY) {
                lastTask = subtask;
                break;  // don't add any more tasks for now!
            }
            // if the parent step is sequential, set this task so it starts after the previous ends
            else if (step.getStepKind() == Step.SEQUENTIAL && lastTask != null) {
                logger.info("making constraint so that task " + subtask.getVerb() + " goes after " + lastTask.getVerb());

                NewConstraint constraint = factory.newConstraint();
                constraint.setConstrainingTask(lastTask);
                constraint.setConstrainingAspect(AspectType.END_TIME);

                constraint.setConstrainedTask(subtask);
                constraint.setConstrainedAspect(AspectType.START_TIME);

                constraint.setConstraintOrder(Constraint.BEFORE);

                workflow.addConstraint(constraint);

            }

            lastTask = subtask;
            // TODO: choice and try (in progress)

        }

        if (lastTask != null) {
            workflow.setParentTask(task);
            NewExpansion expansion = (NewExpansion) factory.createExpansion(task.getPlan(), task, workflow, null);

            //logger.debug("publishing parent task " + task);
            blackboard.publishAdd(task);

            //logger.debug("publishing expansion " + expansion);
            blackboard.publishAdd(expansion);

        }
        // check: if this was a TRY request and we haven't added any tasks, it means there weren't any left
        // and we should post an exception
        else if (request != null && request.getType() == ExceptionHandlerRequest.TRY) {
            logger.info("no more tasks left to try, posting exception");
            LittleJILException exception = new LittleJILException(task, "NoMoreAlternativesException");
            blackboard.publishAdd(exception);
        }



        return task;
    }

    /**
     * Encapsulates a task with pre or post requisistes into a "parent" task that contains as sub-tasks
     * the pre-req, the actual task, and the post-req in sequential order
     * @param step
     * @return
     */
    private Task makeTaskWithRequisites(Step step) {

        RequisiteBinding preReqBinding = step.getPrerequisite();
        RequisiteBinding postReqBinding = step.getPostrequisite();

        if (preReqBinding == null && postReqBinding == null) {
            logger.warn("makeTaskWithRequisites called for a step without requisites");
            return makeTask(step);
        }

        NewTask parentTask = factory.newTask();
        parentTask.setVerb(new Verb(step.getName() + "Parent"));

        // There is a particular corner case regarding pre-requisistes and expansions.
        // If a task with pre-requisite has a RESTART expansion handler, then we
        // need to handler that in the "parent task" that we are creating,
        // because otherwise when the task is restarted the pre-requisite will not be run
        for (Enumeration handlers = step.handlers(); handlers.hasMoreElements();) {
            HandlerBinding handlerBinding = (HandlerBinding) handlers.nextElement();
            if (handlerBinding.getControlFlow() == HandlerBinding.RESTART) {
                stepsTable.put(parentTask, step);
                break;
            }
        }

        logger.debug("created parent task " + parentTask.getVerb());


        NewWorkflow workflow = factory.newWorkflow();

        // create the actual "real" task
        NewTask task = (NewTask) makeTask(step, false);   // the false flag is so that makeTask ignores pre and post-reqs
        task.setParentTask(parentTask);

        // first add the pre-req task, if any
        if (preReqBinding != null) {
            // have to create a task for this pre-req and add a constraint so that it goes before
            final Step preReqStep = ((Reference) preReqBinding.getTarget()).refersTo();
            logger.info("step has prerequisite " + preReqStep.getName() + ", adding constraint");
            NewTask preReqTask = (NewTask) makeTask(preReqStep);

            logger.debug("adding pre-req task " + preReqTask.getVerb() + " to workflow of task " + parentTask.getVerb());
            preReqTask.setParentTask(parentTask);
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

        // now add the task to the workflow (we put them in order)
        workflow.addTask(task);
        task.setWorkflow(workflow);

        if (postReqBinding != null) {
            // have to create a task for this post-req and add a constraint to it goes after

            final Step postReqStep = ((Reference) postReqBinding.getTarget()).refersTo();
            logger.info("step has postrequisite " + postReqStep.getName() + ", adding constraint");
            NewTask postReqTask = (NewTask) makeTask(postReqStep);

            logger.debug("adding post-req task " + postReqTask.getVerb() + " to workflow of task " + parentTask.getVerb());
            postReqTask.setParentTask(parentTask);
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

        workflow.setParentTask(parentTask);
        NewExpansion expansion = (NewExpansion) factory.createExpansion(parentTask.getPlan(), parentTask, workflow, null);

        logger.debug("publishing parent task " + parentTask);
        blackboard.publishAdd(parentTask);

        logger.debug("publishing expansion " + expansion);
        blackboard.publishAdd(expansion);

        return parentTask;
    }

    public static double getNextEndTime() {
        return endTime++;
    }

}
































