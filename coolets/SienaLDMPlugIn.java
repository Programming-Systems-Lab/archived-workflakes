/*
 * <copyright>
 * Copyright (c) 2001: The Trustees of Columbia University in the
 *  City of New York, Peppo Valetto. All Rights Reserved.
 * </copyright>
 */

package psl.workflakes.coolets;

import java.util.Hashtable;
import java.util.Vector;
import java.util.Enumeration;
import java.lang.reflect.Method;
import siena.*;
import org.cougaar.core.society.Communications;
import org.cougaar.core.plugin.*;
import org.cougaar.core.cluster.ClusterServesPlugIn;
import org.cougaar.domain.planning.ldm.asset.*;
import org.cougaar.domain.planning.ldm.plan.Verb;
import org.cougaar.domain.planning.ldm.plan.Task;
import org.cougaar.domain.planning.ldm.plan.NewTask;

import psl.worklets.*;
import psl.workflakes.coolets.adaptors.*;

/**
 *
 * <p>Title: SienaLDMPlugIn</p>
 * <p>Description: Utility Cougaar PlugIn that handles incoming <code>Siena</code> events and
 * maps them to LDM assets onto the Cougaar blackboard.
 * Uses a callback mechanism to define Siena subscriptions via <code>CooletIncomingJunction</code> specializations of
 * class {@link SienaLDMJunction SienaLDMJunction} and handle them with handler code provided by those junctions.</p>
 * @see siena the Siena package
 * @see org.cougaar.domain.planning.ldm the Cougaar ldm package
 * @see WorkletPlugIn
 * @see CooletIncomingJunction
 * @see SienaLDMJunction
 * <p>Copyright: Copyright (c) 2002: The Trustees of Columbia University in the
 *  City of New York, Peppo Valetto. All Rights Reserved.</p>
 * @author Peppo Valetto
 * @version 1.0
 */
