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

// source machine generated at Mon Mar 11 23:51:01 CET 2002 - Do not edit
/* @generated */
/** Implementation of LanguagePG.
 *  @see LanguagePG
 *  @see NewLanguagePG
 **/

package psl.workflakes.exercise.tutorial.assets;

import org.cougaar.domain.planning.ldm.measure.*;
import org.cougaar.domain.planning.ldm.asset.*;
import org.cougaar.domain.planning.ldm.plan.*;
import java.util.*;



import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.beans.PropertyDescriptor;
import java.beans.IndexedPropertyDescriptor;

public class LanguagePGImpl extends java.beans.SimpleBeanInfo
  implements NewLanguagePG, Cloneable
{
  public LanguagePGImpl() {
  };

  // Slots

  private boolean theKnowsJava;
  public boolean getKnowsJava(){ return theKnowsJava; }
  public void setKnowsJava(boolean knowsJava) {
    theKnowsJava=knowsJava;
  }
  private boolean theKnowsJavaScript;
  public boolean getKnowsJavaScript(){ return theKnowsJavaScript; }
  public void setKnowsJavaScript(boolean knowsJavaScript) {
    theKnowsJavaScript=knowsJavaScript;
  }


  public LanguagePGImpl(LanguagePG original) {
    theKnowsJava = original.getKnowsJava();
    theKnowsJavaScript = original.getKnowsJavaScript();
  }

  public boolean hasDataQuality() { return false; }
  public org.cougaar.domain.planning.ldm.dq.DataQuality getDataQuality() { return null; }

  // static inner extension class for real DataQuality Support
  public final static class DQ extends LanguagePGImpl implements org.cougaar.domain.planning.ldm.dq.NewHasDataQuality {
   public DQ() {
    super();
   }
   public DQ(LanguagePG original) {
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


  private transient LanguagePG _locked = null;
  public PropertyGroup lock(Object key) {
    if (_locked == null)
      _locked = new _Locked(key);
    return _locked; }
  public PropertyGroup lock() { return lock(null); }
  public NewPropertyGroup unlock(Object key) { return this; }

  public Object clone() throws CloneNotSupportedException {
    LanguagePGImpl _tmp = new LanguagePGImpl(this);
    return _tmp;
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

  private final static PropertyDescriptor properties[] = new PropertyDescriptor[2];
  static {
    try {
      properties[0]= new PropertyDescriptor("knowsJava", LanguagePG.class, "getKnowsJava", null);
      properties[1]= new PropertyDescriptor("knowsJavaScript", LanguagePG.class, "getKnowsJavaScript", null);
    } catch (Exception e) { System.err.println("Caught: "+e); e.printStackTrace(); }
  };

  public PropertyDescriptor[] getPropertyDescriptors() {
    return properties;
  }
  private final class _Locked extends java.beans.SimpleBeanInfo
    implements LanguagePG, Cloneable, LockedPG
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
         return LanguagePGImpl.this;
       else 
         throw new IllegalAccessException("unlock: mismatched internal and provided keys!");
    }

    public PropertyGroup copy() {
      return new LanguagePGImpl(LanguagePGImpl.this);
    }

    public Object clone() throws CloneNotSupportedException {
      return new LanguagePGImpl(LanguagePGImpl.this);
    }

    public boolean getKnowsJava() { return LanguagePGImpl.this.getKnowsJava(); }
    public boolean getKnowsJavaScript() { return LanguagePGImpl.this.getKnowsJavaScript(); }
  public final boolean hasDataQuality() { return LanguagePGImpl.this.hasDataQuality(); }
  public final org.cougaar.domain.planning.ldm.dq.DataQuality getDataQuality() { return LanguagePGImpl.this.getDataQuality(); }
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
      return LanguagePGImpl.class;
    }

  }

}
