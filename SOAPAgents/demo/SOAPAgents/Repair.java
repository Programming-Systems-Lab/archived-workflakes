package SOAPAgents;

import java.util.*;
import java.net.*;
import org.apache.axis.encoding.XMLType;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.utils.Options;
import javax.xml.rpc.ParameterMode;
import javax.xml.namespace.QName;
import AddressPackage.ws.Address;
import AddressPackage.ws.PhoneNumber;

/**
* This class sends the SOAP messages to the Web service
* @author Serafina Sumargo
*/

public class Repair{

	/**
	* This method sends SOAP messages to the web service with the
	* required parameters. It links the appropriate Bean serializer
	* and deserializer for any complex data type.
	* <p>
	* @param url the URL object for the url of the web service
	* @param methodName The method name
	* @param params The required parameters
	* @param classes The Class objects for the parameters
	* @param qnames The QNames for each parameter
	* @param uri The target uri of the web service
	* @param output The Class object for the return value
	* @param qnameOut The QName object for the return value
	* @param complex An array of Class objects for the complex data
	* type.
	* @return An object that could be the return value or an Exception
	*/

	public static Object activateRepair(URL url, String methodName, Object[] params, String[] paramName, 
Class[] classes, QName[] qnames, String uri, Class output, QName 
qnameOut, Class[] complex) 
{
		Object result = null;
		try{
			Service service = new Service();
			Call call = (Call)service.createCall();
			call.setTargetEndpointAddress(url);
			call.setOperationName(new QName(uri, methodName));
			call.setUseSOAPAction(true);
			call.setSOAPActionURI(uri+methodName);

			if(complex != null) {
			for(int i=0; i<complex.length; i++){
				String tmp = complex[i].getName();
				if(tmp.indexOf(".") != -1){
					StringTokenizer token = new StringTokenizer(tmp, ".", false);					
					for(int j=0; j<token.countTokens(); j++){
						tmp = token.nextToken();
					} 
					tmp = token.nextToken();
				}
	call.registerTypeMapping(complex[i], new QName(uri, tmp), org.apache.axis.encoding.ser.BeanSerializerFactory.class, org.apache.axis.encoding.ser.BeanDeserializerFactory.class);
				
			}
			}

			if(params != null){
				for(int i=0; i<params.length; i++){	
					call.addParameter(paramName[i], qnames[i], classes[i], javax.xml.rpc.ParameterMode.IN);
				}
			}

			if(qnameOut != null)
				call.setReturnType(qnameOut, output);
			result = call.invoke(params);
		}catch(Exception e){
			return new Exception("Error in Repair.class: "+e.getMessage());
		}

		return result;
	}
}
