/*
 * <copyright>
 * Copyright (c) 2001: The Trustees of Columbia University in the
 *  City of New York, Peppo Valetto. All Rights Reserved.
 * </copyright>
 */
 package psl.workflakes.coolets;

import org.cougaar.core.cluster.*;
import psl.worklets.WVM;

public class WorkletClusterImpl
	extends ClusterImpl {

	private WVM nodeWVM;
	private String WVMid;
	
	public String getNodeWVMid() { return WVMid; }
	public void setNodeWVMid(String s) { WVMid = new String (s); }
	public WVM getNodeWVM() { return nodeWVM; }
	public void setNodeWVM(WVM aWVM) {nodeWVM = aWVM; }	
}