/*
 * <copyright>
 * Copyright (c) 2001: The Trustees of Columbia University in the
 *  City of New York, Peppo Valetto. All Rights Reserved.
 * </copyright>
 */
 package psl.workflakes.smartinf;

import java.util.*;
import java.net.*;
import java.lang.reflect.*;
import psl.worklets.WVM;
import psl.worklets.Worklet;
import psl.workflakes.smartinf.smartjunction.*;

public class WorkletOracle
	implements WklOracleTargetInf
{
	static private Vector knownSmartJunctions = new Vector(); // to hold known junctions info

	private final String WVM_ID;
	private final int port = 9100;
	private WklTargetInf wti;
	private WVM myWVM = null;
	private String host = null;
	private String requestStructure = null;
	
	public WorkletOracle(String name)
	{
		WklTargetInfDesc InfDesc = new WklTargetInfDesc();
		wti = WklTargetInfDesc.getTargetInfMgr();
		
		try
    		{
			host = InetAddress.getLocalHost().getHostName();
    		} 
		
    		catch (java.net.UnknownHostException e) 
		{
      			System.err.println("Shutting down; no known hostname found");
      			System.exit(0);
    		} 
		myWVM = new WVM(this, host, name);
    		myWVM.start();
		
		WVM_ID = name;
		registerOracle ();
	}

    public static void main(String[] args)
    {
	// System.out.println ("---> In main()");
	if (args.length != 1) 
	    {
		System.out.println ("You must provide exactly one agent name.");
		System.exit (0);
	    }
	
	try
	    {
		WorkletOracle wOracle = new WorkletOracle(args[0]);
	    }
	catch (Exception e) { }
	
	fillJunctionKBVector();
    }

    private void registerOracle ()
    {
	
    }

    // FAKE 
    private static void fillJunctionKBVector()
    {
	if (knownSmartJunctions == null)
	    {
		System.out.println ("Creating junction data structure ... ");
		knownSmartJunctions = new Vector();
	    }
	System.out.println ("Initializing Junction knowledge base ... " + knownSmartJunctions.size());
    	try
    	{
    		knownSmartJunctions.addElement (Class.forName ("psl.workflakes.smartinf.smartjunction.WklOracleJunction"));
    		knownSmartJunctions.addElement (Class.forName ("psl.workflakes.smartinf.smartjunction.WklRequestJunction"));
    		knownSmartJunctions.addElement (Class.forName("psl.workflakes.smartinf.smartjunction.SomeJunction"));
    		knownSmartJunctions.addElement (Class.forName("psl.workflakes.smartinf.smartjunction.SomeOtherJunction"));
    		knownSmartJunctions.addElement (Class.forName ("psl.workflakes.smartinf.smartjunction.FactoryWklJunction"));
		// knownSmartJunctions.addElement (new URL("http://www.psl.cs.columbia.edu/workletRepository/junctions/"));
	}
/*
	catch (MalformedURLException e)
	{
		System.out.println (e);
	}
*/
	catch (ClassNotFoundException e)
	{
		System.out.println (e);
	}
    }
    
    // A Semantic match can return multiple matching junction class - as opposed to Identity match
    // FAKE: just calls runIdentityMatch() at this point
    private Class[] runSemanticMatch(Class target, String policy) // policy is redundant
    {
	return new Class[] {runIdentityMatch (target, policy)};
    }

    // An Identity match can obviosuly return only one matching junction class - as opposed to Semantic match
    private Class runIdentityMatch(Class target, String policy) // policy is redundant
    {
	Class junctionClass = null;
	String className = target.getName();
	// Set candidateTargets;
	
	System.out.println ("matching with " + policy);
	
	try
	{
	    for (Enumeration enum = knownSmartJunctions.elements() ; enum.hasMoreElements() ; )
		{
		    junctionClass = (Class) enum.nextElement();
		    System.out.println ("\t trying to match junction type: " + junctionClass.getName());
		    // candidateTargets = SmartWorkletJunction.getJunctionTargets(junctionClass);
		    for (Iterator iter = SmartWorkletJunction.getJunctionTargets(junctionClass).iterator() ; iter.hasNext() ; )
			{
			    Class candidate = (Class)iter.next();
			    String candName = candidate.getName();
			    if (className.compareTo(candName) == 0)
				{
				// returns immediately once it finds a Field in the Junction class that matches with the traget class
				// there might be more than one, although it seems unlikely
				    System.out.println ("Class of the matching junction is: " + junctionClass.getName());
				    return junctionClass; 
				}		
			}
		    junctionClass = null;
		}
	}
	catch (Exception e)
	{
	    System.err.println (e);
	    throw new RuntimeException(e.getMessage());
	}
	return junctionClass;
    } 

// implementation of the WklOracleTargetInf interface
	
	public String getWVMID() {return WVM_ID;}
	public String getWVMHost() {return host;}
	
    public Class[] matchJunctions (Class target, String policy)
    {
	Class[] junctionClass = null;
	
	System.out.println ("Class of the target is: " + target.getName());
	System.out.println ("Match policy is: " + policy);
	if (policy.compareTo(new String("Identity")) == 0)
	    {
		Class matchingJunction = runIdentityMatch(target, policy);
		junctionClass = new Class[]{matchingJunction};
	    }
	else if (policy.compareTo(new String("Semantics")) == 0)
	    {
		junctionClass = runSemanticMatch(target, policy);
	    }
		
	if (junctionClass == null || junctionClass.length == 0)
		    throw new RuntimeException ("No matching junction retrieved for target: " + target.getName());
	else
	    return junctionClass;
    }

    // FAKE FOR NOW - magic later
    public void sortJunctions (Class [] jArray, Hashtable ht) 
    {
	// SortJunctions{} takes as input an array jArray of candidate junctions for 
	// all the interfaces active in the Hashtable ht of a WklMatchingCriteria, and,
	// presumably by exploiting the information in requestStructure,
	// prunes and orders jArray SEMANTICALLY;
	// the result must be that jArray contains only the best matching junction for each interface
	// and the order of jArray COUNTS, since the first junction is the one that must be
	// executed first on the target

	System.out.println ("prioritizing matching junctions ...");
	System.out.println ("prioritized matching junctions");
    }

    public void storeRequestStructure (String str)
    {
	requestStructure = str;
    }

    public void requireDeployment (Worklet wkl)
    {
	wkl.deployWorklet(myWVM);	
    }
	
//implementation of the interface WklTargetInf 
    public Set getInfDesc(Class c)
    {
	return (wti.getInfDesc(c));
    }
	
    public Hashtable inspectMyself(Class c)
    {
	return (wti.inspectMyself(c));
    }
	
    public String outputInfStructure (Hashtable ht)
    {
	return wti.outputInfStructure (ht);
    }


    public String XMLizeInfStructure (Hashtable ht) 
    {
    	return wti.XMLizeInfStructure (ht);
    }
    public String toXMLStructure (String s) { return wti.toXMLStructure(s);}
    public String fromXMLStructure (String s) { return wti.fromXMLStructure(s);}

}
