/*
 * <copyright>
 * Copyright (c) 2001: The Trustees of Columbia University in the
 *  City of New York, Peppo Valetto. All Rights Reserved.
 * </copyright>
 */
 package psl.workflakes.coolets;

import java.util.Enumeration;
import org.cougaar.util.UnaryPredicate;
import org.cougaar.core.cluster.Subscription;
import org.cougaar.core.cluster.IncrementalSubscription;
import org.cougaar.core.plugin.PlugInAdapter;
import psl.worklets.*;
import psl.workflakes.coolets.adaptors.*;

public class CooletIncomingJunction
	extends WorkletJunction
	implements CooletJunctionInf
{
	protected NodeWklInf myTarget;
	protected WklInstallInf myPlugInTarget;
	protected UnaryPredicate myTriggerPred;
	protected String clusterID;
	protected String pluginID;

	public CooletIncomingJunction (String host, String name, int port) {
		super (host, name, port);
		myTriggerPred = null;
		myPlugInTarget = null;
		clusterID = null;
		pluginID = null;
		System.out.println (getClass().getName());
	}
	
	public void init(Object system, WVM wvm) {
		super.init(system, wvm);
		//System.out.println ("system class: " + system.getClass().getName());
		//myTriggerPred = new genericPredicate();
		myTarget = (NodeWklInf) system;
		PlugInAdapter thePlugIn = myTarget.findPlugIn(clusterID, pluginID);
		if (thePlugIn != null && (thePlugIn instanceof psl.workflakes.coolets.WorkletPlugIn))
			myPlugInTarget = (WklInstallInf)thePlugIn;
	}
	

	public void execute() {
		try {
			myPlugInTarget.installJunction(this, myTriggerPred);
		} catch (Exception e) { // right plugin could not be found or installed
			e.printStackTrace();
			throw (new RuntimeException ("Cannot deliver junction to PlugIn " + pluginID + " on cluster " + clusterID));
		}		
	}
	
	//
	// implementation of CooletJunctionInf
	//
	public void embeddedExec(IncrementalSubscription sub) {
		// do something
		System.out.println ("Junction executing on target: " + myTarget.getClass().getName());
		System.out.println (" handling a subscription of size " + sub.size());
	}
	
	public void embeddedExec() {
		// do something
		System.out.println ("Junction executing on target: " + myTarget.getClass().getName());
	}
	
	public Class getPredClass() { 
		if (myTriggerPred == null)
			return null;
			
		return myTriggerPred.getClass();
	}
	
	public UnaryPredicate getPred() { return myTriggerPred; }
	public void setPred(UnaryPredicate pred) {myTriggerPred = pred; }
	public void setClusterID (String cluster) { clusterID = cluster; }
	public String getClusterID() { return clusterID; }
	public void setPluginID (String plugin) { pluginID = plugin; }
	public String getPluginID() { return pluginID; }
}