/**
 * AddressBookService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package AddressPackage.ws;

public interface AddressBookService extends javax.xml.rpc.Service {
    public java.lang.String getAddressBookAddress();

    public AddressPackage.ws.AddressBook getAddressBook() throws javax.xml.rpc.ServiceException;

    public AddressPackage.ws.AddressBook getAddressBook(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
