package psl.workflakes.littlejil;

/**
 * This interface is used for testing... It provides methods for a worklet to tell the implementation
 * what tasks are being executed.
 * @author matias@cs.columbia.edu
 */
public interface TaskMonitor {

    public boolean executing(String taskName);

}
