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

// represents the response from the Worklet Oracle to the Worklet Factory
public class FactoryWklJunction
	extends SmartWorkletJunction
{
    private WklFactoryTargetInf myTarget;
    private Class[] retrievedJunctions;
    private URL junctionURL;
    private String requestCode = null;
	
	public FactoryWklJunction (String remoteHost, String remoteName)
   	{
		super(remoteHost, remoteName);
		junctionURL = null;
   	}
    
    	public void init(Object _target, WVM _wvm)
    	{
		super.init(_target, _wvm);
		myTarget = (WklFactoryTargetInf) _target;
    	}
    	
    	public void execute()
    	{
	    WklRequestJunction theRequest = myTarget.retrieveRequest(requestCode);
	    if (theRequest == null)
		throw new RuntimeException ("no matching Request junction found");
	    if (junctionURL != null)
		{
		    try
			{
			    // find junction from URL information
			    WorkletJunction wj = myTarget.getJunctionByURL(junctionURL, theRequest.getRequestHost(), theRequest.getRequestName());
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
		    System.out.println ("FactoryWklJunction now asking for the assembling of worklet");
		    WorkletJunction[] wjArray = new WorkletJunction[retrievedJunctions.length];
		    for (int i=0; i < retrievedJunctions.length; i++)
		    {
		    	WorkletJunction aJunction = myTarget.produceJunction (retrievedJunctions[i], theRequest.getRequestHost(), theRequest.getRequestName());
		    	System.out.println ("produced a junction of class: " + aJunction.getClass().getName());
			try
			    {
				Class threadJunctionClass = Class.forName("psl.workflakes.smartinf.smartjunction.SmartThreadWorkletJunction");
				if (theRequest.getRequestID() != null) // it means that the original request corresponds to a thread to be woken up
				    {
					if (threadJunctionClass.isAssignableFrom(retrievedJunctions[i]))
					    ((SmartThreadWorkletJunction)aJunction).setLockID(theRequest.getRequestID());
				    }
			    }
			catch (ClassNotFoundException e)
			    {
				System.err.println (e.getMessage());
			    }
			wjArray[i] = aJunction;
		    }
		    myTarget.requireDeployment(myTarget.assembleWorklet(wjArray));
		}
	    myTarget.removeRequest (requestCode);
	}
 	
    public URL getJunctionURL() {return junctionURL;}
    public Class[] getJunctions() {return retrievedJunctions;}
    public String getCode() {return requestCode;}
    public void setJunctionURL (URL anURL) {junctionURL = anURL;}
    public void setCode(String aCode) {requestCode = aCode;}
    public void setJunctions (Class[] junctionArray) 
    {
	retrievedJunctions = junctionArray;
	System.out.println ("Retrieved " + retrievedJunctions.length + " candidate junctions");
    }
 }



