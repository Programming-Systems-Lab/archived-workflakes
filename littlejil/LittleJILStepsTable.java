package psl.workflakes.littlejil;

import java.util.Hashtable;
import java.util.Enumeration;
import java.util.Vector;
import java.util.Collection;
import java.io.Serializable;

import org.cougaar.planning.ldm.plan.Task;
import laser.littlejil.Step;

/**
 * This table is used by plugins (eg LittleJILExpanderPlugin and ExceptionHandlerPlugin) to map
 * between tasks and the original LittleJIL steps. This is necessary:
 *   - to lookup HandlerBindings for a task
 *   - to lookup parameterBindings for a task
 *
 * @author matias
 */
public class LittleJILStepsTable implements Serializable {

    private static final Collection EMPTY_COLLECTION = new Vector();
    private Hashtable table;

    public LittleJILStepsTable() {
        table = new Hashtable();
    }

    public void put(Task task, Step step) {
        Entry entry = new Entry(step);
        table.put(task, entry);
    }

    /*public Object put(Step step, Task task) {
        return table.put(step, task);
    }*/

    public Entry getEntry(Task task) {
        return (Entry) table.get(task);
    }

    public Step getStep(Task task) {
        return ((Entry) table.get(task)).getStep();
    }

    public Collection getParameterBindings(Task task) {
        return ((Entry) table.get(task)).getParameterBindings();
    }

    /*public Task getTask(Step step) {
        return (Task) table.get(step);
    }*/

    public static class Entry {
        private Step step;
        private Collection parameterBindings;

        public Entry(Step step) {
            this(step, EMPTY_COLLECTION);
        }

        public Entry(Step step, Collection parameterBindings) {
            this.step = step;
            this.parameterBindings = parameterBindings;
        }

        public Step getStep() {
            return step;
        }

        public Collection getParameterBindings() {
            return parameterBindings;
        }

        public void setParameterBindings(Collection parameterBindings) {
            this.parameterBindings = parameterBindings;
        }

        public void setStep(Step step) {
            this.step = step;
        }
    }

}
