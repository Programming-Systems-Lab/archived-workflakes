/*
 * <copyright>
 * Copyright (c) 2001: The Trustees of Columbia University in the
 *  City of New York, Peppo Valetto. All Rights Reserved.
 * </copyright>
 */
package psl.workflakes.coolets;

import psl.workflakes.smartinf.*;
import org.cougaar.util.UnaryPredicate;


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
	

		
	