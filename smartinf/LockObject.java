/*
 * <copyright>
 * Copyright (c) 2001: The Trustees of Columbia University in the
 *  City of New York, Peppo Valetto. All Rights Reserved.
 * </copyright>
 */
 
 package psl.workflakes.smartinf;

public class LockObject 
{
    private String myID;
    private Object myMonitor;
    
    public LockObject()
    {
	System.out.println ("LockObject constructor - 0 parameters.");
	myID = null;
	myMonitor = null;
    }
    
    public LockObject(String s)
    {
	System.out.println ("LockObject constructor - 1 parameters. ID is: " + s);
	myID = s;
	myMonitor = null;
    }

    public LockObject(String s, Object o)
    {
	System.out.println ("LockObject constructor - 2 parameters. ID is: " + s);
	myID = s;
	myMonitor = o;
	if (myMonitor == null)
	    System.out.println (getClass().getName() + " Monitor is null!!!");
    }

    public String getID() {return myID;}
    public void setID(String s) {myID = s;}
    public Object getMonitor() 
    {
	if (myMonitor == null)
	    System.out.println (getClass().getName() + " Monitor is null!!!");
	return myMonitor;
    }
    public void setMonitor(Object anObj) {myMonitor = anObj;}
}



