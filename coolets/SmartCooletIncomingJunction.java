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
import psl.workflakes.smartinf.*;
import psl.workflakes.smartinf.smartjunction.*;

/**
 *
 * <p>Title: SmartCooletIncomingJunction</p>
 * <p>Description: Workflow Definition Junction that can also exploit the self-describing facilities of PlugIn adaptors
 * of type {@link SmartWorkletPlugIn SmartWorkletPlugIn}.
 * </p>
 * <p>Copyright: Copyright (c) 2002: The Trustees of Columbia University in the
 *  City of New York, Peppo Valetto. All Rights Reserved.</p>
 * @author Peppo Valetto
 * @version 1.0
 * @deprecated It is adviced to use {@link CooletIncomingJunction CooletIncomingJunction} instead.
 * <code>SmartCooletIncomingJunction</code> is a specialization of <code>CooletIncomingJunction</code> that was intended to exploit the support
 * for self-describing Worklets adaptors from the <code>smartinf</code> package.
 * Since that package needs a major overhaul from the current
 * reference implementation, it does not pay to use <code>SmartCooletIncomingJunction</code>.
 * @see psl.workflakes.smartinf the smartinf package, for details and a reference implementation of those facilities.
 * @see CooletIncomingJunction
 * @see WorkletPlugIn
 * @see SmartWorkletPlugIn
 */
public class SmartCooletIncomingJunction
	extends SmartWorkletJunction
	implements CooletJunctionInf
{
        /**
         * Adaptor interface exposing a {@link WorkletNode WorkletNode} to this junction.
         */
        protected NodeWklInf myTarget;
        /**
         * Adaptor interface for the {@link WorkletPlugIn WorkletPlugIn} that is the target of this junction.
         */
	protected WklInstallInf myPlugInTarget;
        /**
         * Predicate that triggers the execution of the logic implemetned by this junction.
         */
	protected UnaryPredicate myTriggerPred;
        /**
         * ID of the <code>Cluster</code> where this junction is installed.
         */
	private String clusterID;
        /**
         * ID of the <code>WorkletPlugIn</code> where this junction is installed.
         */
	private String pluginID;

        /**
         * Instantiates a <code>sMARTCooletIncomingJunction</code>. Overrides the constructor of <code>SmartWorkletJunction</code>
         * @see psl.worklets.WorkletJunction for parameters to this constructor
         * @param host target host
         * @param name target RMI name
         * @param rPort target RMI port
         * @param sPort target socket port
         */
	public SmartCooletIncomingJunction (String host, String name, int rPort, int sPort) {
		super (host, name, rPort, sPort);
		myTriggerPred = null;
		myPlugInTarget = null;
		clusterID = null;
		pluginID = null;
	}

        /**
         * Executed when this junction lands onto the target WVM. Overrides <code>init()</code> in <code>SmartWorkletJunction</code>.
         * @param system target adaptor interface.
         * @param wvm handle to the target WVM
         */
	public void init(Object system, WVM wvm) {
		super.init(system, wvm);
		//System.out.println ("system class: " + system.getClass().getName());
		//myTriggerPred = new genericPredicate();
		myTarget = (NodeWklInf) system;
		PlugInAdapter thePlugIn = myTarget.findPlugIn(clusterID, pluginID);
		if (thePlugIn != null && (thePlugIn instanceof psl.workflakes.coolets.WorkletPlugIn))
			myPlugInTarget = (WklInstallInf)thePlugIn;
	}

        /**
         * Overrides <code>init()</code> in <code>SmartWorkletJunction</code>.
         * Installs itself on the target <code>WorkletPlugIn</code> for either execution within the target main running loop.
         */
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

        /**
         * Invoked by the target in response to a succesfull subscription on {@link #myTriggerPred the trigger predicate} of this junction.
         * This is a skeleton method to be used as an example and to be overridden by specializations of <code>SmartCooletIncomingJunction</code>.
         * @param sub the <code>IncrementalSubscription</code> data structure that must be handled in this execution round.
         */
	public void embeddedExec(IncrementalSubscription sub) {
		// do something
		System.out.println ("Junction executing on target: " + myTarget.getClass().getName());
		System.out.println (" handling a subscription of size " + sub.size());
	}

        /**
         * Invoked by the target in response to a succesfull installation of this <code>SmartCooletIncomingJunction</code>..
         * This is a skeleton method to be used as an example and to be overridden by specializations of <code>CooletIncomingJunction</code>.
         */
	public void embeddedExec() {
		// do something
		System.out.println ("Junction executing on target: " + myTarget.getClass().getName());
	}

                /**
         * Accessor method for retrieving the Class of the {@link #myTriggerPred the trigger predicate} of this junction.
         * @return the predicate Class object.
         */
	public Class getPredClass() {
		if (myTriggerPred == null)
			return null;

		return myTriggerPred.getClass();
	}

        /**
         * Accessor method for retrieving the {@link #myTriggerPred the trigger predicate} of this junction.
         * @return handle to the Predicate.
         */
	public UnaryPredicate getPred() { return myTriggerPred; }
	/**
         * Accessor method for setting the {@link #myTriggerPred the trigger predicate} of this junction.
         * @param pred the Predicate to be set.
         */
        public void setPred(UnaryPredicate pred) {myTriggerPred = pred; }
        /**
         * Accessor method for setting the ID of the hosting cluster of this junction.
         * @param cluster the cluster ID.
         */
	public void setClusterID (String cluster) { clusterID = cluster; }
        /**
         * Accessor method for getting the ID of the hosting cluster of this junction.
         * @return the cluster ID.
         */
	public String getClusterID() { return clusterID; }
        /**
         * Accessor method for setting the ID of the hosting PlugIn of this junction.
         * @param plugin the PlugIn ID.
         */
	public void setPluginID (String plugin) { pluginID = plugin; }
        /**
         * Accessor method for getting the ID of the hosting PlugIn of this junction.
         * @return the PlugIn ID.
         */
	public String getPluginID() { return pluginID; }
}