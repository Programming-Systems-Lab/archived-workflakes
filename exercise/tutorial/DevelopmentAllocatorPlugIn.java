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
import java.util.Vector;
import java.util.Collection;
import org.cougaar.util.UnaryPredicate;
import org.cougaar.domain.planning.ldm.asset.*;
import org.cougaar.domain.planning.ldm.plan.*;
import org.cougaar.domain.glm.ldm.asset.Organization;
import org.cougaar.domain.glm.ldm.asset.OrganizationPG;
import psl.worklets.WorkletJunction;
import psl.worklets.Worklet;
import psl.workflakes.coolets.*;
import psl.workflakes.coolets.adaptors.*;

/**
 * This UnaryPredicate matches all Task objects
 */
class isDevTaskPredicate implements UnaryPredicate {
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
class isProgrammerPredicate implements UnaryPredicate{
  public boolean execute(Object o) {
    return o instanceof ProgrammerAsset;
  }
}

/*class ExampleJunction
	extends WorkletJunction {
	private Class appClass = null;

	ExampleJunction (String host, String name, int port) {
		super (host, name, port);
		try {
			appClass = Class.forName("DummyApp");
		} catch (ClassNotFoundException e) {}

		System.out.println ("Going to: " + name + " on " + host +":" + port);
	}

  	public void execute() {
    	System.out.println("a worklet has arrived ... ");
    	System.out.println(" of class " + getClass().getName());
		try {
			appClass.newInstance();
		} catch (Exception e)
		{}
	}
}
*/

public class DevelopmentAllocatorPlugIn
	extends WorkletPlugIn
	implements DevAllocAdaptorInf
{
	private IncrementalSubscription myTasks;
	private IncrementalSubscription myHackers;

	protected void setupSubscriptions()
	{
		super.setupSubscriptions();

		System.out.println("DevelopmentAllocatorPlugIn - in setupSubscriptions()");
		myHackers = (IncrementalSubscription)subscribe(new isProgrammerPredicate());
		//requireJunction ("psl.workflakes.exercise.tutorial.DevAllocAdaptorInf", new isDevTaskPredicate());

		/*
		Worklet wkl = new Worklet(null);
		ExampleJunction request = new ExampleJunction ("ibm9151", "Management", 9101);
		wkl.addJunction(request);
		wkl.deployWorklet(myWVM);
		*/
		System.out.println("DevelopmentAllocatorPlugIn - out of setupSubscriptions()");
	}

	protected void execute()
	{
		super.execute();
	}


	// implementation of DevAllAdaptorInf
	public Collection getProgrammers() { return myHackers.getCollection(); }

	public void allocateAsset (Task task, Asset asset) {
		publishChange(asset);

      	AllocationResult estAR = null;

      Allocation allocation = theLDMF.createAllocation(task.getPlan(), task, asset, estAR, Role.ASSIGNED);
      publishAdd(allocation);
	}

        //fake - unused
        public Enumeration getExecutors() { return null; }
        //fake - unused
        public void setAllocJunction(AllocatorJunction j) { }
        //fake - unused
        public void allocateTask (Task t, double time) { }
}