/*
 * <copyright>
 * Copyright (c) 2001: The Trustees of Columbia University in the
 *  City of New York, Peppo Valetto. All Rights Reserved.
 * </copyright>
 */

 package psl.workflakes.coolets.adaptors;

import org.cougaar.util.UnaryPredicate;
import org.cougaar.domain.planning.ldm.plan.*;
import org.cougaar.domain.planning.ldm.asset.*;


import psl.workflakes.coolets.ExpanderJunction;

/**
 *
 * <p>Title: ExpanderAdaptorInf</p>
 * <p>Description: Default interface for an Expander <code>WorkletPlugIn</code></p>
 * <p>Copyright: Copyright (c) 2002: The Trustees of Columbia University in the
 *  City of New York, Peppo Valetto. All Rights Reserved.</p>
 * @author Peppo Valetto
 * @version 1.0
 */
public interface ExpanderAdaptorInf
extends WklInstallInf {
        /**
         * sets the {@link psl.workflakes.coolets.CooletIncomingJunction CooletIncomingJunction} of type
         * <code>ExpanderJunction</code> that handles expansion logic
         * @param gwj handle to the ExpanderJunction
         */
  	public void setPlannerJunction(ExpanderJunction gwj);
        /**
         * Convenience method to cerate a new Cougaar <code>Workflow<</code>
         * @return the newly created Workflow
         */
	public NewWorkflow newWorkflow();
        /**
         * Convenience method to create a new Cougaar <code>Expansion</code>
         * @param plan Cougaar <code>Plan</code> the Expansion must be associated to
         * @param task expanded task
         * @param new_wf Cougaar <code>Workflow</code> containing this Expansion
         * @param estAR Estimated <code>AllocationResult</code> data structure
         * for this expansion
         * @return the newly created Expansion
         */
	public Expansion createExpansion(Plan plan, Task task, NewWorkflow new_wf, AllocationResult estAR);
	/**
         * Convenience method to create a new Cougaar <code>Constraint</code>
         * @return the newly created <code>Constraint</code>
         */
        public NewConstraint newConstraint();
        /**
         * Convenience method to create a new Cougaar <code>Task</code>
         * @return the newly created <code>Task</code>
         */
	public NewTask newTask();
        /**
         * Convenience method to create a new Cougaar <code>Preference</code>
         * @param aspect the kind of <code>Aspect</code> impacted by this Preference
         * @param sfn scorign funciotn for evaluating the preference
         * @return the newly created <code>Preference</code>
         */

	public Preference newPreference(int aspect, ScoringFunction sfn);
        /**
         * Adds an object to the current <code>Plan</code>
         * @param o to be added
         */
	public void addToPlan (Object o);
        /**
         * Removes an object from the <code>Plan</code>
         * @param o to be removed
         */
	public void removeFromPlan (Object o);
        /**
         * Convenience method to read the end time for a <code>Task</code>
         * @param t the Task in object
         * @return the end time
         */
	public double getEndTime(Task t);
        /**
         * Convenience method to read the start time for a given <code>Task</code>
         * @param t the Task in object
         * @return the start time
         */
	public double getStartTime(Task t);
        /**
         * Reads the preference for a given aspect from a <code>task</code>
         * @param task the Task in object
         * @param aspect_type the type of the aspect of interest
         * @return the Preference of interest
         */
	public Preference getPreference(Task task, int aspect_type);
        /**
         * sets a root task for a workflow hierarchy
         * @param taskName the root task
         */
	public void setInitialWFTask(String taskName);
}