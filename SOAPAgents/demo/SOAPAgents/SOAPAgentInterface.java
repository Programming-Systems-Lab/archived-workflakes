package SOAPAgents;

import java.rmi.*;
import java.rmi.server.*;

/**
* @author Serafina Sumargo
*/
public interface SOAPAgentInterface extends java.rmi.Remote{

	/**
	* This method takes in the url to the webservice and
	* returns an array of Strings with all available methods
	* <p>
	* @param url Url to the web service
	* @return An array of method names
	* @exception Throws RemoteException due to RMI Exceptions
	*/

	public String[] getMethods(String url) throws RemoteException;


	/**
	* This method is called to execute an operation on a web service.
	* <p>
	* @param string_url The url to the web service
	* @param methodName The operation you are trying to operate
	* @param params All of the Parameters required for that method
	* in the right order.
	* @param classes An array of Class objects that represents the
	* Class objects of the parameters
	* @param outputClass The Class of the return value of that method
	* @param complex An array of Class object that contains all 
	* possible Complex data type
	* @return An Object that could be of Exception or any return type
	* expected
	* @exception Throws a RemoteException due to RMI Exceptions
	*/
 
	public Object repair(String string_url, String methodName, Object[] params, Class[] classes, Class 
outputClass, Class[] complex) throws RemoteException; 
}
