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
public class ParameterBindingModeType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The copy-in type
    **/
    public static final int VALUE_0_TYPE = 0;

    /**
     * The instance of the copy-in type
    **/
    public static final ParameterBindingModeType VALUE_0 = new ParameterBindingModeType(VALUE_0_TYPE, "copy-in");

    /**
     * The copy-out type
    **/
    public static final int VALUE_1_TYPE = 1;

    /**
     * The instance of the copy-out type
    **/
    public static final ParameterBindingModeType VALUE_1 = new ParameterBindingModeType(VALUE_1_TYPE, "copy-out");

    /**
     * The copy-in-and-out type
    **/
    public static final int VALUE_2_TYPE = 2;

    /**
     * The instance of the copy-in-and-out type
    **/
    public static final ParameterBindingModeType VALUE_2 = new ParameterBindingModeType(VALUE_2_TYPE, "copy-in-and-out");

    /**
     * The constrain type
    **/
    public static final int VALUE_3_TYPE = 3;

    /**
     * The instance of the constrain type
    **/
    public static final ParameterBindingModeType VALUE_3 = new ParameterBindingModeType(VALUE_3_TYPE, "constrain");

    private static java.util.Hashtable _memberTable = init();

    private int type = -1;

    private java.lang.String stringValue = null;


      //----------------/
     //- Constructors -/
    //----------------/

    private ParameterBindingModeType(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- types.ParameterBindingModeType(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns an enumeration of all possible instances of
     * ParameterBindingModeType
    **/
    public static java.util.Enumeration enumerate()
    {
        return _memberTable.elements();
    } //-- java.util.Enumeration enumerate() 

    /**
     * Returns the type of this ParameterBindingModeType
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
        members.put("copy-in", VALUE_0);
        members.put("copy-out", VALUE_1);
        members.put("copy-in-and-out", VALUE_2);
        members.put("constrain", VALUE_3);
        return members;
    } //-- java.util.Hashtable init() 

    /**
     * Returns the String representation of this
     * ParameterBindingModeType
    **/
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Returns a new ParameterBindingModeType based on the given
     * String value.
     * 
     * @param string
    **/
    public static ParameterBindingModeType valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid ParameterBindingModeType";
            throw new IllegalArgumentException(err);
        }
        return (ParameterBindingModeType) obj;
    } //-- types.ParameterBindingModeType valueOf(java.lang.String) 

}
