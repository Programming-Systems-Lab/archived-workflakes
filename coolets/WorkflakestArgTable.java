/*
 * <copyright>
 * Copyright (c) 2002: The Trustees of Columbia University in the
 *  City of New York, Peppo Valetto. All Rights Reserved.
 * </copyright>
 *
 * This class abstracts out of Node and WorkletNode the parsing and setting of 
 * system properties and other parameters related to a Workflakes node.
 * Much of the implementation code is ripped off the class Node
 * and its private method setSystemProperties()
 */
 package psl.workflakes.coolets;
 
 import java.util.List;
 import java.util.ArrayList;
 
 import org.cougaar.core.society.ArgTable;
 import org.cougaar.core.society.ArgTableIfc;

 class WorkflakesArgTable {
	static private ArgTable myArgs;
	
	public static ArgTable getArgTable() {return myArgs;}
 		
 	public static List parseArgs(String[] arguments) {
		List revisedArgs = new ArrayList();
		
		/**
		 * Code ripped off org.cougaar.core.society.Node.setSystemProperties()
		 */
		// separate the arguments into "-D" properties and normal arguments
		for (int i = 0; i < arguments.length; i++) {
			String argi = arguments[i];
			if (argi.startsWith("-D")) {
			// transfer a "late" system property
			int sepIdx = argi.indexOf('=');
			if (sepIdx < 0) {
			  System.setProperty(argi.substring(2), "");
			} else {
			  System.setProperty(argi.substring(2, sepIdx), argi.substring(sepIdx+1));
			}
			} else {
			// keep a non "-D" argument
			revisedArgs.add(argi);
			}
		}
		return revisedArgs;
 	}
 	
 	public WorkflakesArgTable (List argList) {
 		myArgs = new ArgTable(argList);	
 	}
 	
 	public void setSystemProperties () {
 		/**
		 * Code ripped off org.cougaar.core.society.Node.setSystemProperties()
		 */
		 
		// transfer the command-line arguments to system properties
	    String validateJars = 
	      (String) myArgs.get(ArgTableIfc.SIGNED_PLUGIN_JARS);
	    if (validateJars != null) {
	      System.setProperty("org.cougaar.validate.jars", validateJars);
	      System.err.println("Set name to "+validateJars);
	    }
	
	    String name = (String) myArgs.get(ArgTableIfc.NAME_KEY);
	    if (name != null) {
	      System.setProperty("org.cougaar.node.name", name);
	      System.err.println("Set name to "+name);
	    }
	
	    String config = (String) myArgs.get(ArgTableIfc.CONFIG_KEY);
	    if (config != null) {
	      System.setProperty("org.cougaar.config", config);
	      System.err.println("Set config to "+config);
	    }
	
	    String cs = (String) myArgs.get(ArgTableIfc.CS_KEY);
	    if (cs != null && cs.length()>0) {
	      System.setProperty("org.cougaar.config.server", cs);
	      System.err.println("Using ConfigurationServer at "+cs);
	    }
	
	    String ns = (String) myArgs.get(ArgTableIfc.NS_KEY);
	    if (ns != null && ns.length()>0) {
	      System.setProperty("org.cougaar.name.server", ns);
	      System.err.println("Using NameServer at "+ns);
	    }
	
	    String port = (String) myArgs.get(ArgTableIfc.PORT_KEY);
	    if (port != null && port.length()>0) {
	      System.setProperty("org.cougaar.name.server.port", port);
	      System.err.println("Using NameServer on port " + port);
	    }
	
	    String filename = (String) myArgs.get(ArgTableIfc.FILE_KEY);
	    if (filename != null) {
	      System.setProperty("org.cougaar.filename", filename);
	      System.err.println("Using file "+filename);
	    }
	
	    String experimentId = 
	      (String) myArgs.get(ArgTableIfc.EXPERIMENT_ID_KEY);
	    if (experimentId != null) {
	      System.setProperty("org.cougaar.experiment.id", experimentId);
	      System.err.println("Using experiment ID "+experimentId);
	    }
 	}
 	
}