/*
 * <copyright>
 * Copyright (c) 2001: The Trustees of Columbia University in the
 *  City of New York, Peppo Valetto. All Rights Reserved.
 * </copyright>
 */
 package psl.workflakes.coolets.adaptors;

import java.util.Enumeration;
import org.cougaar.domain.planning.ldm.plan.Task;
import org.cougaar.domain.planning.ldm.asset.Asset;

import psl.workflakes.coolets.*;
/**
 *
 * <p>Title: AllocatorAdaptorInf</p>
 * <p>Description: Adaptor interface for a "shell" Allocator PlugIn</p>.
 * Accommodates specializations of {@link psl.workflakes.coolets.CooletIncomingJunction CooletIncomingJunction} that implement
 * Allocator logic.
 * <p>Copyright: Copyright (c) 2002: The Trustees of Columbia University in the
 *  City of New York, Peppo Valetto. All Rights Reserved.</p>
 * @author Peppo Valetto
 * @version 1.0
 * @see WklInstallInf
 */

/* adaptor interface for generic plugin interaction with Junctions that implements Allocator logic */
public interface AllocatorAdaptorInf
	extends WklInstallInf {
        /**
         * Allocate an <code>Asset</code> to a <code>Task</code>
         * @param t the Task
         * @param a the Asset
         */
	public void allocateAsset (Task t, Asset a);
        /**
         * Allocate a <code>Task</code> after a given moment in time
         */
        public void allocateTask (Task t, double after);

        public void setAllocJunction (AllocatorJunction j);

        public Enumeration getExecutors();

}