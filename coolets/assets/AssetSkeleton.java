/*
 * <copyright>
 *  Copyright 1997-2001 BBNT Solutions, LLC
 *  under sponsorship of the Defense Advanced Research Projects Agency (DARPA).
 * 
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the Cougaar Open Source License as published by
 *  DARPA on the Cougaar Open Source Website (www.cougaar.org).
 * 
 *  THE COUGAAR SOFTWARE AND ANY DERIVATIVE SUPPLIED BY LICENSOR IS
 *  PROVIDED 'AS IS' WITHOUT WARRANTIES OF ANY KIND, WHETHER EXPRESS OR
 *  IMPLIED, INCLUDING (BUT NOT LIMITED TO) ALL IMPLIED WARRANTIES OF
 *  MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE, AND WITHOUT
 *  ANY WARRANTIES AS TO NON-INFRINGEMENT.  IN NO EVENT SHALL COPYRIGHT
 *  HOLDER BE LIABLE FOR ANY DIRECT, SPECIAL, INDIRECT OR CONSEQUENTIAL
 *  DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE OF DATA OR PROFITS,
 *  TORTIOUS CONDUCT, ARISING OUT OF OR IN CONNECTION WITH THE USE OR
 *  PERFORMANCE OF THE COUGAAR SOFTWARE.
 * </copyright>
 */

/* @generated Tue May 21 11:59:24 CEST 2002 from properties.def - DO NOT HAND EDIT */
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
