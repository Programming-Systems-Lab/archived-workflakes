/*
 * <copyright>
 * Copyright (c) 2001: The Trustees of Columbia University in the
 *  City of New York, Peppo Valetto. All Rights Reserved.
 * </copyright>
 */
 package psl.workflakes.coolets.adaptors;

import psl.worklets.*;
import org.cougaar.util.UnaryPredicate;
import psl.workflakes.smartinf.*;

/* adaptor interface for generic plugin interaction with Junctions */
/**
 *
 * <p>Title: SmartWklInstallInf</p>
 * <p>Description: specialization of {@link psl.workflakes.smartinf.WklTargetInf} that
 * is used as an adaptor interface for incoming junction on generic "shell" PlugIns
 * that support the <code>smartinf</code> self-description facilities.
 * </p>
 * <p>Copyright: Copyright (c) 2002: The Trustees of Columbia University in the
 *  City of New York, Peppo Valetto. All Rights Reserved.</p>
 * <p>Company: </p>
 * @author Peppo Valetto
 * @version 1.0
 * @deprecated use {@link WklInstallInf WklInstallInf} instead.
 * <code>SmartWklInstallInf</code was intended to add support
 * for self-describing Worklets adaptors from the <code>smartinf</code> package.
 * Since that package needs a major overhaul from the current
 * reference implementation, it does not pay to use <code>SmartWklInstallInf</code>.
 * @see WklInstallInf
 * @see psl.workflakes.smartinf the smartinf package
 */
public interface SmartWklInstallInf
	extends WklTargetInf {
        /**
         * installs an incoming junction for immediate execution
         * @param junc the junction to be installed
         */
	public void installJunction (WorkletJunction junc);
        /**
         * installs an incoming junction for conditional execution when a Cougaar subscription is matched
         * @param junc the junction to be installed
         * @param pred the predicate describing the corresponding Cougaar subscription
         */
	public void installJunction (WorkletJunction junc, UnaryPredicate pred);

}
