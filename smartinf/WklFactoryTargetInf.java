/*
 * <copyright>
 * Copyright (c) 2001: The Trustees of Columbia University in the
 *  City of New York, Peppo Valetto. All Rights Reserved.
 * </copyright>
 */
 package psl.workflakes.smartinf;

import java.net.URL;
import psl.worklets.Worklet;
import psl.worklets.WorkletJunction;
import psl.workflakes.smartinf.smartjunction.WklRequestJunction;
//import psl.workflakes.smartinf.smartjunction.FactoryWklJunction;

public interface WklFactoryTargetInf
	extends WklTargetInf
{
    public void requireDeployment (Worklet w);
    public void processRequestWorklet(WklRequestJunction request);
    public Worklet assembleWorklet (WorkletJunction[] junctions);
    public Worklet assembleWorklet (WorkletJunction aJunction);
    public WorkletJunction produceJunction(Class c, String host, String agent);
    public WorkletJunction getJunctionByURL (URL anURL, String host, String agent);
    public String getWVMHost();
    public String getWVMID();
    public WklRequestJunction retrieveRequest(String code);
    public void removeRequest(String code);
}
