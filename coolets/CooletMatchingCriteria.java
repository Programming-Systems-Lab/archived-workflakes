/*
 * <copyright>
 * Copyright (c) 2001: The Trustees of Columbia University in the
 *  City of New York, Peppo Valetto. All Rights Reserved.
 * </copyright>
 */
package psl.workflakes.coolets;

import psl.workflakes.smartinf.*;
import org.cougaar.util.UnaryPredicate;

/**
 *
 * <p>Title: CooletMatchingCriteria</p>
 * <p>Description: Defines criteria for matching that can be used by the self-describing facilities of PlugIn adaptors
 * of type {@link SmartWorkletPlugIn SmartWorkletPlugIn}.
 * </p>
 * <p>Copyright: Copyright (c) 2002: The Trustees of Columbia University in the
 *  City of New York, Peppo Valetto. All Rights Reserved.</p>
 * @author Peppo Valetto
 * @version 1.0
 * Note: it is a dummy placeholder class that is devoid of semantics and functionality.
 * Since the {@link psl.workflakes.smartinf smartinf} package needs a major overhaul from the current
 * reference implementation, it does not pay to use <code>CooletMatchingCriteria</code>.
 * @see psl.workflakes.smartinf the smartinf package for details and a reference implementation of those facilities.
 * @see psl.workflakes.smartinf.WklMatchingCriteria
 * @see CooletIncomingJunction
 * @see WorkletPlugIn
 * @see SmartWorkletPlugIn
 */
public class CooletMatchingCriteria
	extends WklMatchingCriteria {

	private Class predClass;

	public CooletMatchingCriteria (Class c) {
		super(c);
		predClass = null;
	}


	public void addPredCriterion (Class c) { predClass = c; }
	public Class getPredClass () { return predClass; }

}



