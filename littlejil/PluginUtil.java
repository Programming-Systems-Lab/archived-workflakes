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

        while (e.hasMoreElements()) {
            v.addElement(e.nextElement());
        }

        return v;
    }

    /**
     * Used to keep track of timestamps and log them in an analyzable format
     */
    public static class Timing {

        private static final Logger logger = Logger.getLogger(Timing.class);

        private static ArrayList columns = new ArrayList();
        private static StringBuffer row = new StringBuffer();
        private static int currentCol = 0;
        private static boolean printColumns = false;

        public static void addTimestamp(String name) {
            addTimestamp(name, System.currentTimeMillis());
        }

        /**
         * Add a timestamp that will go in the "current" row of values.
         * @param value
         */
        public static void addTimestamp(String name, long value) {

            if (columns.size() == currentCol) {
                columns.add(name);
                printColumns = true;
            } else if (!columns.get(currentCol).equals(name)) {

                // if the name matches the next column, then assume the current column
                // is no longer used, and remove it
                if (columns.size() > currentCol + 1 && columns.get(currentCol + 1).equals(name)) {
                    columns.remove(currentCol);
                } else {
                    // otherwise, we assume this is a new column and add it
                    columns.add(currentCol, name);
                }
                printColumns = true;
            }

            if (row.length() > 0) {
                row.append(",");
            }

            row.append(value);

            currentCol++;
        }

        public static void newRow() {

            if (row.length() == 0) {
                return; // nothing to print
            }

            if (printColumns) {
                logger.debug("");
                StringBuffer buf = new StringBuffer();
                for (int i = 0; i < columns.size(); i++) {
                    String s = (String) columns.get(i);
                    if (i > 0) {
                        buf.append(",");
                    }
                    buf.append(s);
                }
                logger.debug(buf.toString());
                printColumns = false;
            }

            logger.debug(row.toString());

            row = new StringBuffer();
            currentCol = 0;
        }

        // for testing only
        public static void main(String[] args) {
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
            addTimestamp("bar");
            newRow();

            addTimestamp("glob");
            addTimestamp("foo");
            addTimestamp("bar");
            newRow();
        }

    }
}
