package psl.workflakes.littlejil.xmlschema;

/*
 * This class was automatically generated with 
 * <a href="http://castor.exolab.org">Castor 0.9.3.9+</a>, using an
 * XML Schema.
 * $Id$
 */

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import org.exolab.castor.xml.*;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * 
 * 
 * @version $Revision$ $Date$
**/
public class Range implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private java.lang.String _lower;

    private java.lang.String _upper;


      //----------------/
     //- Constructors -/
    //----------------/

    public Range() {
        super();
    } //-- Range()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'lower'.
     * 
     * @return the value of field 'lower'.
    **/
    public java.lang.String getLower()
    {
        return this._lower;
    } //-- java.lang.String getLower() 

    /**
     * Returns the value of field 'upper'.
     * 
     * @return the value of field 'upper'.
    **/
    public java.lang.String getUpper()
    {
        return this._upper;
    } //-- java.lang.String getUpper() 

    /**
    **/
    public boolean isValid()
    {
        try {
            validate();
        }
        catch (org.exolab.castor.xml.ValidationException vex) {
            return false;
        }
        return true;
    } //-- boolean isValid() 

    /**
     * 
     * 
     * @param out
    **/
    public void marshal(java.io.Writer out)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, out);
    } //-- void marshal(java.io.Writer) 

    /**
     * 
     * 
     * @param handler
    **/
    public void marshal(org.xml.sax.ContentHandler handler)
        throws java.io.IOException, org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, handler);
    } //-- void marshal(org.xmlschema.sax.ContentHandler)

    /**
     * Sets the value of field 'lower'.
     * 
     * @param lower the value of field 'lower'.
    **/
    public void setLower(java.lang.String lower)
    {
        this._lower = lower;
    } //-- void setLower(java.lang.String) 

    /**
     * Sets the value of field 'upper'.
     * 
     * @param upper the value of field 'upper'.
    **/
    public void setUpper(java.lang.String upper)
    {
        this._upper = upper;
    } //-- void setUpper(java.lang.String) 

    /**
     * 
     * 
     * @param reader
    **/
    public static Range unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (Range) Unmarshaller.unmarshal(Range.class, reader);
    } //-- Range unmarshal(java.io.Reader) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
