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
import psl.workflakes.smartinf.*;
import psl.workflakes.smartinf.smartjunction.*;

/**
 *
 * <p>Title: SmartWorkletPlugIn</p>
 * <p>Description: "shell" PlugIn that also contains means to retrieve descriptors of adaptor interfaces applicable to the PlugIn.
 * @see psl.workflakes.smartinf the smartinf package for details and a reference implementation of those facilities.
 * </p>
 * <p>Copyright: Copyright (c) 2002: The Trustees of Columbia University in the
 *  City of New York, Peppo Valetto. All Rights Reserved.</p>
 * @author Peppo Valetto
 * @version 1.0
 * @deprecated It is adviced to use {@link WorkletPlugIn WorkletPlugIn} instead.
 * <code>SmartWorkletPlugIn</code> is a specialization of <code>WorkletPlugIn</code> that was intended to add support
 * for self-describing Worklets adaptors from the <code>smartinf</code> package.
 * Since that package needs a major overhaul from the current
 * reference implementation, it does not pay to use <code>SmartWorkletPlugIn</code>.
 */
public class SmartWorkletPlugIn
	extends WorkletPlugIn
	implements WklInstallInf
	 {
         /**
          * interface for self-describing the adaptors suitable for this PlugIn
          */
        protected WklTargetInf wti;

	private String host;

         /**
         * Overrides <code>setupSubScriptions()</code> in <code>SimplePlugIn</code>.
         * Subclasses of <code>SmartWorkletPlugIn</code> can override this method to set up
         * "fixed" subscriptions on their own, and require "flexible" subscriptions from
         * {@link CooletIncomingJunction CooletIncomingJunctions}
         */
	protected void setupSubscriptions()
	{
		ClusterServesPlugIn cluster = getDelegate().getCluster();
		if (cluster == null) {
			throw new RuntimeException("Cannot get handle to cluster from plugin");
		}
		comPort = Communications.getPort();
		uniqueName = getClusterIdentifier().toString();

		String pluginName = getBlackboardClientName();
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
			WklTargetInfDesc InfDesc = new WklTargetInfDesc();
			wti = WklTargetInfDesc.getTargetInfMgr();
		}
		else {
			// handle this case better !!!
			myWVM = null;
			wti = null;
		}

		// now do whatever initial setting of (fixed) subscription is necessary

		// now do whatever needs to do to get "flexible" subscriptions and behaviors
		// via Worklets: use requireJunction()

	}

	/**
         * Overrides <code>execute()</code> in <code>SimplePlugIn</code>.
         * Specializations of <code>SmartWorkletPlugIn</code> can either override this method, or
         * specialize it by calling <code>super.execute()</code> as their first statement.
         */
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

        /**
         * Dummy method that provides a framework for the implementation of a generic way to require a {@link CooletIncomingJunction CooletIncomingJunction} that matches some adaptor interface of the PlugIn and is stimulated by the given predicate.
         * @param cooletsInf full qualified name of an itnerface for the adaptor associated to the PlugIn.
         * @param pred predicate that is to be associated to the requested {@link CooletIncomingJunction CooletIncomingJunction}
         */
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
		CooletIncomingJunction request = new CooletIncomingJunction (comAddress, "NewLocalWorkletFactory", comPort, 9101);
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

        /**
         * Installs a {@link CooletIncomingJunction CooletIncomingJunction} for immediate execution.
         * @param junc Junction to be installed onto this PlugIn for execution
         */

	public void installJunction (WorkletJunction junc) {
		execEngine = (CooletJunctionInf)junc;
		System.out.println ("Junction of class: " + junc.getClass().getName() + " installed");
	}

         /**
         * Installs a {@link CooletIncomingJunction CooletIncomingJunction} for execution when the associated predicate is matched.
         * @param junc Junction to be installed onto this PlugIn for execution
         * @param pred predicate to be associated to the junction
         */
	public void installJunction (WorkletJunction junc, UnaryPredicate pred) {
		IncrementalSubscription sub = (IncrementalSubscription)subscribe(pred);
		subsWithBehaviors.put (sub, junc);
		System.out.println ("Junction of class: " + junc.getClass().getName() + " installed with predicate of class: " + pred.getClass().getName());
		wake();
	}

	//implementation of the interface WklTargetInf

    /**
     * accessor method for the descriptors managed by the <code>wti</code>
     * @param c a Class objet
     * @return a set of descriptors
     */
    public Set getInfDesc(Class c)
    {
	return (wti.getInfDesc(c));
    }

    /**
     * Inspection of the interfaces implemented by this PlugIn which can be exploited by {@link CooletIncomingJunction CooletIncomingJunctions}.
     * @param c a Class object shold be the Class of this PlugIn
     * @return a <code>Hashtable</code> listing details of the implemented interfaces for this PlugIn.
     * @see psl.workflakes.smartinf.WklTargetInf
     */
    public Hashtable inspectMyself(Class c)
    {
	return (wti.inspectMyself(c));
    }

    /**
     * outputs a (not so much) pretty-print of the inspection hashtable
     * @see #inspectMyself
     * @param ht a <code>Hashtable</code> to be output
     * @return Pretty-print <code>String</code>
     */
    public String outputInfStructure (Hashtable ht)
    {
	return wti.outputInfStructure (ht);
	}

    /**
     * Dummy Method. outputs an XML form of the inspection hashtable
     * To be done.
     * @param ht a <code>Hashtable</code> to be output
     * @return c
     */
    public String XMLizeInfStructure (Hashtable ht)
    {
    	return wti.XMLizeInfStructure (ht);
    }

    /**
     * Dummy method. Pretty-print to XML transformation for the output of the inspection hashtable.
     * @param s Pretty-print <code>String</code>
     * @return XML-ized <code>String</code>
     */
    public String toXMLStructure (String s) { return wti.toXMLStructure(s);}
    /**
     * Dummy method.  XML to Pretty-print transformation for the output of the inspection hashtable.
     * @param s XML-ized <code>String</code>
     * @return Pretty-print <code>String</code>
     */
    public String fromXMLStructure (String s) { return wti.fromXMLStructure(s);}
}