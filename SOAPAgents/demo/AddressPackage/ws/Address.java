/**
 * Address.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package AddressPackage.ws;

public class Address  implements java.io.Serializable {
    private java.lang.String city;
    private java.lang.String email;
    private AddressPackage.ws.PhoneNumber phoneNumber;
    private java.lang.String state;
    private java.lang.String streetName;
    private int streetNum;
    private int zip;

    public Address() {
    }

	public String toString(){
		String string = new String("");
		string+=getStreetNum()+" "+getStreetName()+"\n";
		string+=getCity()+", "+getState()+" "+getZip()+"\n";
		string+=getPhoneNumber().toString();
		string+=getEmail()+"\n";
		return string;
	}

    public java.lang.String getCity() {
        return city;
    }

    public void setCity(java.lang.String city) {
        this.city = city;
    }

    public java.lang.String getEmail() {
        return email;
    }

    public void setEmail(java.lang.String email) {
        this.email = email;
    }

    public AddressPackage.ws.PhoneNumber getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(AddressPackage.ws.PhoneNumber phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public java.lang.String getState() {
        return state;
    }

    public void setState(java.lang.String state) {
        this.state = state;
    }

    public java.lang.String getStreetName() {
        return streetName;
    }

    public void setStreetName(java.lang.String streetName) {
        this.streetName = streetName;
    }

    public int getStreetNum() {
        return streetNum;
    }

    public void setStreetNum(int streetNum) {
        this.streetNum = streetNum;
    }

    public int getZip() {
        return zip;
    }

    public void setZip(int zip) {
        this.zip = zip;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Address)) return false;
        Address other = (Address) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((city==null && other.getCity()==null) || 
             (city!=null &&
              city.equals(other.getCity()))) &&
            ((email==null && other.getEmail()==null) || 
             (email!=null &&
              email.equals(other.getEmail()))) &&
            ((phoneNumber==null && other.getPhoneNumber()==null) || 
             (phoneNumber!=null &&
              phoneNumber.equals(other.getPhoneNumber()))) &&
            ((state==null && other.getState()==null) || 
             (state!=null &&
              state.equals(other.getState()))) &&
            ((streetName==null && other.getStreetName()==null) || 
             (streetName!=null &&
              streetName.equals(other.getStreetName()))) &&
            streetNum == other.getStreetNum() &&
            zip == other.getZip();
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getCity() != null) {
            _hashCode += getCity().hashCode();
        }
        if (getEmail() != null) {
            _hashCode += getEmail().hashCode();
        }
        if (getPhoneNumber() != null) {
            _hashCode += getPhoneNumber().hashCode();
        }
        if (getState() != null) {
            _hashCode += getState().hashCode();
        }
        if (getStreetName() != null) {
            _hashCode += getStreetName().hashCode();
        }
        _hashCode += getStreetNum();
        _hashCode += getZip();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Address.class);

    static {
        org.apache.axis.description.FieldDesc field = new org.apache.axis.description.ElementDesc();
        field.setFieldName("city");
        field.setXmlName(new javax.xml.namespace.QName("", "city"));
        field.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(field);
        field = new org.apache.axis.description.ElementDesc();
        field.setFieldName("email");
        field.setXmlName(new javax.xml.namespace.QName("", "email"));
        field.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(field);
        field = new org.apache.axis.description.ElementDesc();
        field.setFieldName("phoneNumber");
        field.setXmlName(new javax.xml.namespace.QName("", "phoneNumber"));
        field.setXmlType(new javax.xml.namespace.QName("urn:AddressFetcher", "PhoneNumber"));
        typeDesc.addFieldDesc(field);
        field = new org.apache.axis.description.ElementDesc();
        field.setFieldName("state");
        field.setXmlName(new javax.xml.namespace.QName("", "state"));
        field.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(field);
        field = new org.apache.axis.description.ElementDesc();
        field.setFieldName("streetName");
        field.setXmlName(new javax.xml.namespace.QName("", "streetName"));
        field.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(field);
        field = new org.apache.axis.description.ElementDesc();
        field.setFieldName("streetNum");
        field.setXmlName(new javax.xml.namespace.QName("", "streetNum"));
        field.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        typeDesc.addFieldDesc(field);
        field = new org.apache.axis.description.ElementDesc();
        field.setFieldName("zip");
        field.setXmlName(new javax.xml.namespace.QName("", "zip"));
        field.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        typeDesc.addFieldDesc(field);
    };

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
