package psl.workflakes.littlejil;

import java.util.Hashtable;

/**
 * This interface is implemented by classes that can be used to execute Little-JIL tasks via
 * the TaskExecutorClassPlugin.
 *
 * @author matias@cs.columbia.edu
 */
public interface ExecutableTask {

    /**
     * Executes the given method in the implementing class.
     * @param method the method name to execute. Note that this is up to the class to interpret,
     * and does not necessarily map to any real methods.
     * @param inParams input parameters to the task, as defined in the Little-JIL diagram.
     * @param outParams optional output parameters that should be copied-out of this task, as defined
     * in the Little-JIL diagram.
     * @throws Exception if an error occurred.
     */
    public void execute(String method, Hashtable inParams, Hashtable outParams) throws Exception;

}
