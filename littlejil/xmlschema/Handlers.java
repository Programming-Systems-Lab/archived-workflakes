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
import java.util.Enumeration;
import java.util.Vector;
import org.exolab.castor.xml.*;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * 
 * 
 * @version $Revision$ $Date$
**/
public class Handlers implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private java.util.Vector _handlerBindingList;


      //----------------/
     //- Constructors -/
    //----------------/

    public Handlers() {
        super();
        _handlerBindingList = new Vector();
    } //-- Handlers()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vHandlerBinding
    **/
    public void addHandlerBinding(HandlerBinding vHandlerBinding)
        throws java.lang.IndexOutOfBoundsException
    {
        _handlerBindingList.addElement(vHandlerBinding);
    } //-- void addHandlerBinding(HandlerBinding) 

    /**
     * 
     * 
     * @param index
     * @param vHandlerBinding
    **/
    public void addHandlerBinding(int index, HandlerBinding vHandlerBinding)
        throws java.lang.IndexOutOfBoundsException
    {
        _handlerBindingList.insertElementAt(vHandlerBinding, index);
    } //-- void addHandlerBinding(int, HandlerBinding) 

    /**
    **/
    public java.util.Enumeration enumerateHandlerBinding()
    {
        return _handlerBindingList.elements();
    } //-- java.util.Enumeration enumerateHandlerBinding() 

    /**
     * 
     * 
     * @param index
    **/
    public HandlerBinding getHandlerBinding(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _handlerBindingList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (HandlerBinding) _handlerBindingList.elementAt(index);
    } //-- HandlerBinding getHandlerBinding(int) 

    /**
    **/
    public HandlerBinding[] getHandlerBinding()
    {
        int size = _handlerBindingList.size();
        HandlerBinding[] mArray = new HandlerBinding[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (HandlerBinding) _handlerBindingList.elementAt(index);
        }
        return mArray;
    } //-- HandlerBinding[] getHandlerBinding() 

    /**
    **/
    public int getHandlerBindingCount()
    {
        return _handlerBindingList.size();
    } //-- int getHandlerBindingCount() 

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
    **/
    public void removeAllHandlerBinding()
    {
        _handlerBindingList.removeAllElements();
    } //-- void removeAllHandlerBinding() 

    /**
     * 
     * 
     * @param index
    **/
    public HandlerBinding removeHandlerBinding(int index)
    {
        java.lang.Object obj = _handlerBindingList.elementAt(index);
        _handlerBindingList.removeElementAt(index);
        return (HandlerBinding) obj;
    } //-- HandlerBinding removeHandlerBinding(int) 

    /**
     * 
     * 
     * @param index
     * @param vHandlerBinding
    **/
    public void setHandlerBinding(int index, HandlerBinding vHandlerBinding)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _handlerBindingList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _handlerBindingList.setElementAt(vHandlerBinding, index);
    } //-- void setHandlerBinding(int, HandlerBinding) 

    /**
     * 
     * 
     * @param handlerBindingArray
    **/
    public void setHandlerBinding(HandlerBinding[] handlerBindingArray)
    {
        //-- copy array
        _handlerBindingList.removeAllElements();
        for (int i = 0; i < handlerBindingArray.length; i++) {
            _handlerBindingList.addElement(handlerBindingArray[i]);
        }
    } //-- void setHandlerBinding(HandlerBinding) 

    /**
     * 
     * 
     * @param reader
    **/
    public static Handlers unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (Handlers) Unmarshaller.unmarshal(Handlers.class, reader);
    } //-- Handlers unmarshal(java.io.Reader) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
