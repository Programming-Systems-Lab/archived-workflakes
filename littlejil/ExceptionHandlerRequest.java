package psl.workflakes.littlejil;

import laser.littlejil.Step;
import org.cougaar.planning.ldm.plan.Task;

import java.io.Serializable;

/**
 * The superclass for "requests" from the ExceptionHandlerPlugin to the LittleJILExpanderPlugin
 * that are used for continuing, restarting, completing tasks.
 * @author matias@cs.columbia.edu
 */
public class ExceptionHandlerRequest implements Serializable {

    private Step step;              // the step that this request refers to
    private Task task;              // the task that represents this step
    private Step failedStep;        // the task which failed (used for CONTINUE and TRY)

    private int type;
    public static final int RESTART = 0;
    public static final int CONTINUE = 1;
    public static final int TRY = 2;
    public static final int COMPLETE = 3;

    public ExceptionHandlerRequest(int type, Step step, Task task, Step failedStep) {
        this.type = type;
        this.step = step;
        this.task = task;
        this.failedStep = failedStep;
    }

    public int getType() {
        return type;
    }

    public Step getStep() {
        return step;
    }

    public Task getTask() {
        return task;
    }

    public Step getFailedStep() {
        return failedStep;
    }

}
