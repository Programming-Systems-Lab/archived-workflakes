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
/**
 *
 * <p>Title: CooletJunctionInf</p>
 * <p>Description: interface for {@link psl.workflakes.coolets.CooletIncomingJunction CooletIncomingJunctions}</p>
 * <p>Copyright: Copyright (c) 2002: The Trustees of Columbia University in the
 *  City of New York, Peppo Valetto. All Rights Reserved.</p>
 * @author Peppo Valetto
 * @version 1.0
 * @see psl.workflakes.coolets.CooletIncomingJunction
 */
public interface CooletJunctionInf
{
        /**
         * method called by the hosting <code>WorkletPlugIn</code> for unconditional execution of
         * this <code>CooletIncomingJunction</code>
         */
	public void embeddedExec();
        /**
         * method called by the hosting <code>WorkletPlugIn</code> for  execution of
         * this <code>CooletIncomingJunction</code> as a response to a succesfull
         * <code>IncrementalSubscription</code>
         * @param sub the subscription associated to this <code>CooletIncomingJunction</code>
         */
	public void embeddedExec(IncrementalSubscription sub);
        /**
         * Convenience method to retrieve the Class of the predicate associated to this <code>CooletIncomingJunction</code>
         * @return the Class object for the predicate
         */
	public Class getPredClass();
        /**
         * retrieves the predicate associated to this <code>CooletIncomingJunction</code>
         * @return the predicate
         */
	public UnaryPredicate getPred();
        /**
         * Set a predicate that must be associated to this <code>CooletIncomingJunction</code>
         * @param pred the predicate
         */
	public void setPred(UnaryPredicate pred);
        /**
         * Accessor method for setting the Cluster ID of the hosting Cluster
         * @param cluster the Cluster ID
         */
	public void setClusterID (String cluster);
        /**
         * Accessor method for getting the Cluster ID of the hosting Cluster
         * @return cluster the Cluster ID
         */
	public String getClusterID();
        /**
         * Accessor method for setting the PlugIn ID of the hosting PlugIn
         * @param cluster the PlugIn ID
         */
	public void setPluginID (String plugin);
        /**
         * Accessor method for getting the PlugIn ID of the hosting PlugIn
         * @return cluster the PlugIn ID
         */
	public String getPluginID();
}
