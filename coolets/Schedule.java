/*
 * <copyright>
 *  Copyright 1997-2000 Defense Advanced Research Projects
 *  Agency (DARPA) and ALPINE (a BBN Technologies (BBN) and
 *  Raytheon Systems Company (RSC) Consortium).
 *  This software to be used only in accordance with the
 *  COUGAAR licence agreement.
 *
 * Derivative Work Copyright (c) 2001: The Trustees of Columbia University in the
 *  City of New York, Peppo Valetto. All Rights Reserved.
 * </copyright>
 */
package psl.workflakes.coolets;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Vector;
import java.util.Enumeration;

/**
 * This class is used by {@link ExecAgentAssetAdapter ExecAgentAssetAdapter} to hold the asset's
 * view of its schedule.
 */
public class Schedule extends Hashtable implements Serializable {

  /**
   * Create an empty schedule
   */
  public Schedule() {
  }

  /**
   * Assign an activity to a month.
   * @param month the month that the activity is scheduled for
   * @param work the activity for the month
   */
  public Object setWork(int month, Object work) {
    return this.put(new Integer(month), work);
  }

  /**
   * Get the activity scheduled for this month
   * @param month the month that we're interested in
   * @return the work scheduled for this month (or null if none)
   */
  public Object getWork(int month) {
    return this.get(new Integer(month));
  }

  /**
   * Remove the assignment for this month.
   * @param month the month to delete the assignment from
   * @return the previously-scheduled activity
   */
  public Object clearWork(int month) {
    return this.remove(new Integer(month));
  }
}
