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
public class Interface implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private java.util.Vector _items;


      //----------------/
     //- Constructors -/
    //----------------/

    public Interface() {
        super();
        _items = new Vector();
    } //-- Interface()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vInterfaceItem
    **/
    public void addInterfaceItem(InterfaceItem vInterfaceItem)
        throws java.lang.IndexOutOfBoundsException
    {
        _items.addElement(vInterfaceItem);
    } //-- void addInterfaceItem(InterfaceItem) 

    /**
     * 
     * 
     * @param index
     * @param vInterfaceItem
    **/
    public void addInterfaceItem(int index, InterfaceItem vInterfaceItem)
        throws java.lang.IndexOutOfBoundsException
    {
        _items.insertElementAt(vInterfaceItem, index);
    } //-- void addInterfaceItem(int, InterfaceItem) 

    /**
    **/
    public java.util.Enumeration enumerateInterfaceItem()
    {
        return _items.elements();
    } //-- java.util.Enumeration enumerateInterfaceItem() 

    /**
     * 
     * 
     * @param index
    **/
    public InterfaceItem getInterfaceItem(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _items.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (InterfaceItem) _items.elementAt(index);
    } //-- InterfaceItem getInterfaceItem(int) 

    /**
    **/
    public InterfaceItem[] getInterfaceItem()
    {
        int size = _items.size();
        InterfaceItem[] mArray = new InterfaceItem[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (InterfaceItem) _items.elementAt(index);
        }
        return mArray;
    } //-- InterfaceItem[] getInterfaceItem() 

    /**
    **/
    public int getInterfaceItemCount()
    {
        return _items.size();
    } //-- int getInterfaceItemCount() 

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
    public void removeAllInterfaceItem()
    {
        _items.removeAllElements();
    } //-- void removeAllInterfaceItem() 

    /**
     * 
     * 
     * @param index
    **/
    public InterfaceItem removeInterfaceItem(int index)
    {
        java.lang.Object obj = _items.elementAt(index);
        _items.removeElementAt(index);
        return (InterfaceItem) obj;
    } //-- InterfaceItem removeInterfaceItem(int) 

    /**
     * 
     * 
     * @param index
     * @param vInterfaceItem
    **/
    public void setInterfaceItem(int index, InterfaceItem vInterfaceItem)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _items.size())) {
            throw new IndexOutOfBoundsException();
        }
        _items.setElementAt(vInterfaceItem, index);
    } //-- void setInterfaceItem(int, InterfaceItem) 

    /**
     * 
     * 
     * @param interfaceItemArray
    **/
    public void setInterfaceItem(InterfaceItem[] interfaceItemArray)
    {
        //-- copy array
        _items.removeAllElements();
        for (int i = 0; i < interfaceItemArray.length; i++) {
            _items.addElement(interfaceItemArray[i]);
        }
    } //-- void setInterfaceItem(InterfaceItem) 

    /**
     * 
     * 
     * @param reader
    **/
    public static Interface unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (Interface) Unmarshaller.unmarshal(Interface.class, reader);
    } //-- Interface unmarshal(java.io.Reader) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
