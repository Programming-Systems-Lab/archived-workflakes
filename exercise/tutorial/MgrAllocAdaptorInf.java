/*
 * <copyright>
 * Copyright (c) 2001: The Trustees of Columbia University in the
 *  City of New York, Peppo Valetto. All Rights Reserved.
 * </copyright>
 */
 package psl.workflakes.exercise.tutorial;

import java.util.Enumeration;

import org.cougaar.domain.planning.ldm.plan.Task;
import org.cougaar.domain.planning.ldm.asset.Asset;
import psl.workflakes.coolets.adaptors.*;

public interface MgrAllocAdaptorInf 
	extends AllocatorAdaptorInf {
	public Enumeration getDevOrg();
}