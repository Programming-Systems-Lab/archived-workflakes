/*
 * <copyright>
 * Copyright (c) 2001: The Trustees of Columbia University in the
 *  City of New York, Peppo Valetto. All Rights Reserved.
 * </copyright>
 */
 package psl.workflakes.exercise.tutorial;

import java.util.Enumeration;
import java.util.Vector;
import org.cougaar.domain.planning.ldm.plan.Task;
import org.cougaar.domain.planning.ldm.plan.Preference;
import org.cougaar.domain.planning.ldm.plan.AspectType;
import org.cougaar.domain.planning.ldm.plan.AspectValue;
import org.cougaar.domain.planning.ldm.plan.ScoringFunction;
import org.cougaar.core.cluster.Subscription;
import org.cougaar.core.cluster.IncrementalSubscription;
import psl.worklets.WVM;
import psl.workflakes.coolets.*;
import psl.workflakes.exercise.tutorial.assets.ProgrammerAsset;

public class DevAllocJunction
	extends CooletIncomingJunction {

	public DevAllocJunction (String host, String name, int port) {
		super (host, name, port);
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
		while (enum.hasMoreElements()) {
			Task t = (Task)enum.nextElement();
			if (t.getPlanElement() != null)
				continue;
				
			allocateTask (t);
			if (t.getPlanElement() == null) {
				System.out.println ("for now no programmer can be assigned to task " + t);	
			}
		}
	}
	
	
  private int allocateTask(Task task)
  {
  	int after = getStartMonth(task);
  	int end = after;
	// select an available programmer at random
    Vector programmers = new Vector(((DevAllocAdaptorInf)myPlugInTarget).getProgrammers());
    boolean allocated = false;
    while ((!allocated) && (programmers.size() > 0)) 
    {
    	int stuckee = (int)Math.floor(Math.random() * programmers.size());
      	ProgrammerAsset asset = (ProgrammerAsset)programmers.elementAt(stuckee);
      	programmers.remove(asset);
		
		Schedule sched = asset.getSchedule();
			
      	// Check the programmer's schedule
		Preference durationPref = task.getPreference(AspectType.DURATION);
      	int duration = (int) durationPref.getScoringFunction().getBest().getValue();
     	int earliest_start = findEarliest(sched, after, duration);

		end = earliest_start + duration;
		System.out.println ("\tproposed end at: " + end);
		AspectValue endValue = new AspectValue(AspectType.END_TIME, end);
		double no_good = task.getPreference(AspectType.START_TIME).getScoringFunction().WORST;
		double still_good = task.getPreference(AspectType.START_TIME).getScoringFunction().OK;
		System.out.println ("\tWorst value is: "+ no_good);
		System.out.println ("\tOK  value is: "+ still_good);
		double endScore = task.getPreference(AspectType.START_TIME).getScoringFunction().getScore(endValue);
		System.out.println ("\tscore for month "+ end + " is: " + endScore);
		boolean onTime = (endScore <= still_good);
		if (!onTime)
			break;
			
		System.out.println("\nAllocating the following task to "
	   		+asset.getTypeIdentificationPG().getTypeIdentification()+": "
   			 +asset.getItemIdentificationPG().getItemIdentification());
     		 System.out.println("Task: "+task);
     		 
	    // Add the task to the programmer's schedule
   		for (int i=earliest_start; i<end; i++) {
        	sched.setWork(i, task);
      	}
		
		((DevAllocAdaptorInf)myPlugInTarget).allocateAsset (task, asset);
		
		// Create an estimate that reports that we did just what we
      	// were asked to do
		String tmpstr =  " start_month: "+earliest_start;
		tmpstr +=  " duration: "+duration;
		tmpstr +=  " end_month: "+end;
		tmpstr +=  " onTime: "+onTime;
		System.out.println(tmpstr);
    	
    	allocated = true;
   	}
    return end;
  }


	  /**
   * Extract the earliest start month from a task
   */
  private int getStartMonth(Task t) {
      Preference pref = t.getPreference(AspectType.START_TIME);
      ScoringFunction sf = pref.getScoringFunction();
      return (int) sf.getBest().getValue();
      }

  /**
   * find the earliest available time in the schedule.
   * @param sched the programmer's schedule
   * @param earliest the earliest month to look for
   * @param duration the number of months we want to schedule
   */
  private int findEarliest(Schedule sched, int earliest, int duration) {
    boolean found = false;
    int month = earliest;
    while (!found) {
      found = true;
      for (int i=month; i<month+duration; i++) {
        if (sched.getWork(i) != null) {
          found = false;
          month = i+1;
          break;
        }
      }
    }
    return month;
  }

	
		
}