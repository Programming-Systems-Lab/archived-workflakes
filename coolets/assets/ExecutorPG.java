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

/* @generated Tue Feb 18 01:40:04 CET 2003 from properties.def - DO NOT HAND EDIT */
/** Primary client interface for ExecutorPG.
 * properties of an executor agent
 *  @see NewExecutorPG
 *  @see ExecutorPGImpl
 **/

package psl.workflakes.coolets.assets;

import org.cougaar.domain.planning.ldm.measure.*;
import org.cougaar.domain.planning.ldm.asset.*;
import org.cougaar.domain.planning.ldm.plan.*;
import java.util.*;



public interface ExecutorPG extends PropertyGroup, org.cougaar.domain.planning.ldm.dq.HasDataQuality {
  /** executable tasks of the executor **/
  String getCapabilities();
  /** class names of the junction for the executor **/
  String getJunction();
  /** identification of the PlugIn originating the asset **/
  String getExecId();

  // introspection and construction
  /** the method of factoryClass that creates this type **/
  String factoryMethod = "newExecutorPG";
  /** the (mutable) class type returned by factoryMethod **/
  String mutableClass = "psl.workflakes.coolets.assets.NewExecutorPG";
  /** the factory class **/
  Class factoryClass = psl.workflakes.coolets.assets.PropertyGroupFactory.class;
  /** the (immutable) class type returned by domain factory **/
   Class primaryClass = psl.workflakes.coolets.assets.ExecutorPG.class;
  String assetSetter = "setExecutorPG";
  String assetGetter = "getExecutorPG";
  /** The Null instance for indicating that the PG definitely has no value **/
  ExecutorPG nullPG = new Null_ExecutorPG();

/** Null_PG implementation for ExecutorPG **/
final class Null_ExecutorPG
  implements ExecutorPG, Null_PG
{
  public String getCapabilities() { throw new UndefinedValueException(); }
  public String getJunction() { throw new UndefinedValueException(); }
  public String getExecId() { throw new UndefinedValueException(); }
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
    return ExecutorPGImpl.class;
  }

  public boolean hasDataQuality() { return false; }
  public org.cougaar.domain.planning.ldm.dq.DataQuality getDataQuality() { return null; }
}

/** Future PG implementation for ExecutorPG **/
final class Future
  implements ExecutorPG, Future_PG
{
  public String getCapabilities() {
    waitForFinalize();
    return _real.getCapabilities();
  }
  public String getJunction() {
    waitForFinalize();
    return _real.getJunction();
  }
  public String getExecId() {
    waitForFinalize();
    return _real.getExecId();
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
    return ExecutorPGImpl.class;
  }
  public synchronized boolean hasDataQuality() {
    return (_real!=null) && _real.hasDataQuality();
  }
  public synchronized org.cougaar.domain.planning.ldm.dq.DataQuality getDataQuality() {
    return (_real==null)?null:(_real.getDataQuality());
  }

  // Finalization support
  private ExecutorPG _real = null;
  public synchronized void finalize(PropertyGroup real) {
    if (real instanceof ExecutorPG) {
      _real=(ExecutorPG) real;
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
