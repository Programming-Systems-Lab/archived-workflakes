/*
 * <copyright>
 * Copyright (c) 2001: The Trustees of Columbia University in the
 *  City of New York, Peppo Valetto. All Rights Reserved.
 * </copyright>
 */
 package psl.workflakes.coolets;

import java.rmi.registry.*;
import java.rmi.RemoteException;
import org.cougaar.core.society.*;
import org.cougaar.core.society.rmi.*;
import psl.worklets.WVM;

/**
 *
 * <p>Title: WorkletNameServer</p>
 * <p>Description: Name Server for RMI specialzed for {@link WorkletNode WorkletNodes}.</p>
 * <p>Copyright: Copyright (c) 2002: The Trustees of Columbia University in the
 *  City of New York, Peppo Valetto. All Rights Reserved.</p>
 * @author Peppo Valetto
 * @version 1.0
 */
public class WorkletNameServer
	extends RMINameServer {

        /**
         * creates a <code>WorkletNameServer</code>
         */
	public static void create() {
		create(true);
	}

        /**
         * creates a <code>WorkletNameServer</code>
         * @param tryHard flag to see if multiple binding attempts must be done. Set to <code>true</code>.
         */
	public static void create(boolean tryHard) {
		NS ns = null;

		int port = Communications.getPort();
    	String addr = Communications.getFdsAddress();
    	String nsURL = "//" + addr + ":" + port + "/NameService";

    	Registry RMIreg = null;
    	while (RMIreg == null) {
	    	try {
		    	RMIreg = LocateRegistry.getRegistry (addr, port);
		    } catch (RemoteException getExc) { // couldn't attach to the registry specified
		    	System.err.println ("local RMI registry not found on port " + port + "Creating it ...");
		    	try {
		    		RMIreg = LocateRegistry.createRegistry(port);
				} catch (RemoteException createExc) { // couldn't even create it from scratch
					if (!tryHard) {
						System.err.println ("local RMI registry could not be created on port " + port);
						return;
					}
					System.err.println("!");
				} // end of inner catch
			} // end of outer catch
		} // end of while

		try {
			Object remObj = RMIreg.lookup(nsURL);
			ns = (NS) remObj;
			if (ns != null)
				System.err.println("Worklet Name Server found at " + nsURL);
		} catch (Exception lookupExc) { // could not lookup - it means NameService is not up yet
			try {
				ns = new NSImpl();
				RMIreg.rebind (nsURL, ns);
				System.err.println("Worklet Name Server started at " + nsURL);
			} catch (RemoteException e) {
				System.err.println ("NameServer (" + nsURL + ") startup failed: ");
				e.printStackTrace();
			}  // end of inner catch
		} // end of outer catch

	}
}
