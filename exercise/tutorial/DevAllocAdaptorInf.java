/*
 * <copyright>
 * Copyright (c) 2001: The Trustees of Columbia University in the
 *  City of New York, Peppo Valetto. All Rights Reserved.
 * </copyright>
 */
 package psl.workflakes.exercise.tutorial;

import java.util.Collection;
import org.cougaar.domain.planning.ldm.plan.*;
import psl.worklets.*;
import psl.workflakes.coolets.adaptors.*;
import psl.workflakes.exercise.tutorial.assets.*;

/* adaptor interface for DevelopmentAllocatorPlugIn interaction with Junctions */

public interface DevAllocAdaptorInf
	extends AllocatorAdaptorInf {
		public Collection getProgrammers();
	}
	