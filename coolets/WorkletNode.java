/*
 * <copyright>
 * Copyright (c) 2001: The Trustees of Columbia University in the
 *  City of New York, Peppo Valetto. All Rights Reserved.
 * </copyright>
 */
 package psl.workflakes.coolets;

import java.util.*;
import java.net.UnknownHostException;
import java.io.IOException;
import java.sql.SQLException;
import javax.naming.NamingException;

import org.cougaar.core.society.*;
import org.cougaar.core.cluster.*;
import org.cougaar.core.agent.*;
import org.cougaar.core.plugin.*;
import org.cougaar.core.mts.*;
import org.cougaar.core.component.*;
import org.cougaar.util.PropertyParser;
import org.cougaar.util.EmptyIterator;
import org.cougaar.util.FilteredIterator;
import org.cougaar.util.UnaryPredicate;

import psl.worklets.WVM;
import psl.workflakes.coolets.adaptors.NodeWklInf;

/**
 *
 * <p>Title: WorkletNode</p>
 * <p>Description: Cougaar <code>Node</code> that includes a WVM for accommodating Worklets
 * @see psl.worklets the worklets package
 * @see psl.worklets.WVM the WVM class
 * @see org.cougaar.core.society.Node the Node class
 * </p>
 * <p>Copyright: Copyright (c) 2002  The Trustees of Columbia University in the
 *  City of New York, Peppo Valetto. All Rights Reserved.</p>
 * @author Peppo Valetto
 * @version 1.0
 */
