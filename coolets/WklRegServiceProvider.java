package psl.workflakes.coolets;

import java.util.Hashtable;
import java.util.Collection;

import org.cougaar.core.component.*;
import org.cougaar.core.cluster.*;
import org.cougaar.core.plugin.PlugInAdapter;

import psl.worklets.WVM;

public class WklRegServiceProvider
	implements ServiceProvider {
	
	private Hashtable clusterIndex;
	private Hashtable pluginIndex;
	private WVM nodeWVM;
	private String WVMname;
	
	public WklRegServiceProvider(WVM aWVM, String name) {
		nodeWVM = aWVM;
		WVMname = name;
		clusterIndex = new Hashtable();
		pluginIndex = new Hashtable();		
	}
	
	public Object getService(ServiceBroker sb, Object requestor, Class serviceClass) {
	    if (WklRegService.class.isAssignableFrom(serviceClass))  {
			// intoduce checks on requestor
			return new WklRegServiceProxy();
		}
		else
			return null;
	}
	
	public void releaseService(ServiceBroker sb, Object requestor, Class serviceClass, Object service) {
	
	}

	private final class WklRegServiceProxy
		implements WVMRegService, WklClusterRegService, WklPlugInRegService {
		
		public String toString() {
			return (super.toString() + 
			": I am a WklRegService implementation");
		}

		//implementation of WklClusterRegService interface
		public void registerCluster (ClusterImpl cluster) {
			ClusterIdentifier clusterKey = cluster.getClusterIdentifier();
			synchronized (clusterIndex) {
				clusterIndex.put(clusterKey, cluster);
			}
		}
		
		public void unRegisterCluster (String clusterName) {
			ClusterIdentifier cid = ClusterIdentifier.getClusterIdentifier(clusterName);
			unRegisterCluster (cid);
		}
		
		public void unRegisterCluster (ClusterIdentifier cid) {
			synchronized (clusterIndex) {
				clusterIndex.remove(cid);
				pluginIndex.remove(cid);
				System.out.println ("Unregistered cluster " + cid.toString());
			}
		}
				
		public Collection getClusterListings() {
			Collection c;
			
			synchronized (clusterIndex) {
				c = clusterIndex.values();
			}
			
			return c;
		}
		
		
		//implementation of WklPlugInRegService interface
		public void registerPlugIn (ClusterIdentifier cid, PlugInAdapter pia) {
			Hashtable pia_ht;
			
			synchronized (pluginIndex) {
				if (! pluginIndex.containsKey(cid)) {
					pia_ht = new Hashtable();
					pia_ht.put(pia.toString(), pia);
					pluginIndex.put (cid, pia_ht);
				}
				else {
					pia_ht = (Hashtable)pluginIndex.get(cid);
					pia_ht.put(pia.toString(), pia);
				}
			}				
		}
		
		public void unRegisterPlugIn (ClusterIdentifier cid, String pid) {
			synchronized (pluginIndex) {
				Hashtable pia_ht = (Hashtable)pluginIndex.get(cid);
				pia_ht.remove(pid);
				System.out.println ("Unregistered plugin " + pid + " on cluster " + cid.toString());
			}	
		}
		
		public void unRegisterPlugIn (ClusterIdentifier cid, PlugInAdapter pia) {
			unRegisterPlugIn (cid, pia.toString());
		}

		public Collection getPlugInListings(ClusterIdentifier cid) {
			Collection c;
			
			synchronized (pluginIndex) {
				c = ((Hashtable)pluginIndex.get(cid)).values();	
			}
			
			return c;	
		}
		
		//implementation of WklRegService interface
		public WVM getWVM() { return nodeWVM; }
		public String getWVMName() { return WVMname; };
		public void setWVM(WVM aWVM) { nodeWVM = aWVM; }
		public void setWVMName(String s) { WVMname = s; }
				
	}
}
	