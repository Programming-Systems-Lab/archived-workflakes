/*
 * <copyright>
 * Copyright (c) 2001: The Trustees of Columbia University in the
 *  City of New York, Peppo Valetto. All Rights Reserved.
 * </copyright>
 */
 package psl.workflakes.coolets;

import org.cougaar.core.society.*;
import org.cougaar.core.society.rmi.*;

/**
 *
 * <p>Title: WorkletMessageTransport</p>
 * <p>Description: RMI-based transport facility specialized for {@link WorkletNode WorkletNodes}.</p>
 * <p>Copyright: Copyright (c) 2002: The Trustees of Columbia University in the
 *  City of New York, Peppo Valetto. All Rights Reserved.</p>
 * @author Peppo Valetto
 * @version 1.0
 */
public class WorkletMessageTransport
	extends RMIMessageTransport {

        /**
         * Name Server used by this message transport
         */
	WorkletNameServer wklNS;

	static {
		keepStatistics = false;
	}

        /**
         * instantiates a <code>WorkletMessageTransport</code.
         * @param id RMI ID
         */
	public WorkletMessageTransport (String id) {
	super (id);
	wklNS = new WorkletNameServer();
	}
	/**
         * Creates and start a {@link WorkletNameServer WorkletNameServer}
         */
	public static void startNameService() {
		WorkletNameServer.create();
	}

        /**
         * accessor method for the NameServer
         * @return a handle to a NameServer
         */
	public NameServer getNameServer() {
    	return wklNS;
  	}


}