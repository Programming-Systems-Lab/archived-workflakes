/*
 * <copyright>
 * Copyright (c) 2001: The Trustees of Columbia University in the
 *  City of New York, Peppo Valetto. All Rights Reserved.
 * </copyright>
 */
 package psl.workflakes.coolets;

import java.util.*;
import java.net.UnknownHostException;
import org.cougaar.core.society.*;
import org.cougaar.core.cluster.*;
import org.cougaar.core.plugin.*;
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
	private transient MessageTransport theMessenger = null;

/*	static {
    	Communications.putTransport("Wkl", "psl.workflakes.coolets.WorkletMessageTransport");
    }
*/

        /**
         * Constructor that overrides the <code>Node</code> constructor.
         * @param someArgs Node arg table
         * @param port port used for RMI communication
         * @throws UnknownHostException
         */
	public WorkletNode(ArgTable someArgs, Integer port)
		throws UnknownHostException {
	    	super(someArgs, port );
	    	System.out.println (getClass().getName() + " constructor");
    	}

        /**
         * Constructor that overrides the <code>Node</code> constructor.
         * @param someArgs Node arg table
         * @throws UnknownHostException
         */
	public WorkletNode(ArgTable someArgs)
		throws UnknownHostException {
    	super( someArgs );
    	System.out.println (getClass().getName() + " constructor");
  }

        /**
         * Accessor method to get a handle on the WVM of this node
         * @return handle to the WVM
         */
	public WVM getNodeWVM() { return nodeWVM; }

        /**
         * Overrides the <code>initNode()</code> method of class Node. Initializes the Node including the set up of the WVM
         * @throws UnknownHostException
         */
	protected void initNode ()
		throws UnknownHostException {

		String name = null;

		//this code is ripped off the initNode of parent class org.cougaar.core.society.Node
    	if(getArgs().containsKey(NAME_KEY)) {
      		name = (String)getArgs().get( NAME_KEY );
    	} else {
      		name = System.getProperty("org.cougaar.node.name");
    	}
    	if (name == null) name = findHostName();

    	setIdentifier(name);
    	// end of code ripped off from Node


    	// set up the Node WVM for Worklet Functionality
    	// influences RMITransport setup for Cougaar
    	System.out.println (getClass().getName() + " creating WVM now ..");
    	int port = Communications.getPort();
    	String addr = Communications.getFdsAddress();
    	WVMid = getIdentifier() + "_WVM";
    	nodeWVM = new WVM (this, addr, WVMid, port);

		// start up matching Name Service
		WorkletMessageTransport.startNameService();

   		// more ripped off code from parent class
   		createProfiler();
    	this.initTransport();        // set up the message handler - customized fror WorkletNode
    	loadClusters();         // set up the clusters.
    	// end of code ripped off from parent class
	}

	private void initTransport() {
		System.out.println ("In WorkletNode.initTransport");
		String name = getIdentifier();

		theMessenger = new WorkletMessageTransport(name);

	    Communications.setDefaultMessageTransport(theMessenger);
    	theMessenger.setDisableRetransmission(theMessenger.isDisableRetransmission());
    	theMessenger.start();
    	System.err.println("Started "+theMessenger);
	}

        /**
         * Accessor method to get a handle of to the node <code>MessageTransport</code>
         * @return handle to <code>MessageTransport</code> for this Node
         */
	protected MessageTransport getMessenger() {
		return theMessenger;
	}

        /**
         * Sets up a cluster for this node. If the cluster to be set up is an instance of
         * @link{WorkletClusterImpl WorkletClusterImpl} sets the WVM of the node as the WVM of that cluster as well.
         * @param clusterid the ID of the cluaster to be set up
         * @return handle to the Cluster service in the Node
         */
	public ClusterServesClusterManagement createCluster(String clusterid) {
		ClusterServesClusterManagement cluster = super.createCluster(clusterid);
		if (cluster != null && (cluster instanceof psl.workflakes.coolets.WorkletClusterImpl)) {
			((WorkletClusterImpl)cluster).setNodeWVM(nodeWVM);
			((WorkletClusterImpl)cluster).setNodeWVMid(WVMid);
		}
		return cluster;
	}

        /**
         * Ripped off from the Node class. changed to fit WorkletNode
         * @param args arguments array passed to the Node
         */
 	static public void launch(String[] args) {

    //printVersion();

    // BEGIN BBN Debugging
    // quickly walk through the args and process any
    // that match -D
    Properties debugProperties = new Properties();
    Vector revisedArgs = new Vector(); // elements of args not related to -D values
    for ( int i=0; i<args.length; i+=1 ){
      if (args[i].startsWith("-D")) {
        debugProperties.put(args[i].substring(2), Boolean.TRUE);
      } else {
        // process a non -D argument
        revisedArgs.addElement(args[i]);
      }
    }

    // now, swap in the revised Argument listing
    String[] replacementArgs = new String[revisedArgs.size()];
    for ( int i=0; i<revisedArgs.size(); i+=1 )
      replacementArgs[i]=(String)revisedArgs.elementAt(i);
    args = replacementArgs;

    WorkletNode myNode = null;
    ArgTable myArgs = new ArgTable(args);

/*
    // <START_PLUGIN_JAR_VALIDATION>
    String validateJars = (String)myArgs.get(SIGNED_PLUGIN_JARS);
    if( validateJars != null) {
      {
        boolean isValid = validatePluginJarsByStream();
        if( isValid == false ) {
          throw new RuntimeException("Error!  Found unsigned jars in plugin directory!");
        }
      }
    }
    // </START_PLUGIN_JAR_VALIDATION>
*/
    String nodeName = (String) myArgs.get(NAME_KEY) ;
    java.util.Properties props = System.getProperties();

    String config = (String) myArgs.get(CONFIG_KEY);
    if (config != null) {
      props.put("org.cougaar.config", config);
      System.err.println("Set config to "+config);
    }

    String cs = (String) myArgs.get(CS_KEY);
    if (cs != null && cs.length()>0) {
      props.put("org.cougaar.config.server", cs);
      System.err.println("Using ConfigurationServer at "+cs);
    }

    String ns = (String) myArgs.get(NS_KEY);
    if (ns != null && ns.length()>0) {
      props.put("org.cougaar.name.server", ns);
      System.err.println("Using NameServer at "+ns);
    }

    String port = (String) myArgs.get(PORT_KEY);
    if (port != null && port.length()>0) {
      props.put("org.cougaar.name.server.port", port);
      System.err.println("Using NameServer on port " + port);
    }

    // try block to ensure we catch all exceptions and exit gracefully
    try{
      myNode = new WorkletNode(myArgs);
      myNode.initNode();
      // done with our job... quietly finish.
    }
    catch(Exception e){
      System.out.println("Caught an exception at the highest try block.  Exception is: " + e );
      e.printStackTrace();
    }
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


	// implementation of NodeWklInf

        /**
         * finds a cluster handle given an ID
         * @param cluster Cluster ID
         * @return handle to a Cluster with that ID
         */
	public ClusterImpl findCluster (String cluster) {
		Enumeration clusterEnum = getClusters().elements();
		ClusterImpl cl = null;
		while (clusterEnum.hasMoreElements()) {
			cl = (ClusterImpl)clusterEnum.nextElement();
			if (cl.getClusterIdentifier().toString().equals(cluster))
				return cl;
		}
		return null;
	}

        /**
         * finds a PlugIn on a cluster given the cluster and a PlugIn ID
         * @param clImpl cluster object
         * @param plugin PlugIn ID
         * @return handle to the corresponding PlugIn
         */
	public PlugInAdapter findPlugIn (ClusterImpl clImpl, String plugin) {
		List plugins = clImpl.getPlugins();
		Iterator iter = plugins.iterator();
		while (iter.hasNext()) {
			PlugInAdapter pia = (PlugInAdapter) iter.next();
			if (pia.toString().equals(plugin))
				return pia;
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
