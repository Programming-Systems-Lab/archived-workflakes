/*
 * <copyright>
 * Copyright (c) 2001: The Trustees of Columbia University in the
 *  City of New York, Peppo Valetto. All Rights Reserved.
 * </copyright>
 */
 package psl.workflakes.coolets;
 
import java.util.Iterator;

import org.cougaar.core.cluster.*;
import org.cougaar.core.component.ServiceBroker;
import org.cougaar.core.plugin.PlugInAdapter;
import org.cougaar.util.EmptyIterator;
import org.cougaar.util.StateModelException;
import org.cougaar.util.UnaryPredicate;

import psl.worklets.WVM;

/**
 *
 * <p>Title: WorkletClusterImpl</p>
 * <p>Description: Cougaar <code>Cluster</code> implementation specialized for {@link WorkletNode WorkletNodes}.
 * @see org.cougaar.core.cluster.ClusterImpl
 * </p>
 * <p>Copyright: Copyright (c) 2002: The Trustees of Columbia University in the
 *  City of New York, Peppo Valetto. All Rights Reserved.</p>
 * @author Peppo Valetto
 * @version 1.0
 */
public class WorkletClusterImpl
	extends ClusterImpl {

	private WVM nodeWVM;
	private String WVMid;
	private SienaLDMPlugIn sienaGateway;

	 public void load() 
	 	throws StateModelException {
	 	System.out.println ("Loading " + getClass().getName() + 
	 		" named " + getClusterIdentifier().toString());
	 	System.out.println (getBindingSite().getClass().getName());
	 	super.load();
	 	ServiceBroker sb = getServiceBroker();
	 	WklClusterRegService wklreg = (WklClusterRegService) sb.getService(this, WklClusterRegService.class, null);
	 	wklreg.registerCluster(this);
	 	setNodeWVMid(wklreg.getWVMName());
	 	setNodeWVM(wklreg.getWVM());
	 	sb.releaseService(this, WklClusterRegService.class, wklreg);			
	 }
	 
	 public void unload()
	 	throws StateModelException {
	 	System.out.println ("Unloading a " + getClass().getName() + 
	 		" named " + getClusterIdentifier().toString());
	 	ServiceBroker sb = getServiceBroker();
	 	WklClusterRegService wklreg = (WklClusterRegService) sb.getService(this, WklClusterRegService.class, null);
	 	wklreg.unRegisterCluster(this.getClusterIdentifier());
	 	sb.releaseService(this, WklClusterRegService.class, wklreg);
	 	super.unload();
	 }
	 
	 public Iterator getPlugIns() {
	 	ServiceBroker sb = getServiceBroker();
	 	WklClusterRegService wklreg = (WklClusterRegService) sb.getService(this, WklClusterRegService.class, null);
	 	Iterator plugins = wklreg.getPlugInListings(this.getClusterIdentifier()).iterator();
	 	sb.releaseService(this, WklClusterRegService.class, wklreg);
	 	return plugins;
	 }
	
        /**
         * Accessor method for getting the WVM RMI ID of the <code>WorkletNode</code> hosting this cluster.
         * @return the WVM RMI ID
         */
	public String getNodeWVMid() { return WVMid; }
        /**
         * Accessor method for setting the WVM RMI ID of the <code>WorkletNode</code> hosting this cluster.
         * @param s the WVM RMI ID
         */
        public void setNodeWVMid(String s) { WVMid = new String (s); }
        /**
         * Accessor method for getting the WVM of the <code>WorkletNode</code> hosting this cluster.
         * @return a handle to the WVM
         */
	public WVM getNodeWVM() { return nodeWVM; }
        /**
         * Accessor method for setting the WVM of the <code>WorkletNode</code> hosting this cluster.
         * @param aWVM the WVM to be set
         */
	public void setNodeWVM(WVM aWVM) {nodeWVM = aWVM; }
        /**
         * Accessor method for getting a handle to the (optional) {@link SienaLDMPlugIn SienaLDMPlugIn} servicing this <code>WorkletNode</code>.
         * @return a handle to the <code>SienaLDMPlugIn</code>
         */
	public SienaLDMPlugIn getSienaGateway() { return sienaGateway; }
        /**
         * Accessor method for setting the (optional) {@link SienaLDMPlugIn SienaLDMPlugIn} servicing this <code>WorkletNode</code>.
         * @param sienaPI a handle to the <code>SienaLDMPlugIn</code>
         */
	public void setSienaGateway(SienaLDMPlugIn sienaPI) {sienaGateway = sienaPI; }
}