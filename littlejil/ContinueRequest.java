package psl.workflakes.littlejil;

import laser.littlejil.Step;

import java.util.Hashtable;

import org.cougaar.planning.ldm.plan.Task;

/**
 * This class is used by the ExceptionHandlerPlugin to indicate to the LittleJILExpander that a
 * task needs to be reposted for completion. It contains information as to what subtasks
 * have not yet been allocated, from which the LittleJILExpander can deduce which need to be
 * posted for the task to complete
 *
 * @author matias@cs.columbia.edu
 */
public class ContinueRequest extends ExceptionHandlerRequest {

    Hashtable incompleteSteps;  // the substeps that have not been completed yet

    public ContinueRequest(Step step, Task task) {
        super(step, task);

        incompleteSteps = new Hashtable();
    }

    public void addIncompleteStep(Step step) {
        incompleteSteps.put(step, step);
    }

    public boolean isIncomplete(Step step) {
        return incompleteSteps.containsKey(step);
    }
}
