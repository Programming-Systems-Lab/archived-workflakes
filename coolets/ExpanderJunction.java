/*
 * <copyright>
 * Copyright (c) 2001: The Trustees of Columbia University in the
 *  City of New York, Peppo Valetto. All Rights Reserved.
 * </copyright>
 */
package psl.workflakes.coolets;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;
import java.util.Collection;
import org.cougaar.core.cluster.IncrementalSubscription;
import org.cougaar.domain.planning.ldm.plan.*;
import org.cougaar.domain.planning.ldm.asset.*;
import org.cougaar.util.UnaryPredicate;
import org.cougaar.core.plugin.PlugInAdapter;
import psl.worklets.WVM;

import psl.workflakes.coolets.adaptors.ExpanderAdaptorInf;


/**
 *
 * <p>Title:ExpanderJunction </p>
 * <p>Description: Base Class for Workflow Definition Junctions that are to interact
 * with {@link WorkletPlugIn WorkletPlugIns}
 * that implement {@link psl.workflakes.coolets.adaptors.ExpanderAdaptorInf ExpanderAdaptorInf}</p>
 * Subclasses of this class should define subscriptions matching <code>Tasks</code>
 * that need to be expanded and provide the expansion logic by implementing the
 * <bf>abstract</bf> method <code>plan()</code>
 * <p>Copyright: Copyright (c) 2002: The Trustees of Columbia University in the
 *  City of New York, Peppo Valetto. All Rights Reserved.</p>
 * @author Peppo Valetto
 * @version 1.0
 */
public abstract class ExpanderJunction
	extends CooletIncomingJunction {

	public ExpanderJunction (String host, String name, int port) {
		super (host, name, port);
  	}

  	public void init (Object system, WVM wvm) {
		super.init(system, wvm);
		((ExpanderAdaptorInf)myPlugInTarget).setPlannerJunction(this);
		System.out.println ("trigger predicate class: " + myTriggerPred);
		System.out.println ("target plugin class: " + myPlugInTarget.getClass().getName());
		// initiate WF
	}

	public void execute() {
		super.execute();
	}

        /**
         * For each Task that needs expansion, <code>plan()</code> id called
         * and then the corresponding Cougaar <code>Expansion</code> is created and posted
         * @param sub subscription to be handled
         */
	public void embeddedExec (IncrementalSubscription sub) {
		super.embeddedExec(sub);
		//Enumeration enum = sub.getAddedList();
		Enumeration enum = sub.elements();
		while (enum.hasMoreElements()) {
			Task task = (Task) enum.nextElement();
			if (task.getPlanElement() == null) {
				// Create expansion and workflow to represent the expansion
		  		// of this task
	  			NewWorkflow new_wf = ((ExpanderAdaptorInf)myPlugInTarget).newWorkflow();
	  			new_wf.setParentTask(task);

	  			plan(new_wf);

	  			AllocationResult estAR = null;
	  			Expansion new_exp =
					((ExpanderAdaptorInf)myPlugInTarget).createExpansion(task.getPlan(), task, new_wf, estAR);
	  			//publishAdd(new_wf);
	  			((ExpanderAdaptorInf)myPlugInTarget).addToPlan(new_wf);
	  			//publishAdd(new_exp);
	  			((ExpanderAdaptorInf)myPlugInTarget).addToPlan(new_exp);
	  		}
		}
	}

	/**
         * abstract method to be overriden with situational logic to plan an expansion
         * @param new_wf an instance of Cougaar <code>Workflow</code> that must be set up
         */
	public abstract void plan(NewWorkflow new_wf);

  /**
   * Create a task.
   * @param verb The string for the verb for the task.
   * @param parent_task The task being expanded
   * @param wf the workflow being filled out
   * @return A new sub-task member of the workflow
   */
  protected NewTask makeTask(String verb, Task parent_task, Workflow wf) {
	NewTask new_task = ((ExpanderAdaptorInf)myPlugInTarget).newTask();

	new_task.setParentTask(parent_task);
	new_task.setWorkflow(wf);

	// Set the verb as given
	new_task.setVerb(Verb.getVerb(verb));

	// Copy important fields from the parent task
	new_task.setPlan(parent_task.getPlan());
	new_task.setDirectObject(parent_task.getDirectObject());
	new_task.setPrepositionalPhrases(parent_task.getPrepositionalPhrases());

	return new_task;
  }

  /**
   * Sets a preferred start time for a given task
   * @param new_task the Cougaar <code>Task</code> subject to the preferences
   * @param start start time
   */
  protected void setPreferences(NewTask new_task, double start) {
	// Establish preferences for task
	Vector preferences = new Vector();

	// Add a start_time preference
	ScoringFunction scorefcn = ScoringFunction.createNearOrBelow //ASAP AFTER start moment
	  (new AspectValue(AspectType.START_TIME, start), 0.05);
	Preference pref =
	  ((ExpanderAdaptorInf)myPlugInTarget).newPreference(AspectType.START_TIME, scorefcn);

	new_task.setPreferences(preferences.elements());
  }

}
