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

/* @generated Tue Feb 18 01:40:03 CET 2003 from ExecAgents_assets.def - DO NOT HAND EDIT */
package psl.workflakes.coolets.assets;
import org.cougaar.domain.planning.ldm.asset.*;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.util.Vector;
import java.beans.PropertyDescriptor;
import java.beans.IndexedPropertyDescriptor;
import java.beans.IntrospectionException;
/** Representation of a Task executor Asset **/

public class ExecAgentAsset extends psl.workflakes.coolets.ExecAgentAssetAdapter {

  public ExecAgentAsset() {
    myExecutorPG = null;
    myWVMPG = null;
  }

  public ExecAgentAsset(ExecAgentAsset prototype) {
    super(prototype);
    myExecutorPG=null;
    myWVMPG=null;
  }

  /** For infrastructure only - use org.cougaar.domain.planning.ldm.Factory.copyInstance instead. **/
  public Object clone() throws CloneNotSupportedException {
    ExecAgentAsset _thing = (ExecAgentAsset) super.clone();
    if (myExecutorPG!=null) _thing.setExecutorPG(myExecutorPG.lock());
    if (myWVMPG!=null) _thing.setWVMPG(myWVMPG.lock());
    return _thing;
  }

  /** create an instance of the right class for copy operations **/
  public Asset instanceForCopy() {
    return new ExecAgentAsset();
  }

  /** create an instance of this prototype **/
  public Asset createInstance() {
    return new ExecAgentAsset(this);
  }

  protected void fillAllPropertyGroups(Vector v) {
    super.fillAllPropertyGroups(v);
    { Object _tmp = getExecutorPG();
    if (_tmp != null && !(_tmp instanceof Null_PG)) {
      v.addElement(_tmp);
    } }
    { Object _tmp = getWVMPG();
    if (_tmp != null && !(_tmp instanceof Null_PG)) {
      v.addElement(_tmp);
    } }
  }

  private transient ExecutorPG myExecutorPG;

  public ExecutorPG getExecutorPG() {
    ExecutorPG _tmp = (myExecutorPG != null) ?
      myExecutorPG : (ExecutorPG)resolvePG(ExecutorPG.class);
    return (_tmp == ExecutorPG.nullPG)?null:_tmp;
  }
  public void setExecutorPG(PropertyGroup arg_ExecutorPG) {
    if (!(arg_ExecutorPG instanceof ExecutorPG))
      throw new IllegalArgumentException("setExecutorPG requires a ExecutorPG argument.");
    myExecutorPG = (ExecutorPG) arg_ExecutorPG;
  }

  private transient WVMPG myWVMPG;

  public WVMPG getWVMPG() {
    WVMPG _tmp = (myWVMPG != null) ?
      myWVMPG : (WVMPG)resolvePG(WVMPG.class);
    return (_tmp == WVMPG.nullPG)?null:_tmp;
  }
  public void setWVMPG(PropertyGroup arg_WVMPG) {
    if (!(arg_WVMPG instanceof WVMPG))
      throw new IllegalArgumentException("setWVMPG requires a WVMPG argument.");
    myWVMPG = (WVMPG) arg_WVMPG;
  }

  // generic search methods
  public PropertyGroupSchedule searchForPropertyGroupSchedule(Class c) {
    return super.searchForPropertyGroupSchedule(c);
  }

  public PropertyGroup getLocalPG(Class c, long t) {
    if (ExecutorPG.class.equals(c)) {
      return (myExecutorPG==ExecutorPG.nullPG)?null:myExecutorPG;
    }
    if (WVMPG.class.equals(c)) {
      return (myWVMPG==WVMPG.nullPG)?null:myWVMPG;
    }
    return super.getLocalPG(c,t);
  }

  public void setLocalPG(Class c, PropertyGroup pg) {
    if (ExecutorPG.class.equals(c)) {
      myExecutorPG=(ExecutorPG)pg;
    } else
    if (WVMPG.class.equals(c)) {
      myWVMPG=(WVMPG)pg;
    } else
      super.setLocalPG(c,pg);
  }

  public void setLocalPGSchedule(PropertyGroupSchedule pgSchedule) {
      super.setLocalPGSchedule(pgSchedule);
  }

  public PropertyGroup removeLocalPG(Class c) {
    PropertyGroup removed = null;
    if (ExecutorPG.class.equals(c)) {
      removed=myExecutorPG;
      myExecutorPG=null;
    } else
    if (WVMPG.class.equals(c)) {
      removed=myWVMPG;
      myWVMPG=null;
    } else
      removed=super.removeLocalPG(c);
    return removed;
  }

  public PropertyGroup removeLocalPG(PropertyGroup pg) {
    PropertyGroup removed = null;
    Class pgc = pg.getPrimaryClass();
    if (ExecutorPG.class.equals(pgc)) {
      removed=myExecutorPG;
      myExecutorPG=null;
    } else
    if (WVMPG.class.equals(pgc)) {
      removed=myWVMPG;
      myWVMPG=null;
    } else
      removed= super.removeLocalPG(pg);
    return removed;
  }

  public PropertyGroupSchedule removeLocalPGSchedule(Class c) {
    PropertyGroupSchedule removed = null;
    return removed;
  }

  public PropertyGroup generateDefaultPG(Class c) {
    if (ExecutorPG.class.equals(c)) {
      return (myExecutorPG= new ExecutorPGImpl());
    } else
    if (WVMPG.class.equals(c)) {
      return (myWVMPG= new WVMPGImpl());
    } else
      return super.generateDefaultPG(c);
  }

  // dumb serialization methods

  private void writeObject(ObjectOutputStream out) throws IOException {
    out.defaultWriteObject();
      if (myExecutorPG instanceof Null_PG || myExecutorPG instanceof Future_PG) {
        out.writeObject(null);
      } else {
        out.writeObject(myExecutorPG);
      }
      if (myWVMPG instanceof Null_PG || myWVMPG instanceof Future_PG) {
        out.writeObject(null);
      } else {
        out.writeObject(myWVMPG);
      }
  }

  private void readObject(ObjectInputStream in) throws ClassNotFoundException, IOException {
    in.defaultReadObject();
      myExecutorPG=(ExecutorPG)in.readObject();
      myWVMPG=(WVMPG)in.readObject();
  }
  // beaninfo support
  private static PropertyDescriptor properties[];
  static {
    try {
      properties = new PropertyDescriptor[2];
      properties[0] = new PropertyDescriptor("ExecutorPG", ExecAgentAsset.class, "getExecutorPG", null);
      properties[1] = new PropertyDescriptor("WVMPG", ExecAgentAsset.class, "getWVMPG", null);
    } catch (IntrospectionException ie) {}
  }

  public PropertyDescriptor[] getPropertyDescriptors() {
    PropertyDescriptor[] pds = super.getPropertyDescriptors();
    PropertyDescriptor[] ps = new PropertyDescriptor[pds.length+2];
    System.arraycopy(pds, 0, ps, 0, pds.length);
    System.arraycopy(properties, 0, ps, pds.length, 2);
    return ps;
  }
}
