/*
 * <copyright>
 * Copyright (c) 2001: The Trustees of Columbia University in the
 *  City of New York, Peppo Valetto. All Rights Reserved.
 * </copyright>
 */
 package psl.workflakes.coolets.adaptors;

import java.util.Collection;

import psl.worklets.*;
import org.cougaar.util.UnaryPredicate;

/* adaptor interface for generic plugin interaction with Junctions */
/**
 *
 * <p>Title: WklInstallInf</p>
 * <p>Description: adaptor interface for the generic "shell" PlugIn
 * {@link psl.workflakes.coolets.WorkletPlugIn WorkletPlugin}.
 * </p>
 * <p>Copyright: Copyright (c) 2002: The Trustees of Columbia University in the
 *  City of New York, Peppo Valetto. All Rights Reserved.</p>
 * @author Peppo Valetto
 * @version 1.0
 * @see psl.workflakes.coolets.WorkletPlugIn
 * @see psl.workflakes.coolets.CooletIncomingJunction
 */
public interface WklInstallInf {
        /**
         * installs an incoming junction for immediate execution
         * @param junc the junction to be installed
         */
	public void installJunction (WorkletJunction junc);
        /**
         * installs an incoming junction for conditional execution when a Cougaar subscription is matched
         * @param junc the junction to be installed
         * @param pred the predicate describing the corresponding Cougaar subscription
         */
	public void installJunction (WorkletJunction junc, UnaryPredicate pred);
        /**
         * retrieves current time
         * @return the current time
         */
	public long getCurrentTime();
        /**
         * performs a query on the Cougaar blackboard.
         * @param pred the predicate defining the query
         * @return a set of results form the Cougaar blackboard.
         */
	public Collection delegateQuery(UnaryPredicate pred);

}
