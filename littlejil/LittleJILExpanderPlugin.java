package psl.workflakes.littlejil;

import java.util.*;

import laser.littlejil.*;
import org.apache.log4j.Logger;
import org.cougaar.core.blackboard.IncrementalSubscription;
import org.cougaar.core.domain.RootFactory;
import org.cougaar.core.plugin.ComponentPlugin;
import org.cougaar.core.service.DomainService;
import org.cougaar.planning.ldm.plan.*;
import org.cougaar.util.UnaryPredicate;

/**
 * The <code>LittleJILExpanderPlugin</code> subscribes to LittleJIL
 * <code>Diagram</code> objects. For each diagram, it gets the root task and
 * calls the internal <code>makeTask</code> method. <code>makeTask</code>
 * creates a Cougaar Task object for the given LittleJIL step, and then
 * recursively calls <code>makeTask</code> for each of the substeps of the
 * current step. As it calls <code>makeTask</code> it sets the constraints
 * appropriately if the step is of sequential kind. It also handles step
 * cardinality, and has special cases for <em>choice</em> tasks and for
 * restarting tasks with <em>continue</em> exception handlers. For tasks with
 * pre- or post-requisites, the method <code>makeTaskWithRequisites</code> is
 * called, which creates a "parent" task with the requisites and the actual
 * tasks as subtasks.
 *
 * It also keeps a table that maps between Tasks and Steps in the blackboard.
 * @author matias
 */
public class LittleJILExpanderPlugin extends ComponentPlugin {

    private static final Logger logger = Logger.getLogger(LittleJILExpanderPlugin.class);
    private IncrementalSubscription diagramSubscription;
    private IncrementalSubscription postHandlerRequestSubscription;
    private IncrementalSubscription stepsTableSubscription;
    private IncrementalSubscription resourceTableSubscription;
    private IncrementalSubscription exceptionHandlerRequestSubscription;
    private IncrementalSubscription makeTaskRequestSubscription;

    private DomainService domainService;
    private RootFactory factory;

    private LittleJILStepsTable stepsTable; // used to keep a mapping of task->step
    private LittleJILResourceTable resourceTable;

    // the "end time" for new tasks, which keeps increasing...
    private static double endTime = 1.0;
    public static final Verb DUMMY_TASK = new Verb("DUMMY_TASK");



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

    private static class StepsTablePredicate implements UnaryPredicate {
        public boolean execute(Object o) {
            return (o instanceof LittleJILStepsTable);
        }
    }

    private static class ResourceTablePredicate implements UnaryPredicate {
           public boolean execute(Object o) {
               return (o instanceof LittleJILResourceTable);
           }
       }

    private static class PostHandlerRequestPredicate implements UnaryPredicate {
        public boolean execute(Object o) {
            return (o instanceof PostHandlerTaskRequest);
        }
    }

    private static class ExceptionHandlerRequestPredicate implements UnaryPredicate {
        public boolean execute(Object o) {
            return (o instanceof ExceptionHandlerRequest);
        }
    }

    private static class MakeTaskRequestPredicate implements UnaryPredicate {
        public boolean execute(Object o) {
            return (o instanceof MakeTaskRequest);
        }
    }

    public void setupSubscriptions() {

        // set up the subscription to get diagrams
        diagramSubscription = (IncrementalSubscription) blackboard.subscribe(new DiagramPredicate());
        stepsTableSubscription = (IncrementalSubscription) blackboard.subscribe(new StepsTablePredicate());
        resourceTableSubscription = (IncrementalSubscription) blackboard.subscribe(new ResourceTablePredicate());
        exceptionHandlerRequestSubscription = (IncrementalSubscription) blackboard.subscribe(new ExceptionHandlerRequestPredicate());
        postHandlerRequestSubscription = (IncrementalSubscription) blackboard.subscribe(new PostHandlerRequestPredicate());
        makeTaskRequestSubscription = (IncrementalSubscription) blackboard.subscribe(new MakeTaskRequestPredicate());

        logger.info("ready.");

    }

