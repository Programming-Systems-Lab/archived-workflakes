/**
 * AddressBookSoapBindingImpl.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package AddressPackage.ws;

import AddressPackage.AddressBook;

public class AddressBookSoapBindingImpl implements AddressPackage.ws.AddressBook{

	AddressBook book = new AddressBook();

    public java.lang.String addEntry(java.lang.String in0, AddressPackage.ws.Address in1) throws java.rmi.RemoteException {
        return book.addEntry(in0, in1);
    }

    public AddressPackage.ws.Address getAddressFromName(java.lang.String in0) throws java.rmi.RemoteException {
        return book.getAddressFromName(in0);
    }

    public java.util.Vector getAllListings() throws java.rmi.RemoteException {
        return book.getAllListings();
    }

    public java.lang.String editName(java.lang.String in0, java.lang.String in1) throws java.rmi.RemoteException {
        return book.editName(in0, in1);
    }

    public java.lang.String editAddress(java.lang.String in0, AddressPackage.ws.Address in1) throws java.rmi.RemoteException {
        return book.editAddress(in0, in1);
    }

    public java.lang.String editPhone(java.lang.String in0, AddressPackage.ws.PhoneNumber in1) throws java.rmi.RemoteException {
        return book.editPhone(in0, in1);
    }

    public java.lang.String editEmail(java.lang.String in0, java.lang.String in1) throws java.rmi.RemoteException {
        return book.editEmail(in0, in1);
    }

    public java.lang.String removeAddress(java.lang.String in0) throws java.rmi.RemoteException {
        return book.removeAddress(in0);
    }

}
