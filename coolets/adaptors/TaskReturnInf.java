/*
 * <copyright>
 * Copyright (c) 2001: The Trustees of Columbia University in the
 *  City of New York, Peppo Valetto. All Rights Reserved.
 * </copyright>
 */
 package psl.workflakes.coolets.adaptors;

import org.cougaar.core.society.UniqueObject;
import org.cougaar.util.UnaryPredicate;
import org.cougaar.domain.planning.ldm.asset.Asset;
import org.cougaar.domain.planning.ldm.plan.Task;
import org.cougaar.domain.planning.ldm.plan.Allocation;

import psl.workflakes.coolets.assets.*;

/**
 *
 * <p>Title: TaskReturnInf </p>
 * <p>Description: Interface for
 * {@link psl.workflakes.coolets.TaskReturnJunction TaskReturnJunction}</p>
 * <p>Copyright: Copyright (c) 2002: The Trustees of Columbia University in the
 *  City of New York, Peppo Valetto. All Rights Reserved.</p>
 * @author Peppo Valetto
 * @version 1.0
 */
public interface TaskReturnInf {
	void failTask (Task t);
	void postAction (Allocation a, ExecAgentAsset executor);
	UniqueObject findByUID (UnaryPredicate pred);
}