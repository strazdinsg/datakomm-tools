package no.ntnu.alesund;

/**
 * A general interface for classes that can marshall an object to a string 
 * and vice versa. Each class should then define the format for the string
 * (Json, XML, etc)
 *
 * @author Girts Strazdins, 2016-10-09
 */
public abstract class Marshalling {

    private boolean debug = false;    
    
    /**
     * Enable/disable debug output
     * @param debug 
     */
    public void setDebug(boolean debug) {
        this.debug = debug;
    }
    
    /**
     * Marshal an object to XML string
     * @param o object to be marshalled
     * @param c class of the object
     * @param objectAlias an alias that will be used as the main tag in the XML
     * when left blank, the class name including the whole package will be used
     * @return 
     */
    public abstract String marshall(Object o, Class c, String objectAlias);

    /**
     * Unmarshall an object back from an XML string
     * @param s String representation of the serialized content
     * @param c expected class of the object
     * @return the created object. It can be casted to the correct class 
     */
    public abstract Object unmarshall(String s, Class c);

    /**
     * Print message to System.out if debug is enabled
     *
     * @param msg
     */
    protected void debugOut(String msg) {
        if (debug) {
            System.out.println(msg);
        }
    }
}
