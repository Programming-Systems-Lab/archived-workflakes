package psl.workflakes.littlejil;

import org.cougaar.planning.ldm.plan.AspectType;
import org.cougaar.planning.ldm.plan.Constraint;

/**
 * 
 * @author matias
 */
public class PluginUtil {
    public static String constraintToString(Constraint c) {

        return "[Constraint: " + c.getConstrainedTask().getVerb() + " constrained by " +
                c.getConstrainingTask().getVerb() +
                ", aspect=" + AspectType.ASPECT_STRINGS[c.getConstrainingAspect()] + "]";

    }
}
