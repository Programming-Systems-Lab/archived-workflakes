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

/* @generated Sun Jun 30 00:56:40 CEST 2002 from properties.def - DO NOT HAND EDIT */
/** Primary client interface for WVMPG.
 * properties identifying a WVM for requesting a junction
 *  @see NewWVMPG
 *  @see WVMPGImpl
 **/

package psl.workflakes.coolets.assets;

import org.cougaar.domain.planning.ldm.measure.*;
import org.cougaar.domain.planning.ldm.asset.*;
import org.cougaar.domain.planning.ldm.plan.*;
import java.util.*;



public interface WVMPG extends PropertyGroup, org.cougaar.domain.planning.ldm.dq.HasDataQuality {
  /** IP address of the WVM **/
  String getAddress();
  /** RMI id of the WVM **/
  String getId();
  /** socket port of the WVM **/
  int getSocketPort();
  /** RMI port of the WVM **/
  int getRmiPort();
  int getWebPort();

  // introspection and construction
  /** the method of factoryClass that creates this type **/
  String factoryMethod = "newWVMPG";
  /** the (mutable) class type returned by factoryMethod **/
  String mutableClass = "psl.workflakes.coolets.assets.NewWVMPG";
  /** the factory class **/
  Class factoryClass = psl.workflakes.coolets.assets.PropertyGroupFactory.class;
  /** the (immutable) class type returned by domain factory **/
   Class primaryClass = psl.workflakes.coolets.assets.WVMPG.class;
  String assetSetter = "setWVMPG";
  String assetGetter = "getWVMPG";
  /** The Null instance for indicating that the PG definitely has no value **/
  WVMPG nullPG = new Null_WVMPG();

/** Null_PG implementation for WVMPG **/
final class Null_WVMPG
  implements WVMPG, Null_PG
{
  public String getAddress() { throw new UndefinedValueException(); }
  public String getId() { throw new UndefinedValueException(); }
  public int getSocketPort() { throw new UndefinedValueException(); }
  public int getRmiPort() { throw new UndefinedValueException(); }
  public int getWebPort() { throw new UndefinedValueException(); }
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
    return WVMPGImpl.class;
  }

  public boolean hasDataQuality() { return false; }
  public org.cougaar.domain.planning.ldm.dq.DataQuality getDataQuality() { return null; }
}

/** Future PG implementation for WVMPG **/
final class Future
  implements WVMPG, Future_PG
{
  public String getAddress() {
    waitForFinalize();
    return _real.getAddress();
  }
  public String getId() {
    waitForFinalize();
    return _real.getId();
  }
  public int getSocketPort() {
    waitForFinalize();
    return _real.getSocketPort();
  }
  public int getRmiPort() {
    waitForFinalize();
    return _real.getRmiPort();
  }
  public int getWebPort() {
    waitForFinalize();
    return _real.getWebPort();
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
    return WVMPGImpl.class;
  }
  public synchronized boolean hasDataQuality() {
    return (_real!=null) && _real.hasDataQuality();
  }
  public synchronized org.cougaar.domain.planning.ldm.dq.DataQuality getDataQuality() {
    return (_real==null)?null:(_real.getDataQuality());
  }

  // Finalization support
  private WVMPG _real = null;
  public synchronized void finalize(PropertyGroup real) {
    if (real instanceof WVMPG) {
      _real=(WVMPG) real;
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
