package psl.workflakes.littlejil;

import java.io.Serializable;

import org.cougaar.planning.ldm.plan.Task;
import org.cougaar.planning.ldm.plan.Allocation;

/**
 * An object used in the plugins representing an exception thrown by a step
 * @author matias
 */
public class LittleJILException implements Serializable {

    private Task task;              // task which threw the exception
    //private Allocation allocation;  // the allocation that corresponds to this task
    private Object exception;       // the exception being thrown
    private LittleJILException nestedException;

    public LittleJILException(Task task, Object exception) {
        this.task = task;
        this.exception = exception;
    }

    /**
     * Makes a new LittleJILException with a nested exception. The task is
     * set to the given exception's parent task.
     * @param nestedException
     */
    public LittleJILException(LittleJILException nestedException) {
        this.nestedException = nestedException;

        task = nestedException.getTask().getWorkflow().getParentTask();
        exception = nestedException.getException();

    }

    /*public LittleJILException(Task task, Allocation allocation, Object exception) {
        this.task = task;
        this.allocation = allocation;
        this.exception = exception;
    }*/

    public Task getTask() {
        return task;
    }

    /*public Allocation getAllocation() {
        return allocation;
    }*/

    public Object getException() {
        return exception;
    }

    public LittleJILException getNestedException() {
        return nestedException;
    }

}
