package psl.workflakes.coolets;

import java.util.Collection;

import org.cougaar.core.component.Service;
import org.cougaar.core.cluster.*;

public interface WklClusterRegService
	extends WklRegService {
		void registerCluster (ClusterImpl cluster);
		void unRegisterCluster (String clusterName);
		void unRegisterCluster (ClusterIdentifier cid);
		Collection getPlugInListings(ClusterIdentifier cid);
	}
	