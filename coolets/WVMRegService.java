package psl.workflakes.coolets;

import java.util.Collection;

import org.cougaar.core.component.Service;
import org.cougaar.core.cluster.ClusterIdentifier;

import psl.worklets.WVM;

public interface WVMRegService
	extends WklRegService {
		void setWVM(WVM aWVM);
		void setWVMName (String name);
		Collection getClusterListings();
	}
	