/*
 * <copyright>
 * Copyright (c) 2001: The Trustees of Columbia University in the
 *  City of New York, Peppo Valetto. All Rights Reserved.
 * </copyright>
 */

package psl.workflakes.coolets.adaptors;

import java.lang.reflect.Method;
import java.util.Vector;

import siena.*;
import org.cougaar.domain.planning.ldm.asset.*;
import org.cougaar.domain.planning.ldm.plan.*;
import psl.worklets.*;
import psl.workflakes.coolets.*;

/**
 *
 * <p>Title: SienaLDMAdaptorInf</p>
 * <p>Description: Adaptor interface for "shell" {@link psl.workflakes.coolets.SienaLDMPlugIn}</p>.
 * <p>Copyright: Copyright (c) 2002: The Trustees of Columbia University in the
 *  City of New York, Peppo Valetto. All Rights Reserved.</p>
 * @author Peppo Valetto
 * @version 1.0
 * @see psl.workflakes.coolets.SienaLDMPlugIn
 * @see psl.workflakes.coolets.CooletIncomingJunction
 */
public interface SienaLDMAdaptorInf
	extends WklInstallInf {
        /**
         * sets a Siena subscription and a handler for that subscription
         * @param wj the <code>CooletIncomingJunction</code> implementing the handler
         * @param m the <code>Method</code> of <code>wj</code> implementing the handler code
         * @param f the Siena <code>Filter</code> representing the subscription
         */
	public void setupSienaSub (WorkletJunction wj, Method m, Filter f);
        /**
         * publishes and event on a Siena bus
         * @param n the event to be published
         */
	public void publishEvent (siena.Notification n);
        /**
         * inserts an <code>Asset</code> on the Cougaar blackboard
         * @param someAsset the <code>Asset</code> to be inserted
         */
	public void insertAsset (Asset someAsset);
        /**
         * changes an <code>Asset</code> that is present on the Cougaar blackboard
         * @param someAsset the <code>Asset</code> to be changed
         */
	public void changeAsset (Asset someAsset);
        /**
         * inserts a Cougaar task on the Cougaar blackboard
         * @param someTask the <code>NewTask</code> to be inserted
         */
	public void insertTask (NewTask someTask);
        /**
         * inserts a Cougaar task on the Cougaar blackboard
         * @param verb the verb for that task
         * @param dirObj the <code>Asset</code> assigned to that task
         */
	public void insertTask (String verb, Asset dirObj);
        /**
         * Creates an <code>Asset</code>
         * @param proto the Cougaar prototype for that <code>Asset</code>
         * @return the newly created <code>Asset</code>
         */
	public Asset newAsset(String proto);
	
	public NewTask newTask();
	public void setPrepositionalPhrases (NewTask t, Vector phrases);
}