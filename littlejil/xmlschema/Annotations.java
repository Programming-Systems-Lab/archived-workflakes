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
public class Annotations implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private java.util.Vector _annotationList;


      //----------------/
     //- Constructors -/
    //----------------/

    public Annotations() {
        super();
        _annotationList = new Vector();
    } //-- Annotations()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vAnnotation
    **/
    public void addAnnotation(Annotation vAnnotation)
        throws java.lang.IndexOutOfBoundsException
    {
        _annotationList.addElement(vAnnotation);
    } //-- void addAnnotation(Annotation) 

    /**
     * 
     * 
     * @param index
     * @param vAnnotation
    **/
    public void addAnnotation(int index, Annotation vAnnotation)
        throws java.lang.IndexOutOfBoundsException
    {
        _annotationList.insertElementAt(vAnnotation, index);
    } //-- void addAnnotation(int, Annotation) 

    /**
    **/
    public java.util.Enumeration enumerateAnnotation()
    {
        return _annotationList.elements();
    } //-- java.util.Enumeration enumerateAnnotation() 

    /**
     * 
     * 
     * @param index
    **/
    public Annotation getAnnotation(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _annotationList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (Annotation) _annotationList.elementAt(index);
    } //-- Annotation getAnnotation(int) 

    /**
    **/
    public Annotation[] getAnnotation()
    {
        int size = _annotationList.size();
        Annotation[] mArray = new Annotation[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (Annotation) _annotationList.elementAt(index);
        }
        return mArray;
    } //-- Annotation[] getAnnotation() 

    /**
    **/
    public int getAnnotationCount()
    {
        return _annotationList.size();
    } //-- int getAnnotationCount() 

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
    public void removeAllAnnotation()
    {
        _annotationList.removeAllElements();
    } //-- void removeAllAnnotation() 

    /**
     * 
     * 
     * @param index
    **/
    public Annotation removeAnnotation(int index)
    {
        java.lang.Object obj = _annotationList.elementAt(index);
        _annotationList.removeElementAt(index);
        return (Annotation) obj;
    } //-- Annotation removeAnnotation(int) 

    /**
     * 
     * 
     * @param index
     * @param vAnnotation
    **/
    public void setAnnotation(int index, Annotation vAnnotation)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _annotationList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _annotationList.setElementAt(vAnnotation, index);
    } //-- void setAnnotation(int, Annotation) 

    /**
     * 
     * 
     * @param annotationArray
    **/
    public void setAnnotation(Annotation[] annotationArray)
    {
        //-- copy array
        _annotationList.removeAllElements();
        for (int i = 0; i < annotationArray.length; i++) {
            _annotationList.addElement(annotationArray[i]);
        }
    } //-- void setAnnotation(Annotation) 

    /**
     * 
     * 
     * @param reader
    **/
    public static Annotations unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (Annotations) Unmarshaller.unmarshal(Annotations.class, reader);
    } //-- Annotations unmarshal(java.io.Reader) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
