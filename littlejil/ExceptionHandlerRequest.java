package psl.workflakes.littlejil;

import laser.littlejil.Step;
import org.cougaar.planning.ldm.plan.Task;

import java.io.Serializable;

/**
 * The superclass for "requests" from the ExceptionHandlerPlugin to the LittleJILExpanderPlugin
 * that are used for continuing, restarting, completing tasks.
 * @author matias@cs.columbia.edu
 */
public abstract class ExceptionHandlerRequest implements Serializable {

    protected Step step;              // the step that this request refers to
    protected Task task;              // the task that represents this step

    public ExceptionHandlerRequest(Step step, Task task) {
        this.step = step;
        this.task = task;
    }

    public Step getStep() {
        return step;
    }

    public Task getTask() {
        return task;
    }

}
