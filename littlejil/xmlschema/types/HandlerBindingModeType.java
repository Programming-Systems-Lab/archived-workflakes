/*
 * This class was automatically generated with 
 * <a href="http://castor.exolab.org">Castor 0.9.3.9+</a>, using an
 * XML Schema.
 * $Id$
 */

package psl.workflakes.littlejil.xmlschema.types;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;
import org.exolab.castor.xml.*;

/**
 * 
 * 
 * @version $Revision$ $Date$
**/
public class HandlerBindingModeType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The complete type
    **/
    public static final int COMPLETE_TYPE = 0;

    /**
     * The instance of the complete type
    **/
    public static final HandlerBindingModeType COMPLETE = new HandlerBindingModeType(COMPLETE_TYPE, "complete");

    /**
     * The continue type
    **/
    public static final int CONTINUE_TYPE = 1;

    /**
     * The instance of the continue type
    **/
    public static final HandlerBindingModeType CONTINUE = new HandlerBindingModeType(CONTINUE_TYPE, "continue");

    /**
     * The restart type
    **/
    public static final int RESTART_TYPE = 2;

    /**
     * The instance of the restart type
    **/
    public static final HandlerBindingModeType RESTART = new HandlerBindingModeType(RESTART_TYPE, "restart");

    /**
     * The rethrow type
    **/
    public static final int RETHROW_TYPE = 3;

    /**
     * The instance of the rethrow type
    **/
    public static final HandlerBindingModeType RETHROW = new HandlerBindingModeType(RETHROW_TYPE, "rethrow");

    private static java.util.Hashtable _memberTable = init();

    private int type = -1;

    private java.lang.String stringValue = null;


      //----------------/
     //- Constructors -/
    //----------------/

    private HandlerBindingModeType(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- types.HandlerBindingModeType(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns an enumeration of all possible instances of
     * HandlerBindingModeType
    **/
    public static java.util.Enumeration enumerate()
    {
        return _memberTable.elements();
    } //-- java.util.Enumeration enumerate() 

    /**
     * Returns the type of this HandlerBindingModeType
    **/
    public int getType()
    {
        return this.type;
    } //-- int getType() 

    /**
    **/
    private static java.util.Hashtable init()
    {
        Hashtable members = new Hashtable();
        members.put("complete", COMPLETE);
        members.put("continue", CONTINUE);
        members.put("restart", RESTART);
        members.put("rethrow", RETHROW);
        return members;
    } //-- java.util.Hashtable init() 

    /**
     * Returns the String representation of this
     * HandlerBindingModeType
    **/
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Returns a new HandlerBindingModeType based on the given
     * String value.
     * 
     * @param string
    **/
    public static HandlerBindingModeType valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid HandlerBindingModeType";
            throw new IllegalArgumentException(err);
        }
        return (HandlerBindingModeType) obj;
    } //-- types.HandlerBindingModeType valueOf(java.lang.String) 

}
