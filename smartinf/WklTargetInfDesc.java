/*
 * <copyright>
 * Copyright (c) 2001: The Trustees of Columbia University in the
 *  City of New York, Peppo Valetto. All Rights Reserved.
 * </copyright>
 */
 package psl.workflakes.smartinf;

import java.util.*;
import java.lang.reflect.*;
import java.io.Serializable;

public final class WklTargetInfDesc
    implements Serializable
{
    private Set targetInfDescSet;
    static private WklTargetDescLocator locator = LocateDesc();
    static private TargetDescMgr tinf;
	
    public WklTargetInfDesc()
    {
	targetInfDescSet = null;
	// locator = LocateDesc();
	tinf = new TargetDescMgr();
    }

    static public WklTargetInf getTargetInfMgr()
    {
	return tinf;
    }
		
    private Set getDescSet() {return (targetInfDescSet);}
	
    private void StoreDesc (Class c)
    {
	targetInfDescSet = Collections.synchronizedSet(new HashSet());
	for (Enumeration e = FindAllTargetInf(c).elements() ; e.hasMoreElements() ;)
	    {
		targetInfDescSet.add(getLocator().getDesc((Class)e.nextElement()));
	    }	    
    }
			
    static private WklTargetDescLocator LocateDesc()
    {
	return (new WklTargetDescLocator());
    }
	
    static private WklTargetDescLocator getLocator()
    {
	return locator;
    }
    		
    private Vector FindAllTargetInf(Class myClass)
    {
	Vector targetInfVector = new Vector();

	if (myClass.isInterface())
	    throw (new RuntimeException ("Examined Class is an interface"));		
	try
	    {
		Class baseTargetInf = Class.forName("psl.workflakes.smartinf.WklTargetInf");
		
		Class[] allInf = myClass.getInterfaces();
		for (int i = 0; i < allInf.length; i++)
		    {
			if (baseTargetInf.isAssignableFrom(allInf[i]))
			    {
				targetInfVector.addElement(allInf[i]);
				// System.out.println ("Added interface " + allInf[i].getName() + " to InfVector");
				targetInfVector.addAll(FindTargetInf(allInf[i]));
			    }
		    }
	    }
	catch (java.lang.ClassNotFoundException e)
	    {
		System.err.println (e);
	    }

	return (targetInfVector);
    }
	
    private Vector FindTargetInf (Class myInf)
    {
    	Vector targetInfVector = new Vector();
    	
	if (!myInf.isInterface())
	    throw (new RuntimeException ("Class is NOT an interface"));	
	try
	    {
		Class baseTargetInf = Class.forName("psl.workflakes.smartinf.WklTargetInf");
		
		Class[] allInf = myInf.getInterfaces();
		for (int i = 0; i < allInf.length; i++)
		    {
			if (!baseTargetInf.isAssignableFrom(allInf[i]))
			    throw (new RuntimeException ("Interface is not of type WklTargetInf (Worklet Target Interface)"));
			targetInfVector.addElement(allInf[i]);
			// System.out.println ("Added interface " + allInf[i].getName() + " to InfVector");
			targetInfVector.addAll(FindTargetInf (allInf[i]));
		    }
	    }
	catch (java.lang.ClassNotFoundException e)
	    {
		System.err.println (e);
	    }
	return (targetInfVector);
    }


// embedded classes

    //TargetDescMgr
    private final class TargetDescMgr
	implements WklTargetInf, Serializable
    {
	public Set getInfDesc(Class c)
	{
	    StoreDesc(c);
	    return (getDescSet());
	}

	public Hashtable inspectMyself(Class c)
	{
	    Hashtable myInterfacesTable = new Hashtable();

	    for (Enumeration e = FindAllTargetInf(c).elements() ; e.hasMoreElements() ;)
	     	{
		    Class targetInf = (Class) e.nextElement();
		    // System.out.println ("**************");
		    // System.out.println (targetInf);
		    if (!myInterfacesTable.containsKey(targetInf)) //no duplicate interfaces
		    {
		    	//System.out.println (targetInf);
		    	Method[] myMethods = targetInf.getMethods();
		    	LinkedList myMethodList = new LinkedList(Arrays.asList(myMethods));
		    	myInterfacesTable.put(targetInf, myMethodList);
		    }
			    
		    /* for (int j = 0; j < myMethods.length; j++)
			{
			    System.out.println ("#####");
			    System.out.println (myMethods[j]);
			} 
		    */
		    // System.out.println ("**************");
		}
	    return (myInterfacesTable);
	}

	public String outputInfStructure (Hashtable ht)
	{
	    String descriptor = new String("Outputting the structure of target class: " + getClass().getName());
		
	    for (Enumeration htEnum = ht.keys(); htEnum.hasMoreElements() ;)
	     	{
	     	    Object infItem = htEnum.nextElement();
	     	    descriptor = descriptor.concat ("\t" + infItem.toString()+ "\n");
	     	    descriptor = descriptor.concat ("\t\t" + getLocator().getDesc((Class)infItem) + "\n");
		    LinkedList ll = (LinkedList) ht.get(infItem);
	    	    ListIterator l_iterator = ll.listIterator(0);
	    	    while (l_iterator.hasNext())
			{
		    		Object methodItem = l_iterator.next();
		    		// System.out.println(desc_item);
		    		descriptor = descriptor.concat ("\t\t" + methodItem.toString()+ "\n");
			}
		}
	    	
	    return (descriptor);	
	}

	public String XMLizeInfStructure (Hashtable ht) 
    	{
    		String descriptor = new String("<XML v1.0>\n");
    		for (Enumeration htEnum = ht.keys(); htEnum.hasMoreElements();)
		{
	    		Object infItem = htEnum.nextElement();
	    		descriptor = descriptor.concat ("\t<interface>" + infItem.toString()+ "</interface>\n");
	    		descriptor = descriptor.concat ("\t\t<infdesc>" + getLocator().getDesc((Class)infItem) + "</infdesc>\n");
	    		LinkedList ll = (LinkedList) ht.get(infItem);
	    		ListIterator l_iterator = ll.listIterator(0);
	    		while (l_iterator.hasNext())
			{
	    			Object desc_item = l_iterator.next();
	    			if (desc_item instanceof Method)
				descriptor = descriptor.concat("\t\t<method>" + desc_item.toString()+ "</method>\n");
	    			else if ((desc_item instanceof Class) && ((Class)desc_item).isInterface())
	    				descriptor = descriptor.concat("\t<interface>" + desc_item.toString()+ "</interface>\n");	    		
			}
		}
		descriptor = descriptor.concat("</XML>");
		return descriptor;    
    	}
    	
    	public String toXMLStructure (String s) { return (new String());}
	public String fromXMLStructure (String s) { return (new String());}
    }
        
    // end of embedded classes

}

