/*
 * <copyright>
 * Copyright (c) 2001: The Trustees of Columbia University in the
 *  City of New York, Peppo Valetto. All Rights Reserved.
 * </copyright>
 */
 package psl.workflakes.coolets;

import java.io.Serializable;
import java.net.*;
import psl.worklets.*;
import psl.workflakes.coolets.adaptors.*;

/**
 *
 * <p>Title: WklDispatcher</p>
 * <p>Description: Commodity skeleton class for building a mechanism to dispatch Workflow Definition Junctions onto a
 * {@link WorkletNode WorkletNode}.
 * Use as an example: modify constuctor to fit the application needs.</p>
 * <p>Copyright: Copyright (c) 2002: The Trustees of Columbia University in the
 *  City of New York, Peppo Valetto. All Rights Reserved.</p>
 * @author Peppo Valetto
 * @version 1.0
 */
public class WklDispatcher implements Serializable {

  /**
   * Command line main.
   * @param args Takes 3 command line parameters:
   * 1) Host name of the <code>WorkletNode</code>
   * 2) RMI Name of the WVM running on the target <code>WorkletNode</code>
   * 3) RMI port of the WVM running on the target <code>WorkletNode</code>
   */
  public static void main(String args[]) {
    WklDispatcher disp = new WklDispatcher(args);
  }
  private WklDispatcher(String args[]) {
    if (args.length != 4) {
      System.out.println("usage: java WklDispatcher <rHost> <rName> <rPort> <sockPort>");
      System.exit(0);
  }

    String rHost = args[0];
    String rName = args[1];
    int rPort = Integer.parseInt(args[2]);
    int sockPort = Integer.parseInt(args[3]);

   try {
      WVM wvm = new WVM(this,  InetAddress.getLocalHost().getHostName(),"WklDispatcher", rPort,
      			null, null, null, null, null, null, 0);
      Worklet wkl = new Worklet(null);
      CooletIncomingJunction junc = new CooletIncomingJunction(rHost, rName, rPort, sockPort);
      wkl.addJunction(junc);
      wkl.deployWorklet(wvm);
    } catch (java.net.UnknownHostException e) {
      System.out.println("Exception: " + e.getMessage());
      e.printStackTrace();
      System.exit(0);
    }
  }
}