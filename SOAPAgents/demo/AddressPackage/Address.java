package AddressPackage;

import AddressPackage.PhoneNumber;

public class Address
{
	private int         streetNum;
	private String      streetName;
	private String      city;
	private String      state;
	private int         zip;
	private PhoneNumber phoneNumber;
	private String email;
	
	public Address()
	{
		streetNum = 0;
		streetName = new String("");
		city = new String("");
		state = new String("");
		zip = 0;
		phoneNumber = new PhoneNumber();
		email = new String("");
	}
	
	
	
	public Address(int streetNum, String streetName, String city, String state,
				   int zip, PhoneNumber phoneNumber, String email)
	{
		this.streetNum = streetNum;
		this.streetName = streetName;
		this.city = city;
		this.state = state;
		this.zip = zip;
		this.phoneNumber = phoneNumber;
		this.email = email;
	}
	
	public void setStreetNum(int streetNum)
	{
		this.streetNum = streetNum;
	}
	
	public int getStreetNum()
	{
		return streetNum;
	}
	
	public void setStreetName(String streetName)
	{
		this.streetName = streetName;
	}
	
	public String getStreetName()
	{
		return streetName;
	}
	
	public void setCity(String city)
	{
		this.city = city;
	}
	
	public String getCity()
	{
		return city;
	}
	
	public void setState(String state)
	{
		this.state = state;
	}
	
	public String getState()
	{
		return state;
	}
	
	public void setZip(int zip)
	{
		this.zip = zip;
	}
	
	public int getZip()
	{
		return zip;
	}
	
	public void setPhoneNumber(PhoneNumber phoneNumber)
	{
		this.phoneNumber = phoneNumber;
	}
	
	public PhoneNumber getPhoneNumber()
	{
		return phoneNumber;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return email;
	}
	
	public String toString()
	{
		return streetNum + " " + streetName + "\n" +
			city + ", " + state + " " + zip + "\n" +
			phoneNumber + "\n" + email;
	}
}
