/*
 * <copyright>
 * Copyright (c) 2001: The Trustees of Columbia University in the
 *  City of New York, Peppo Valetto. All Rights Reserved.
 * </copyright>
 */
 package psl.workflakes.smartinf.smartjunction;

import java.io.Serializable;

// WklTargetDescLocator is just a placeholder for some distributed service for the lookup/oracle of descriptors for Target Interfaces

public class WklJunctionDescLocator
	implements Serializable
{
    
    public WklJunctionDescLocator() {}
	
    public String getDesc(Class c)
    {
	String s = "this is the ";
	if (c.getName() == "psl.workflakes.smartinf.smartjunction.SmartWorkletJunction")
	    s = s.concat("*VOID* ");	
	s = s.concat("descriptor for the ");
	s = s.concat(c.getName());
	s = s.concat(" smart junction");
	if (c.getName() == "psl.workflakes.smartinf.smartjunction.SmartWorkletJunction")
	    s = s.concat(" - root of the hierarchy.");
	return (s);
    }
}
