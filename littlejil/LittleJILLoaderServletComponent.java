package psl.workflakes.littlejil;

import psl.ai2tv.workflow.assets.*;

import java.io.*;
import java.util.Vector;
import javax.servlet.*;
import javax.servlet.http.*;

import laser.littlejil.Diagram;
import laser.littlejil.Program;
import org.apache.log4j.Logger;
import org.cougaar.core.blackboard.BlackboardClient;
import org.cougaar.core.service.BlackboardService;
import org.cougaar.core.service.PrototypeRegistryService;
import org.cougaar.core.service.DomainService;
import org.cougaar.core.servlet.BaseServletComponent;
import org.cougaar.core.domain.RootFactory;

/**
 * Servlet that can load Little-JIL diagrams into the blackboard. Mostly for testing
 * @author matias
 */
public class LittleJILLoaderServletComponent extends BaseServletComponent implements BlackboardClient {

    private static final Logger logger = Logger.getLogger(LittleJILLoaderServletComponent.class);
    private RootFactory factory;

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

    public void setDomainService(DomainService domainService) {
        factory = domainService.getFactory();
    }

    public void setPrototypeRegistryService(PrototypeRegistryService prototypeRegistryService) {

        factory.addPropertyGroupFactory(new PropertyGroupFactory());

        // set the Prototypes for Assets
        ReportAsset reportProto = (ReportAsset) factory.createPrototype(ReportAsset.class, "ReportProto");
        prototypeRegistryService.cachePrototype("ReportProto", reportProto);

        ClientAsset clientProto = (ClientAsset)factory.createPrototype(ClientAsset.class, "ClientProto");
        prototypeRegistryService.cachePrototype("ClientProto", clientProto);

    }

    private class LoaderServlet extends HttpServlet {

        public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

            String diagramName = request.getParameter("diagramName");
            String msg = "";
            if (request.getParameter("ai2tv") != null) {

                final ClientAsset client = (ClientAsset) factory.createInstance("ClientProto");
                client.setClientPG(factory.createPropertyGroup("ClientPG"));
                client.setFramePG(factory.createPropertyGroup("FramePG"));

                ReportAsset reportAsset = (ReportAsset) factory.createInstance("ReportProto");
                NewBucketPG bucketPG = (NewBucketPG) factory.createPropertyGroup("BucketPG");
                bucketPG.setSampleTime(System.currentTimeMillis());
                bucketPG.setGroup(new Vector() { { add(client);} });
                reportAsset.setBucketPG(bucketPG);

                blackboard.openTransaction();
                blackboard.publishAdd(reportAsset);
                blackboard.closeTransaction();

                logger.debug("published report asset");
                msg = "Published report asset";


            }
            else if (diagramName != null) {

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
            out.println("<br><br><input type=submit name=\"ai2tv\" value=\"Test ai2tv diagram\">");
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
