/*
 * <copyright>
 * Copyright (c) 2001: The Trustees of Columbia University in the
 *  City of New York, Peppo Valetto. All Rights Reserved.
 * </copyright>
 */

package psl.workflakes.coolets;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import java.util.Set;
import java.lang.reflect.*;
import java.net.*;

import org.cougaar.core.cluster.IncrementalSubscription;
import org.cougaar.util.StateModelException;
import org.cougaar.util.UnaryPredicate;
import org.cougaar.core.cluster.ClusterServesPlugIn;
import org.cougaar.core.plugin.*;
import org.cougaar.core.plugin.util.PlugInHelper;
import org.cougaar.core.society.Communications;

import psl.worklets.*;
import psl.workflakes.coolets.adaptors.*;

public class WorkletPlugIn
	extends SimplePlugIn
	implements WklInstallInf
	 {
	protected WVM myWVM;
	private String comAddress;
	private String uniqueName;
	private int comPort;
	private CooletJunctionInf execEngine = null;
	private Hashtable subsWithBehaviors = new Hashtable();
	
	private String host;

	protected void setupSubscriptions()
	{	  
		ClusterServesPlugIn cluster = getCluster();
		comPort = Communications.getPort();
		uniqueName = cluster.getClusterIdentifier().toString();

		String pluginName = getSubscriptionClientName();
		try {
			comAddress = InetAddress.getLocalHost().getHostName();
		} catch (java.net.UnknownHostException e) {
      		System.out.println("Exception: " + e.getMessage());
      		e.printStackTrace();
      		System.exit(0);
      	}
		System.out.println ("Cluster name: " + uniqueName + " PlugIn name: " + pluginName);
		//System.out.println (this.toString());
		// myWVM = new WVM (this, comAddress, uniqueName, comPort);
		
		// now get a handle for the Node's WVM
		if (cluster instanceof WorkletClusterImpl) {
			myWVM = ((WorkletClusterImpl)cluster).getNodeWVM();
		}
		else {
			// handle this case better !!!
			myWVM = null;
		}			
		
		// now do whatever initial setting of (fixed) subscription is necessary
		
		// now do whatever needs to do to get "flexible" subscriptions and behaviors
		// via Worklets: use requireJunction() 
		
	}
		
	// 
	protected void execute() {
		System.out.println ("--- Started execute() of " + getClass().getName());
		// first handle any fixed subscriptions
		
		// now execute anything demanded by the latest junction that has been installed
		//if (execEngine != null)
		try {
			execEngine.embeddedExec();
		} catch (NullPointerException e) {
			// disregard it; it means there is no "absolute" work to be done at this time.
		}
				
		// now go through the subscriptions requested by junctions
		Enumeration enum = subsWithBehaviors.keys();
		while (enum.hasMoreElements()) {
			IncrementalSubscription sub = (IncrementalSubscription) enum.nextElement();
			WorkletJunction junc = (WorkletJunction) subsWithBehaviors.get(sub);
			execEngine = (CooletJunctionInf)junc;
			execEngine.embeddedExec(sub);
		}
		execEngine = null;
		System.out.println ("--- Finished execute() of " + getClass().getName());
	
	}	

	protected void requireJunction(String cooletsInf, UnaryPredicate pred) {
		// should implement a generic way to require a Junction that matches
		// some adaptor interface of the PlugIn and is stimulated by the given predicate
		
		CooletMatchingCriteria match = new CooletMatchingCriteria (this.getClass());
		//System.out.println ("Predicate class is " + pred.getClass().getName());
		match.addPredCriterion(pred.getClass());
		try {
			match.activateCriterion (Class.forName(cooletsInf));
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e.getMessage());
		}
				
		Worklet wkl = new Worklet(null);
		CooletIncomingJunction request = new CooletIncomingJunction (comAddress, "NewLocalWorkletFactory", 9101);
		/*
		request.setRequestHost(comAddress);
		request.setRequestName(uniqueName);
		request.setWklURL(null);
		request.setRequestStructure(XMLizeInfStructure(inspectMyself(this.getClass())));
		request.setMatchMode(match);

		wkl.addJunction(request);
		wkl.deployWorklet(myWVM);
		System.out.println ("Sending request of a junction for predicate: " + pred.getClass().getName()); 
		*/
		System.out.println ("Faking request of a junction for predicate: " + pred.getClass().getName()); 
		return;
	}
	

	// implementation of WklInstallInf
	public void installJunction (WorkletJunction junc) {
		execEngine = (CooletJunctionInf)junc;
		System.out.println ("Junction of class: " + junc.getClass().getName() + " installed");	
	}
	
		
	public void installJunction (WorkletJunction junc, UnaryPredicate pred) {
		IncrementalSubscription sub = (IncrementalSubscription)subscribe(pred);
		subsWithBehaviors.put (sub, junc);
		System.out.println ("Junction of class: " + junc.getClass().getName() + " installed with predicate of class: " + pred.getClass().getName());
		wake();
	}
}