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

/* @generated Tue Feb 18 02:31:29 CET 2003 from properties.def - DO NOT HAND EDIT */
/** Primary client interface for LanguagePG.
 * Listing of the programming languages a programmer can use.
 *  @see NewLanguagePG
 *  @see LanguagePGImpl
 **/

package psl.workflakes.exercise.tutorial.assets;

import org.cougaar.domain.planning.ldm.measure.*;
import org.cougaar.domain.planning.ldm.asset.*;
import org.cougaar.domain.planning.ldm.plan.*;
import java.util.*;



public interface LanguagePG extends PropertyGroup, org.cougaar.domain.planning.ldm.dq.HasDataQuality {
  /** True if the programmer is fluent in Java **/
  boolean getKnowsJava();
  /** True if the programmer is fluent in JavaScript **/
  boolean getKnowsJavaScript();

  // introspection and construction
  /** the method of factoryClass that creates this type **/
  String factoryMethod = "newLanguagePG";
  /** the (mutable) class type returned by factoryMethod **/
  String mutableClass = "psl.workflakes.exercise.tutorial.assets.NewLanguagePG";
  /** the factory class **/
  Class factoryClass = psl.workflakes.exercise.tutorial.assets.PropertyGroupFactory.class;
  /** the (immutable) class type returned by domain factory **/
   Class primaryClass = psl.workflakes.exercise.tutorial.assets.LanguagePG.class;
  String assetSetter = "setLanguagePG";
  String assetGetter = "getLanguagePG";
  /** The Null instance for indicating that the PG definitely has no value **/
  LanguagePG nullPG = new Null_LanguagePG();

/** Null_PG implementation for LanguagePG **/
final class Null_LanguagePG
  implements LanguagePG, Null_PG
{
  public boolean getKnowsJava() { throw new UndefinedValueException(); }
  public boolean getKnowsJavaScript() { throw new UndefinedValueException(); }
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
    return LanguagePGImpl.class;
  }

  public boolean hasDataQuality() { return false; }
  public org.cougaar.domain.planning.ldm.dq.DataQuality getDataQuality() { return null; }
}

/** Future PG implementation for LanguagePG **/
final class Future
  implements LanguagePG, Future_PG
{
  public boolean getKnowsJava() {
    waitForFinalize();
    return _real.getKnowsJava();
  }
  public boolean getKnowsJavaScript() {
    waitForFinalize();
    return _real.getKnowsJavaScript();
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
    return LanguagePGImpl.class;
  }
  public synchronized boolean hasDataQuality() {
    return (_real!=null) && _real.hasDataQuality();
  }
  public synchronized org.cougaar.domain.planning.ldm.dq.DataQuality getDataQuality() {
    return (_real==null)?null:(_real.getDataQuality());
  }

  // Finalization support
  private LanguagePG _real = null;
  public synchronized void finalize(PropertyGroup real) {
    if (real instanceof LanguagePG) {
      _real=(LanguagePG) real;
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
