/*
 * <copyright>
 * Copyright (c) 2001: The Trustees of Columbia University in the
 *  City of New York, Peppo Valetto. All Rights Reserved.
 * </copyright>
 */
 package psl.workflakes.coolets.adaptors;

import org.cougaar.domain.planning.ldm.plan.Task;
import org.cougaar.domain.planning.ldm.asset.Asset;

/* adaptor interface for generic plugin interaction with Junctions */

public interface AllocatorAdaptorInf 
	extends WklInstallInf {
	void allocateAsset (Task t, Asset a);

}