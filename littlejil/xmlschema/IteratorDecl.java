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
public class IteratorDecl implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private java.lang.String _id;

    private InterfaceDecl _interfaceDecl;

    private ExternalObject _externalObject;


      //----------------/
     //- Constructors -/
    //----------------/

    public IteratorDecl() {
        super();
    } //-- IteratorDecl()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'externalObject'.
     * 
     * @return the value of field 'externalObject'.
    **/
    public ExternalObject getExternalObject()
    {
        return this._externalObject;
    } //-- ExternalObject getExternalObject() 

    /**
     * Returns the value of field 'id'.
     * 
     * @return the value of field 'id'.
    **/
    public java.lang.String getId()
    {
        return this._id;
    } //-- java.lang.String getId() 

    /**
     * Returns the value of field 'interfaceDecl'.
     * 
     * @return the value of field 'interfaceDecl'.
    **/
    public InterfaceDecl getInterfaceDecl()
    {
        return this._interfaceDecl;
    } //-- InterfaceDecl getInterfaceDecl() 

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
     * Sets the value of field 'externalObject'.
     * 
     * @param externalObject the value of field 'externalObject'.
    **/
    public void setExternalObject(ExternalObject externalObject)
    {
        this._externalObject = externalObject;
    } //-- void setExternalObject(ExternalObject) 

    /**
     * Sets the value of field 'id'.
     * 
     * @param id the value of field 'id'.
    **/
    public void setId(java.lang.String id)
    {
        this._id = id;
    } //-- void setId(java.lang.String) 

    /**
     * Sets the value of field 'interfaceDecl'.
     * 
     * @param interfaceDecl the value of field 'interfaceDecl'.
    **/
    public void setInterfaceDecl(InterfaceDecl interfaceDecl)
    {
        this._interfaceDecl = interfaceDecl;
    } //-- void setInterfaceDecl(InterfaceDecl) 

    /**
     * 
     * 
     * @param reader
    **/
    public static IteratorDecl unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (IteratorDecl) Unmarshaller.unmarshal(IteratorDecl.class, reader);
    } //-- IteratorDecl unmarshal(java.io.Reader) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
