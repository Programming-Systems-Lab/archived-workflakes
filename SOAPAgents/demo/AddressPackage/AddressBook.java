package AddressPackage;

import java.util.*;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import org.apache.soap.util.xml.*;
import AddressPackage.ws.Address;
import AddressPackage.ws.PhoneNumber;

public class AddressBook
{
	private static Hashtable name2AddressTable = new Hashtable();
	
	public AddressBook()
	{
	}
	
	public String addEntry(String name, Address address)
	{
		name2AddressTable.put(name, address);
		return new String(name +" has been added to Address Book! size: "+name2AddressTable.size());
	}
	
	public Address getAddressFromName(String name)
		throws IllegalArgumentException
	{
		if (name == null)
			{
				throw new IllegalArgumentException("The name argument must not be " +
												   "null.");
			}
		
		return (Address)name2AddressTable.get(name);
	}

	public Vector getAllListings(){
		Vector vec = new Vector();

		for(Enumeration keys = name2AddressTable.keys(); keys.hasMoreElements();){
			String string = new String("");
			String name = (String)keys.nextElement();
			string=name+"\n"+(Address)name2AddressTable.get(name)+"\n";
			vec.add(string);
		}
		return vec;
	}

	public String editName(String name, String newName){
		if(!name2AddressTable.containsKey(name))
			return new String(name+" not found in Address Book");

		Address add = (Address)name2AddressTable.remove(name);
		addEntry(newName, add);

		return new String(name+" has been changed to "+newName);
	}

	public String editAddress(String name, Address newAdd){
		if(!name2AddressTable.containsKey(name))
			return new String(name+" not found in Address Book");

		Address add = (Address)name2AddressTable.remove(name);
		newAdd.setPhoneNumber(add.getPhoneNumber());
		newAdd.setEmail(add.getEmail());
		addEntry(name, newAdd);

		return new String("Address has been changed");
	}

	public String editPhone(String name, PhoneNumber phone){
		if(!name2AddressTable.containsKey(name))
			return new String(name+" not found in Address Book SIZE: "+name2AddressTable.size());

		Address add = (Address)name2AddressTable.get(name);
		add.setPhoneNumber(phone);

		return new String("Phone Number has been changed");
	}

	public String editEmail(String name, String email){
		if(!name2AddressTable.containsKey(name))
			return new String(name+" not found in Address Book");

		Address add = (Address)name2AddressTable.get(name);
		add.setEmail(email);

		return new String("Email has been changed");
	}

	public String removeAddress(String name){
		if(!name2AddressTable.containsKey(name))
			return new String(name+" not found in Address Book");

		Address add = (Address)name2AddressTable.remove(name);

		return new String("Address has been removed");
	}
}
