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
public class Program implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private java.lang.Object _root;

    private java.util.Vector _diagramList;


      //----------------/
     //- Constructors -/
    //----------------/

    public Program() {
        super();
        _diagramList = new Vector();
    } //-- Program()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vDiagram
    **/
    public void addDiagram(Diagram vDiagram)
        throws java.lang.IndexOutOfBoundsException
    {
        _diagramList.addElement(vDiagram);
    } //-- void addDiagram(Diagram) 

    /**
     * 
     * 
     * @param index
     * @param vDiagram
    **/
    public void addDiagram(int index, Diagram vDiagram)
        throws java.lang.IndexOutOfBoundsException
    {
        _diagramList.insertElementAt(vDiagram, index);
    } //-- void addDiagram(int, Diagram) 

    /**
    **/
    public java.util.Enumeration enumerateDiagram()
    {
        return _diagramList.elements();
    } //-- java.util.Enumeration enumerateDiagram() 

    /**
     * 
     * 
     * @param index
    **/
    public Diagram getDiagram(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _diagramList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (Diagram) _diagramList.elementAt(index);
    } //-- Diagram getDiagram(int) 

    /**
    **/
    public Diagram[] getDiagram()
    {
        int size = _diagramList.size();
        Diagram[] mArray = new Diagram[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (Diagram) _diagramList.elementAt(index);
        }
        return mArray;
    } //-- Diagram[] getDiagram() 

    /**
    **/
    public int getDiagramCount()
    {
        return _diagramList.size();
    } //-- int getDiagramCount() 

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
    public void removeAllDiagram()
    {
        _diagramList.removeAllElements();
    } //-- void removeAllDiagram() 

    /**
     * 
     * 
     * @param index
    **/
    public Diagram removeDiagram(int index)
    {
        java.lang.Object obj = _diagramList.elementAt(index);
        _diagramList.removeElementAt(index);
        return (Diagram) obj;
    } //-- Diagram removeDiagram(int) 

    /**
     * 
     * 
     * @param index
     * @param vDiagram
    **/
    public void setDiagram(int index, Diagram vDiagram)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _diagramList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _diagramList.setElementAt(vDiagram, index);
    } //-- void setDiagram(int, Diagram) 

    /**
     * 
     * 
     * @param diagramArray
    **/
    public void setDiagram(Diagram[] diagramArray)
    {
        //-- copy array
        _diagramList.removeAllElements();
        for (int i = 0; i < diagramArray.length; i++) {
            _diagramList.addElement(diagramArray[i]);
        }
    } //-- void setDiagram(Diagram) 

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
     * @param reader
    **/
    public static Program unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (Program) Unmarshaller.unmarshal(Program.class, reader);
    } //-- Program unmarshal(java.io.Reader) 

	// NOTE: can also marshal and unmarshal to and from a Document 
	
    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
