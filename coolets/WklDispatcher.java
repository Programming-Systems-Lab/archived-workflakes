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

public class WklDispatcher implements Serializable {
	
  public static void main(String args[]) {
    WklDispatcher disp = new WklDispatcher(args);
  }
  private WklDispatcher(String args[]) {
    if (args.length != 3) {
      System.out.println("usage: java WklDispatcher <rHost> <rName> <rPort>");
      System.exit(0);
  }
    
    String rHost = args[0];
    String rName = args[1];
    int rPort = Integer.parseInt(args[2]);

   try {
      WVM wvm = new WVM(this,  InetAddress.getLocalHost().getHostName(),"WklDispatcher", rPort);
      Worklet wkl = new Worklet(null);
      CooletIncomingJunction junc = new CooletIncomingJunction(rHost, rName, rPort);
      wkl.addJunction(junc);
      wkl.deployWorklet(wvm);
    } catch (java.net.UnknownHostException e) {
      System.out.println("Exception: " + e.getMessage());
      e.printStackTrace();
      System.exit(0);
    } 
  }
}