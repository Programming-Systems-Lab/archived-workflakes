package psl.workflakes.coolets;

import java.util.Collection;

import org.cougaar.core.cluster.ClusterIdentifier;
import org.cougaar.core.plugin.PlugInAdapter;


public interface WklPlugInRegService
	extends WklRegService {
		void registerPlugIn	(ClusterIdentifier cid, PlugInAdapter pia);
		void unRegisterPlugIn (ClusterIdentifier cid, String pid);
		void unRegisterPlugIn (ClusterIdentifier cid, PlugInAdapter pia);
	}
	