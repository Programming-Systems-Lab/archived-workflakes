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
  public static final String factoryMethod = "newLanguagePG";
  /** the (mutable) class type returned by factoryMethod **/
  public static final String mutableClass = "psl.workflakes.exercise.tutorial.assets.NewLanguagePG";
  /** the factory class **/
  public static final Class factoryClass = psl.workflakes.exercise.tutorial.assets.PropertyGroupFactory.class;
  /** the (immutable) class type returned by domain factory **/
  public static final Class primaryClass = psl.workflakes.exercise.tutorial.assets.LanguagePG.class;
  public static final String assetSetter = "setLanguagePG";
  public static final String assetGetter = "getLanguagePG";
  /** The Null instance for indicating that the PG definitely has no value **/
  public static final LanguagePG nullPG = new Null_LanguagePG();

/** Null_PG implementation for LanguagePG **/
static final class Null_LanguagePG
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
public final static class Future
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
