package psl.workflakes.littlejil;

import java.util.*;

import org.cougaar.core.plugin.Annotation;
import org.cougaar.planning.ldm.plan.Task;

/**
 * This class is used to "annotate" choice tasks, so that the TaskExpander can
 * decide whether to post a choice subtask or not.
 * The same ChoiceAnnotation instance should be placed in every immediate subtask of a Choice task.
 * Currently a shouldChoose() method is given that randomly decides if a given subtask should be chosen or not
 *
 * WARNING: according to the Cougaar API, Annotations should only be used by the plugin that creates them
 * (in this case, the LittleJILExpanderPlugin), because they are not shared between different clusters.
 * This implementation is assuming that the LittleJILExpanderPlugina and the TaskExpanderPlugins are indeed
 * running within the same cluster.
 *
 * @author matias
 */
public class ChoiceAnnotation implements Annotation {

    private static Random random = new Random();   // used to randomly choose tasks

    private boolean chosen;     // true if a subtask has already been chosen
    private Vector tasks;       // the subtasks among which to choose one

    // for subtasks of tasks that may or may not be chosen, we keep a link to the parent Annotation
    // so that the decision at the TaskExpander can be based on the parent task, and not the individual
    // subtasks of it.
    private ChoiceAnnotation parentAnnotation;


    public ChoiceAnnotation() {
        this(null);
    }

    public ChoiceAnnotation(ChoiceAnnotation parentAnnotation) {
        this.parentAnnotation = parentAnnotation;
        tasks = new Vector();
    }

    public boolean isChosen() {
        return chosen;
    }

    public void setChosen(boolean chosen) {
        this.chosen = chosen;
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public Enumeration getTasks() {
        return tasks.elements();
    }

    public int getNumTasks() {
        return tasks.size();
    }

    /**
     * Randomly decides if a given task should be chosen from the ones in this choice group.
     * To decide, a random integer is generated between (0,number of tasks]. If the given task
     * is the one at the randomly chosen position in the tasks vector, then the task is considered
     * chosen. Otherwise, false is returned. In either case, the task is removed from the choice vector.
     * This way, there will always be a task chosen for each choice group.
     * @param task
     * @return true if the task should be chosen
     */
    public boolean shouldChoose(Task task) {

        if (parentAnnotation != null) {
            // refer this to the parent annotation
            return parentAnnotation.shouldChoose(task.getWorkflow().getParentTask());
        }

        if (chosen) {
            return false;
        }
        else if (!tasks.contains(task)) {
            return false;
        }
        else {
            int i = random.nextInt(tasks.size());
            Task t = (Task) tasks.elementAt(i);

            tasks.removeElement(task);  // will not participate in choice any more

            if (t == task) {
                chosen = true;
                return true;
            }
            else {
                return false;
            }
        }

    }

}