    public void execute() {

        // if there isn't a steps table in the blackboard yet, create one. otherwise, we will
        // use the one that is there.
        if (stepsTableSubscription.size() == 0) {
            stepsTable = new LittleJILStepsTable();
            blackboard.publishAdd(stepsTable);
        } else {
            stepsTable = (LittleJILStepsTable) stepsTableSubscription.first();
        }

        resourceTable = (LittleJILResourceTable) resourceTableSubscription.first();
        assert(resourceTable != null);

        /*// TODO: temporary
        if (resourceTableSubscription.size() == 0) {
            resourceTable = new LittleJILResourceTable();
            blackboard.publishAdd(resourceTable);
        } else {
            resourceTable = (LittleJILResourceTable) resourceTableSubscription.first();
        }*/

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

            if (diagram.getRootStep() == null) {
                logger.warn("Diagram has no root step!");
                continue;
            }

            NewTask task = (NewTask) makeTask(diagram.getRootStep());

            NewWorkflow workflow = factory.newWorkflow();
            workflow.setParentTask(rootTask);
            workflow.addTask(task);
            task.setParentTask(rootTask);
            task.setWorkflow(workflow);

            blackboard.publishAdd(rootTask);
            blackboard.publishAdd(factory.createExpansion(rootTask.getPlan(), rootTask, workflow, null));

            PluginUtil.Timing.addTimestamp("publish rootTask");

        }

        // process any HandlerBindings that may have been posted by the ExceptionHandlerPlugin
        // for each one, we need to create the task for that handler (if any) and post it
        // so it executes before its source task
        for (Enumeration e = postHandlerRequestSubscription.getAddedList(); e.hasMoreElements();) {

            PostHandlerTaskRequest request = (PostHandlerTaskRequest) e.nextElement();
            HandlerBinding binding = request.getHandlerBinding();
            Task task = request.getTask();

            if (binding.getTarget() != null && binding.getTarget() instanceof Step) {

                logger.debug("found handler binding in blackboard for task: " + task.getVerb());

                NewTask handlerTask = (NewTask) makeTask((Step)binding.getTarget());

                // insert it into the task's workflow (ie, at the same level as the given task)
                NewWorkflow parentWorkflow = (NewWorkflow) task.getWorkflow();

                parentWorkflow.addTask(handlerTask);
                handlerTask.setWorkflow(parentWorkflow);
                handlerTask.setParentTask(parentWorkflow.getParentTask());

                NewConstraint constraint = factory.newConstraint();

                /*if (binding.getControlFlow() == HandlerBinding.RETHROW) {
                    constraint.setConstrainingTask(task);
                    constraint.setConstrainedTask(handlerTask);
                }
                else {*/
                    constraint.setConstrainingTask(handlerTask);
                    constraint.setConstrainedTask(task);
                //}

                constraint.setConstrainingAspect(AspectType.END_TIME);
                constraint.setConstrainedAspect(AspectType.START_TIME);

                constraint.setConstraintOrder(Constraint.BEFORE);

                parentWorkflow.addConstraint(constraint);
            }


        }


        for (Enumeration e = exceptionHandlerRequestSubscription.getAddedList(); e.hasMoreElements();) {

            ExceptionHandlerRequest request = (ExceptionHandlerRequest) e.nextElement();
            makeTask(request.getStep(), request, true);

        }


