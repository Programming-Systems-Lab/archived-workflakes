/*
 * <copyright>
 * Copyright (c) 2001: The Trustees of Columbia University in the
 *  City of New York, Peppo Valetto. All Rights Reserved.
 * </copyright>
 */
 
 package psl.workflakes.coolets.adaptors;

import org.cougaar.domain.planning.ldm.plan.*;
import org.cougaar.domain.planning.ldm.asset.*;
 
 public interface LDMPlugInAdaptorInf
 extends WklInstallInf {
 	public void addPropertyGroupFactory (java.lang.Object pf);
	public Asset createPrototype(java.lang.Class theClass, String protoName);
	public void cachePrototype(String protoID,Asset asset);
	public PropertyGroup createPropertyGroup(String groupName);	
	public Asset createInstance(String assetID);
	public void addAsset (Asset anAsset);
}
