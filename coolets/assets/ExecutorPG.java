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
  public static final String factoryMethod = "newExecutorPG";
  /** the (mutable) class type returned by factoryMethod **/
  public static final String mutableClass = "psl.workflakes.coolets.assets.NewExecutorPG";
  /** the factory class **/
  public static final Class factoryClass = psl.workflakes.coolets.assets.PropertyGroupFactory.class;
  /** the (immutable) class type returned by domain factory **/
  public static final Class primaryClass = psl.workflakes.coolets.assets.ExecutorPG.class;
  public static final String assetSetter = "setExecutorPG";
  public static final String assetGetter = "getExecutorPG";
  /** The Null instance for indicating that the PG definitely has no value **/
  public static final ExecutorPG nullPG = new Null_ExecutorPG();

/** Null_PG implementation for ExecutorPG **/
static final class Null_ExecutorPG
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
public final static class Future
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
