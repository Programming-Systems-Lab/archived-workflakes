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
public class Substeps implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private java.util.Vector _substepBindingList;


      //----------------/
     //- Constructors -/
    //----------------/

    public Substeps() {
        super();
        _substepBindingList = new Vector();
    } //-- Substeps()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vSubstepBinding
    **/
    public void addSubstepBinding(SubstepBinding vSubstepBinding)
        throws java.lang.IndexOutOfBoundsException
    {
        _substepBindingList.addElement(vSubstepBinding);
    } //-- void addSubstepBinding(SubstepBinding) 

    /**
     * 
     * 
     * @param index
     * @param vSubstepBinding
    **/
    public void addSubstepBinding(int index, SubstepBinding vSubstepBinding)
        throws java.lang.IndexOutOfBoundsException
    {
        _substepBindingList.insertElementAt(vSubstepBinding, index);
    } //-- void addSubstepBinding(int, SubstepBinding) 

    /**
    **/
    public java.util.Enumeration enumerateSubstepBinding()
    {
        return _substepBindingList.elements();
    } //-- java.util.Enumeration enumerateSubstepBinding() 

    /**
     * 
     * 
     * @param index
    **/
    public SubstepBinding getSubstepBinding(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _substepBindingList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (SubstepBinding) _substepBindingList.elementAt(index);
    } //-- SubstepBinding getSubstepBinding(int) 

    /**
    **/
    public SubstepBinding[] getSubstepBinding()
    {
        int size = _substepBindingList.size();
        SubstepBinding[] mArray = new SubstepBinding[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (SubstepBinding) _substepBindingList.elementAt(index);
        }
        return mArray;
    } //-- SubstepBinding[] getSubstepBinding() 

    /**
    **/
    public int getSubstepBindingCount()
    {
        return _substepBindingList.size();
    } //-- int getSubstepBindingCount() 

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
    public void removeAllSubstepBinding()
    {
        _substepBindingList.removeAllElements();
    } //-- void removeAllSubstepBinding() 

    /**
     * 
     * 
     * @param index
    **/
    public SubstepBinding removeSubstepBinding(int index)
    {
        java.lang.Object obj = _substepBindingList.elementAt(index);
        _substepBindingList.removeElementAt(index);
        return (SubstepBinding) obj;
    } //-- SubstepBinding removeSubstepBinding(int) 

    /**
     * 
     * 
     * @param index
     * @param vSubstepBinding
    **/
    public void setSubstepBinding(int index, SubstepBinding vSubstepBinding)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _substepBindingList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _substepBindingList.setElementAt(vSubstepBinding, index);
    } //-- void setSubstepBinding(int, SubstepBinding) 

    /**
     * 
     * 
     * @param substepBindingArray
    **/
    public void setSubstepBinding(SubstepBinding[] substepBindingArray)
    {
        //-- copy array
        _substepBindingList.removeAllElements();
        for (int i = 0; i < substepBindingArray.length; i++) {
            _substepBindingList.addElement(substepBindingArray[i]);
        }
    } //-- void setSubstepBinding(SubstepBinding) 

    /**
     * 
     * 
     * @param reader
    **/
    public static Substeps unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (Substeps) Unmarshaller.unmarshal(Substeps.class, reader);
    } //-- Substeps unmarshal(java.io.Reader) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
