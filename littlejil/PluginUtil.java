package psl.workflakes.littlejil;

import org.cougaar.planning.ldm.plan.AspectType;
import org.cougaar.planning.ldm.plan.Constraint;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Vector;

/**
 * General utility functions
 * @author matias
 */
public class PluginUtil {

    public static String constraintToString(Constraint c) {

        return "[Constraint: " + c.getConstrainedTask().getVerb() + " constrained by " +
                c.getConstrainingTask().getVerb() +
                ", aspect=" + AspectType.ASPECT_STRINGS[c.getConstrainingAspect()] + "]";

    }

    public static Collection collectionFromEnumeration(Enumeration e) {

        Vector v = new Vector();

        while (e.hasMoreElements()) {
            v.addElement(e.nextElement());
        }

        return v;
    }
}
