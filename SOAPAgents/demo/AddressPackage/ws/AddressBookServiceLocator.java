/**
 * AddressBookServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package AddressPackage.ws;

public class AddressBookServiceLocator extends org.apache.axis.client.Service implements AddressPackage.ws.AddressBookService {

    // Use to get a proxy class for AddressBook
    private final java.lang.String AddressBook_address = "http://lasalle.psl.cs.columbia.edu:8080/axis/services/AddressBook";

    public java.lang.String getAddressBookAddress() {
        return AddressBook_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String AddressBookWSDDServiceName = "AddressBook";

    public java.lang.String getAddressBookWSDDServiceName() {
        return AddressBookWSDDServiceName;
    }

    public void setAddressBookWSDDServiceName(java.lang.String name) {
        AddressBookWSDDServiceName = name;
    }

    public AddressPackage.ws.AddressBook getAddressBook() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(AddressBook_address);
        }
        catch (java.net.MalformedURLException e) {
            return null; // unlikely as URL was validated in WSDL2Java
        }
        return getAddressBook(endpoint);
    }

    public AddressPackage.ws.AddressBook getAddressBook(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            AddressPackage.ws.AddressBookSoapBindingStub _stub = new AddressPackage.ws.AddressBookSoapBindingStub(portAddress, this);
            _stub.setPortName(getAddressBookWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (AddressPackage.ws.AddressBook.class.isAssignableFrom(serviceEndpointInterface)) {
                AddressPackage.ws.AddressBookSoapBindingStub _stub = new AddressPackage.ws.AddressBookSoapBindingStub(new java.net.URL(AddressBook_address), this);
                _stub.setPortName(getAddressBookWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        java.rmi.Remote _stub = getPort(serviceEndpointInterface);
        ((org.apache.axis.client.Stub) _stub).setPortName(portName);
        return _stub;
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("urn:AddressFetcher", "AddressBookService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("AddressBook"));
        }
        return ports.iterator();
    }

}
