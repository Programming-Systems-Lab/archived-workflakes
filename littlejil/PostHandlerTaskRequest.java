package psl.workflakes.littlejil;

import laser.littlejil.HandlerBinding;
import org.cougaar.planning.ldm.plan.Task;

/**
 * This class is used by the ExceptionHandlerPlugin to ask the LittleJILExpanderPlugin to
 * post handler tasks for exception handlers
 *
 * @author matias
 */
public class PostHandlerTaskRequest {

    private HandlerBinding handlerBinding;
    private Task task;

    public PostHandlerTaskRequest(HandlerBinding handlerBinding, Task task) {
        this.handlerBinding = handlerBinding;
        this.task = task;
    }

    /**
     * @return the handler binding for the handler task that needs to be run
     */
    public HandlerBinding getHandlerBinding() {
        return handlerBinding;
    }

    /**
     * @return the task that the handler task should go before
     */
    public Task getTask() {
        return task;
    }

}
