package psl.workflakes.littlejil;

import org.cougaar.planning.ldm.plan.Task;
import laser.littlejil.Step;

/**
 * This class is used for the TaskExpanderPlugin to ask the LittleJILExpander to
 * make a task from a step (in particular used for CHOICE tasks) that hadn't been made before
 * @author matias
 */
public class MakeTaskRequest {

    private Task parentTask;   // the task under which the Step to be expanded lies
    private Step step;        // the step that needs to be made into a Task

    public MakeTaskRequest(Task parentTask, Step step) {
        this.parentTask = parentTask;
        this.step = step;
    }

    public Task getParentTask() {
        return parentTask;
    }

    public Step getStep() {
        return step;
    }

}
