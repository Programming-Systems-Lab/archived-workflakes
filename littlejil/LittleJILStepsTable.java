package psl.workflakes.littlejil;

import java.util.Hashtable;

import org.cougaar.planning.ldm.plan.Task;
import laser.littlejil.Step;

/**
 * This table is used by plugins (at least the ExceptionHandlerPlugin) to map between
 * tasks and the original LittleJIL stepts. This is necessary for example to lookup
 * handlers that a task may have.
 *
 * @author matias
 */
public class LittleJILStepsTable {

    private Hashtable table;

    public LittleJILStepsTable() {
        table = new Hashtable();
    }

    public Object put(Task task, Step step) {
        return table.put(task, step);
    }

    public Step get(Task task) {
        return (Step) table.get(task);
    }

}
