package SOAPAgents;

import java.net.*;
import java.util.*;
import java.net.*;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLReader;
import javax.wsdl.Definition;
import javax.wsdl.Types;  
import javax.wsdl.Message;
import javax.wsdl.Part;
import javax.wsdl.PortType;
import javax.wsdl.Operation;
import javax.xml.namespace.QName;
import com.ibm.wsdl.*;

/**
* This class parses a given WSDL file and retrieves information like
* the method names available, the parameters of the methods and the
* return types.
* @author Serafina Sumargo
*/

public class WSDLParser{
	WSDLFactory factory;
	WSDLReader reader;
	String url;
	Definition wsdlInstance;
	int portIndex;
	String uri;
	String[] methodNames;
	ArrayList paramInput;
	ArrayList paramOutput;

	/**
	* Contructor
	* <p>
	* @param url URL of the WSDL file to parse
	*/
	public WSDLParser(String url) throws Exception{
		factory = WSDLFactory.newInstance();
		reader = factory.newWSDLReader();
		this.url = url; //url of wsdl file
		wsdlInstance = reader.readWSDL(null, url);
		uri = getTargetURI();
		portIndex = getPortIndex();
		methodNames = getMethodNames();
		paramInput = new ArrayList(methodNames.length);
		paramOutput = new ArrayList(methodNames.length);
		for(int i=0; i<methodNames.length; i++){
			paramInput.add(new ArrayList());
			getInput(methodNames[i], i);
			paramOutput.add(new ArrayList());
			getOutput(methodNames[i], i);
		}
	}

	/**
	* This method retrieves the type name from a Part
	* <p>
	* @param part A Part object
	* @return The Type name in QName format
	* @exception Exception
	*/

	public  QName getParamType(Object part) throws Exception{
		return ((Part)part).getTypeName();
	}

	/**
	* This method retrieves the name of the parameter from a Part
	* <p>
	* @param part A Part object
	* @return The parameter name in String format
	* @exception Exception
	*/

	public String getParamName(Object part) throws Exception{
		return ((Part)part).getName();
	}


	/**
	* This method retrieves all of input parameter names
	* required by the specified method
	* <p>
	* @param methodName The method name
	* @return An Array containing all the parameter names
	* @exception Exception
	*/

	public String[] getInputParamNames(String methodName) throws Exception{
		ArrayList array = getParamInput(methodName);
		String[] list = new String[array.size()];
		for(int i=0; i<array.size(); i++){
			list[i] = getParamName(array.get(i));
		}

		return list;
	}

	/**
	* This method retrieves all of the input parameter types of the
	* specified method
	* <p>
	* @param methodName The method name
	* @return An array of parameter types in QName format
	* @exception Exception
	*/

	public QName[] getInputParamType(String methodName) throws Exception{
		ArrayList parts = getParamInput(methodName);
		QName[] list = new QName[parts.size()];
		for(int i=0; i<parts.size(); i++){
			list[i] = getParamType(parts.get(i));
		}

		return list;
	}

	/**
	* This method retrieves the returned parameter name
	* <p>
	* @param methodName The method name
	* @return The name of the returned parameter
	* @exception Exception
	*/

	public String getOutputParamName(String methodName) throws Exception{
		ArrayList part = getParamOutput(methodName);
		if(part == null || part.size() == 0)
			return null;
		return getParamName(part.get(0));
	}

	/**
	* This method retrieves the returned parameter type
	* <p>
	* @param methodName The method name
	* @return The return parameter type in QName format
	* @exception Exception
	*/

	public QName getOutputParamType(String methodName) throws Exception{
		ArrayList part = getParamOutput(methodName);
		if(part == null || part.size() == 0)
			return null;

		return getParamType(part.get(0));
	}

	/**
	* This method retrieves all the returned parameter information
	* <p>
	* @param methodName The method name
	* @return An ArrayList of Parts
	* @exception Exception
	*/

	public ArrayList getParamOutput(String method) throws Exception{
		for(int i=0; i<methodNames.length; i++){
			if(methodNames[i].equals(method)){
				return (ArrayList)paramOutput.get(i);
			}
		}
		return null;
	}

	private ArrayList getParamInput(int index) throws Exception{
		return (ArrayList)paramInput.get(index);
	}

	/**
	* This method retrieves all of the method's input parameter
	* information
	* <p>
	* @param method The method name
	* @return An ArrayList of Parts
	* @exception Exception
	*/

