/*
 * <copyright>
 * Copyright (c) 2001: The Trustees of Columbia University in the
 *  City of New York, Peppo Valetto. All Rights Reserved.
 * </copyright>
 */
 package psl.workflakes.exercise.tutorial;

import java.util.Enumeration;
import java.util.Vector;
import psl.workflakes.coolets.*;
import psl.worklets.WVM;
import org.cougaar.domain.planning.ldm.plan.Task;
import org.cougaar.domain.planning.ldm.asset.Asset;
import org.cougaar.core.cluster.Subscription;
import org.cougaar.core.cluster.IncrementalSubscription;

public class MgrAllocJunction
	extends CooletIncomingJunction {

	public MgrAllocJunction (String host, String name, int rPort, int sPort) {
		super (host, name, rPort, sPort);
	}
	
	public void init (Object system, WVM wvm) {
		super.init(system, wvm);
		System.out.println ("system class: " + system.getClass().getName());
		System.out.println ("trigger predicate class: " + myTriggerPred);
	}
	
	public void execute() {
		super.execute();
	}
	public void embeddedExec (IncrementalSubscription sub) {
		super.embeddedExec(sub);
		
		Enumeration enum = sub.elements();
		
		while (enum.hasMoreElements()) 
		{
			Task t = (Task)enum.nextElement();
			if (t.getPlanElement() != null)
				continue;
			
			 Enumeration devOrgs = ((MgrAllocAdaptorInf)myPlugInTarget).getDevOrg();
			while (devOrgs.hasMoreElements()) 
			{ 
				Asset devOrgAsset = (Asset)devOrgs.nextElement();
				if (devOrgAsset != null) {
					((MgrAllocAdaptorInf)myPlugInTarget).allocateAsset (t, devOrgAsset);
					break;
				}
			
			}
		}	
	}
		
}