package psl.workflakes.littlejil;

import org.apache.log4j.Logger;
import org.cougaar.core.blackboard.IncrementalSubscription;
import org.cougaar.core.plugin.SimplePlugin;
import org.cougaar.core.plugin.ComponentPlugin;
import org.cougaar.core.service.BlackboardService;
import org.cougaar.util.UnaryPredicate;
import psl.workflakes.littlejil.xmlschema.Diagram;

import java.util.Enumeration;

/**
 * This class is just to test posting and getting LittleJIL castor objects
 * from a cougaar blackboard
 * @author matias
 */

public class TestLittleJILPlugin extends ComponentPlugin {
    private static final String DIAGRAM_FILENAME = "test-diagram.xml";

    private static final Logger logger = Logger.getLogger(TestLittleJILPlugin.class);
    private IncrementalSubscription diagramSubscription;

    public void setupSubscriptions() {

        BlackboardService blackBoard = getBlackboardService();

        Diagram diagram = null;
        try {
            diagram = LittleJILXMLParser.loadDiagram(DIAGRAM_FILENAME);
        } catch (Exception e) {
            logger.fatal("Could not load diagram: " + e);
            return;
        }

        blackBoard.publishAdd(diagram);
        logger.debug("published diagram");


        // now set up the subscription to get diagrams
        diagramSubscription = (IncrementalSubscription) blackBoard.subscribe(new UnaryPredicate () {
            public boolean execute(Object o) {
                return (o instanceof Diagram);
            }
        });


    }

    public void execute() {

        for (Enumeration e = diagramSubscription.getAddedList();e.hasMoreElements();) {
            logger.debug("found diagram in blackboard:");
            Diagram diagram = (Diagram) e.nextElement();
            LittleJILXMLParser.outputDiagram(diagram);
        }

    }

}
