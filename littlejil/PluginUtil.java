package psl.workflakes.littlejil;

import org.cougaar.planning.ldm.plan.AspectType;
import org.cougaar.planning.ldm.plan.Constraint;
import org.apache.log4j.Logger;

import java.util.*;

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

        while (e != null && e.hasMoreElements()) {
            v.addElement(e.nextElement());
        }

        return v;
    }

    /**
     * Used to keep track of timestamps and log them in an analyzable format.
     * It adds named timestamps to a row of values, formatting them in CSV (comma-separated values) format.
     */
    public static class Timing {

        private static Logger logger = Logger.getLogger(Timing.class);

        private static StringBuffer row = new StringBuffer();
        private static StringBuffer header = new StringBuffer();
        private static StringBuffer lastHeader = new StringBuffer();

        public static void addTimestamp(String name) {
            addTimestamp(name, System.currentTimeMillis());
        }

        /**
         * Add a timestamp that will go in the "current" row of values.
         * @param value
         */
        public static void addTimestamp(String name, long value) {

            if (header.length() > 0) {
                header.append(",");
                row.append(",");
            }

            header.append(name);
            row.append(value);

        }

        public static void newRow() {

            if (row.length() == 0) {
                return; // nothing to print
            }

            // print header row if new or if it has changed from the previous header
            if (lastHeader == null || !header.toString().equals(lastHeader.toString())) {
                logger.debug("\r\n" + header);
            }

            logger.debug(row.toString());

            row = new StringBuffer();
            lastHeader = header;
            header = new StringBuffer();

        }

        // for testing only
        public static void main(String[] args) {

            logger = Logger.getLogger("test");

            addTimestamp("foo");
            addTimestamp("bar");
            newRow();

            addTimestamp("foo");
            addTimestamp("bar");
            newRow();

            addTimestamp("foo");
            addTimestamp("glob");
            addTimestamp("bar");
            newRow();

            addTimestamp("foo");
            addTimestamp("glob");
            addTimestamp("bar");
            newRow();

            addTimestamp("foo");
            addTimestamp("bbb");
            addTimestamp("ccc");
            newRow();

            addTimestamp("glob");
            addTimestamp("foo");
            addTimestamp("bar");
            newRow();
        }

    }
}
