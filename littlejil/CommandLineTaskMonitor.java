package psl.workflakes.littlejil;

import psl.worklets.WVM;
import org.apache.log4j.Logger;

/**
 * A command-line implementation of a TaskMonitor
 * @author matias@cs.columbia.edu
 */
public class CommandLineTaskMonitor implements TaskMonitor {

    private static final Logger logger = Logger.getLogger(CommandLineTaskMonitor.class);

    public static void main(String[] args) {
        new CommandLineTaskMonitor(args[0]);
    }

    public CommandLineTaskMonitor(String hostname) {

        logger.debug("initializing WVM...");
        WVM wvm = new WVM(this,hostname,"TaskMonitor");

    }

    public boolean executing(String taskName) {

        logger.info("executing " + taskName);
        return !(taskName.endsWith("Fail"));


    }

}
