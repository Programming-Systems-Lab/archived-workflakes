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
public class ParameterDeclModeType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The in type
    **/
    public static final int VALUE_0_TYPE = 0;

    /**
     * The instance of the in type
    **/
    public static final ParameterDeclModeType VALUE_0 = new ParameterDeclModeType(VALUE_0_TYPE, "in");

    /**
     * The out type
    **/
    public static final int VALUE_1_TYPE = 1;

    /**
     * The instance of the out type
    **/
    public static final ParameterDeclModeType VALUE_1 = new ParameterDeclModeType(VALUE_1_TYPE, "out");

    /**
     * The in-and-out type
    **/
    public static final int VALUE_2_TYPE = 2;

    /**
     * The instance of the in-and-out type
    **/
    public static final ParameterDeclModeType VALUE_2 = new ParameterDeclModeType(VALUE_2_TYPE, "in-and-out");

    /**
     * The local type
    **/
    public static final int VALUE_3_TYPE = 3;

    /**
     * The instance of the local type
    **/
    public static final ParameterDeclModeType VALUE_3 = new ParameterDeclModeType(VALUE_3_TYPE, "local");

    private static java.util.Hashtable _memberTable = init();

    private int type = -1;

    private java.lang.String stringValue = null;


      //----------------/
     //- Constructors -/
    //----------------/

    private ParameterDeclModeType(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- types.ParameterDeclModeType(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns an enumeration of all possible instances of
     * ParameterDeclModeType
    **/
    public static java.util.Enumeration enumerate()
    {
        return _memberTable.elements();
    } //-- java.util.Enumeration enumerate() 

    /**
     * Returns the type of this ParameterDeclModeType
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
        members.put("in", VALUE_0);
        members.put("out", VALUE_1);
        members.put("in-and-out", VALUE_2);
        members.put("local", VALUE_3);
        return members;
    } //-- java.util.Hashtable init() 

    /**
     * Returns the String representation of this
     * ParameterDeclModeType
    **/
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Returns a new ParameterDeclModeType based on the given
     * String value.
     * 
     * @param string
    **/
    public static ParameterDeclModeType valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid ParameterDeclModeType";
            throw new IllegalArgumentException(err);
        }
        return (ParameterDeclModeType) obj;
    } //-- types.ParameterDeclModeType valueOf(java.lang.String) 

}
