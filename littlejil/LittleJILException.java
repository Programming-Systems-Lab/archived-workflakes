package psl.workflakes.littlejil;

import org.cougaar.planning.ldm.plan.Task;

/**
 * An object used in the plugins representing an exception thrown by a step
 * @author matias
 */
public class LittleJILException {

    private Task task;          // task which threw the exception
    private Object exception;   // the exception being thrown

    public LittleJILException(Task task, Object exception) {
        this.task = task;
        this.exception = exception;
    }

    public Task getTask() {
        return task;
    }

    public Object getException() {
        return exception;
    }

}
