/*
 * <copyright>
 * Copyright (c) 2001: The Trustees of Columbia University in the
 *  City of New York, Peppo Valetto. All Rights Reserved.
 * </copyright>
 */
 package psl.workflakes.smartinf;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Vector;
import java.util.Enumeration;
import java.util.Iterator;

public class WklMatchingCriteria
    implements Serializable
{
	private final static String defaultPolicy = "Identity";
	private final static boolean defaultActivation = false;
	private final static String baseTargetClassName = "psl.workflakes.smartinf.WklTargetInf";	
	private Hashtable criteria;
	private WklTargetInf wti;
	
	public WklMatchingCriteria ()
	{
		criteria = new Hashtable();
		WklTargetInfDesc InfDesc = new WklTargetInfDesc();
	}
	
	public WklMatchingCriteria (Class c)
	{
		this();
		checkCriterionClass(c);
		wti = WklTargetInfDesc.getTargetInfMgr();
		for (Enumeration enum = wti.inspectMyself(c).keys() ; enum.hasMoreElements() ; )
		{	
			criteria.put (enum.nextElement(), new AtomicCriterion (defaultPolicy, defaultActivation));
		}
	}
	
	public WklMatchingCriteria (String s, Class c)
	{
		this();
		checkCriterionClass(c);
		wti = WklTargetInfDesc.getTargetInfMgr();
		for (Enumeration enum = wti.inspectMyself(c).keys() ; enum.hasMoreElements() ; )
		{	
			criteria.put (enum.nextElement(), new AtomicCriterion (s, defaultActivation));
		}		
	}
	
	public void activateCriterion (Class c)
	{
		checkCriterionClass(c);
		AtomicCriterion a = (AtomicCriterion)criteria.get(c);
		a.setActivated(true);
		criteria.put (c, a);
	}
	
	public void deactivateCriterion (Class c)
	{
		checkCriterionClass(c);
		AtomicCriterion a = (AtomicCriterion)criteria.get(c);
		a.setActivated(false);
		criteria.put (c, a);
	}
	
	public void modifyCriterion (Class c, String s)
	{
		checkCriterionClass(c);
		AtomicCriterion a = (AtomicCriterion)criteria.get(c);
		a.setMatchPolicy(s);
		criteria.put (c, a);
	}	
		
	public Hashtable getActiveCriteria() 
	{
		Hashtable ht = new Hashtable();
	
		for (Enumeration enum = criteria.keys() ; enum.hasMoreElements() ; )
		{
			Class inf = (Class) enum.nextElement();
			AtomicCriterion a = (AtomicCriterion) criteria.get(inf);
			if (a.isActivated())
				ht.put (inf, a.getMatchPolicy());
		}
		
		return ht;
		
	}
	
	public String getCriterion (Class c)
	{
		checkCriterionClass(c);
		AtomicCriterion a = (AtomicCriterion)criteria.get(c);
		if (a == null || !a.isActivated())
			return null;
		return a.getMatchPolicy();
	}
	
	public void addCriterion (Class c)
	{
		checkCriterionClass(c);
		criteria.put (c, new AtomicCriterion (defaultPolicy, defaultActivation));
	}
	
	public void addCriterion (Class c, String s)
	{
		checkCriterionClass(c);
		criteria.put (c, new AtomicCriterion (s, defaultActivation));
	}
	
	protected void checkCriterionClass (Class c) {
		try {
			Class baseClass = Class.forName(baseTargetClassName);
			if (!baseClass.isAssignableFrom(c))
				throw (new RuntimeException ("Examined Class does not represent a valid criterion class"));
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e.getMessage());
		} 
	}
			

// inner classes
	private final class AtomicCriterion
	    implements Serializable
	{
		private boolean activated;
		private String matchPolicy;
		
		AtomicCriterion (String policy, boolean flag)
		{
			matchPolicy = policy;
			activated = flag;
		}
		
		boolean isActivated() {return activated;}
		String getMatchPolicy() {return matchPolicy;}
		
		void setActivated (boolean flag) {activated = flag;}
		void setMatchPolicy (String s) {matchPolicy = s;}
	}	
}
