/**
 * AddressBook.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package AddressPackage.ws;

public interface AddressBook extends java.rmi.Remote {
    public java.lang.String addEntry(java.lang.String in0, AddressPackage.ws.Address in1) throws java.rmi.RemoteException;
    public AddressPackage.ws.Address getAddressFromName(java.lang.String in0) throws java.rmi.RemoteException;
    public java.util.Vector getAllListings() throws java.rmi.RemoteException;
    public java.lang.String editName(java.lang.String in0, java.lang.String in1) throws java.rmi.RemoteException;
    public java.lang.String editAddress(java.lang.String in0, AddressPackage.ws.Address in1) throws java.rmi.RemoteException;
    public java.lang.String editPhone(java.lang.String in0, AddressPackage.ws.PhoneNumber in1) throws java.rmi.RemoteException;
    public java.lang.String editEmail(java.lang.String in0, java.lang.String in1) throws java.rmi.RemoteException;
    public java.lang.String removeAddress(java.lang.String in0) throws java.rmi.RemoteException;
}
