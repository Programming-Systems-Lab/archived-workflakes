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
/** AbstractFactory implementation for Properties.
 * Prevents clients from needing to know the implementation
 * class(es) of any of the properties.
 **/

package psl.workflakes.coolets.assets;

import org.cougaar.domain.planning.ldm.measure.*;
import org.cougaar.domain.planning.ldm.asset.*;
import org.cougaar.domain.planning.ldm.plan.*;
import java.util.*;



public class PropertyGroupFactory {
  // brand-new instance factory
  public static NewWVMPG newWVMPG() {
    return new WVMPGImpl();
  }
  // instance from prototype factory
  public static NewWVMPG newWVMPG(WVMPG prototype) {
    return new WVMPGImpl(prototype);
  }

  // brand-new instance factory
  public static NewExecutorPG newExecutorPG() {
    return new ExecutorPGImpl();
  }
  // instance from prototype factory
  public static NewExecutorPG newExecutorPG(ExecutorPG prototype) {
    return new ExecutorPGImpl(prototype);
  }

  /** Abstract introspection information.
   * Tuples are {<classname>, <factorymethodname>}
   * return value of <factorymethodname> is <classname>.
   * <factorymethodname> takes zero or one (prototype) argument.
   **/
  public static String properties[][]={
    {"psl.workflakes.coolets.assets.WVMPG", "newWVMPG"},
    {"psl.workflakes.coolets.assets.ExecutorPG", "newExecutorPG"}
  };
}
