/*
 * <copyright>
 * Copyright (c) 2001: The Trustees of Columbia University in the
 *  City of New York, Peppo Valetto. All Rights Reserved.
 * </copyright>
 */
 package psl.workflakes.smartinf.smartjunction;

import java.net.URL;
import psl.worklets.WVM;
import psl.worklets.Worklet;
import psl.worklets.WorkletJunction;
import psl.workflakes.smartinf.WklFactoryTargetInf;
import psl.workflakes.smartinf.WklMatchingCriteria;

public class WklRequestJunction
    extends SmartWorkletJunction
{
    protected WklFactoryTargetInf myTarget;
    protected String requestHost;
    protected String requestName;
    protected String requestID;
    protected String wklRequestStructure;
    protected URL wklRequestURL;
    protected WklMatchingCriteria matchMode;

    public WklRequestJunction (String remoteHost, String remoteName, int port)
    {
	super(remoteHost, remoteName, port);
	wklRequestURL = null;
	wklRequestStructure = null;
	requestID = null;
	}

	public WklRequestJunction (String remoteHost, String remoteName)
    {
	super(remoteHost, remoteName);
	wklRequestURL = null;
	wklRequestStructure = null;
	requestID = null;
    }

    public void init(Object _target, WVM _wvm)
    {
	// System.out.println (_target.getClass().getName());
	super.init(_target, _wvm);
	myTarget = (WklFactoryTargetInf) _target;
    }

    public String getRequestHost() {return requestHost;}
    public String getRequestName() {return requestName;}
    public String getRequestID() {return requestID;}
    public URL getWklURL() {return wklRequestURL;}
    public String getRequestStructure() {return wklRequestStructure;}
    public WklMatchingCriteria getMatchMode() {return matchMode;}

    public void setRequestHost (String host) {requestHost = host;}
    public void setRequestName (String name) {requestName = name;}
    public void setRequestID (String anID) {requestID = anID;}
    public void setWklURL (URL anURL) {wklRequestURL = anURL;}
    public void setRequestStructure (String s) {wklRequestStructure = s;}
    public void setMatchMode (WklMatchingCriteria wmc) {matchMode = wmc;}

    public void execute()
    {
    	if (wklRequestURL != null)
    	{
		try
	    	{
	    		// find junction from URL information
			WorkletJunction wj = myTarget.getJunctionByURL(wklRequestURL, requestHost, requestName);
			// ask factory to assemble and send worklet
			myTarget.requireDeployment(myTarget.assembleWorklet(wj));
	    	}
		catch (Exception e)
	    	{
			throw new RuntimeException(e.getMessage());	
	    	}
	}
	else
	{
	    // the factory will ask an Oracle for the right junction(s)
	    myTarget.processRequestWorklet(this);
	    System.out.println ("WklRequestJunction is finished ...");
	    // worklet exits here
	}
    }
}





