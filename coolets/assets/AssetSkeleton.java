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
/** Abstract Asset Skeleton implementation
 * Implements default property getters, and additional property
 * lists.
 * Intended to be extended by org.cougaar.domain.planning.ldm.asset.Asset
 **/

package psl.workflakes.coolets.assets;

import org.cougaar.domain.planning.ldm.measure.*;
import org.cougaar.domain.planning.ldm.asset.*;
import org.cougaar.domain.planning.ldm.plan.*;
import java.util.*;


import java.io.Serializable;
import java.beans.PropertyDescriptor;
import java.beans.IndexedPropertyDescriptor;

public abstract class AssetSkeleton extends org.cougaar.domain.planning.ldm.asset.Asset {

  protected AssetSkeleton() {}

  protected AssetSkeleton(AssetSkeleton prototype) {
    super(prototype);
  }

  /**                 Default PG accessors               **/

  /** Search additional properties for a WVMPG instance.
   * @return instance of WVMPG or null.
   **/
  public WVMPG getWVMPG()
  {
    WVMPG _tmp = (WVMPG) resolvePG(WVMPG.class);
    return (_tmp==WVMPG.nullPG)?null:_tmp;
  }

  /** Test for existence of a WVMPG
   **/
  public boolean hasWVMPG() {

    return (getWVMPG() != null);
  }

  /** Set the WVMPG property.
   * The default implementation will create a new WVMPG
   * property and add it to the otherPropertyGroup list.
   * Many subclasses override with local slots.
   **/
  public void setWVMPG(PropertyGroup aWVMPG) {
    if (aWVMPG == null) {
      removeOtherPropertyGroup(WVMPG.class);
    } else {
      addOtherPropertyGroup(aWVMPG);
    }
  }

  /** Search additional properties for a ExecutorPG instance.
   * @return instance of ExecutorPG or null.
   **/
  public ExecutorPG getExecutorPG()
  {
    ExecutorPG _tmp = (ExecutorPG) resolvePG(ExecutorPG.class);
    return (_tmp==ExecutorPG.nullPG)?null:_tmp;
  }

  /** Test for existence of a ExecutorPG
   **/
  public boolean hasExecutorPG() {

    return (getExecutorPG() != null);
  }

  /** Set the ExecutorPG property.
   * The default implementation will create a new ExecutorPG
   * property and add it to the otherPropertyGroup list.
   * Many subclasses override with local slots.
   **/
  public void setExecutorPG(PropertyGroup aExecutorPG) {
    if (aExecutorPG == null) {
      removeOtherPropertyGroup(ExecutorPG.class);
    } else {
      addOtherPropertyGroup(aExecutorPG);
    }
  }

}