public class SienaLDMPlugIn
	extends WorkletPlugIn
	implements SienaLDMAdaptorInf {

        /**
         * URL of the Siena bus used by this PlugIn
         */
	protected String senpURL = null;
        /**
         * Handle to the Siena bus used by this PlugIn
         */
	protected HierarchicalDispatcher hd;
	/**
         * <code>Hashtable</code> maintaining Siena <code>Filters</code>
         * associated to corresponding callback codes and data structures.
         *
         */
        protected Hashtable filterTable;

        /**
         * Initialization code for this PlugIn
         */
	protected void setupSubscriptions() {
		super.setupSubscriptions();

		//initialization code
		filterTable = new Hashtable();
		senpURL = getSienaURL();
		hd = setupDispatcher(senpURL);

		if (hd != null) {
			ClusterServesPlugIn cluster = getCluster();
			if (cluster instanceof WorkletClusterImpl) {
				((WorkletClusterImpl)cluster).setSienaGateway (this);
			}
		}

		//now set up any fixed Subscriptions needed by the application

	}

        /**
         * Skeleton method that represents a placeholder for any necessary override by
         * specializations of <code>SienaLDMPlugIn</code>
         * of the standard <code>execute()</code> method of {@link WorkletPlugIn WorkletPlugIn}
         */
	protected void execute() {
		super.execute();
	}

	/**
         * Accessor method to get a handle to the Siena bus used by this PlugIn
         * must be overridden in case in an application the plugin
         * wants to hook up to a non-default siena dispatcher
         * @return URL of the Siena bus to be used.
         */
        protected String getSienaURL() { return senpURL; }

        /**
         * Creates and/or attaches a Siena <code>HierarchicalDispatcher</code> to a master dispatcher.
         * @see siena.HierarchicalDispatcher
         * @param master the master dispatcher (may be null, in which case a master is created locally)
         * @return handle to the <code>HierarchicalDispatcher</code> to be used by this PlugIn to communicate onto Siena.
         */
	private HierarchicalDispatcher setupDispatcher (String master) {
		if (master == null) {
			master = "senp://localhost:7130";
			senpURL = master;
		}
		try {
			hd = new HierarchicalDispatcher();
    	 	hd.setMaster(master);
	  		System.out.println (getClass().getName() + " running attached to Master on " + master);
	  	} catch (InvalidSenderException se) {
      		System.err.println("Could not connect to Siena\n InvalidSenderException\n"+ se);
      		hd = null;
    	} catch (java.io.IOException ie) {
      		System.err.println("Could not connect to Siena\n Unable to set receiver\n" + ie);
      		hd = null;
    	}
	  	return hd;
	}

	// implementation of SienaLDMAdaptorInf

        /**
         * creates a Siena subscription for this PlugIn and a handler for it
         * @param wj instance of Junction that will handle incoming Siena events matching the subscription
         * @param m Method in <code>wj</code> that wil be used as a callback
         * @param f Siena <code>Filter</code> representing the Siena subscription
         */
	public void setupSienaSub (WorkletJunction wj, Method m, Filter f) {
		WorkflakesNotifiable eventHandler;

		if (( eventHandler = (WorkflakesNotifiable)filterTable.get(wj) ) == null) {
			eventHandler = new WorkflakesNotifiable();
			filterTable.put (wj, eventHandler);
			eventHandler.setJunction(wj);
			eventHandler.setHandlerMethod(m);
		}
		eventHandler.addFilter(f);
		System.out.println ("Setting handler : " + eventHandler.toString() +
							"for filter: " + f.toString());
	}

        /**
         * Utility method to publish a Siena event on the bus
         * @param n the event to be published
         */
	public void publishEvent (Notification n) {
		try{
			hd.publish (n);
		} catch ( siena.SienaException se ) {
    		System.err.println("Could not send Siena event" + se);
		}
	}
	/**
         * Utility method to insert a Cougaar <code>Asset</code> onto the Cougaar blackboard.
         * @param someAsset the <code>Asset</code> to be put on the blackboard.
         */
	public void insertAsset (Asset someAsset) {
		boolean inTrans = false;
		try {
			if (getSubscriber().isMyTransaction() == false) {
      			openTransaction();
      			inTrans = true;
      		}
      		getSubscriber().publishAdd(someAsset);
      	} catch (Exception e) {
      		synchronized (System.err) {
      			System.err.println ("Couldn't add Asset " + someAsset.toString());
        		System.err.println("Caught "+e);
        		e.printStackTrace();
        	}
      	}
   		finally {
   			if (inTrans == true) {
		      	closeTransaction();
		      	inTrans = false;
		    }
    	}
	}

        /**
         * Utility method to modify a Cougaar <code>Asset</code> existing onto the Cougaar blackboard.
         * @param someAsset the <code>Asset</code> to be changed
         */
	public void changeAsset (Asset someAsset) {
		boolean inTrans = false;
		try {
			if (getSubscriber().isMyTransaction() == false) {
      			openTransaction();
      			inTrans = true;
      		}
      		getSubscriber().publishChange(someAsset);
      	} catch (Exception e) {
      		synchronized (System.err) {
      			System.err.println ("Couldn't change Asset " + someAsset.toString());
        		System.err.println("Caught "+e);
        		e.printStackTrace();
        	}
      	}
   		finally {
      		if (inTrans == true) {
      			closeTransaction();
      			inTrans = false;
      		}
    	}
	}

        /**
         * Utility method to insert a new workflow task onto the Cougaar blackboard.
         * @param verb the Cougaar <code>Verb</code> defining the workflow task
         * @param dirObj the Cougaar <code>Asset</code> representing the direct object for the task
         */
	public void insertTask (String verb, Asset dirObj) {
		boolean inTrans = false;
		System.out.println("In InsertTask()");

		try {
			if (getSubscriber().isMyTransaction() == false) {
	      		openTransaction();
	      		inTrans = true;
	      	}
	      	NewTask someTask = makeNewTask(verb);
	      	someTask.setDirectObject(dirObj);
	      	getSubscriber().publishAdd(someTask);
	    } catch (Exception e) {
      		synchronized (System.err) {
      			System.err.println ("Couldn't add Task " + verb);
        		System.err.println("Caught "+e);
        		e.printStackTrace();
        	}
      	}
   		finally {
   			if (inTrans == true) {
      			closeTransaction();
      			inTrans = false;
      		}
    	}
    	System.out.println("Out of InsertTask()");
	}

        /**
         * Utility method to insert a new workflow task onto the Cougaar blackboard.
         * @param someTask a Cougaar <code>NewTask</code> data structure definignt the new task.
         */
	public void insertTask (NewTask someTask) {
		boolean inTrans = false;
		try {
			if (getSubscriber().isMyTransaction() == false) {
	      		openTransaction();
	      		inTrans = true;
	      	}

			getSubscriber().publishAdd(someTask);
      	} catch (Exception e) {
      		synchronized (System.err) {
      			System.err.println ("Couldn't add Task " + someTask.toString());
        		System.err.println("Caught "+e);
        		e.printStackTrace();
        	}
      	}
   		finally {
   			if (inTrans == true) {
      			closeTransaction();
      			inTrans = false;
      		}
    	}
	}

	private NewTask makeNewTask(String verbName) {
		NewTask nt = theLDMF.newTask();
		nt.setVerb(Verb.getVerb(verbName));
		nt.setPlan(theLDMF.getRealityPlan());
		return nt;
	}

        /**
         * Utility method to create a new Cougaar <code>Asset</code>
         * @param proto prototype defining the structure of the <code>Asset</code>
         * @return an Asset instance
         */
	public Asset newAsset(String proto) { return theLDMF.createInstance(proto); }

        /**
         *
         * <p>Title: WorkflakesNotifiable</p>
         * <p>Description: Implementation of the Siena <code>Notifiable</code> interface, which
         * is used to implement callback-style event handlers for the {@link SienaLDMPlugIn SienaLDMPlugIn}.
         * @see siena.Notifiable
         * </p>
         * <p>Copyright: Copyright (c) 2002: The Trustees of Columbia University in the
         *  City of New York, Peppo Valetto. All Rights Reserved.</p>
         * @author Peppo Valetto
         * @version 1.0
         */
	class WorkflakesNotifiable
		implements Notifiable {
		private Vector myFilters;
		private WorkletJunction myWJ;
		private Method myHandler;


		WorkflakesNotifiable () {
			myFilters = new Vector();
			myWJ = null;
			myHandler = null;
		}

                /**
                 * Instantiates a <code>WorkflakesNotifiable</code> for a given Siena <code>Filter</code>.
                 * @param f the Siena <code>Filter</code>
                 */
		WorkflakesNotifiable(Filter f) {
			this();
			addFilter(f);
		}

                /**
                 * adds a Siena <code>Filter</code> to which this Notifiable responds.
                 * @param f the Siena <code>Filter</code>
                 */
		void addFilter (Filter f) {
			try {
				// subscribe
				hd.subscribe (f, this);
				myFilters.add(f);
			} catch (SienaException sie) {
				System.err.println ("Could not satisfy subscription to " + f);
				sie.printStackTrace();
				return;
			}
		}

                /**
                 * sets the junction that works as a callback handler for events this Notifiable subscribes to.
                 * @param wj the <code>WorkletJunction</code> containing the callback code.
                 */
		void setJunction (WorkletJunction wj) {
			myWJ = wj;
		}

                /**
                 * specifies the callback <code>Method</code> to be executed in response to a recieved Siena event.
                 * @see #setJunction
                 * @param m the <code>Method</code> object rpresenting the callback code
                 */
		public void setHandlerMethod(Method m) {
			// should I check on the signature of the method m
			// to see if it is consistent with void <whateverMethod>(Notification)
			//?
			myHandler = m;
		}

		// implementation of Notifiable

                /**
                 * implementation of the <code>notify()</code> method of the <code>Notifiable</code> interface.
                 * @see siena.Notifiable
                 * @param n incoming siena event to be handled
                 */
		public void notify (Notification n) {
			if (myWJ == null || myHandler == null)
				return; // ignore event n since there is no handling routine set
			try {
				System.out.println ("Handling notification for " +
									myWJ + " ... Executing method " +
									myWJ.getClass().getName() + ":" + myHandler.toString());
				myHandler.invoke(myWJ, new Object[] {n});
			} catch (Exception e) {
				System.err.println ("Cannot execute method: " + myWJ.getClass() + "::" + myHandler.getName());
				e.printStackTrace();
			}
			System.out.println ("FINISHED Handling of notification");
		}

		/**
                 * Dummy method, for compliance with the Siena <code>Notifiable</code> interface.
                 * @see siena.Notifiable
                 */
		public void notify(Notification []n) {}

		public String toString() {
			String desc = ("Junction: " + myWJ.getClass().getName() + "\n" +
					"Handler: " + myHandler.toString() + "\n");
					desc = desc + "Filters: ";
			for (Enumeration e = myFilters.elements(); e.hasMoreElements(); )
				desc = desc + "\n\t" + ((Filter)e.nextElement()).toString();
			desc = desc + "\n";
			return desc;
		}

	}
}
