/*
 * <copyright>
 * Copyright (c) 2001: The Trustees of Columbia University in the
 *  City of New York, Peppo Valetto. All Rights Reserved.
 * </copyright>
 */
 package psl.workflakes.exercise.tutorial;

import java.io.Serializable;
import java.net.*;
import psl.worklets.*;
import psl.workflakes.coolets.*;

public class Exc6WklDispatcher {
	
	public static void main(String args[]) {
    Exc6WklDispatcher disp = new Exc6WklDispatcher(args);
  }
  private Exc6WklDispatcher(String args[]) {
    if (args.length != 4) {
      System.out.println("usage: java WklDispatcher <rHost> <rName> <rPort> <sockPort>");
      System.exit(0);
    }
    
    String rHost = args[0];
    String rName = args[1];
    int rPort = Integer.parseInt(args[2]);
    int sockPort = Integer.parseInt(args[3]);
    
    try {
      WVM wvm = new WVM(this,  InetAddress.getLocalHost().getHostName(),"WklDispatcher",  rPort,
      			null, null, null, null, null, null, 0);
      
      // worklet for ManagementAllocator
      Worklet wkl = new Worklet(null);
      MgrAllocJunction junc1 = new MgrAllocJunction(rHost, rName, rPort, sockPort);
      junc1.setPred (new isTaskPredicate());
      junc1.setClusterID ("Management");
      junc1.setPluginID ("psl.workflakes.exercise.tutorial.ManagerAllocatorPlugIn[]");
      wkl.addJunction(junc1);
      wkl.deployWorklet(wvm);
      
      // worklet for DevelopmentAllocator
      wkl = new Worklet(null);
      DevAllocJunction junc2 = new DevAllocJunction(rHost, rName, rPort, sockPort);
      junc2.setPred (new isDevTaskPredicate());
      junc2.setClusterID ("Development");
      junc2.setPluginID ("psl.workflakes.exercise.tutorial.DevelopmentAllocatorPlugIn[]");
      wkl.addJunction(junc2);
      wkl.deployWorklet(wvm);
    } catch (java.net.UnknownHostException e) {
      System.out.println("Exception: " + e.getMessage());
      e.printStackTrace();
      System.exit(0);
    } 
  }
}