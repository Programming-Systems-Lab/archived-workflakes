package AddressPackage;

public class PhoneNumber
{
	private int areaCode;
	private String  number;
	
	public PhoneNumber()
	{
		areaCode = 0;
		number = new String("");
	}
	
	public PhoneNumber(int areaCode, String number)
	{
		this.areaCode = areaCode;
		this.number   = number;
	}
	
	public void setAreaCode(int areaCode)
	{
		this.areaCode = areaCode;
	}
	
	public int getAreaCode()
	{
		return areaCode;
	}
	
	public void setNumber(String number)
	{
		this.number = number;
	}
	
	public String getNumber()
	{
		return number;
	}
	
	public String toString()
	{
		return "(" + areaCode + ") " + number;
	}
}
