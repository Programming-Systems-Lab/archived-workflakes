/**
 * <p>Title: AllocatorPlugIn</p>
 * <p>Description: Base Class for {@link WorkletPlugIn WorkletPlugIns}
 * that implement {@link psl.workflakes.coolets.adaptors.AllocatorAdaptorInf AllocatorAdaptorInf}</p>
 * Accommodates and works in conjunction with {@link psl.workflakes.coolets.AllocatorJunction AllocatorJunction}
 * to define allocations of (leaf) <code>Tasks</code> to WEJ that must carryout computational
 * actions on some taret component.
 * <copyright>
 *  Copyright 1997-2000 Defense Advanced Research Projects
 *  Agency (DARPA) and ALPINE (a BBN Technologies (BBN) and
 *  Raytheon Systems Company (RSC) Consortium).
 *  This software to be used only in accordance with the
 *  COUGAAR licence agreement.
 * Derivative Work Copyright (c) 2001: The Trustees of Columbia University in the
 *  City of New York, Peppo Valetto. All Rights Reserved.
 * </copyright>
 */
package psl.workflakes.coolets;

import org.cougaar.core.society.Communications;
import org.cougaar.core.society.UniqueObject;
import org.cougaar.core.cluster.IncrementalSubscription;
import org.cougaar.core.cluster.ClusterServesPlugIn;
import org.cougaar.domain.planning.ldm.plan.*;
import org.cougaar.domain.planning.ldm.asset.Asset;
import org.cougaar.domain.planning.ldm.asset.PropertyGroup;
import org.cougaar.util.UnaryPredicate;
import java.util.Enumeration;
import java.util.Vector;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.lang.reflect.*;

import psl.worklets.*;
import psl.workflakes.coolets.*;
import siena.*;
import psl.workflakes.coolets.adaptors.*;
import psl.workflakes.coolets.assets.*;

/**
 * This PlugIn subscribes to tasks in a workflow and allocates
 * the workflow sub-tasks to executor agent assets.
 **/
