/*
 * <copyright>
 * Copyright (c) 2001: The Trustees of Columbia University in the
 *  City of New York, Peppo Valetto. All Rights Reserved.
 * </copyright>
 */
package psl.workflakes.littlejil;

import org.cougaar.planning.ldm.asset.Asset;

/**
 *
 * <p>Title:ExecAgentAssetAdapter </p>
 * <p>Description: provides a standard way to manipulate
 * assets of type {@link psl.workflakes.coolets.assets.ExecAgentAsset ExecAgentAsset}</p>
 * <p>Copyright: Copyright (c) 2002: The Trustees of Columbia University in the
 *  City of New York, Peppo Valetto. All Rights Reserved.</p>
 * @author Peppo Valetto
 * @version 1.0
 */
public class ExecAgentAssetAdapter extends Asset {
	private static String version = "0.1";
	//private Schedule schedule = new Schedule();

  /**
   * Create a new ExecAgentAssetAdapter
   */
  public ExecAgentAssetAdapter() {
    super();
  }

  /**
   * Create a new ExecAgentAssetAdapter from a Prototype
   * @param prototype the Asset's prototype
   */
  public ExecAgentAssetAdapter(Asset prototype) {
    super(prototype);
  }

  /**
   * returns the version for this <code>ExecAgentAsset</code>
   * @return version ID
   */
  public String getVersion() {
    return version;
  }



}