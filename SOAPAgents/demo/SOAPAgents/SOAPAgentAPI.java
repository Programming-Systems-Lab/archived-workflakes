package SOAPAgents;

import java.net.*;
import java.util.*;
import java.net.*;
import org.w3c.dom.*;
import org.apache.soap.util.xml.*;
import org.apache.soap.*;
import org.apache.soap.encoding.*;
import org.apache.soap.encoding.soapenc.*;
import org.apache.soap.rpc.*;
import javax.xml.namespace.QName;
import java.rmi.*;
import java.rmi.server.*;
import AddressPackage.ws.Address;
import AddressPackage.ws.PhoneNumber;

/**
* This is the main API for the Web Service client.
* This class implements the SOAPAgentInterface
* @author Serafina Sumargo
*/

public class SOAPAgentAPI extends UnicastRemoteObject implements SOAPAgentInterface{

	/**
	* The main contructor
	*/

	public SOAPAgentAPI() throws RemoteException{
		super();
	}

	/**
	* This method retrieves all of the methods available at
	* the specified web service
	* <p>
	* @param url The url of the web service
	* @return An array of method names
	* @exception RemoteException
	*/

	public String[] getMethods(String url) throws RemoteException{
		try{
		//reading wsdl here...
                String wsdlFile = url+"?wsdl";
                
                WSDLParser parser = new WSDLParser(wsdlFile);
		return parser.getAllMethodNames();
		}catch(Exception e){
			System.err.println(e.getMessage());	
		}

		return null;
	}

	/**
	* This method invokes the Repair class to start execution of the
	* requested operation
	* <p>
	* @param string_url The url of the web service
	* @param methodName The operation name
	* @param params An array of parameters that is required by the
	* method
	* @param classes An array of Class objects that represents the
	* Class objects of the required parameter in the same order
	* @param outputClass A Class object for the return value
	* @param complex An array of Class objects that are considered
	* to be of complex data type
	* @return An object that could be the return value or an Exception
	* @exception RemoteException
	*/

	public Object repair(String string_url, String methodName, Object[] params, Class[] classes, Class 
outputClass, Class[] complex) throws RemoteException{
		URL url = null;
		String uri = null;
		String[] paramName = null;
		QName[] paramType = null;
		QName outputType = null;
		try{
		url = new URL(string_url);
		
		//reading wsdl here...
		String wsdlFile = url+"?wsdl";

		WSDLParser parser = new WSDLParser(wsdlFile);

		uri = parser.getURI();
	
		if(params != null) {
			paramName = parser.getInputParamNames(methodName);
			paramType = parser.getInputParamType(methodName);		
		}
		outputType = parser.getOutputParamType(methodName);

		}catch(Exception e){
			return new Exception("Error in RepairInterface: "+e.getMessage());
		}
		return Repair.activateRepair(url, methodName, params, paramName, classes, paramType, uri, outputClass, outputType, complex);


	}
}
