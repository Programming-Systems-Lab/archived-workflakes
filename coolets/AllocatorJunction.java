package psl.workflakes.coolets;

/**
 * <p>Title: AllocatorJunction</p>
 * <p>Description: Base Class for Workflow Definition Junctions that are to interact
 * with {@link WorkletPlugIn WorkletPlugIns}
 * that implement {@link psl.workflakes.coolets.adaptors.AllocatorAdaptorInf AllocatorAdaptorInf}</p>
 * Subclasses of this class should define subscriptions matching <code>Tasks</code>
 * that need to be allocated to some WEJ for execution on the target system.
 * They should also define the logic for such allocations implementing the
 * <code>findExecutor</code> method as well as the
 * <bf>abstract</bf> methods in this class.
 * <p>Copyright: Copyright (c) 2002: The Trustees of Columbia University in the
 *  City of New York, Peppo Valetto. All Rights Reserved.</p>
 * @author Peppo Valetto
 * @version 1.0
 */
import java.util.Enumeration;
import psl.worklets.WVM;
import org.cougaar.core.cluster.IncrementalSubscription;
import org.cougaar.domain.planning.ldm.plan.Task;
import org.cougaar.domain.planning.ldm.plan.Verb;
import org.cougaar.domain.planning.ldm.plan.Allocation;
import org.cougaar.domain.planning.ldm.plan.Preference;
import org.cougaar.domain.planning.ldm.plan.AspectType;
import psl.workflakes.coolets.assets.*;
import psl.workflakes.coolets.adaptors.AllocatorAdaptorInf;


public abstract class AllocatorJunction extends CooletIncomingJunction {

  public AllocatorJunction (String host, String name, int rPort, int sPort) {
    super (host, name, rPort, sPort);
  }

  public void init (Object system, WVM wvm) {
    super.init(system, wvm);
    ((AllocatorAdaptorInf)myPlugInTarget).setAllocJunction(this);
  }

  public void execute() {
    super.execute();
  }

  public void embeddedExec (IncrementalSubscription sub) {
    super.embeddedExec(sub);
    //Enumeration enum = sub.getAddedList();
    Enumeration enum = sub.elements();
    while (enum.hasMoreElements()) {
      Task task = (Task)enum.nextElement();
      if (task.getPlanElement() == null) {
        System.out.println ("\tExamining task  " + task.getVerb());
        ((AllocatorAdaptorInf)myPlugInTarget).allocateTask (task,startTime(task));
      }
    }
  }

   /**
   * Compute the start moment from a task
   * This base implmentation simply extracts the start time from the provided task;
   * override if needed.
   */
  protected double startTime(Task t) {
	  double ret = -1;
	  Preference start_pref = t.getPreference(AspectType.START_TIME);
	  if (start_pref != null)
		ret = (double)start_pref.getScoringFunction().getBest().getValue();
	  return ret;
  }

   /**
   * Find an available ExecAgentAsset (that is a WEJ) for this task.
   * This base method implements a very simple, naive way to match tasks to WEJ
   * Can be overridden at will by subclasses
   * @param v Verb of the Task
   * @return a matching WEJ
   */
  public ExecAgentAsset findExecutor (Verb v) {
    ExecAgentAsset asset = null;
    Enumeration e = ((AllocatorAdaptorInf)myPlugInTarget).getExecutors();
    while (e.hasMoreElements()) {
	asset = (ExecAgentAsset)e.nextElement();
	String caps = asset.getExecutorPG().getCapabilities();
	if (caps.equals("any")|| caps.equals(v.toString())) // change to something smarter
          return asset;
      	}

	return null;
  }

   /**
   * Returns info on the WVM resident with a target component.
   * Abstract: must be implemented with situational logic in subclasses
   * @param a provide info about the Task, the WEJ and the Target
   * @return a WVM <code>PropertyGroup</code> including the complete WVM profile
   */
  public abstract WVMPG getTargetWVM(Allocation a);

  /**
   * Loads a WEJ with enough data related to the task instance and data, in order to
   * carry out its computational task
   * Abstract: override with actual implmentation of situational logic in subclasses.
   * @param tj the WEJ
   * @param a the <code>Allocation</code> pertaining to this WEJ execution
   */
  public abstract void loadTaskData (TaskJunction tj, Allocation a);

}