/*
 * <copyright>
 * Copyright (c) 2001: The Trustees of Columbia University in the
 *  City of New York, Peppo Valetto. All Rights Reserved.
 * </copyright>
 */
 package psl.workflakes.coolets;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Enumeration;

import org.cougaar.core.cluster.IncrementalSubscription;
import org.cougaar.core.plugin.SimplePlugIn;
import org.cougaar.core.cluster.ClusterServesPlugIn;
import org.cougaar.core.society.Communications;
import org.cougaar.domain.planning.ldm.plan.*;
import org.cougaar.domain.planning.ldm.asset.*;
import org.cougaar.util.UnaryPredicate;

import psl.workflakes.coolets.adaptors.*;

/**
 *
 * <p>Title: JunctionLDMPlugIn</p>
 * <p>Description: Utility Cougaar LDM PlugIn that can be used to manage
 * mappings between workflow tasks and Cougaar <code>Assets</code>
 * representing <code>WorkletJunctions</code> that Workflakes can use to actuate those tasks.
 * @see org.cougaar.domain.planning.ldm the Cougaar ldm package
 * @see psl.worklets.WorkletJunction
 * @see psl.workflakes.coolets.adaptors.JuncLDMAdaptorInf
 * <p>Copyright: Copyright (c) 2002: The Trustees of Columbia University in the
 *  City of New York, Peppo Valetto. All Rights Reserved.</p>
 * @author Peppo Valetto
 * @version 1.0
 */
public class JunctionLDMPlugIn
extends WorkletPlugIn
implements JuncLDMAdaptorInf {

        /**
         * Calls default <code>setupSubscriptions()</code> in
         * {@link WorkletPlugIn WorkletPlugIn}
         */
	protected void setupSubscriptions() {
		super.setupSubscriptions();
	}

        /**
         * Calls default <code>execute()</code> in
         * {@link WorkletPlugIn WorkletPlugIn}
         */
	protected void execute() {
		super.execute();
	}

	//implementation of interface JuncLDMAdaptorInf

        /**
         * Convenience method to access LDM facilities for
         * adding a Cougaar <code>PropertyGroupFactory</code>
         * @param pf the factory to be added
         */
	public void addPropertyGroupFactory (java.lang.Object pf) {
		//System.out.println ("In addPropertyGroupFactory()");
		theLDMF.addPropertyGroupFactory(pf);
		}
        /**
         * Convenience method to access LDM facilities for
         * creating a Cougaar <code>Prototype</code> for an Asset
         * @param theClass the Asset subclass
         * @param protoName a symbolic ID for the new Prototype
         * @return the cougaar Asset describing the Prototype
         */
	public Asset createPrototype(java.lang.Class theClass, String protoName) {
		//System.out.println ("In createPrototype()");
		return theLDMF.createPrototype(theClass, protoName);
	}

        /**
         * Convenience method to access LDM facilities for
         * caching a Cougaar <code>Prototype</code> for an Asset
         * @param protoID the Prototype ID
         * @param asset the Asset describign the Prototype
         */
	public void cachePrototype(String protoID,Asset asset) {
		//System.out.println ("In cachePrototype()");
		theLDM.cachePrototype (protoID, asset);
	}

        /**
         * Convenience method to access LDM facilities for
         * caching a Cougaar <code>PropertyGroup</code>
         * @param groupName an ID for the new PropertyGroup
         * @return the newly created PropertyGroup
         */
	public PropertyGroup createPropertyGroup(String groupName) {
		//System.out.println ("In createPropertyGroup()");
		return theLDMF.createPropertyGroup(groupName);
	}

        /**
         * Convenience method to access LDM facilities for
         * creating an instance of a Cougaar <code>Asset</code>
         * @param assetID the ID of the Prototype used as a model for the new Asset
         * @return the newly created Asset
         */
	public Asset createInstance(String assetID) {
		//System.out.println ("In createInstance()");
		return theLDMF.createInstance(assetID);
	}

        /**
         * Convenience method to access LDM facilities for
         * addign an instance of a Cougaar <code>Asset</code> into the blackboard
         * @param anAsset the Asset instance
         */
	public void addAsset (Asset anAsset) {
		//System.out.println ("In addAsset()");
		try {
      		openTransaction();
			getSubscriber().publishAdd(anAsset);
			} catch  (Exception e) {
      		synchronized (System.err) {
        		System.err.println("Caught "+e);
        		e.printStackTrace();
        	}
		}
   		finally {
      	closeTransaction();
    	}
	}

	// note this does not return the WVM socket port
        /**
         * Convenience method to return the RMI port used by the
         * {@link WorkletNode WorkletNode} where this PlugIn resides
         * It's the same port used by the RMI transport of the resident
         * {@link psl.worklets.WVM WVM}.
         * @return the RMI port number
         */
	public int getRMIPort() {
		System.out.println ("In getRMIPort()");
		return Communications.getPort(); }

}