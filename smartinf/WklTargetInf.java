/*
 * <copyright>
 * Copyright (c) 2001: The Trustees of Columbia University in the
 *  City of New York, Peppo Valetto. All Rights Reserved.
 * </copyright>
 */
 package psl.workflakes.smartinf;

import java.util.Hashtable;
import java.util.Set;

public interface WklTargetInf
{
    public Set getInfDesc(Class c);
    public Hashtable inspectMyself(Class c);
    public String outputInfStructure (Hashtable ll);

    // XML stuff -- FAKE for now
    public String XMLizeInfStructure (Hashtable ll);
    public String toXMLStructure (String s);
    public String fromXMLStructure (String s);
}





