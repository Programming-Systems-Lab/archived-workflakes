/*
 * <copyright>
 * Copyright (c) 2001: The Trustees of Columbia University in the
 *  City of New York, Peppo Valetto. All Rights Reserved.
 * </copyright>
 */
 package psl.workflakes.coolets.adaptors;

import org.cougaar.core.cluster.ClusterImpl;
import org.cougaar.core.plugin.PlugInAdapter;

public interface NodeWklInf
{
	public ClusterImpl findCluster (String cluster);
	public PlugInAdapter findPlugIn(ClusterImpl clImpl, String plugin);
	public PlugInAdapter findPlugIn (String cluster, String plugin);
}