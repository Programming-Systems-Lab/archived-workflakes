/*
 * <copyright>
 * Copyright (c) 2001: The Trustees of Columbia University in the
 *  City of New York, Peppo Valetto. All Rights Reserved.
 * </copyright>
 */
 package psl.workflakes.coolets;

import org.cougaar.core.society.*;
import org.cougaar.core.society.rmi.*;

public class WorkletMessageTransport
	extends RMIMessageTransport {
	
	WorkletNameServer wklNS;
	
	static {
		keepStatistics = false;
	}
	
	public WorkletMessageTransport (String id) {
	super (id);	
	wklNS = new WorkletNameServer();		
	}
	
	public static void startNameService() {
		WorkletNameServer.create();
	}
	
	public NameServer getNameServer() {
    	return wklNS;
  	}
	
	
}