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

// source machine generated at Thu May 31 13:58:58 GMT+02:00 2001 - Do not edit
/* @generated */
/** Primary client interface for SkillsPG.
 *  @see NewSkillsPG
 *  @see SkillsPGImpl
 **/

package psl.workflakes.exercise.tutorial.assets;

import org.cougaar.domain.planning.ldm.measure.*;
import org.cougaar.domain.planning.ldm.asset.*;
import org.cougaar.domain.planning.ldm.plan.*;
import java.util.*;



public interface SkillsPG extends PropertyGroup, org.cougaar.domain.planning.ldm.dq.HasDataQuality {
  /** The number of years the programmer has been coding **/
  int getYearsExperience();
  /** The average number lines of source code the programmer can produce per day **/
  int getSLOCPerDay();

  // introspection and construction
  /** the method of factoryClass that creates this type **/
  public static final String factoryMethod = "newSkillsPG";
  /** the (mutable) class type returned by factoryMethod **/
  public static final String mutableClass = "psl.workflakes.exercise.tutorial.assets.NewSkillsPG";
  /** the factory class **/
  public static final Class factoryClass = psl.workflakes.exercise.tutorial.assets.PropertyGroupFactory.class;
  /** the (immutable) class type returned by domain factory **/
  public static final Class primaryClass = psl.workflakes.exercise.tutorial.assets.SkillsPG.class;
  public static final String assetSetter = "setSkillsPG";
  public static final String assetGetter = "getSkillsPG";
  /** The Null instance for indicating that the PG definitely has no value **/
  public static final SkillsPG nullPG = new Null_SkillsPG();

/** Null_PG implementation for SkillsPG **/
static final class Null_SkillsPG
  implements SkillsPG, Null_PG
{
  public int getYearsExperience() { throw new UndefinedValueException(); }
  public int getSLOCPerDay() { throw new UndefinedValueException(); }
  public Object clone() throws CloneNotSupportedException {
    throw new CloneNotSupportedException();
  }
  public NewPropertyGroup unlock(Object key) { return null; }
  public PropertyGroup lock(Object key) { return null; }
  public PropertyGroup lock() { return null; }
  public PropertyGroup copy() { return null; }
  public Class getPrimaryClass(){return primaryClass;}
  public String getAssetGetMethod() {return assetGetter;}
  public String getAssetSetMethod() {return assetSetter;}
  public Class getIntrospectionClass() {
    return SkillsPGImpl.class;
  }

  public boolean hasDataQuality() { return false; }
  public org.cougaar.domain.planning.ldm.dq.DataQuality getDataQuality() { return null; }
}

/** Future PG implementation for SkillsPG **/
public final static class Future
  implements SkillsPG, Future_PG
{
  public int getYearsExperience() {
    waitForFinalize();
    return _real.getYearsExperience();
  }
  public int getSLOCPerDay() {
    waitForFinalize();
    return _real.getSLOCPerDay();
  }
  public Object clone() throws CloneNotSupportedException {
    throw new CloneNotSupportedException();
  }
  public NewPropertyGroup unlock(Object key) { return null; }
  public PropertyGroup lock(Object key) { return null; }
  public PropertyGroup lock() { return null; }
  public PropertyGroup copy() { return null; }
  public Class getPrimaryClass(){return primaryClass;}
  public String getAssetGetMethod() {return assetGetter;}
  public String getAssetSetMethod() {return assetSetter;}
  public Class getIntrospectionClass() {
    return SkillsPGImpl.class;
  }
  public synchronized boolean hasDataQuality() {
    return (_real!=null) && _real.hasDataQuality();
  }
  public synchronized org.cougaar.domain.planning.ldm.dq.DataQuality getDataQuality() {
    return (_real==null)?null:(_real.getDataQuality());
  }

  // Finalization support
  private SkillsPG _real = null;
  public synchronized void finalize(PropertyGroup real) {
    if (real instanceof SkillsPG) {
      _real=(SkillsPG) real;
      notifyAll();
    } else {
      throw new IllegalArgumentException("Finalization with wrong class: "+real);
    }
  }
  private synchronized void waitForFinalize() {
    while (_real == null) {
      try {
        wait();
      } catch (InterruptedException _ie) {}
    }
  }
}
}
