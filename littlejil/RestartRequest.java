package psl.workflakes.littlejil;

import laser.littlejil.Step;
import org.cougaar.planning.ldm.plan.Task;

/**
 * This class is used by the ExceptionHandlerPlugin to indicate to the LittleJILExpander that a
 * task needs to be reposted once again.
 *
 * @author matias@cs.columbia.edu
 */
public class RestartRequest extends ExceptionHandlerRequest{

    public RestartRequest(Step step, Task task) {
        super(step, task);
    }

}
