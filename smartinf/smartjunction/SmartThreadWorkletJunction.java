/*
 * <copyright>
 * Copyright (c) 2001: The Trustees of Columbia University in the
 *  City of New York, Peppo Valetto. All Rights Reserved.
 * </copyright>
 */
 package psl.workflakes.smartinf.smartjunction;

import java.util.Hashtable;
import psl.worklets.WVM;
import psl.workflakes.smartinf.WklThreadTargetInf;
import psl.workflakes.smartinf.LockObject;

// this is an abstract SmartWorkletJunction that also knows that it needs to
// wake up a thread on the target system
// the thread is identified via a String, i.e. the field lockID
public abstract class SmartThreadWorkletJunction
    extends SmartWorkletJunction
{
    protected String lockID;
    protected WklThreadTargetInf myThreadInf;
 
    public SmartThreadWorkletJunction (String remoteHost, String remoteName)
    {
	super(remoteHost, remoteName);
	lockID = null;
	myThreadInf = null;
    }

 public void init(Object _target, WVM _wvm)
    {
	// System.out.println (_target.getClass().getName());
	super.init(_target, _wvm);
	myThreadInf = (WklThreadTargetInf) _target;
    }

    public String getLockID () {return (lockID);}
    public void setLockID(String lid) {lockID = lid;}

    protected void wakeUpThread ()
    {
	Hashtable ht = myThreadInf.getLockTable();
	LockObject SyncObj = (LockObject) ht.get(lockID);
	Object theMonitor = SyncObj.getMonitor();
	
	synchronized(theMonitor)
	{
	    System.out.println ("\t Junction is waking up thread: " + theMonitor.toString());
	    SyncObj.getMonitor().notifyAll();
	}
    }
}
