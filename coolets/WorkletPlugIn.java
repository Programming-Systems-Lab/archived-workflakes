/*
 * <copyright>
 * Copyright (c) 2001: The Trustees of Columbia University in the
 *  City of New York, Peppo Valetto. All Rights Reserved.
 * </copyright>
 */

package psl.workflakes.coolets;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import java.util.Set;
import java.lang.reflect.*;
import java.net.*;

import org.cougaar.core.component.ServiceBroker;
import org.cougaar.core.cluster.IncrementalSubscription;
import org.cougaar.util.StateModelException;
import org.cougaar.util.UnaryPredicate;
import org.cougaar.core.cluster.ClusterServesPlugIn;
import org.cougaar.core.plugin.*;
import org.cougaar.core.plugin.util.PlugInHelper;
import org.cougaar.core.society.Communications;

import psl.worklets.*;
import psl.workflakes.coolets.adaptors.*;

/**
 *
 * <p>Title: WorkletPlugin</p>
 * <p>Description: Base class for <code>Cougaar</code> "shell" plugins, which demandate the definition of
 *  their running logic to specialized incoming <code>WorkletJunctions</code> which are
 *  subclasses of {@link CooletIncomingJunction CooletIncomingJunction}
 *  </p>
 * <p>Copyright (c) 2001:  The Trustees of Columbia University in the
 *  City of New York, Peppo Valetto. All Rights Reserved.</p>
 * @author Peppo Valetto
 * @version 1.0
 * @see CooletIncomingJunction
 * @see psl.worklets.WorkletJunction
 */
public class WorkletPlugIn
	extends SimplePlugIn
	implements WklInstallInf
	 {
	/**
     * Cluster name
     */
     protected String uniqueName;
     
     /**
      * handle to the WVM running in the {@link WorkletNode WorkletNode} hosting
      * this <code>WorkletPlugIn</code>
      */
	protected WVM myWVM;
	
	/**
	 * RMI name of the node WVM
	 */
    protected String WVMName;
    
    /**
     * IP Address of the WVM
     */
	protected String comAddress;
	
	/**
     * running port of the RMI trasnporter of the WVM
     */
     protected int comPort;
    
     /**
      * executor of incoming junctons
      */
	protected CooletJunctionInf execEngine = null;
    
    /**
     * table matching up subscribed predicates to corresponding junctions to be executed
     */
	protected Hashtable subsWithBehaviors = new Hashtable();

	private String host;

	public void load()
		throws StateModelException {
		super.load();
		ServiceBroker sb = getDelegate().getServiceBroker();
	 	WklPlugInRegService wklreg = (WklPlugInRegService) sb.getService(this, WklPlugInRegService.class, null);
	 	wklreg.registerPlugIn(this.getClusterIdentifier(), this);
	 	WVMName = wklreg.getWVMName();
	 	myWVM = wklreg.getWVM();
	 	sb.releaseService(this, WklPlugInRegService.class, wklreg);		
	}
	
	public void unload()
		throws StateModelException {
		ServiceBroker sb = getDelegate().getServiceBroker();
	 	WklPlugInRegService wklreg = (WklPlugInRegService) sb.getService(this, WklPlugInRegService.class, null);
	 	wklreg.unRegisterPlugIn(this.getClusterIdentifier(), this);
	 	sb.releaseService(this, WklPlugInRegService.class, wklreg);
		super.unload();		
	}
    /**
     * Overrides <code>setupSubScriptions()</code> in <code>SimplePlugIn</code>.
     * Subclasses of <code>WorkletPlugIn</code> can override this method to set up
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

		// now do whatever initial setting of (fixed) subscription is necessary

		// now do whatever needs to do to get "flexible" subscriptions and behaviors
		// via Worklets: use requireJunction()

	}

	/**
         * Overrides <code>execute()</code> in <code>SimplePlugIn</code>.
         * Specializations of <code>WorkletPlugIn</code> can either override this method, or
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
		if (pred != null) {
			IncrementalSubscription sub = (IncrementalSubscription)subscribe(pred);
			subsWithBehaviors.put (sub, junc);
			System.out.println ("Junction of class: " + junc.getClass().getName() + " installed with predicate of class: " + pred.getClass().getName());
			wake();
		}
	}

  /**
   *
   * Convenience method that can be used to query the Cougaar blackboard for objects
   * matching predicate <code>pred</code> through the standard API provided by
   * {@link org.cougaar.core.plugin.SimplePlugIn SimplePlugIn}
   * It is useful for <@link CooletIncomingJucntion CooletIncomingJunctions} that need
   * to issue queries besides their subscriptions and outside of the plugin main execution loop.
   * <p>
   * @param pred the predicate used to express the query
   * @return a set of results for the query expressed by <code>pred</code>,
   * as returned by the Cougaar blackboard
   */
  public Collection delegateQuery (UnaryPredicate pred) {
  	System.out.println ("In delegateQuery()");
 	Vector res = new Vector();
 	try {
 			/*
			if (getSubscriber().isMyTransaction() == false) {
	      		System.out.println ("In delegateQuery() - Opening Transaction");
	      		openTransaction();
	      	}
	      	System.out.println ("In delegateQuery() - Transaction is open");
			*/
			res.addAll(query(pred));
			} catch  (Exception e) {
      		synchronized (System.err) {
        		System.err.println("Caught "+e);
        		e.printStackTrace();
        	}
		}
		/*
   		finally {
   			if (getSubscriber().isMyTransaction() == false) {
				System.out.println ("In delegateQuery() - Closing Transaction");
   				closeTransaction();
   				System.out.println ("In delegateQuery() - Transaction closed");
   			}
    	}
    	*/
    System.out.println ("Out of delegateQuery()");
    return res;
 }

        /**
         * Utility method to get current time.
         * @return the System time as milliseconds from 1970, as per standard java call
         */
	public long getCurrentTime() { return currentTimeMillis(); }
}