/*
 * <copyright>
 * Copyright (c) 2001: The Trustees of Columbia University in the
 *  City of New York, Peppo Valetto. All Rights Reserved.
 * </copyright>
 */
 package psl.workflakes.coolets.adaptors;

import psl.worklets.*;
import org.cougaar.util.UnaryPredicate;
import psl.workflakes.smartinf.*;

/* adaptor interface for generic plugin interaction with Junctions */

public interface SmartWklInstallInf 
	extends WklTargetInf {
	public void installJunction (WorkletJunction junc);
	public void installJunction (WorkletJunction junc, UnaryPredicate pred);
	
}
