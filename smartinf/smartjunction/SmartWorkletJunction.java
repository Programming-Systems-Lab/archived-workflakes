/*
 * <copyright>
 * Copyright (c) 2001: The Trustees of Columbia University in the
 *  City of New York, Peppo Valetto. All Rights Reserved.
 * </copyright>
 */
 package psl.workflakes.smartinf.smartjunction;

import java.util.Hashtable;
import java.util.Set;
import java.util.HashSet;
import java.lang.reflect.*;
import psl.worklets.WorkletJunction;
import psl.worklets.WVM;
import psl.workflakes.smartinf.*;

public abstract class SmartWorkletJunction // assumes only ONE target per junction
	extends WorkletJunction
{
    static private WklJunctionDescLocator locator = LocateDesc();
    //static protected Set targetSet;

    protected WklTargetInf voidInf;
    protected Class targetClass;

  public SmartWorkletJunction(String remoteHost, String remoteName, int port) 
  {
	super(remoteHost, remoteName, port);
	voidInf = null;
	targetClass = null;
  }
  
  public SmartWorkletJunction(String remoteHost, String remoteName) 
  {
	super(remoteHost, remoteName);
	voidInf = null;
	targetClass = null;
  }

  public void init(Object _target, WVM _wvm) 
  {
      super.init(_target, _wvm);
      setTargetClass (_target);
  }
  
  // inspects the target of the junction, which is set at run-time
  public Hashtable getTargetStructure() 
    {
  	return (inspectJunctionTarget(targetClass));
    }
  
  // inspects the target of the junction, which is set at run-time  
  public Set getTargetDesc() 
    {
  	WklTargetInf wti = WklTargetInfDesc.getTargetInfMgr();
  	return (wti.getInfDesc(targetClass));
    }
  
  // inspects the possible target(s?) of the junction, statically from the field definitions 
  static public Set getJunctionTargets(Class myClass)
  {
      HashSet targetSet = new HashSet();
      Field[] myFields = myClass.getDeclaredFields();
	
	//System.out.println ("candidate junction Class is:" + myClass.getName());
	
  	try
  	{
  		Class baseTargetInf = Class.forName("psl.workflakes.smartinf.WklTargetInf");
  		for (int i = 0; i < myFields.length; i++)
  		{
		    if (baseTargetInf.isAssignableFrom(myFields[i].getType()))
			{
			    System.out.println ("\tClass of Field: " + myFields[i].getName() + " is: " + myFields[i].getType().getName());
			    targetSet.add(myFields[i].getType());
			}
  		}
  	}
  	catch (ClassNotFoundException e)
  	{
  		System.err.println (e);	
  	}
  	
  	return targetSet;
  }
  
  // inspects the possible target(s?) of the junction, statically from the field definitions
  public String getJunctionDesc()
  {
  	return locator.getDesc(this.getClass());
  }
    
    protected void setTargetClass (Object _target)
    {
	Class c = _target.getClass();
	try
	    {
		Class baseTargetInf = Class.forName("psl.workflakes.smartinf.WklTargetInf");

		System.out.println ("initializing Smart Junction: " +  this.getClass().getName());
		targetClass = c;

		if (baseTargetInf.isAssignableFrom(targetClass))
		    {
			voidInf = (WklTargetInf)_target;
		    }
		else
		    throw (new RuntimeException("invalid interface for this junction's target"));
	    }
	catch  (ClassNotFoundException e)
	    {
		System.err.println (e);
	    }
	catch (Exception e)
	    {
		System.err.println("target is not a valid interface");
	    }
    }

    private Hashtable inspectJunctionTarget (Class c)
    {
  	WklTargetInf wti = WklTargetInfDesc.getTargetInfMgr();
	return (wti.inspectMyself(c));
    }

    // FAKE - local locator 
    static private WklJunctionDescLocator LocateDesc()
    {
	return (new WklJunctionDescLocator());
    }
} 
