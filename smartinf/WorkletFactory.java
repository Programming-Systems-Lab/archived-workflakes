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
import psl.worklets.WorkletJunction;
import psl.workflakes.smartinf.smartjunction.*;

public class WorkletFactory
	implements WklFactoryTargetInf
{
    private final String WVM_ID;
    private final int port = 9100;
    private WorkletOracle myOracle= null;
    private WklTargetInf wti;
    private WVM myWVM = null;
    private String host = null;
    private Hashtable requestTable;
	
	public WorkletFactory(String name)
	{
	    // myOracle = locateOracle(this);
		requestTable = new Hashtable();
		WklTargetInfDesc InfDesc = new WklTargetInfDesc();
		wti = WklTargetInfDesc.getTargetInfMgr();
		WVM_ID = name;
		
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
    		//myWVM.start();	
	}

	// FAKE: local Factory - must modify using some remote lookup
	public static WklFactoryTargetInf locateWklFactory()
	{
		return new WorkletFactory("NewLocalWorkletFactory");
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
			WorkletFactory wFactory = new WorkletFactory(args[0]);
		}
		catch (Exception e) 
		{
			System.out.println(e);
			System.exit(0);
		}
		// Thread.currentThread().suspend(); // deprecated - change this!
	}
	
	// FAKE and not used: local Oracle - must modify using some remote lookup
	private WorkletOracle locateOracle(WorkletFactory wklFactory)
	{
		return(new WorkletOracle ("LocalWorkletOracleFor" + WVM_ID));
	}
	
    // FAKE interaction with cache; not-found by default
    private boolean cachedJunctionExists (String reqStructure, WklMatchingCriteria match) 
    {
	System.out.println ("looking up cache ...");
	System.out.println ("cache looked up ...");
	return false;
    }
	
    private Class cachedJunctionRetrieve (String targetStructure)
	throws java.lang.ClassNotFoundException
    {
	System.out.println ("retrieving cached junction ...");
	System.out.println ("cached junction retrieved ...");
	return Class.forName ("psl.workflakes.smartinf.smartjunction.SomeJunction"); // random junction - complete hack!
    }

// implementation of the WklFactoryTargetInf interface

    public String getWVMID() {return WVM_ID;}
    public String getWVMHost() {return host;}
    
    public void processRequestWorklet(WklRequestJunction request) 
    {
	String reqStructure = request.getRequestStructure();
	String theHost = request.getRequestHost();
	String theName = request.getRequestName();
	String requestCode = request.getRequestID();
	WklMatchingCriteria matchMode = request.getMatchMode();
	
	// check if factory knows about a suitable junction
	if (cachedJunctionExists (reqStructure, matchMode))
	    {
		// if so send it out to whomever had requested it
		try
		    {
			WorkletJunction cachedJunction = produceJunction (cachedJunctionRetrieve(reqStructure), theHost, theName);
			Worklet wkl = new Worklet(null);
			wkl.addJunction(cachedJunction);
			
			wkl.deployWorklet(myWVM);
		    }
		catch (Exception e)
		    {
			throw new RuntimeException(e.getMessage());	
		    }
	    }
	else
	    {
		// ask Oracle to retrieve some matching junction
		if (requestCode == null)
		    requestCode = new Integer(request.hashCode()).toString();

		LockObject requestLock = new LockObject(requestCode, request);

		FactoryWklJunction homeJunction = new FactoryWklJunction (host, WVM_ID);
		System.out.println ("Created comeback junction to host: " + host + "and WVM: " + WVM_ID);
		System.out.println ("for request with code: " + requestCode);
		homeJunction.setCode(requestCode);
		Worklet fwdWkl = new Worklet(homeJunction); // worklet must return here
		
		WklOracleJunction fwdRequest = new WklOracleJunction(host, "NewLocalWorkletOracle");
		fwdRequest.setRequestHost(host);
		fwdRequest.setRequestName(WVM_ID);
		fwdRequest.setMatchMode(matchMode);
		fwdRequest.setWklURL(null);
		fwdRequest.setRequestStructure(reqStructure);
		fwdWkl.addJunction(fwdRequest);
		
		fwdWkl.deployWorklet(myWVM);
		//System.out.println("storing request " + requestCode);
		requestTable.put (requestCode, requestLock); // when worklet returns here, it will be able to reference the original request
		System.out.println("stored request " + requestCode);
	    }
		
    }
	
    public WklRequestJunction retrieveRequest(String code)
    {
	// System.out.println ("Code for retrieving request is: " + code);
	return (WklRequestJunction)((LockObject)requestTable.get(code)).getMonitor();
    }

    public void removeRequest(String code)
    {
	requestTable.remove(code);
    }
    
    public Worklet assembleWorklet (WorkletJunction[] junctions)
    {
	Worklet wkl = new Worklet(null);
	for (int i = 0; i < junctions.length; i++)
	    wkl.addJunction(junctions[i]);
	return wkl;
    }
	
    public Worklet assembleWorklet (WorkletJunction aJunction)
    {
	Worklet wkl = new Worklet(null);
	wkl.addJunction(aJunction);
	return wkl;
    }
	
    public void requireDeployment (Worklet w)
    {
	w.deployWorklet(myWVM);
    }
	
    public WorkletJunction produceJunction(Class junctionClass, String host, String agent)
    {
	WorkletJunction newJunction;
	try
	    {	
		Constructor c = junctionClass.getConstructor (new Class[]{host.getClass(), agent.getClass()});
		newJunction = (WorkletJunction) c.newInstance(new Object[]{host, agent});
	    }
	catch (Exception e) 
	    {
		System.err.println ("junction creation failed for junction type: " + junctionClass.toString());
		throw new RuntimeException(e.getMessage());
	    }
	return (newJunction); 
    }

    // FAKE, must implement real one	
    public WorkletJunction getJunctionByURL (URL anURL, String host, String agent)
    {
	// should extract class from an URL
	// then instantiate a junction of the right class with the right parameters
	return (new SomeJunction(host, agent));
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
