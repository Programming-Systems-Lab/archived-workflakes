/*
 * <copyright>
 * Copyright (c) 2001: The Trustees of Columbia University in the
 *  City of New York, Peppo Valetto. All Rights Reserved.
 * </copyright>
 */

 package psl.workflakes.coolets.adaptors;

import org.cougaar.domain.planning.ldm.plan.*;
import org.cougaar.domain.planning.ldm.asset.*;

 /**
  *
  * <p>Title: LDMPlugInAdaptorInf</p>
  * <p>Description: Adaptor interface for LDM "shell" PlugIns</p>.
  * <p>Copyright: Copyright (c) 2002: The Trustees of Columbia University in the
  *  City of New York, Peppo Valetto. All Rights Reserved.
  * @author Peppo Valetto
  * @version 1.0
  */
 public interface LDMPlugInAdaptorInf
 extends WklInstallInf {
        /**
         * adds to the LDM a Cougaar PropertyGroup Factory
         * @param pf the Property Group Factory to be added
         */
 	public void addPropertyGroupFactory (java.lang.Object pf);
        /**
         * Creates a Cougaar prototype for an <code>Asset</code>
         * @param theClass the Class object for the <code>Asset</code>
         * @param protoName the ID to be assigned to the prototype
         * @return the <code>Asset</code> prototype
         */
	public Asset createPrototype(java.lang.Class theClass, String protoName);
        /**
         * puts a prototype in the LDM cache
         * @param protoID the ID of the prototype
         * @param asset the prototype <code>Asset</code>
         */
	public void cachePrototype(String protoID,Asset asset);
        /**
         * Creates a Cougaar <code>PropertyGroup</code>
         * @param groupName the ID of the <code>PropertyGroup</code>
         * @return the created <code>PropertyGroup</code> object
         */
	public PropertyGroup createPropertyGroup(String groupName);
        /**
         * Creates an instance of a Cougaar <code>Asset</code>
         * @param assetID the ID for that Asset
         * @return the created <code>Asset</code> object
         */
	public Asset createInstance(String assetID);
        /**
         * adds an <code>Asset</code> to the LDM.
         * @param anAsset
         */
	public void addAsset (Asset anAsset);
}
