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
public class Reactions implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private java.util.Vector _reactionBindingList;


      //----------------/
     //- Constructors -/
    //----------------/

    public Reactions() {
        super();
        _reactionBindingList = new Vector();
    } //-- Reactions()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vReactionBinding
    **/
    public void addReactionBinding(ReactionBinding vReactionBinding)
        throws java.lang.IndexOutOfBoundsException
    {
        _reactionBindingList.addElement(vReactionBinding);
    } //-- void addReactionBinding(ReactionBinding) 

    /**
     * 
     * 
     * @param index
     * @param vReactionBinding
    **/
    public void addReactionBinding(int index, ReactionBinding vReactionBinding)
        throws java.lang.IndexOutOfBoundsException
    {
        _reactionBindingList.insertElementAt(vReactionBinding, index);
    } //-- void addReactionBinding(int, ReactionBinding) 

    /**
    **/
    public java.util.Enumeration enumerateReactionBinding()
    {
        return _reactionBindingList.elements();
    } //-- java.util.Enumeration enumerateReactionBinding() 

    /**
     * 
     * 
     * @param index
    **/
    public ReactionBinding getReactionBinding(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _reactionBindingList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (ReactionBinding) _reactionBindingList.elementAt(index);
    } //-- ReactionBinding getReactionBinding(int) 

    /**
    **/
    public ReactionBinding[] getReactionBinding()
    {
        int size = _reactionBindingList.size();
        ReactionBinding[] mArray = new ReactionBinding[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (ReactionBinding) _reactionBindingList.elementAt(index);
        }
        return mArray;
    } //-- ReactionBinding[] getReactionBinding() 

    /**
    **/
    public int getReactionBindingCount()
    {
        return _reactionBindingList.size();
    } //-- int getReactionBindingCount() 

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
    public void removeAllReactionBinding()
    {
        _reactionBindingList.removeAllElements();
    } //-- void removeAllReactionBinding() 

    /**
     * 
     * 
     * @param index
    **/
    public ReactionBinding removeReactionBinding(int index)
    {
        java.lang.Object obj = _reactionBindingList.elementAt(index);
        _reactionBindingList.removeElementAt(index);
        return (ReactionBinding) obj;
    } //-- ReactionBinding removeReactionBinding(int) 

    /**
     * 
     * 
     * @param index
     * @param vReactionBinding
    **/
    public void setReactionBinding(int index, ReactionBinding vReactionBinding)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _reactionBindingList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _reactionBindingList.setElementAt(vReactionBinding, index);
    } //-- void setReactionBinding(int, ReactionBinding) 

    /**
     * 
     * 
     * @param reactionBindingArray
    **/
    public void setReactionBinding(ReactionBinding[] reactionBindingArray)
    {
        //-- copy array
        _reactionBindingList.removeAllElements();
        for (int i = 0; i < reactionBindingArray.length; i++) {
            _reactionBindingList.addElement(reactionBindingArray[i]);
        }
    } //-- void setReactionBinding(ReactionBinding) 

    /**
     * 
     * 
     * @param reader
    **/
    public static Reactions unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (Reactions) Unmarshaller.unmarshal(Reactions.class, reader);
    } //-- Reactions unmarshal(java.io.Reader) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
