package psl.workflakes.coolets;

import java.util.Vector;
import java.util.Arrays;
import org.cougaar.domain.planning.ldm.asset.PropertyGroup;

import psl.worklets.*;


/**
 * <p>Title: TaskJunction</p>
 * <p>Description: Base class for Workflow Executor Junctions that are loaded onto worklets by
 * the Workflakes engine and shipped onto the targt system</p>
 * <p>Copyright: Copyright (c) 2002: The Trustees of Columbia University in the
 *  City of New York, Peppo Valetto. All Rights Reserved.</p>
 * @author Peppo Valetto
 * @version 1.0
 */

public class TaskJunction
  extends WorkletJunction {
    protected TaskReturnJunction retHandle;
	protected String taskVerb;
	protected String messageDelivered;
	protected Vector taskData; // Vector of PropertyGroups

        /**
         *
         * @return returns the <code>Verb</code> of the assigned Cougaar <code>Task</code>
         */
	public  String getTaskVerb() { return taskVerb; }
        /**
         * Sets the <code>Verb</code> of the assigned Cougaar <code>Task</code>
         * @param verb the Verb name
         */
	public void setTaskVerb(String verb) { taskVerb = new String(verb); }
        /**
         * Sets the handle to the corresponsing {@link TaskReturnJunction TaskReturnJunction}
         * (if any)
         * @param j an instance of class <code>TaskReturnJunction</code>
         */
	public void setRetHandle (TaskReturnJunction j) { retHandle = j; }

        /**
         * Typical junction Constructor
         * @param host target hostname
         * @param name RMI name of the target <code>WVM</code>
         * @param rPort target RMI port
         * @param sPort target socket port of the target <code>WVM</code>
         */
	public TaskJunction (String host, String name, int rPort, int sPort) {
		super (host, name, rPort, sPort, false, new String("default"), null);
		retHandle = null;
		taskData = new Vector();
		messageDelivered = null;

		System.out.println ("\t\tTaskJunction instantiated ...");
	}

        /**
         * Typical junction Constructor
         * @param host target hostname
         * @param name target RMI name of the target <code>WVM</code>
         */
/*
	public TaskJunction (String host, String name) {
		super (host, name);
		retHandle = null;
		taskData = new Vector();
		messageDelivered = null;
		System.out.println ("\t\tTaskJunction instantiated ...");
	}
*/
        /**
         * typical junction initialization just does default
         * @param system
         * @param wvm
         */
	public void init(Object system, WVM wvm) {
		//super.init(system, wvm);
		System.out.println ("\t\tTaskJunction initialized ...");
	}

        /**
         * typical junction initialization
         * default beahvior also sets flag for return junction
         */
	protected void execute() {
		System.out.println ("\t\tTaskJunction has gotten to target");
		System.out.println ("\t\tTaskJunction operating for Task: " + getTaskVerb() + " ...");
		retHandle.setSuccess(true);
	}

        /**
         * Adds a Cougaar <code>PropertyGroup</code> to the sets of data
         * travelling with the junction
         * @param pg the PropertyGroup to be added
         */
	public void setTaskData(PropertyGroup pg) { taskData.add(pg); }
        /**
         * Adds an array of Cougaar <code>PropertyGroup</code> to the sets of data
         * travelling with the junction
         * @param pg the array of PropertyGroup to be added
         */
	public void setTaskData(PropertyGroup[] pg) {
		System.out.println ("Adding " + pg.length + " Property Groups to the Task Data");
		taskData.addAll(Arrays.asList(pg));
	}
        /**
         * returns the whole set of data travelling with the junction
         * @return a Vector containing all of the <code>PropertyGroups</code>
         */
	public Vector getTaskData() { return taskData; }
        /**
         * sets a message to be delivered when junction gets executed on target
         * @param msg the message to be set
         */
	public void setMessage(String msg) { messageDelivered = new String(msg); }
        /**
         * retrieves the message to be delivered when junction gets executed on target
         * @return the message to be output
         */
	public String getMessage() {return messageDelivered; }

        /**
         * sets the value of the success flag on the associated return junction
         * @param flag the value of the success flag
         */
	protected void setSuccess(boolean flag) {retHandle.setSuccess(flag);}
}