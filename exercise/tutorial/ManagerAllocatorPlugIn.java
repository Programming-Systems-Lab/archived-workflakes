/*
 * <copyright>
 * Copyright (c) 2001: The Trustees of Columbia University in the
 *  City of New York, Peppo Valetto. All Rights Reserved.
 * </copyright>
 */
 package psl.workflakes.exercise.tutorial;

import psl.workflakes.exercise.tutorial.assets.*;
import org.cougaar.core.plugin.SimplePlugIn;
import org.cougaar.core.cluster.IncrementalSubscription;
import java.util.Enumeration;
import java.util.Collection;
import org.cougaar.util.UnaryPredicate;
import org.cougaar.domain.planning.ldm.asset.*;
import org.cougaar.domain.planning.ldm.plan.*;
import org.cougaar.domain.glm.ldm.asset.Organization;
import org.cougaar.domain.glm.ldm.asset.OrganizationPG;
import psl.workflakes.coolets.*;

/**
 * This UnaryPredicate matches all Task objects
 */
class isTaskPredicate implements UnaryPredicate{
  public boolean execute(Object o) {
  	boolean ret = false;
   	if (o instanceof Task)
   	{
   		Task t = (Task)o;
		ret = t.getVerb().equals(Verb.getVerb("CODE"));
	}
	return ret;   
  }
}

/**
 * This UnaryPredicate matches all ProgrammerAsset objects
 */
class isDevOrgPredicate implements UnaryPredicate{
  public boolean execute(Object o) {
  	boolean ret = false;
  	if (o instanceof Organization)
  	{
  		Organization org = (Organization) o;
  		ret = org.getOrganizationPG().inRoles(Role.getRole("SoftwareDevelopment"));
  	}
    return ret;
  }
}

public class ManagerAllocatorPlugIn
	extends WorkletPlugIn
	implements MgrAllocAdaptorInf
{
	private IncrementalSubscription myTasks;
	private IncrementalSubscription swDevOrgs;

	protected void setupSubscriptions()
	{
		super.setupSubscriptions();
		System.out.println("ManagerAllocatorPlugIn - in setupSubscriptions()");
		//requireJunction ("psl.workflakes.exercise.tutorial.MgrAllocAdaptorInf", new isTaskPredicate());
		swDevOrgs = (IncrementalSubscription)subscribe(new isDevOrgPredicate());
		System.out.println("ManagerAllocatorPlugIn - out of setupSubscriptions()");
	}
		
	protected void execute()
	{
		super.execute();				
	}
	
	//implementation of interface MgrAllocAdaptorInf
	
	public Enumeration getDevOrg() { return swDevOrgs.elements(); }
	
	public void allocateAsset (Task t, Asset a)
	{
		AllocationResult ar = null;
		
		Allocation allocation = theLDMF.createAllocation(t.getPlan(), t, a, ar, Role.ASSIGNED);
		System.out.println ("Task " + t + " allocated to: " + a.getItemIdentificationPG().getItemIdentification());
		publishAdd(allocation);
	}

}