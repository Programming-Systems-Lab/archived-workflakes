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
package psl.workflakes.exercise.tutorial;

import org.cougaar.domain.planning.ldm.asset.Asset;

/**
 * This ALP Asset class serves as a base class for the ProgrammerAsset class.
 * The ProgrammerAsset class is generated using the AssetWriter utility
 * @author ALPINE (alpine-software@bbn.com)
 * @version $Id$
 */
public class ProgrammerAssetAdapter extends Asset {
  private psl.workflakes.exercise.tutorial.Schedule schedule = new Schedule();

  /**
   * Create a new ProgrammerAssetAdapter
   */
  public ProgrammerAssetAdapter() {
    super();
  }

  /**
   * Create a new ProgrammerAssetAdapter
   * @param prototype the Asset's prototype
   */
  public ProgrammerAssetAdapter(Asset prototype) {
    super(prototype);
  }

  /**
   * Get the schedule of assignments for this programmer
   * @return this programmer's schedule
   */
  public psl.workflakes.exercise.tutorial.Schedule getSchedule() {
    return schedule;
  }

  /**
   * Set the schedule of assignments for this programmer
   * @param this programmer's new schedule
   */
  public void setSchedule(psl.workflakes.exercise.tutorial.Schedule newSchedule) {
    schedule = newSchedule;
  }

}