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
  * <p>Title: JuncLDMAdaptorInf</p>
  * <p>Description: Adaptor interface for importing into the LDM information about junctions
  * that match specific workflow tasks
  * This interface is implemented by {@link psl.workflakes.coolets.JunctionLDMPlugIn JunctionLDMPlugIn}</p>
  * <p>Copyright: Copyright (c) 2002:  The Trustees of Columbia University in the
  *  City of New York, Peppo Valetto. All Rights Reserved.</p>
  * @author Peppo Valetto
  * @version 1.0
  */
 public interface JuncLDMAdaptorInf
 extends LDMPlugInAdaptorInf {

	// note this does not return the WVM socket port
	// but the RMIport of the RMIregistry of the Cougaar Node
	public int getRMIPort();
 }