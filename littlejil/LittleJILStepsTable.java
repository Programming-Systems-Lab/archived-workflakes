package psl.workflakes.littlejil;

import java.util.Hashtable;
import java.io.Serializable;

import org.cougaar.planning.ldm.plan.Task;
import laser.littlejil.Step;

/**
 * This table is used by plugins (at least the ExceptionHandlerPlugin) to map two-way between
 * tasks and the original LittleJIL steps. This is necessary:
 *   - to lookup HandlerBindings for a task: put(Task, Step) and get(Task) are used
 *   - to lookup task implementations of handler steps: put(Step, Task) and get(Step) are used
 *
 * @author matias
 */
public class LittleJILStepsTable implements Serializable {

    private Hashtable table;

    public LittleJILStepsTable() {
        table = new Hashtable();
    }

    public Object put(Task task, Step step) {
        return table.put(task, step);
    }

    public Object put(Step step, Task task) {
        return table.put(step, task);
    }

    public Step get(Task task) {
        return (Step) table.get(task);
    }

    public Task get(Step step) {
        return (Task) table.get(step);
    }

}
