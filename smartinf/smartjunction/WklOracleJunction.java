/*
 * <copyright>
 * Copyright (c) 2001: The Trustees of Columbia University in the
 *  City of New York, Peppo Valetto. All Rights Reserved.
 * </copyright>
 */
 package psl.workflakes.smartinf.smartjunction;

import java.util.Arrays;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.HashSet;
import psl.worklets.WVM;
import psl.worklets.WorkletJunction;
import psl.workflakes.smartinf.WklOracleTargetInf;
import psl.workflakes.smartinf.WklMatchingCriteria;

public class WklOracleJunction
    extends WklRequestJunction
{
    private WklOracleTargetInf myTarget;
    private Class[] junctionArray;

    public WklOracleJunction (String remoteHost, String remoteName, int rPort, int sPort)
    {
	super(remoteHost, remoteName, rPort, sPort);
    }

    public void init(Object _target, WVM wvm)
    {
	_system = _target;
	_wvm = wvm;
	setTargetClass (_target); // settings needed by the SmartWorkletJunction superclass
	myTarget = (WklOracleTargetInf) _target;
    }

    public void execute()
    {
	Hashtable matchInf = matchMode.getActiveCriteria();
	HashSet hs = new HashSet();

    	myTarget.storeRequestStructure (wklRequestStructure);
    	for (Enumeration enum = matchInf.keys() ; enum.hasMoreElements() ; )
    	{
    		Class c = (Class) enum.nextElement();
    		String policy = (String) matchInf.get(c); 
		// no duplicates wanted
		hs.addAll(Arrays.asList(myTarget.matchJunctions(c, policy)));
    	}
	
	junctionArray = (Class[])hs.toArray(new Class[]{});
	if (junctionArray.length > 1)
	    myTarget.sortJunctions (junctionArray, matchInf);
	FactoryWklJunction fj = (FactoryWklJunction)_originJunction;
	fj.setJunctions(junctionArray);
    }

}
