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

/* @generated Wed May 22 14:27:04 CEST 2002 from properties.def - DO NOT HAND EDIT */
/** Abstract Asset Skeleton implementation
 * Implements default property getters, and additional property
 * lists.
 * Intended to be extended by org.cougaar.domain.planning.ldm.asset.Asset
 **/

package psl.workflakes.exercise.tutorial.assets;

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

  /** Search additional properties for a SkillsPG instance.
   * @return instance of SkillsPG or null.
   **/
  public SkillsPG getSkillsPG()
  {
    SkillsPG _tmp = (SkillsPG) resolvePG(SkillsPG.class);
    return (_tmp==SkillsPG.nullPG)?null:_tmp;
  }

  /** Test for existence of a SkillsPG
   **/
  public boolean hasSkillsPG() {

    return (getSkillsPG() != null);
  }

  /** Set the SkillsPG property.
   * The default implementation will create a new SkillsPG
   * property and add it to the otherPropertyGroup list.
   * Many subclasses override with local slots.
   **/
  public void setSkillsPG(PropertyGroup aSkillsPG) {
    if (aSkillsPG == null) {
      removeOtherPropertyGroup(SkillsPG.class);
    } else {
      addOtherPropertyGroup(aSkillsPG);
    }
  }

  /** Search additional properties for a LanguagePG instance.
   * @return instance of LanguagePG or null.
   **/
  public LanguagePG getLanguagePG()
  {
    LanguagePG _tmp = (LanguagePG) resolvePG(LanguagePG.class);
    return (_tmp==LanguagePG.nullPG)?null:_tmp;
  }

  /** Test for existence of a LanguagePG
   **/
  public boolean hasLanguagePG() {

    return (getLanguagePG() != null);
  }

  /** Set the LanguagePG property.
   * The default implementation will create a new LanguagePG
   * property and add it to the otherPropertyGroup list.
   * Many subclasses override with local slots.
   **/
  public void setLanguagePG(PropertyGroup aLanguagePG) {
    if (aLanguagePG == null) {
      removeOtherPropertyGroup(LanguagePG.class);
    } else {
      addOtherPropertyGroup(aLanguagePG);
    }
  }

}
