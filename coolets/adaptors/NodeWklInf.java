/*
 * <copyright>
 * Copyright (c) 2001: The Trustees of Columbia University in the
 *  City of New York, Peppo Valetto. All Rights Reserved.
 * </copyright>
 */
 package psl.workflakes.coolets.adaptors;

import org.cougaar.core.cluster.ClusterImpl;
import org.cougaar.core.plugin.PlugInAdapter;

/**
 *
 * <p>Title: NodeWklInf</p>
 * <p>Description: Interface for {@link psl.workflakes.coolets.WorkletNode WorkletNode}
 * </p>
 * <p>Copyright: Copyright (c) 2002: The Trustees of Columbia University in the
 *  City of New York, Peppo Valetto. All Rights Reserved.</p>
 * @author Peppo Valetto
 * @version 1.0
 */
public interface NodeWklInf
{
        /**
         * finds a cluster given a cluster ID
         * @param cluster the cluster ID
         * @return handle to the <code>ClusterImpl</code> with that ID
         */
	public ClusterImpl findCluster (String cluster);
        /**
         * finds a PlugIn given a cluster and a PlugIn ID
         * @param clImpl the cluster
         * @param plugin the PlugIn ID
         * @return handle to the requested <code>PlugInAdapter</code>
         */
	public PlugInAdapter findPlugIn(ClusterImpl clImpl, String plugin);
        /**
         * finds a PlugIn given a cluster ID and a PlugIn ID
         * @param cluster the cluster ID
         * @param plugin the PlugIn ID
         * @return handle to the requested <code>PlugInAdapter</code>
         */
	public PlugInAdapter findPlugIn (String cluster, String plugin);
}