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
  String factoryMethod = "newSkillsPG";
  /** the (mutable) class type returned by factoryMethod **/
  String mutableClass = "psl.workflakes.exercise.tutorial.assets.NewSkillsPG";
  /** the factory class **/
  Class factoryClass = psl.workflakes.exercise.tutorial.assets.PropertyGroupFactory.class;
  /** the (immutable) class type returned by domain factory **/
   Class primaryClass = psl.workflakes.exercise.tutorial.assets.SkillsPG.class;
  String assetSetter = "setSkillsPG";
  String assetGetter = "getSkillsPG";
  /** The Null instance for indicating that the PG definitely has no value **/
  SkillsPG nullPG = new Null_SkillsPG();

/** Null_PG implementation for SkillsPG **/
final class Null_SkillsPG
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
final class Future
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