public class WorkletNode
	extends Node
	implements NodeWklInf {

	private WVM nodeWVM;
	private String WVMid;	

	public WorkletNode() {
    	super();
    	System.out.println (getClass().getName() + " constructor");
  }
  
  
   /**
     * Accessor method to get a handle on the WVM of this node
     * @return handle to the WVM
     */
	public WVM getNodeWVM() { return nodeWVM; }
	
	protected void initNode () 
	 throws UnknownHostException, NamingException, IOException,
           SQLException, InitializerServiceException {
		System.out.println ("In" + getClass().getName() + "::initNode()");
		
		//establishing the service that provides info about WVM, the WorkletCluster set
		// and the WorkletPlugIn set
		establishRegService();
		
		super.initNode();	
	}

	 protected void add(ComponentDescription[] descs) {
		//first of all set up WVM so that WorkletClusters and their Plugins can refer to it	
	 	createWVM();
	 	// now use the canonical Node.add() method to load clusters and (recursively) plugins
	 	super.add(descs);
	 }

  /**
   * main to launch a WorkletNode from the command line.
   * @param args command line arguments
   */

  static public void main(String[] args){
  	System.out.println ("Launching a WorkletNode...");
   	if ("true".equals(System.getProperty("org.cougaar.useBootstrapper", "true"))) {
      Bootstrapper.launch(Node.class.getName(), args);
    } else {
      WorkletNode.launch(args);
    }
  }

    /**
     * Ripped off from the Node class. Then changed to fit WorkletNode.
     * @param args arguments array passed to the Node
     */
	static public void launch(String[] args) {
		// display the version info
	    //printVersion();
	
	    // convert any command-line args to System Properties
	    WorkflakesArgTable myArgTable = new WorkflakesArgTable(WorkflakesArgTable.parseArgs(args));
	    myArgTable.setSystemProperties();
	
	    // check for valid plugin jars

	    boolean validateJars = PropertyParser.getBoolean("org.cougaar.validate.jars", false);
	    if (validateJars) {
	      // validate
	      if (validatePluginJarsByStream()) {
	        // validation succeeded
	      } else {
	        throw new RuntimeException(
	          "Error!  Found unsigned jars in plugin directory!");
	      }
	    } else {
	      // not validating
	    }
	
	    // try block to ensure we catch all exceptions and exit gracefully
        try {
	      WorkletNode myNode = new WorkletNode();
	      myNode.initNode();
	      // done with our job... quietly finish.
	    } catch (Throwable e) {
	      System.out.println(
	          "Caught an exception at the highest try block.  Exception is: " +
	          e );
	      e.printStackTrace();
	    }
		
	}

	private Iterator getClusters() {
		ServiceBroker sb = getServiceBroker();
		
		WVMRegService wklreg = (WVMRegService) sb.getService(this, 
			WVMRegService.class, null);
		Iterator iter = wklreg.getClusterListings().iterator();
		sb.releaseService(this, WVMRegService.class, wklreg);
		
		return iter;	
	}
	
	private void establishRegService() {
		ServiceBroker sb = getServiceBroker();
		WklRegServiceProvider wklService = new WklRegServiceProvider(null, null);
		sb.addService(WklClusterRegService.class, wklService);
		sb.addService(WklPlugInRegService.class, wklService);
		sb.addService(WVMRegService.class, wklService);	
	}

	private void createWVM() {
		System.out.println (getClass().getName() + " creating WVM now ..");
		int port = Communications.getPort();
		String addr = Communications.getFdsAddress();
		WVMid = getIdentifier() + "_WVM";
		nodeWVM = new WVM (this, addr, WVMid, port);
		
		if (!getServiceBroker().hasService(WVMRegService.class))
			establishRegService();
					
		registerWVM();
		return;		
	}
	
	private void registerWVM() {
		ServiceBroker sb = getServiceBroker();
		WVMRegService wklreg = (WVMRegService) sb.getService(this, 
			WVMRegService.class, null);
		wklreg.setWVM (nodeWVM);
		wklreg.setWVMName(WVMid);
		sb.releaseService(this, WVMRegService.class, wklreg);	
	}
	
	/**
	 * dummy method that always returns OK, since 	
	 * we cannot use Node.validatePluginJarsByStream() that is private.
	 */
	private static boolean validatePluginJarsByStream() {
		return true;	
	}	

  	// implementation of interface NodeWklInf

    /**
     * finds a cluster handle given an ID
     * @param cluster Cluster ID
     * @return handle to a Cluster with that ID
     */

	public ClusterImpl findCluster (String cluster) {
		Iterator clusterIter = getClusters();
		ClusterImpl cl = null;
		while (clusterIter.hasNext()) {
			cl = (ClusterImpl)clusterIter.next();
			if (cl.getIdentifier().equals(cluster)) {
				System.out.println ("found cluster " + cl.getIdentifier());
				return cl;
			}
		}
		return null;
	}

/* Alternative way to get to a CLuster starting form its name ... hackier but simpler
	public ClusterImpl findCluster (String cluster) {
		ClusterContext c = ClusterContextTable.findContext(ClusterIdentifier.getClusterIdentifier(cluster));	
		if (c == null)
			return null;
		if (c instanceof ClusterImpl) {
			System.out.println ("found cluster " + ((ClusterImpl)c).getIdentifier());
			return ((ClusterImpl)c);
		}
		return null;
	}
*/
    /**
     * finds a PlugIn on a cluster given the cluster and a PlugIn ID
     * @param clImpl cluster object
     * @param plugin PlugIn ID
     * @return handle to the corresponding PlugIn
     */
	public PlugInAdapter findPlugIn (ClusterImpl clImpl, String plugin) {
		if (clImpl instanceof WorkletClusterImpl) {
			Iterator iter = ((WorkletClusterImpl)clImpl).getPlugIns();
			if (!iter.hasNext())
				System.out.println ("NO PLUGINS FOUND!!!");
			while (iter.hasNext()) {
				PlugInAdapter pia = (PlugInAdapter) iter.next();
				if (pia.toString().equals(plugin))
					return pia;
			}
		}

		return null;
	}

    /**
     * finds a PlugIn on a cluster given the cluster ID and a PlugIn ID
     * @param cluster Cluster ID
     * @param plugin PlugIn ID
     * @return handle to the corresponding PlugIn
     */
	public PlugInAdapter findPlugIn (String cluster, String plugin) {
		ClusterImpl ci = findCluster (cluster);
		if (cluster != null)
			return findPlugIn (ci, plugin);
		return null;
	}
}
	