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
import psl.workflakes.littlejil.xmlschema.types.ParameterBindingModeType;

/**
 * 
 * 
 * @version $Revision$ $Date$
**/
public class ParameterBinding implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private ParameterBindingModeType _mode;

    private java.lang.Object _inParent;

    private java.lang.Object _inChild;


      //----------------/
     //- Constructors -/
    //----------------/

    public ParameterBinding() {
        super();
    } //-- ParameterBinding()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'inChild'.
     * 
     * @return the value of field 'inChild'.
    **/
    public java.lang.Object getInChild()
    {
        return this._inChild;
    } //-- java.lang.Object getInChild() 

    /**
     * Returns the value of field 'inParent'.
     * 
     * @return the value of field 'inParent'.
    **/
    public java.lang.Object getInParent()
    {
        return this._inParent;
    } //-- java.lang.Object getInParent() 

    /**
     * Returns the value of field 'mode'.
     * 
     * @return the value of field 'mode'.
    **/
    public ParameterBindingModeType getMode()
    {
        return this._mode;
    } //-- types.ParameterBindingModeType getMode() 

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
     * Sets the value of field 'inChild'.
     * 
     * @param inChild the value of field 'inChild'.
    **/
    public void setInChild(java.lang.Object inChild)
    {
        this._inChild = inChild;
    } //-- void setInChild(java.lang.Object) 

    /**
     * Sets the value of field 'inParent'.
     * 
     * @param inParent the value of field 'inParent'.
    **/
    public void setInParent(java.lang.Object inParent)
    {
        this._inParent = inParent;
    } //-- void setInParent(java.lang.Object) 

    /**
     * Sets the value of field 'mode'.
     * 
     * @param mode the value of field 'mode'.
    **/
    public void setMode(ParameterBindingModeType mode)
    {
        this._mode = mode;
    } //-- void setMode(types.ParameterBindingModeType) 

    /**
     * 
     * 
     * @param reader
    **/
    public static ParameterBinding unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (ParameterBinding) Unmarshaller.unmarshal(ParameterBinding.class, reader);
    } //-- ParameterBinding unmarshal(java.io.Reader) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