	public ArrayList getParamInput(String method) throws Exception{
		for(int i=0; i<methodNames.length; i++){	
			if(methodNames[i].equals(method)){
				return getParamInput(i);
			}
		}

		return null;
	}

	/**
	* This method retrieves all of the method name in the 
	* web service
	* <p>
	* @return An array of method names
	* @exception Exception
	*/

	public String[] getAllMethodNames() throws Exception{
		return methodNames;
	}

	/**
	* This method retrieves the target URI of the web service
	* <p>
	* @return The target uri
	* @exception Exception
	*/

	public String getURI() throws Exception{
		return uri;
	}

	private Message getMessage(String req) throws Exception{
		return wsdlInstance.getMessage(new QName(uri, req));
	}

	private String getTargetURI() throws Exception{
		return wsdlInstance.getTargetNamespace();
	}

	private Part[] getParts(Message message) throws Exception{
		if(message.getParts() == null)
			return null;

		Object[] list = message.getParts().values().toArray();
		Part[] parts = new Part[list.length];

		for(int i=0; i<list.length; i++){
			PartImpl tmp = (PartImpl)list[i];
			parts[i] = (Part)tmp;
		}

		return parts;
	}

       private PortType[] getPortTypes() throws Exception{
                Object[] list = wsdlInstance.getPortTypes().values().toArray();
                PortType[] list3 = new PortType[list.length];
                for(int i=0; i<list.length; i++){
                        PortTypeImpl list2 = (PortTypeImpl)list[i];
                        list3[i] = (PortType)list2;
                }
 
                return list3;
        }

        private Operation[] getOperations(PortType port) throws Exception{
                Object[] list = port.getOperations().toArray();
                Operation[] ops = new Operation[list.length];
                for(int i=0; i<list.length; i++){
                        OperationImpl tmp = (OperationImpl)list[i];
                        ops[i] = (Operation)tmp;
                }
                return ops;
        }

	private int getPortIndex() throws Exception{
                PortType[] ports = getPortTypes();
                for(int i=0; i<ports.length; i++){
                        if(ports[i].getQName().getLocalPart().endsWith("HttpGet") ||
ports[i].getQName().getLocalPart().endsWith("HttpPost")){
                                continue;
                        }else{
				return i;
			}
                }

		return -1;

	}


        private String[] getMethodNames() throws Exception{
               PortType[] ports = getPortTypes();
		int i = portIndex;

		if(i==-1)
			throw new Exception("Invalid Port Type!");

                Operation[] ops = getOperations(ports[i]);
                String[] names = new String[ops.length];  
                for(int j=0; j<ops.length; j++){
                        names[j] = ops[j].getName();
                }
                 
                return names;
        }

        private void getInput(String method, int index) throws Exception{
                String req = getRequest(method);
                Part[] parts = getParts(getMessage(req));

		if(parts != null) {
	
       	       	  for(int i=0; i<parts.length; i++){
			((ArrayList)paramInput.get(index)).add(parts[i]);
               	 }
		}
        }	

	private void getOutput(String method, int index) throws Exception{
		String res = getResponse(method);
		Part[] parts = getParts(getMessage(res));

		if(parts != null){
			for(int i=0; i<parts.length; i++){
				((ArrayList)paramOutput.get(index)).add(parts[i]);
			}
		}
	}

	private String getResponse(String method) throws Exception{
		PortType[] ports = getPortTypes();
		int i = portIndex;
		if(i==-1)
			throw new Exception("Invalid Port Type!");

		Operation[] ops = getOperations(ports[i]);
		i=0;
		for(i=0; i<ops.length; i++){
			if(ops[i].getName().equals(method)){
				if(ops[i].getOutput().getName() != null){
					return ops[i].getOutput().getName();
				}
				String tmp = ops[i].getOutput().getMessage().getQName().getLocalPart();
				if(tmp != null){
					return tmp;
				}
			}
		}

		return null;
	}

       private String getRequest(String method) throws Exception{
               PortType[] ports = getPortTypes();
                int i = portIndex;

		if(i == -1)
			throw new Exception("Invalid Port Type!");

                Operation[] ops = getOperations(ports[i]);
                i=0;
                for(i=0; i<ops.length; i++){
                        if(ops[i].getName().equals(method)){
				if(ops[i].getInput().getName() != null)
	                                return ops[i].getInput().getName();

				String tmp = ops[i].getInput().getMessage().getQName().getLocalPart();
				if(tmp != null){
					return tmp;
				}
                        }
                }
                 
		return null;                 
        }      
}
