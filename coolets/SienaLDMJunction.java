/*
 * <copyright>
 * Copyright (c) 2001: The Trustees of Columbia University in the
 *  City of New York, Peppo Valetto. All Rights Reserved.
 * </copyright>
 */
 package psl.workflakes.coolets;

import java.util.Vector;
import java.util.Enumeration;
import java.lang.reflect.Method;

import siena.Filter;
import siena.Notification;

import psl.worklets.*;
import psl.workflakes.coolets.adaptors.SienaLDMAdaptorInf;

/**
 *
 * <p>Description: Abstract class for {@link CooletIncomingJunction CooletIncomingJunctions}
 * that define Siena subscriptions to be handled by {@link SienaLDMPlugIn SienaLDMPlugIn}.
 * Subclasses must provide implementations for the following methods:
 * <code>makeFilters()</code>
 * <code>handleEvent()</code>
 * and are likely to need to override the following methods:
 * <code>findMethod()</code>
 * <code>setFilter()</code>
 * @see SienaLDMPlugIn
 * @see CooletIncomingJunction
 * @see siena the Siena package
 * </p>
 * <p>Copyright: Copyright (c) 2002: The Trustees of Columbia University in the
 *  City of New York, Peppo Valetto. All Rights Reserved.</p>
 * @author Peppo Valetto
 * @version 1.0
 */
public abstract class SienaLDMJunction
	extends CooletIncomingJunction {

        /**
         * Set of Siena <code>Filters</code> defining Siena subscriptions of itnerests to this junction.
         */
	protected Vector filters;

        /**
         * Ovverrides constuctor of {@link CooletIncomingJunction CooletIncomingJunction} to instantiate a <code>SienaLDMJunction</code>.
         * @see psl.worklets.WorkletJunction for the meaning of the parameters
         * @param host hostname of the WVM of junction target
         * @param name RMI name of the WVM of junction target
         * @param port RMI port of the WVM of junction target
         */
	public SienaLDMJunction (String host, String name, int port) {
		super (host, name, port);
		filters = new Vector();
	}

        /**
         * executed when this junction lands onto the target WVM
         * @param system the adaptor of the target
         * @param wvm the target WVM
         */
	public void init (Object system, WVM wvm) {
		super.init(system, wvm);
		filters.clear(); //reset filters at each junction stop
		makeFilters();
		for (Enumeration e = filters.elements(); e.hasMoreElements(); ) {
			Filter f = (Filter) e.nextElement();
			setFilter(f, findMethod(f));
		}
	}

        /**
         * Override to select handler <code>Method</code> for a given <code>Filter</code>.
         * This default implementation refers to method {@lik #handleEvent() handleEvent()}
         * @param f the Siena <code>Filter</code> of interest
         * @return a <code>Method</code> defining the callback code handling the subscriptin of the <code>Filter f</code>
         */
	//override to select handler Methods for filters
	protected Method findMethod (Filter f) {
		//default implementation refers to method handleEvent()
		Method m = null;
		Notification n = new Notification(); //only to facilitate the getClass() below
		try {
			m = this.getClass().getDeclaredMethod ("handleEvent", new Class[]{n.getClass()});
		} catch (Exception e) {
			// provide customized exception handling here
			System.err.println ("No handler found for Filter " + f.toString());
			e.printStackTrace();
			m = null;
		}
		return m;

	}
        /**
         * Override to activate a Siena Subscription
         * @param f the Siena <code>Filter</code> defining the subscription.
         * @param m the handler <code>Method</code>.
         */
	protected void setFilter (Filter f, Method m) {
		((SienaLDMAdaptorInf)myPlugInTarget).setupSienaSub(this, m, f);
	}

        /**
         * Needs to be provided by subclasses in order to define the Filters of interest.
         */
	protected abstract void makeFilters();

        /**
         * Needs to be provided by subclasses in order to provide event handling application-dependent logic
         * for manipulating an incoming Siena event
         * @param n the incoming Siena event to be manipulated.
         */
	protected abstract void handleEvent (Notification n);

}