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
import psl.workflakes.littlejil.xmlschema.types.StepKindType;

/**
 * 
 * 
 * @version $Revision$ $Date$
**/
public class Step implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private java.lang.String _name;

    private psl.workflakes.littlejil.xmlschema.types.StepKindType _kind;

    private java.lang.String _id;

    private Interface _interface;

    private Prerequisite _prerequisite;

    private Postrequisite _postrequisite;

    private Substeps _substeps;

    private Reactions _reactions;

    private Handlers _handlers;

    private Annotations _annotations;


      //----------------/
     //- Constructors -/
    //----------------/

    public Step() {
        super();
    } //-- Step()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'annotations'.
     * 
     * @return the value of field 'annotations'.
    **/
    public Annotations getAnnotations()
    {
        return this._annotations;
    } //-- Annotations getAnnotations() 

    /**
     * Returns the value of field 'handlers'.
     * 
     * @return the value of field 'handlers'.
    **/
    public Handlers getHandlers()
    {
        return this._handlers;
    } //-- Handlers getHandlers() 

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
     * Returns the value of field 'interface'.
     * 
     * @return the value of field 'interface'.
    **/
    public Interface getInterface()
    {
        return this._interface;
    } //-- Interface getInterface() 

    /**
     * Returns the value of field 'kind'.
     * 
     * @return the value of field 'kind'.
    **/
    public psl.workflakes.littlejil.xmlschema.types.StepKindType getKind()
    {
        return this._kind;
    } //-- types.StepKindType getKind() 

    /**
     * Returns the value of field 'name'.
     * 
     * @return the value of field 'name'.
    **/
    public java.lang.String getName()
    {
        return this._name;
    } //-- java.lang.String getName() 

    /**
     * Returns the value of field 'postrequisite'.
     * 
     * @return the value of field 'postrequisite'.
    **/
    public Postrequisite getPostrequisite()
    {
        return this._postrequisite;
    } //-- Postrequisite getPostrequisite() 

    /**
     * Returns the value of field 'prerequisite'.
     * 
     * @return the value of field 'prerequisite'.
    **/
    public Prerequisite getPrerequisite()
    {
        return this._prerequisite;
    } //-- Prerequisite getPrerequisite() 

    /**
     * Returns the value of field 'reactions'.
     * 
     * @return the value of field 'reactions'.
    **/
    public Reactions getReactions()
    {
        return this._reactions;
    } //-- Reactions getReactions() 

    /**
     * Returns the value of field 'substeps'.
     * 
     * @return the value of field 'substeps'.
    **/
    public Substeps getSubsteps()
    {
        return this._substeps;
    } //-- Substeps getSubsteps() 

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
     * Sets the value of field 'annotations'.
     * 
     * @param annotations the value of field 'annotations'.
    **/
    public void setAnnotations(Annotations annotations)
    {
        this._annotations = annotations;
    } //-- void setAnnotations(Annotations) 

    /**
     * Sets the value of field 'handlers'.
     * 
     * @param handlers the value of field 'handlers'.
    **/
    public void setHandlers(Handlers handlers)
    {
        this._handlers = handlers;
    } //-- void setHandlers(Handlers) 

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
     * Sets the value of field 'interface'.
     * 
     * @param _interface
     * @param interface the value of field 'interface'.
    **/
    public void setInterface(Interface _interface)
    {
        this._interface = _interface;
    } //-- void setInterface(Interface) 

    /**
     * Sets the value of field 'kind'.
     * 
     * @param kind the value of field 'kind'.
    **/
    public void setKind(psl.workflakes.littlejil.xmlschema.types.StepKindType kind)
    {
        this._kind = kind;
    } //-- void setKind(types.StepKindType) 

    /**
     * Sets the value of field 'name'.
     * 
     * @param name the value of field 'name'.
    **/
    public void setName(java.lang.String name)
    {
        this._name = name;
    } //-- void setName(java.lang.String) 

    /**
     * Sets the value of field 'postrequisite'.
     * 
     * @param postrequisite the value of field 'postrequisite'.
    **/
    public void setPostrequisite(Postrequisite postrequisite)
    {
        this._postrequisite = postrequisite;
    } //-- void setPostrequisite(Postrequisite) 

    /**
     * Sets the value of field 'prerequisite'.
     * 
     * @param prerequisite the value of field 'prerequisite'.
    **/
    public void setPrerequisite(Prerequisite prerequisite)
    {
        this._prerequisite = prerequisite;
    } //-- void setPrerequisite(Prerequisite) 

    /**
     * Sets the value of field 'reactions'.
     * 
     * @param reactions the value of field 'reactions'.
    **/
    public void setReactions(Reactions reactions)
    {
        this._reactions = reactions;
    } //-- void setReactions(Reactions) 

    /**
     * Sets the value of field 'substeps'.
     * 
     * @param substeps the value of field 'substeps'.
    **/
    public void setSubsteps(Substeps substeps)
    {
        this._substeps = substeps;
    } //-- void setSubsteps(Substeps) 

    /**
     * 
     * 
     * @param reader
    **/
    public static Step unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (Step) Unmarshaller.unmarshal(Step.class, reader);
    } //-- Step unmarshal(java.io.Reader) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
