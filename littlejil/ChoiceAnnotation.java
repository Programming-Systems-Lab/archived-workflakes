package psl.workflakes.littlejil;

import java.util.*;

import org.cougaar.core.plugin.Annotation;
import laser.littlejil.Step;

/**
 * This class is used to "annotate" choice tasks, so that the TaskExpander can
 * decide whether to post a choice subtask or not.
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

    private Vector substeps;    // the substeps (have not been converted to tasks yet) among which to choose one

    public ChoiceAnnotation() {
        substeps = new Vector();
    }

    public void addSubstep(Step substep) {
        substeps.add(substep);
    }

    public Enumeration getSubsteps() {
        return substeps.elements();
    }

    public int getNumSubSteps() {
        return substeps.size();
    }

    /**
     * Randomly chooses one of the substeps that are choices for the task having this annotation
     * @return the Step chosen
     */
    public Step chooseSubstep() {

        int i = random.nextInt(substeps.size());
        return (Step) substeps.elementAt(i);

    }

}