        for (Enumeration e = makeTaskRequestSubscription.getAddedList(); e.hasMoreElements();) {

            MakeTaskRequest request = (MakeTaskRequest) e.nextElement();

            NewTask subtask = (NewTask) makeTask(request.getStep());

            NewWorkflow workflow = factory.newWorkflow();
            Task parentTask = request.getParentTask();

            workflow.setParentTask(parentTask);
            workflow.addTask(subtask);

            subtask.setParentTask(parentTask);
            subtask.setWorkflow(workflow);

            blackboard.publishAdd(factory.createExpansion(parentTask.getPlan(), parentTask, workflow, null));

            blackboard.publishAdd(parentTask);

            logger.debug("published new workflow for a MakeTaskRequest");
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
     * @param request if not null, we replace the workflow instead of creating a new task (this is used
     * by the ExceptionHandlerPlugin when restarting tasks, etc)
     * @return a Cougaar Task. If the task has subtasks, the task and its expansion are published
     */
    private Task makeTask(Step step, ExceptionHandlerRequest request, boolean checkRequisites) {

        Task task = (request == null ? null : request.getTask());
        if (task == null) {

            if (checkRequisites && (step.getPrerequisite() != null || step.getPostrequisite() != null)) {
                logger.debug("step " + step.getName() + " has pre- or post-requisites");
                return makeTaskWithRequisites(step, request);
            }

            logger.debug("creating new task for step " + step.getName());

            task = factory.newTask();
            ((NewTask) task).setVerb(new Verb(step.getName()));

            ScoringFunction scorefcn = ScoringFunction.createStrictlyAtValue
                    (new AspectValue(AspectType.END_TIME, getNextEndTime()));
            Preference pref = factory.newPreference(AspectType.END_TIME, scorefcn);
            ((NewTask) task).setPreference(pref);

            // add this task to the task->steps table
            stepsTable.put(task, step);
        }
        else {

            // only for RESTART handlers, if the task has pre-reqs, we call makeTaskWithRequisites()
            if (request.getType() == ExceptionHandlerRequest.RESTART &&
                    (step.getPrerequisite() != null || step.getPostrequisite() != null)) {
                return makeTaskWithRequisites(step, request);
            }


            logger.debug("processing ExceptionHandlerRequest for task " + task.getVerb());

            // remove the task's current expansion
            if (task.getPlanElement() != null) {
                blackboard.publishRemove(task.getPlanElement());
            }

        }

        NewWorkflow workflow = factory.newWorkflow();
        Task lastTask = null;

        if (request != null && request.getType() == ExceptionHandlerRequest.COMPLETE) {
            // create a "dummy" task, that will always be "executed" by the ExecutorPlugin, and put in the workflow
            NewTask dummyTask= factory.newTask();
            dummyTask.setVerb(DUMMY_TASK);

            ScoringFunction scorefcn = ScoringFunction.createStrictlyAtValue
                    (new AspectValue(AspectType.END_TIME, getNextEndTime()));
            Preference pref = factory.newPreference(AspectType.END_TIME, scorefcn);
            dummyTask.setPreference(pref);

            workflow.addTask(dummyTask);
            dummyTask.setParentTask(task);
            dummyTask.setWorkflow(workflow);

            lastTask = dummyTask;
        }
        else {
            // get subsets of this step and create tasks for those, and put them in the workflow
            // NOTE: tasks are returned in the correct order (according to Little-JIL API docs)
            boolean continueFlag = false;   // (only if request is ContinueRequest) indicates that tasks should be now added
            ChoiceAnnotation choiceAnnotation = null;   // used only for Choice steps

            if (step.getStepKind() == Step.CHOICE) {
                choiceAnnotation = new ChoiceAnnotation();
                task.setAnnotation(choiceAnnotation);
            }

            for (Enumeration substepsEnum = step.substeps(); substepsEnum.hasMoreElements();) {
                SubstepBinding substepBinding = ((SubstepBinding) substepsEnum.nextElement());
                Step substep = (Step) substepBinding.getTarget();

                if (choiceAnnotation != null) {
                    choiceAnnotation.addSubstep(substep);
                    continue;   // we don't create actual tasks at this point
                }

                if (request != null && !continueFlag) {
                    if (request.getType() == ExceptionHandlerRequest.CONTINUE ||
                            request.getType() == ExceptionHandlerRequest.TRY) {
                        if (request.getFailedStep() == substep) {
                            continueFlag = true;    // after this we can start adding tasks again
                        }
                        continue;
                    }
                }

                int count = 1;
                Collection collection = null;   // used only if the cardinality has a resource parameter
                Iterator iterator = null;       // ditto

                Cardinality cardinality = substepBinding.getCardinality();
                ParameterDeclaration cardinalityParam = null;
                if (cardinality != null) {
                    count = cardinality.getUpperBound();

                    // check if cardinality is bound to a resource
                    cardinalityParam = cardinality.getParameter();
                    if (cardinalityParam != null) {
                        // get resource -- TODO: for now, it MUST to be a Collection
                        Object o = resourceTable.getResource(step.getDiagram(), cardinalityParam.getName());
                        if (o == null) {
                            logger.warn("Cardinality resource " + cardinalityParam.getName() + " not found!");
                        }
                        else if (!(o instanceof Collection)) {
                            logger.warn("Cardinality resource " + cardinalityParam.getName() + " found, but not a Collection");
                        }
                        else {
                            collection = (Collection) o;
                            logger.info("found resource iterator of size "+ collection.size());
                            iterator = collection.iterator();
                        }
                    }
                }

                if (collection != null) {
                    // loop for the smallest of count or the size of the resource collection
                    count = (count < collection.size() ? count : collection.size());
                }

                for (int i=0;i<count ;i++) {

                    NewTask subtask = makeSubTask(substep, substepBinding, task, workflow);

                    // if we are iterating through a collection, set the resource to be an iterator for this collection
                    // that way, the AbstractTaskExecutorPlugin can get the next element from the iterator when the
                    // task is executed. This provides some degree of "late binding", and also gets around the problem
                    // that all the substasks share the same Step (so they couldn't each have a different resource bound
                    // to it).
                    if (iterator != null) {
                        StepInterface stepInterface = substep.getStepInterface();
                        assert(stepInterface != null);

                        ParameterDeclaration param = stepInterface.getParameter(cardinalityParam.getName());
                        assert(param != null);

                        logger.debug("setting resource value for substep " + substep.getName() + " to " + iterator);
                        param.setParameterValue(iterator);
                    }


                    if (step.getStepKind() == Step.TRY) {
                        lastTask = subtask;
                        break;  // don't add any more tasks for now!
                    }
                    // if the parent step is sequential, set this task so it starts after the previous ends
                    else if (step.getStepKind() == Step.SEQUENTIAL && lastTask != null) {
                        logger.debug("making constraint so that task " + subtask.getVerb() + " goes after " + lastTask.getVerb());

                        NewConstraint constraint = factory.newConstraint();
                        constraint.setConstrainingTask(lastTask);
                        constraint.setConstrainingAspect(AspectType.END_TIME);

                        constraint.setConstrainedTask(subtask);
                        constraint.setConstrainedAspect(AspectType.START_TIME);

                        constraint.setConstraintOrder(Constraint.BEFORE);

                        workflow.addConstraint(constraint);

                    }

                    lastTask = subtask;
                }
            }

        }

        if (lastTask != null) {
            workflow.setParentTask(task);
            NewExpansion expansion = (NewExpansion) factory.createExpansion(task.getPlan(), task, workflow, null);

            //logger.debug("publishing parent task " + task);
            blackboard.publishAdd(task);

            //logger.debug("publishing expansion " + expansion);
            blackboard.publishAdd(expansion);

        }
        else if (request != null && request.getType() == ExceptionHandlerRequest.TRY) {
            // check: if this was a TRY request and we haven't added any tasks, it means there weren't any left
            // and we should post an exception

            logger.debug("no more tasks left to try, posting exception");
            LittleJILException exception = new LittleJILException(task, "NoMoreAlternativesException");
            blackboard.publishAdd(exception);
        }



        return task;
    }



    /**
     * Encapsulates a task with pre or post requisistes into a "parent" task that contains as sub-tasks
     * the pre-req, the actual task, and the post-req in sequential order
     * @param step the step to create a task for
     * @param request if not null, instead of creating a new task we replace the workflow for the existing task
     * @return
     */
    private Task makeTaskWithRequisites(Step step, ExceptionHandlerRequest request) {

        RequisiteBinding preReqBinding = step.getPrerequisite();
        RequisiteBinding postReqBinding = step.getPostrequisite();

        if (preReqBinding == null && postReqBinding == null) {
            logger.warn("makeTaskWithRequisites called for a step without requisites");
            return makeTask(step);
        }

        Task parentTask = (request != null ? request.getTask() : null);
        if (parentTask == null) {
            parentTask = factory.newTask();
            ((NewTask) parentTask).setVerb(new Verb(step.getName() + "Parent"));

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
        }
        else {
            logger.debug("processing ExceptionHandlerRequest for task " + parentTask.getVerb());

            // remove the task's current expansion
            if (parentTask.getPlanElement() != null) {
                blackboard.publishRemove(parentTask.getPlanElement());
            }
        }

        NewWorkflow workflow = factory.newWorkflow();

        // create the actual "real" task
        NewTask task = (NewTask) makeTask(step, false);   // the false flag is so that makeTask ignores pre and post-reqs
        task.setParentTask(parentTask);

        // first add the pre-req task, if any
        if (preReqBinding != null) {
            // have to create a task for this pre-req and add a constraint so that it goes before
            final Step preReqStep = ((Reference) preReqBinding.getTarget()).refersTo();
            logger.debug("step has prerequisite " + preReqStep.getName() + ", adding constraint");
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
            logger.debug("step has postrequisite " + postReqStep.getName() + ", adding constraint");
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

    private NewTask makeSubTask(Step step, SubstepBinding substepBinding, Task parentTask, NewWorkflow workflow) {
        // recursive call to create this task -- this will create the task's subtasks, etc
        NewTask subtask = (NewTask) makeTask(step);

        // set the parameter bindings for this task
        LittleJILStepsTable.Entry entry = stepsTable.getEntry(subtask);
        assert(entry != null);  // all tasks should have an entry

        // initialize the subtasks's in parameters... note that these values might get
        // updated later by the TaskExpander (if for example the parameters that they are getting
        // values from are updated by one of the subtasks)
        for (Enumeration parameterBindings = substepBinding.parameterBindings(); parameterBindings.hasMoreElements();) {
            ParameterBinding binding = (ParameterBinding) parameterBindings.nextElement();
            if (binding.getBindingMode() == ParameterBinding.COPY_IN ||
                    binding.getBindingMode() == ParameterBinding.COPY_IN_AND_OUT) {

                ParameterDeclaration childDeclaration = binding.getDeclarationInChild();
                ParameterDeclaration parentDeclaration = binding.getDeclarationInParent();

                if (parentDeclaration.getParameterValue() == null) {

                    // instantiate it a new parameter value
                    logger.debug("instantiating a new object for parent parameter " + parentDeclaration.getName());
                    try {
                        Class c = Class.forName(parentDeclaration.getParameterClassName());

                        // HACK to have an "interesting" value in some cases
                        Object o = null;
                        if (c == String.class) {
                            o = "42";
                        }
                        else {
                            o = c.newInstance();
                        }

                        parentDeclaration.setParameterValue(o);
                    } catch (Exception e) {
                        logger.warn("Could not instantiate parameter: " + e);
                    }

                }

                childDeclaration.setParameterValue(parentDeclaration.getParameterValue());
            }
        }

        entry.setParameterBindings(PluginUtil.collectionFromEnumeration(substepBinding.parameterBindings()));

        logger.debug("adding task " + subtask.getVerb() + " to workflow of task " + parentTask.getVerb());
        subtask.setParentTask(parentTask);
        workflow.addTask(subtask);
        subtask.setWorkflow(workflow);
        return subtask;
    }

    public static double getNextEndTime() {
        return endTime++;
    }

}

