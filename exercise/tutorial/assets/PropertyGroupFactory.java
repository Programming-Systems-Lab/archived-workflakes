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
/** AbstractFactory implementation for Properties.
 * Prevents clients from needing to know the implementation
 * class(es) of any of the properties.
 **/

package psl.workflakes.exercise.tutorial.assets;

import org.cougaar.domain.planning.ldm.measure.*;
import org.cougaar.domain.planning.ldm.asset.*;
import org.cougaar.domain.planning.ldm.plan.*;
import java.util.*;



public class PropertyGroupFactory {
  // brand-new instance factory
  public static NewSkillsPG newSkillsPG() {
    return new SkillsPGImpl();
  }
  // instance from prototype factory
  public static NewSkillsPG newSkillsPG(SkillsPG prototype) {
    return new SkillsPGImpl(prototype);
  }

  // brand-new instance factory
  public static NewLanguagePG newLanguagePG() {
    return new LanguagePGImpl();
  }
  // instance from prototype factory
  public static NewLanguagePG newLanguagePG(LanguagePG prototype) {
    return new LanguagePGImpl(prototype);
  }

  /** Abstract introspection information.
   * Tuples are {<classname>, <factorymethodname>}
   * return value of <factorymethodname> is <classname>.
   * <factorymethodname> takes zero or one (prototype) argument.
   **/
  public static String properties[][]={
    {"psl.workflakes.exercise.tutorial.assets.SkillsPG", "newSkillsPG"},
    {"psl.workflakes.exercise.tutorial.assets.LanguagePG", "newLanguagePG"}
  };
}
