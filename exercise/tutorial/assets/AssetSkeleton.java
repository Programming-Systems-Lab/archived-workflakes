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

// source machine generated at Fri May 25 23:38:17 GMT+02:00 2001 - Do not edit
/* @generated */
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
