/*
 * <copyright>
 * Copyright (c) 2001: The Trustees of Columbia University in the
 *  City of New York, Peppo Valetto. All Rights Reserved.
 * </copyright>
 */
 
 package psl.workflakes.smartinf.smartjunction;

import psl.worklets.WVM;
import psl.workflakes.smartinf.SomeOtherTargetInf;

public class SomeOtherJunction
	extends SmartThreadWorkletJunction
{
    SomeOtherTargetInf myTarget;
	
    public SomeOtherJunction (String remoteHost, String remoteName)
    {
	super(remoteHost, remoteName);
    }

    public void init(Object _target, WVM _wvm)
    {
	System.out.println (_target.getClass().getName());
	super.init(_target, _wvm);
	myTarget = (SomeOtherTargetInf) _target;
    }
    
    public void execute()
    {
    	myTarget.doSomethingElse();
	wakeUpThread();
    }
}
