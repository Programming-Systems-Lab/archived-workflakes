/*
 * <copyright>
 * Copyright (c) 2001: The Trustees of Columbia University in the
 *  City of New York, Peppo Valetto. All Rights Reserved.
 * </copyright>
 */
 package psl.workflakes.coolets;

import org.cougaar.core.society.UID;
import org.cougaar.util.UnaryPredicate;
import org.cougaar.core.plugin.PlugInAdapter;
import org.cougaar.domain.planning.ldm.asset.Asset;
import org.cougaar.domain.planning.ldm.plan.*;
import psl.worklets.*;

import psl.workflakes.coolets.assets.*;
import psl.workflakes.coolets.adaptors.TaskReturnInf;


/**
 *
 * <p>Description:  Workflow Execution Junction that is used to report back results to Workflakes.
 * more precisely to a PlugIn that implements
 * {@link psl.workflakes.coolets.adaptors.TaskReturnInf TaskReturnInf}</p>
 * <p>Copyright: Copyright (c) 2002: The Trustees of Colum</p>
 * <p>Copyright: Copyright (c) 2002: The Trustees of Columbia University in the
 *  City of New York, Peppo Valetto. All Rights Reserved.</p>
 * @author Peppo Valetto
 * @version 1.0
 */
public class TaskReturnJunction
	extends CooletIncomingJunction
{
	private	UID allocID;
	private UID execID;
	private TaskReturnInf originPlugIn;
	boolean success;


	public UID getAllocID() { return allocID; }
	public UID getExecID() { return execID; }
	public boolean getSuccess() { return success; }
	public void setAllocID(UID s) { allocID = s; }
	public void setExecID(UID s) { execID = s; }
	public void setSuccess(boolean flag) { success = flag; }

	public TaskReturnJunction (String host, String name, int port) {
		super (host, name, port);
		allocID = null;
		execID = null;
		originPlugIn = null;
		success = false;
		System.out.println (getClass().getName());
	}

	public void init(Object system, WVM wvm) {
		super.init(system, wvm);
		PlugInAdapter thePlugIn = myTarget.findPlugIn(clusterID, pluginID);
		originPlugIn = (TaskReturnInf) thePlugIn;
	}

	public void execute() {

		UnaryPredicate allocPredicate = new UnaryPredicate() {
			public boolean execute(Object o) {
				if (o instanceof Allocation) {
					Allocation a = (Allocation) o;
					if (a.getUID().equals(allocID))
						return true;
				}
				return false;
			}
	  	};

		UnaryPredicate execPredicate = new UnaryPredicate() {
			public boolean execute(Object o) {
				if (o instanceof Asset) {
					Asset asset = (Asset) o;
					if (asset.getUID().equals(execID))
						return true;
				}
				return false;
			}
	  	};

	  	System.out.println ("\t\tAction for allocation " + allocID + " finished");
		Allocation alloc = (Allocation) originPlugIn.findByUID (allocPredicate);
		System.out.println (alloc.toString());
		ExecAgentAsset exec = (ExecAgentAsset)originPlugIn.findByUID (execPredicate);
		System.out.println (exec.toString());
		Task t = alloc.getTask();

		if (success == false) {
			System.err.println ("Action has failed");
			originPlugIn.failTask(t);
		}
		else {
			System.err.println ("Action has succeeded");
			originPlugIn.postAction (alloc, exec);
		}
	}
}
