package psl.workflakes.coolets;

import org.cougaar.core.component.Service;

import psl.worklets.WVM;

public interface WklRegService
	extends Service {
		WVM getWVM();
		String getWVMName();
	}
	