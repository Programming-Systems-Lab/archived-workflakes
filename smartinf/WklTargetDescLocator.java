/*
 * <copyright>
 * Copyright (c) 2001: The Trustees of Columbia University in the
 *  City of New York, Peppo Valetto. All Rights Reserved.
 * </copyright>
 */
 package psl.workflakes.smartinf;

// WklTargetDescLocator is just a placeholder for some distributed service for the lookup/oracle of descriptors for Target Interfaces

public class WklTargetDescLocator
{
    
    public WklTargetDescLocator() {}
	
    public String getDesc(Class c)
    {
	String s = "this is the ";
	if (c.getName() == "psl.workflakes.smartinf.WklTargetInf")
	    s = s.concat("*VOID* ");	
	s = s.concat("descriptor for the ");
	s = s.concat(c.getName());
	s = s.concat(" interface");
	if (c.getName() == "psl.workflakes.smartinf.WklTargetInf")
	    s = s.concat(" - root of the hierarchy.");
	return (s);
    }
}

