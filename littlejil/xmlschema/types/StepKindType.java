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
public class StepKindType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The sequential type
    **/
    public static final int SEQUENTIAL_TYPE = 0;

    /**
     * The instance of the sequential type
    **/
    public static final StepKindType SEQUENTIAL = new StepKindType(SEQUENTIAL_TYPE, "sequential");

    /**
     * The parallel type
    **/
    public static final int PARALLEL_TYPE = 1;

    /**
     * The instance of the parallel type
    **/
    public static final StepKindType PARALLEL = new StepKindType(PARALLEL_TYPE, "parallel");

    /**
     * The choice type
    **/
    public static final int CHOICE_TYPE = 2;

    /**
     * The instance of the choice type
    **/
    public static final StepKindType CHOICE = new StepKindType(CHOICE_TYPE, "choice");

    /**
     * The try type
    **/
    public static final int TRY_TYPE = 3;

    /**
     * The instance of the try type
    **/
    public static final StepKindType TRY = new StepKindType(TRY_TYPE, "try");

    /**
     * The leaf type
    **/
    public static final int LEAF_TYPE = 4;

    /**
     * The instance of the leaf type
    **/
    public static final StepKindType LEAF = new StepKindType(LEAF_TYPE, "leaf");

    private static java.util.Hashtable _memberTable = init();

    private int type = -1;

    private java.lang.String stringValue = null;


      //----------------/
     //- Constructors -/
    //----------------/

    private StepKindType(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- types.StepKindType(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns an enumeration of all possible instances of
     * StepKindType
    **/
    public static java.util.Enumeration enumerate()
    {
        return _memberTable.elements();
    } //-- java.util.Enumeration enumerate() 

    /**
     * Returns the type of this StepKindType
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
        members.put("sequential", SEQUENTIAL);
        members.put("parallel", PARALLEL);
        members.put("choice", CHOICE);
        members.put("try", TRY);
        members.put("leaf", LEAF);
        return members;
    } //-- java.util.Hashtable init() 

    /**
     * Returns the String representation of this StepKindType
    **/
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Returns a new StepKindType based on the given String value.
     * 
     * @param string
    **/
    public static StepKindType valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid StepKindType";
            throw new IllegalArgumentException(err);
        }
        return (StepKindType) obj;
    } //-- types.StepKindType valueOf(java.lang.String) 

}
