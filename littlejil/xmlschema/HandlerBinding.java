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
import psl.workflakes.littlejil.xmlschema.types.HandlerBindingModeType;

/**
 * 
 * 
 * @version $Revision$ $Date$
**/
public class HandlerBinding implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private HandlerBindingModeType _mode;

    private Trigger _trigger;

    private HandlerStep _handlerStep;

    private java.util.Vector _parameterBindingList;


      //----------------/
     //- Constructors -/
    //----------------/

    public HandlerBinding() {
        super();
        _parameterBindingList = new Vector();
    } //-- HandlerBinding()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vParameterBinding
    **/
    public void addParameterBinding(ParameterBinding vParameterBinding)
        throws java.lang.IndexOutOfBoundsException
    {
        _parameterBindingList.addElement(vParameterBinding);
    } //-- void addParameterBinding(ParameterBinding) 

    /**
     * 
     * 
     * @param index
     * @param vParameterBinding
    **/
    public void addParameterBinding(int index, ParameterBinding vParameterBinding)
        throws java.lang.IndexOutOfBoundsException
    {
        _parameterBindingList.insertElementAt(vParameterBinding, index);
    } //-- void addParameterBinding(int, ParameterBinding) 

    /**
    **/
    public java.util.Enumeration enumerateParameterBinding()
    {
        return _parameterBindingList.elements();
    } //-- java.util.Enumeration enumerateParameterBinding() 

    /**
     * Returns the value of field 'handlerStep'.
     * 
     * @return the value of field 'handlerStep'.
    **/
    public HandlerStep getHandlerStep()
    {
        return this._handlerStep;
    } //-- HandlerStep getHandlerStep() 

    /**
     * Returns the value of field 'mode'.
     * 
     * @return the value of field 'mode'.
    **/
    public HandlerBindingModeType getMode()
    {
        return this._mode;
    } //-- types.HandlerBindingModeType getMode() 

    /**
     * 
     * 
     * @param index
    **/
    public ParameterBinding getParameterBinding(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _parameterBindingList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (ParameterBinding) _parameterBindingList.elementAt(index);
    } //-- ParameterBinding getParameterBinding(int) 

    /**
    **/
    public ParameterBinding[] getParameterBinding()
    {
        int size = _parameterBindingList.size();
        ParameterBinding[] mArray = new ParameterBinding[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (ParameterBinding) _parameterBindingList.elementAt(index);
        }
        return mArray;
    } //-- ParameterBinding[] getParameterBinding() 

    /**
    **/
    public int getParameterBindingCount()
    {
        return _parameterBindingList.size();
    } //-- int getParameterBindingCount() 

    /**
     * Returns the value of field 'trigger'.
     * 
     * @return the value of field 'trigger'.
    **/
    public Trigger getTrigger()
    {
        return this._trigger;
    } //-- Trigger getTrigger() 

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
    public void removeAllParameterBinding()
    {
        _parameterBindingList.removeAllElements();
    } //-- void removeAllParameterBinding() 

    /**
     * 
     * 
     * @param index
    **/
    public ParameterBinding removeParameterBinding(int index)
    {
        java.lang.Object obj = _parameterBindingList.elementAt(index);
        _parameterBindingList.removeElementAt(index);
        return (ParameterBinding) obj;
    } //-- ParameterBinding removeParameterBinding(int) 

    /**
     * Sets the value of field 'handlerStep'.
     * 
     * @param handlerStep the value of field 'handlerStep'.
    **/
    public void setHandlerStep(HandlerStep handlerStep)
    {
        this._handlerStep = handlerStep;
    } //-- void setHandlerStep(HandlerStep) 

    /**
     * Sets the value of field 'mode'.
     * 
     * @param mode the value of field 'mode'.
    **/
    public void setMode(HandlerBindingModeType mode)
    {
        this._mode = mode;
    } //-- void setMode(types.HandlerBindingModeType) 

    /**
     * 
     * 
     * @param index
     * @param vParameterBinding
    **/
    public void setParameterBinding(int index, ParameterBinding vParameterBinding)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _parameterBindingList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _parameterBindingList.setElementAt(vParameterBinding, index);
    } //-- void setParameterBinding(int, ParameterBinding) 

    /**
     * 
     * 
     * @param parameterBindingArray
    **/
    public void setParameterBinding(ParameterBinding[] parameterBindingArray)
    {
        //-- copy array
        _parameterBindingList.removeAllElements();
        for (int i = 0; i < parameterBindingArray.length; i++) {
            _parameterBindingList.addElement(parameterBindingArray[i]);
        }
    } //-- void setParameterBinding(ParameterBinding) 

    /**
     * Sets the value of field 'trigger'.
     * 
     * @param trigger the value of field 'trigger'.
    **/
    public void setTrigger(Trigger trigger)
    {
        this._trigger = trigger;
    } //-- void setTrigger(Trigger) 

    /**
     * 
     * 
     * @param reader
    **/
    public static HandlerBinding unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (HandlerBinding) Unmarshaller.unmarshal(HandlerBinding.class, reader);
    } //-- HandlerBinding unmarshal(java.io.Reader) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
