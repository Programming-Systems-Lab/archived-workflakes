/*
 * <copyright>
 * Copyright (c) 2001: The Trustees of Columbia University in the
 *  City of New York, Peppo Valetto. All Rights Reserved.
 * </copyright>
 */
 package psl.workflakes.smartinf;

import java.util.Hashtable;
import psl.worklets.Worklet;
import psl.workflakes.smartinf.smartjunction.*;

public interface WklOracleTargetInf
	extends WklTargetInf
{
	public Class[] matchJunctions (Class target, String policy);
	public void sortJunctions (Class[] jArray, Hashtable ht);
	public String getWVMHost();
	public String getWVMID();
	public void storeRequestStructure (String str);
	public void requireDeployment (Worklet wkl);
}