public class AllocatorPlugIn
	extends WorkletPlugIn
	implements AllocatorAdaptorInf, ExecutorAdaptorInf, TaskReturnInf
{
  protected AllocatorJunction allocJunction = null;
  protected IncrementalSubscription allExecutors; // executor agent assets that I allocate to

  /**
   * Predicate matching all ExecAgentAssets
   */
  protected UnaryPredicate allExecutorsPredicate = new UnaryPredicate() {
	public boolean execute(Object o) {
	  return (o instanceof ExecAgentAsset);
	}
  };

  /**
   * Establish subscription for tasks and assets
   **/
  public void setupSubscriptions() {
  	super.setupSubscriptions();

  	allExecutors =  (IncrementalSubscription)subscribe(allExecutorsPredicate);
  }

  /**
   * Top level plugin execute loop.  Handle changes to my subscriptions.
   **/
  public void execute() {
        super.execute();
  }

  /**
   * Phisically create the <code>Allocation</code> of a WEJ to a Task and publish it
   * on the blackboard.
   * In current implementation, also invoke the execution of the allocated WEJ via
   * <code>dispatchAction()</code>
   * @see dispatchAction(Allocation a, ExecAgentAsset executor)
   *
   * @param task the <code>Task</code> that is being allocated
   * @param executor the WEJ to be allocated to task
   * @param after start time for the allocated task
   * @return success flag for this allocation action
   */
  protected boolean doAction (Task task, ExecAgentAsset executor, double after) {
    if (executor == null)
      return false;

    double realStart = (double) currentTimeMillis();
    if (realStart < after)
	realStart = after;
    //task.setPreference(AspectType.START_TIME, realStart);
    System.out.println ("Starting at: " + realStart);

    psl.workflakes.coolets.Schedule sched = executor.getSchedule();

    // Add the task to the executor's schedule
    //sched.setWork(realStart, task);
    publishChange(executor);

    AllocationResult estAR = null;

    // Create an estimate that reports that we did just what we
    // were asked to do
    boolean onTime = true;
    int []aspect_types = {AspectType.START_TIME};
    double []results = {realStart};
    estAR =  theLDMF.newAllocationResult(1.0, // rating
      onTime, // success or not
      aspect_types,
      results);

    Allocation allocation =
      theLDMF.createAllocation(task.getPlan(), task, executor, estAR, Role.ASSIGNED);


    //it used to be the reverse: first publishAdd() then dispatchAction()
    dispatchAction (allocation, executor);
    publishAdd(allocation);
    return true;
  }

  /**
   * Try to download the class defintion of a WEJ from some remote repository.
   * The current base implementation is a dummy one. Override as neeeded.
   * @param executor Cougaar <code>Asset</code> representing the WEJ
   * @param className the complete class name to be downloaded
   * @return the Class definition for the required WEJ
   * @throws ClassNotFoundException in case the class cannot be found or downloaded
   * via the remote means
   */
  protected Class remoteClassDownload (ExecAgentAsset executor, String className)
    throws ClassNotFoundException {
    Class theClass;
    // fake! come up with dynamic class downloading from an URL or a Worklet Factory
    if (true)
      throw (new ClassNotFoundException("Can't remotely download Class " + className));
    return theClass;
  }

  // implementation of the AllocatorAdaptorInf interface

  /**
   * set a handle to the {@link AllocatorJunction AllocatorJunction} that is currently executing
   * @param j the junction to be set
   */
  public void setAllocJunction (AllocatorJunction j) { allocJunction = j; }

  /**
   * make available a list of all known Cougaar <code>Assets</code> representing WEJs
   * @return the WEJ list
   */
  public Enumeration getExecutors() {return allExecutors.elements(); }

  /**
   * Empty base implementation; override as needed.
   * @param t a Task
   * @param a Asset to be allocated to that Task
   */
  public void allocateAsset (Task t, Asset a) {
    //fill here
  }

  /**
   * Allocate an ExecAgentAsset (that is, a WEJ) for this task.  Task must be scheduled
   * after the moment "after"
   * @param task the task that must be carried out
   * @param after the computed starting moment for the task
   */
  public void allocateTask(Task task, double after) {
	System.out.println (getClass().getName() + ":allocateTask():");
	ExecAgentAsset executor = allocJunction.findExecutor (task.getVerb());

	if (executor != null) {
		if (doAction (task, executor, after)) {
			System.out.println("\nAllocated the following task to "
				+executor.getTypeIdentificationPG().getTypeIdentification()+": "
				+executor.getItemIdentificationPG().getItemIdentification());
			System.out.println("Task: "+task);
	  	}
		else
		  failTask (task);
	}
	else
          failTask (task);

	System.out.println (getClass().getName() + ":allocateTask():");
    }

  // implementation of TaskReturnInf Interface
  /**
   * Method called when a Worklet carrying one or more WEJs is sent back to base, that is,
   * the <code>WVM</code> associated to this Workflakes node.
   * @param a the Allocation describing the association of a WEJ to a given workflow task
   * @param executor the allocated WEJ
   */
  public void postAction (Allocation a, psl.workflakes.coolets.assets.ExecAgentAsset executor) {
    // this must be executed after the worklet comes back
    double endTime = (double) currentTimeMillis();
    int []aspect_types = {AspectType.END_TIME};
    double []results = {endTime};
    AllocationResult newAR = new AllocationResult(1.0, true, aspect_types, results);
    try {
      openTransaction();
      AllocationResult oldAR = a.getEstimatedResult();
      a.setEstimatedResult (new AllocationResult (oldAR, newAR));
      Task t = a.getTask();
      System.out.println ("\tEND time of " + t.toString() + " is: " + endTime);
      getSubscriber().publishChange(a);
      getSubscriber().publishChange(t); // forces marking of the task as changed!
    } catch  (Exception e) {
      synchronized (System.err) {
        System.err.println("Allocator has caught "+e);
        e.printStackTrace();
      }
    }
   finally {
    closeTransaction();
    }
  }

  /**
   * Measures to be taken when a Task cannot be executed completely.
   * This base implementation is a dummy one to be overridden with situational
   * code by subclasses
   * @param t the Task whose execution is failing
   */
  public void failTask (Task t) {
    // needs to know how to abort WF
    System.out.println ("\tCannot perfom required action for Task: " + t);
  }

  /**
   * Utility method to find a single blackboard item by its UID.
   * Employed by {@link CooletIncomingJunction CooletIncomingJunctions} that need
   * to retrieve this kind of info
   * @param pred a Cougaar <code>UnaryPredicate</code> defining the query to the Blackboard.
   * The UnaryPredicate must express a query that uses an object UID, hence ensuring that
   * only one object in the blackborad can be matched
   * @return the Object matched by the query
   */
  public UniqueObject findByUID (UnaryPredicate pred) {
    // pred should be such that it uses UID info and hence matches only one object
   /*
    Collection coll = null;
    try {
	openTransaction();
	coll = query(pred);
    }catch  (Exception e) {
      synchronized (System.err) {
      	System.err.println("Caught "+e);
      	e.printStackTrace();
      }
    }
    finally {
     	closeTransaction();
    }
   */
   Collection coll = delegateQuery(pred);
    if (coll == null || coll.size() != 1)
	return null;
    Iterator iter = coll.iterator();
    return (iter.hasNext())?((UniqueObject)iter.next()):null;
  }

    // implementation of the ExecutorAdaptorInf interface
  /**
   * Send out a WEJ to the target indicated as part of an allocated task
   * @param a allocation including information about the Task and the target
   * @param executor indicates the WEJ to be dispatched via Worklets.
   */
  public void dispatchAction (Allocation a, ExecAgentAsset executor) {
    Task theTask = a.getTask();
    Verb taskVerb = theTask.getVerb();
    Asset dirObj = theTask.getDirectObject();
    WorkletJunction taskJunction = null;
    TaskReturnJunction homeJunction = new TaskReturnJunction (comAddress, WVMName, comPort);
    homeJunction.setAllocID (a.getUID());
    homeJunction.setExecID (executor.getUID());
    homeJunction.setClusterID (getCluster().getClusterIdentifier().toString());
    homeJunction.setPluginID (getSubscriptionClientName());
    Worklet taskWkl = new Worklet(homeJunction);
    Class juncClass = null;
    String juncClassName = executor.getExecutorPG().getJunction();
    try {
	juncClass = Class.forName(juncClassName);
    } catch (ClassNotFoundException e) {
	try {
		juncClass = remoteClassDownload (executor, juncClassName);
	} catch (ClassNotFoundException e1) {
		failTask (theTask);
		System.out.println (e1);
		e1.printStackTrace();
	}
      }
    int i;
    Constructor theConstructor = null;
    try {
	Class[] constructorParams = {Class.forName("java.lang.String"), Class.forName("java.lang.String"), int.class};
	theConstructor = juncClass.getConstructor (constructorParams);
    } catch (NoSuchMethodException e1) {
	failTask (theTask);
	e1.printStackTrace();
    }
    catch (ClassNotFoundException e2) {
	failTask (theTask);
	e2.printStackTrace();
    }
    //target host setting
    WVMPG targetWVM = allocJunction.getTargetWVM(a);

    Object[] params= {targetWVM.getAddress(), targetWVM.getId(), new Integer(targetWVM.getSocketPort())};
    try {
	taskJunction = (WorkletJunction) theConstructor.newInstance(params);
    } catch (Exception e) { // could not instantiate the junction
	System.err.println ("Task operation cannot be accomplished - failing task " + theTask);
	failTask (theTask);
	e.printStackTrace();
    }
    System.out.println ("***** " + taskJunction.getClass().getName() + " ******");
    if (taskJunction  instanceof TaskJunction) {
	((TaskJunction)taskJunction).setRetHandle(homeJunction);
	((TaskJunction)taskJunction).setTaskVerb(theTask.getVerb().toString());
        // task data loading now
        allocJunction.loadTaskData((TaskJunction)taskJunction, a);

	((TaskJunction)taskJunction).setMessage(
		"executing action for WF task: " + theTask.getVerb().toString());
	taskWkl.addJunction (taskJunction);
	//notifyAllocation(dirObj);
	taskWkl.deployWorklet(myWVM);
    }
    else
	// junction is not a TaskJunction
	failTask(theTask);
  }

}

