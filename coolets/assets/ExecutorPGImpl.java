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
/** Implementation of ExecutorPG.
 *  @see ExecutorPG
 *  @see NewExecutorPG
 **/

package psl.workflakes.coolets.assets;

import org.cougaar.domain.planning.ldm.measure.*;
import org.cougaar.domain.planning.ldm.asset.*;
import org.cougaar.domain.planning.ldm.plan.*;
import java.util.*;



import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.beans.PropertyDescriptor;
import java.beans.IndexedPropertyDescriptor;

public class ExecutorPGImpl extends java.beans.SimpleBeanInfo
  implements NewExecutorPG, Cloneable
{
  public ExecutorPGImpl() {
  }

  // Slots

  private String theCapabilities;
  public String getCapabilities(){ return theCapabilities; }
  public void setCapabilities(String capabilities) {
    if (capabilities!=null) capabilities=capabilities.intern();
    theCapabilities=capabilities;
  }
  private String theJunction;
  public String getJunction(){ return theJunction; }
  public void setJunction(String junction) {
    if (junction!=null) junction=junction.intern();
    theJunction=junction;
  }
  private String theExecId;
  public String getExecId(){ return theExecId; }
  public void setExecId(String execId) {
    if (execId!=null) execId=execId.intern();
    theExecId=execId;
  }


  public ExecutorPGImpl(ExecutorPG original) {
    theCapabilities = original.getCapabilities();
    theJunction = original.getJunction();
    theExecId = original.getExecId();
  }

  public boolean hasDataQuality() { return false; }
  public org.cougaar.domain.planning.ldm.dq.DataQuality getDataQuality() { return null; }

  // static inner extension class for real DataQuality Support
  public final static class DQ extends ExecutorPGImpl implements org.cougaar.domain.planning.ldm.dq.NewHasDataQuality {
   public DQ() {
    super();
   }
   public DQ(ExecutorPG original) {
    super(original);
   }
   public Object clone() { return new DQ(this); }
   private transient org.cougaar.domain.planning.ldm.dq.DataQuality _dq = null;
   public boolean hasDataQuality() { return (_dq!=null); }
   public org.cougaar.domain.planning.ldm.dq.DataQuality getDataQuality() { return _dq; }
   public void setDataQuality(org.cougaar.domain.planning.ldm.dq.DataQuality dq) { _dq=dq; }
   private void writeObject(ObjectOutputStream out) throws IOException {
    out.defaultWriteObject();
    if (out instanceof org.cougaar.core.cluster.persist.PersistenceOutputStream) out.writeObject(_dq);
   }
   private void readObject(ObjectInputStream in) throws ClassNotFoundException, IOException {
    in.defaultReadObject();
    if (in instanceof org.cougaar.core.cluster.persist.PersistenceInputStream) _dq=(org.cougaar.domain.planning.ldm.dq.DataQuality)in.readObject();
   }
    
    private final static PropertyDescriptor properties[]=new PropertyDescriptor[1];
    static {
      try {
        properties[0]= new PropertyDescriptor("dataQuality", DQ.class, "getDataQuality", null);
      } catch (Exception e) { e.printStackTrace(); }
    }
    public PropertyDescriptor[] getPropertyDescriptors() {
      PropertyDescriptor[] pds = super.properties;
      PropertyDescriptor[] ps = new PropertyDescriptor[pds.length+properties.length];
      System.arraycopy(pds, 0, ps, 0, pds.length);
      System.arraycopy(properties, 0, ps, pds.length, properties.length);
      return ps;
    }
  }


  private transient ExecutorPG _locked = null;
  public PropertyGroup lock(Object key) {
    if (_locked == null)
      _locked = new _Locked(key);
    return _locked; }
  public PropertyGroup lock() { return lock(null); }
  public NewPropertyGroup unlock(Object key) { return this; }

  public Object clone() throws CloneNotSupportedException {
    return new ExecutorPGImpl(ExecutorPGImpl.this);
  }

  public PropertyGroup copy() {
    try {
      return (PropertyGroup) clone();
    } catch (CloneNotSupportedException cnse) { return null;}
  }

  public Class getPrimaryClass() {
    return primaryClass;
  }
  public String getAssetGetMethod() {
    return assetGetter;
  }
  public String getAssetSetMethod() {
    return assetSetter;
  }

  private void readObject(ObjectInputStream in) throws ClassNotFoundException, IOException {
    in.defaultReadObject();
    if (theCapabilities!= null) theCapabilities=theCapabilities.intern();
    if (theJunction!= null) theJunction=theJunction.intern();
    if (theExecId!= null) theExecId=theExecId.intern();
  }

  private final static PropertyDescriptor properties[] = new PropertyDescriptor[3];
  static {
    try {
      properties[0]= new PropertyDescriptor("capabilities", ExecutorPG.class, "getCapabilities", null);
      properties[1]= new PropertyDescriptor("junction", ExecutorPG.class, "getJunction", null);
      properties[2]= new PropertyDescriptor("execId", ExecutorPG.class, "getExecId", null);
    } catch (Exception e) { System.err.println("Caught: "+e); e.printStackTrace(); }
  }

  public PropertyDescriptor[] getPropertyDescriptors() {
    return properties;
  }
  private final class _Locked extends java.beans.SimpleBeanInfo
    implements ExecutorPG, Cloneable, LockedPG
  {
    private transient Object theKey = null;
    _Locked(Object key) { 
      if (this.theKey == null){  
        this.theKey = key; 
      } 
    }  

    /** public constructor for beaninfo - probably wont work**/
    public _Locked() {}

    public PropertyGroup lock() { return this; }
    public PropertyGroup lock(Object o) { return this; }

    public NewPropertyGroup unlock(Object key) throws IllegalAccessException {
       if( theKey.equals(key) )
         return ExecutorPGImpl.this;
       else 
         throw new IllegalAccessException("unlock: mismatched internal and provided keys!");
    }

    public PropertyGroup copy() {
      try {
        return (PropertyGroup) clone();
      } catch (CloneNotSupportedException cnse) { return null;}
    }


    public Object clone() throws CloneNotSupportedException {
      return new ExecutorPGImpl(ExecutorPGImpl.this);
    }

    public String getCapabilities() { return ExecutorPGImpl.this.getCapabilities(); }
    public String getJunction() { return ExecutorPGImpl.this.getJunction(); }
    public String getExecId() { return ExecutorPGImpl.this.getExecId(); }
  public final boolean hasDataQuality() { return ExecutorPGImpl.this.hasDataQuality(); }
  public final org.cougaar.domain.planning.ldm.dq.DataQuality getDataQuality() { return ExecutorPGImpl.this.getDataQuality(); }
    public Class getPrimaryClass() {
      return primaryClass;
    }
    public String getAssetGetMethod() {
      return assetGetter;
    }
    public String getAssetSetMethod() {
      return assetSetter;
    }

    public PropertyDescriptor[] getPropertyDescriptors() {
      return properties;
    }

    public Class getIntrospectionClass() {
      return ExecutorPGImpl.class;
    }

  }

}
