/*
 * <copyright>
 * Copyright (c) 2001: The Trustees of Columbia University in the
 *  City of New York, Peppo Valetto. All Rights Reserved.
 * </copyright>
 */
 package psl.workflakes.coolets.adaptors;

import java.util.Enumeration;
import org.cougaar.util.UnaryPredicate;
import org.cougaar.core.cluster.IncrementalSubscription;

public interface CooletJunctionInf
{
	public void embeddedExec();
	public void embeddedExec(IncrementalSubscription sub);
	public Class getPredClass();
	public UnaryPredicate getPred();
	public void setPred(UnaryPredicate pred);
	public void setClusterID (String cluster);
	public String getClusterID();
	public void setPluginID (String plugin);
	public String getPluginID();
}
