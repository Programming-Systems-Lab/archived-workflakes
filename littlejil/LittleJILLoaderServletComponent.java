package psl.workflakes.littlejil;

import psl.ai2tv.workflow.assets.*;
import psl.workflakes.littlejil.assets.*;

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

    private ExecAgentAsset lastAsset;   // last asset posted to blackboard

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

        factory.addPropertyGroupFactory(new psl.ai2tv.workflow.assets.PropertyGroupFactory());
        factory.addPropertyGroupFactory(new psl.workflakes.littlejil.assets.PropertyGroupFactory());

        // set the Prototypes for Assets
        {
            ExecClassAgentAsset prototype = (ExecClassAgentAsset)
                    factory.createPrototype(ExecClassAgentAsset.class, "ExecClassAgentProto");

            prototypeRegistryService.cachePrototype("ExecClassAgent", prototype);
        }

        {
            ExecWorkletAgentAsset prototype = (ExecWorkletAgentAsset)
                    factory.createPrototype(ExecWorkletAgentAsset.class, "ExecWorkletAgentProto");

            prototypeRegistryService.cachePrototype("ExecWorkletAgent", prototype);
        }

        {
            ExecAgentAsset prototype = (ExecAgentAsset)
                    factory.createPrototype(ExecAgentAsset.class, "ExecAgentProto");

            prototypeRegistryService.cachePrototype("ExecAgent", prototype);
        }

        // set the prototypes for AI2TV assets
        ReportAsset reportProto = (ReportAsset) factory.createPrototype(ReportAsset.class, "ReportProto");
        prototypeRegistryService.cachePrototype("ReportProto", reportProto);

        ClientAsset clientProto = (ClientAsset) factory.createPrototype(ClientAsset.class, "ClientProto");
        prototypeRegistryService.cachePrototype("ClientProto", clientProto);

    }

    private class LoaderServlet extends HttpServlet {

        public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

            ServletOutputStream out = response.getOutputStream();

            out.println("<html><style>body { font-family: \"Verdana\"; font-size:10pt; }</style><body>");
            out.println("<h2>Welcome to the Little-JIL tester</h2>");


            String diagramName = request.getParameter("diagramName");
            String type = request.getParameter("type");
            String msg = "";

            // for ai2tv fake diagram run
            blackboard.openTransaction();

            if (request.getParameter("ai2tv") != null) {

                final ClientAsset client = (ClientAsset) factory.createInstance("ClientProto");
                client.setClientPG(factory.createPropertyGroup("ClientPG"));
                client.setFramePG(factory.createPropertyGroup("FramePG"));

                ReportAsset reportAsset = (ReportAsset) factory.createInstance("ReportProto");
                NewBucketPG bucketPG = (NewBucketPG) factory.createPropertyGroup("BucketPG");
                bucketPG.setSampleTime(System.currentTimeMillis());
                bucketPG.setGroup(new Vector() {
                    {
                        add(client);
                    }
                });
                reportAsset.setBucketPG(bucketPG);

                blackboard.publishAdd(reportAsset);

                logger.debug("published report asset");
                msg = "Published report asset";

                out.println("<b>" + msg + "</b><br>");
                out.println("<a href=\"javascript:history.go(-1)\">Back</a>");

            } else if (diagramName != null && type != null) {

                // depending on type, add the right ExecAgentAsset type
                // and remove the one that was there before

                if (lastAsset != null) {
                    blackboard.publishRemove(lastAsset);
                    lastAsset = null;
                }

                ExecAgentAsset asset = null;
                if (type.equals("simulate")) {
                    asset = (ExecAgentAsset) factory.createInstance("ExecAgent");
                    NewExecutorPG executorPG = (NewExecutorPG) factory.createPropertyGroup("ExecutorPG");
                    executorPG.setCapabilities("any");

                    asset.setExecutorPG(executorPG);

                } else if (type.equals("class")) {

                    asset = (ExecClassAgentAsset) factory.createInstance("ExecClassAgent");
                    NewExecutorPG executorPG = (NewExecutorPG) factory.createPropertyGroup("ExecutorPG");
                    executorPG.setCapabilities("any");

                    NewClassPG classPG = (NewClassPG) factory.createPropertyGroup("ClassPG");
                    classPG.setClassName(request.getParameter("className"));
                    asset.setExecutorPG(executorPG);
                    ((ExecClassAgentAsset) asset).setClassPG(classPG);

                } else if (type.equals("worklet")) {
                    asset = (ExecWorkletAgentAsset) factory.createInstance("ExecWorkletAgent");
                    NewExecutorPG executorPG = (NewExecutorPG) factory.createPropertyGroup("ExecutorPG");
                    executorPG.setCapabilities("any");
                    asset.setExecutorPG(executorPG);
                }

                if (asset == null) {
                    msg = "Please select a valid running option (no asset created)";
                } else {
                    lastAsset = asset;
                    blackboard.publishAdd(asset);

                    Diagram diagram = null;
                    try {
                        ObjectInputStream objIn = new ObjectInputStream(
                                new BufferedInputStream(new FileInputStream(diagramName)));

                        Program program = (Program) objIn.readObject();
                        diagram = program.getRootDiagram();

                        blackboard.publishAdd(diagram);

                        logger.debug("published diagram");
                        msg = "Published diagram";

                    } catch (Exception e) {
                        msg = "Could not load diagram: " + e;
                    }
                }

                out.println("<b>" + msg + "</b><br>");
                out.println("<a href=\"javascript:history.go(-1)\">Back</a>");

            }
            else {

                out.println("<form action=\"" + request.getRequestURI() + "\" method=GET>");
                out.println("Diagram name: <input type=text name=diagramName value=\"" +
                        (diagramName == null ? "" : diagramName) + "\"> ");
                out.println("<br>");
                out.println("<br><input type=radio name=\"type\" value=\"simulate\">Simulate workflow</input>");
                out.println("<br><input type=radio name=\"type\" value=\"class\">Use class</input>");
                out.println("<input name=\"className\"/>");
                out.println("<br><input type=radio name=\"type\" value=\"worklet\">Use Worklets</input>");
                out.println("<br><br><input type=submit value=\"Run diagram\">");
                out.println("<br><br><input type=submit name=\"ai2tv\" value=\"Run fake AI2TV workflow\"></input>");
                out.println("</form>");
            }

            blackboard.closeTransaction();

            out.println("</body></html>");

        }
    }

    // from BlackboardClient, have to implement... weird!
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
