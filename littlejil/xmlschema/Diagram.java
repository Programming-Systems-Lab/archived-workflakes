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
public class Diagram implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private java.lang.Object _root;

    private java.lang.String _id;

    private java.util.Vector _stepList;


      //----------------/
     //- Constructors -/
    //----------------/

    public Diagram() {
        super();
        _stepList = new Vector();
    } //-- Diagram()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vStep
    **/
    public void addStep(Step vStep)
        throws java.lang.IndexOutOfBoundsException
    {
        _stepList.addElement(vStep);
    } //-- void addStep(Step) 

    /**
     * 
     * 
     * @param index
     * @param vStep
    **/
    public void addStep(int index, Step vStep)
        throws java.lang.IndexOutOfBoundsException
    {
        _stepList.insertElementAt(vStep, index);
    } //-- void addStep(int, Step) 

    /**
    **/
    public java.util.Enumeration enumerateStep()
    {
        return _stepList.elements();
    } //-- java.util.Enumeration enumerateStep() 

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
     * Returns the value of field 'root'.
     * 
     * @return the value of field 'root'.
    **/
    public java.lang.Object getRoot()
    {
        return this._root;
    } //-- java.lang.Object getRoot() 

    /**
     * 
     * 
     * @param index
    **/
    public Step getStep(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _stepList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (Step) _stepList.elementAt(index);
    } //-- Step getStep(int) 

    /**
    **/
    public Step[] getStep()
    {
        int size = _stepList.size();
        Step[] mArray = new Step[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (Step) _stepList.elementAt(index);
        }
        return mArray;
    } //-- Step[] getStep() 

    /**
    **/
    public int getStepCount()
    {
        return _stepList.size();
    } //-- int getStepCount() 

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
    public void removeAllStep()
    {
        _stepList.removeAllElements();
    } //-- void removeAllStep() 

    /**
     * 
     * 
     * @param index
    **/
    public Step removeStep(int index)
    {
        java.lang.Object obj = _stepList.elementAt(index);
        _stepList.removeElementAt(index);
        return (Step) obj;
    } //-- Step removeStep(int) 

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
     * Sets the value of field 'root'.
     * 
     * @param root the value of field 'root'.
    **/
    public void setRoot(java.lang.Object root)
    {
        this._root = root;
    } //-- void setRoot(java.lang.Object) 

    /**
     * 
     * 
     * @param index
     * @param vStep
    **/
    public void setStep(int index, Step vStep)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _stepList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _stepList.setElementAt(vStep, index);
    } //-- void setStep(int, Step) 

    /**
     * 
     * 
     * @param stepArray
    **/
    public void setStep(Step[] stepArray)
    {
        //-- copy array
        _stepList.removeAllElements();
        for (int i = 0; i < stepArray.length; i++) {
            _stepList.addElement(stepArray[i]);
        }
    } //-- void setStep(Step) 

    /**
     * 
     * 
     * @param reader
    **/
    public static Diagram unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (Diagram) Unmarshaller.unmarshal(Diagram.class, reader);
    } //-- Diagram unmarshal(java.io.Reader) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
