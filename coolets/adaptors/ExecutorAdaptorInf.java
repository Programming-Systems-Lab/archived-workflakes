package psl.workflakes.coolets.adaptors;

/**
 * <p>Title: ExecutorAdaptorInf</p>
 * <p>Description: Base interface for Executor Plugins</p>
 * <p>Copyright: Copyright (c) 2002: The Trustees of Columbia University in the
 *  City of New York, Peppo Valetto. All Rights Reserved.</p>
 * @author Peppo Valetto
 * @version 1.0
 */

 import psl.workflakes.coolets.assets.ExecAgentAsset;
 import org.cougaar.domain.planning.ldm.plan.Allocation;

public interface ExecutorAdaptorInf {
  /**
   * Take care to instantiate a Worklet with a WEJ and send it out to its target.
   * @param a indicates the Task to be fulfilled and all its parameters, ncluding the target
   * for the Worklet
   * @param executor indicates the WEJ to be dispatched
   */
  public void dispatchAction (Allocation a, ExecAgentAsset executor);
}