package psl.workflakes.littlejil;

import org.cougaar.core.servlet.SimpleServletSupport;
import org.cougaar.core.servlet.BlackboardServletSupport;
import org.cougaar.core.servlet.BaseServletComponent;
import org.cougaar.core.service.BlackboardService;
import org.cougaar.core.blackboard.BlackboardClient;
import org.cougaar.core.blackboard.SubscriberException;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.Servlet;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.BufferedInputStream;
import java.io.FileInputStream;

import laser.littlejil.Diagram;
import laser.littlejil.Program;

/**
 * Servlet that can load Little-JIL diagrams into the blackboard. Mostly for testing
 * @author matias
 */
public class LittleJILLoaderServletComponent extends BaseServletComponent implements BlackboardClient {

    private static final Logger logger = Logger.getLogger(LittleJILLoaderServletComponent.class);

    protected String getPath() {
        return "/littlejil";
    }

    protected Servlet createServlet() {
        return new LoaderServlet();
    }

    private BlackboardService blackboard;

    public void setBlackboardService(BlackboardService blackboard) {
        this.blackboard = blackboard;
    }

    private class LoaderServlet extends HttpServlet {

        public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

            String diagramName = request.getParameter("diagramName");
            String msg = "";
            if (diagramName != null) {

                Diagram diagram = null;
                try {
                    ObjectInputStream objIn = new ObjectInputStream(
                            new BufferedInputStream(new FileInputStream(diagramName)));

                    Program program = (Program) objIn.readObject();
                    diagram = program.getRootDiagram();

                    blackboard.openTransaction();
                    blackboard.publishAdd(diagram);
                    blackboard.closeTransaction();

                    logger.debug("published diagram");
                    msg = "Published diagram";

                } catch (Exception e) {
                    msg = "Could not load diagram: " + e;
                }
            }

            ServletOutputStream out = response.getOutputStream();
            out.println("<html><style>HTML { font-family: \"Verdana\"; font-size:10pt; }</style><body>");
            out.println("<h2>Welcome to the Little-JIL loader</h2>");
            out.println("<b>" + msg + "</b><br>");
            out.println("<form action=\"" + request.getRequestURI() + "\" method=GET>");
            out.println("Diagram name: <input type=text name=diagramName value=\"" +
                    (diagramName == null ? "" : diagramName) + "\"> " +
                    "<input type=submit>");
            out.println("</form>");


        }
    }

    // from BlackboardClient... weird!
    public long currentTimeMillis() {
        return System.currentTimeMillis();
    }


    public boolean triggerEvent(Object event) {
        return false;
    }

    public String getBlackboardClientName() {
        return toString();
    }

}
