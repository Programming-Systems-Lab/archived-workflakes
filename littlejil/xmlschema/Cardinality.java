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
public class Cardinality implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private Range _range;

    private ParameterRef _parameterRef;


      //----------------/
     //- Constructors -/
    //----------------/

    public Cardinality() {
        super();
    } //-- Cardinality()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'parameterRef'.
     * 
     * @return the value of field 'parameterRef'.
    **/
    public ParameterRef getParameterRef()
    {
        return this._parameterRef;
    } //-- ParameterRef getParameterRef() 

    /**
     * Returns the value of field 'range'.
     * 
     * @return the value of field 'range'.
    **/
    public Range getRange()
    {
        return this._range;
    } //-- Range getRange() 

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
     * Sets the value of field 'parameterRef'.
     * 
     * @param parameterRef the value of field 'parameterRef'.
    **/
    public void setParameterRef(ParameterRef parameterRef)
    {
        this._parameterRef = parameterRef;
    } //-- void setParameterRef(ParameterRef) 

    /**
     * Sets the value of field 'range'.
     * 
     * @param range the value of field 'range'.
    **/
    public void setRange(Range range)
    {
        this._range = range;
    } //-- void setRange(Range) 

    /**
     * 
     * 
     * @param reader
    **/
    public static Cardinality unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (Cardinality) Unmarshaller.unmarshal(Cardinality.class, reader);
    } //-- Cardinality unmarshal(java.io.Reader) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
