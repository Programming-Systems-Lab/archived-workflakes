/*
 * <copyright>
 * Copyright 1997-2000 Defense Advanced Research Projects Agency (DARPA)
 * and ALPINE (A BBN Technologies (BBN) and Raytheon Systems Company
 * (RSC) Consortium). This software to be used in accordance with the
 * COUGAAR license agreement.  The license agreement and other
 * information on the Cognitive Agent Architecture (COUGAAR) Project can
 * be found at http://www.cougaar.org or email: info@cougaar.org.
 * </copyright>
 */

// source machine generated at Wed Mar 27 15:03:32 CET 2002 - Do not edit
/* @generated */
/** Additional methods for WVMPG
 * offering mutators (set methods) for the object's owner
 **/

package psl.workflakes.coolets.assets;

import org.cougaar.domain.planning.ldm.measure.*;
import org.cougaar.domain.planning.ldm.asset.*;
import org.cougaar.domain.planning.ldm.plan.*;
import java.util.*;




public interface NewWVMPG extends WVMPG, NewPropertyGroup, org.cougaar.domain.planning.ldm.dq.HasDataQuality {
  void setAddress(String address);
  void setId(String id);
  void setSocketPort(int socketPort);
  void setRmiPort(int rmiPort);
  void setWebPort(int webPort);
}
