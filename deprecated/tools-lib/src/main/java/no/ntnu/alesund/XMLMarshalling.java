package no.ntnu.alesund;

import com.thoughtworks.xstream.XStream;

/**
 * A class that can marshall an object to XML string and vice versa
 *
 * @author Girts Strazdins, 2016-10-09
 */
public class XMLMarshalling extends Marshalling {

    private final XStream xstream;

    public XMLMarshalling() {
        xstream = new XStream();
    }

    /**
     * Marshal an object to XML string
     * @param o object to be marshalled
     * @param c class of the object
     * @return 
     */
    @Override
    public String marshall(Object o, Class c) {
        if (alias != null) {
            xstream.alias(alias, c);
        }
        // alias to have "person" as tag/variable name instead of xstreamtest.Person
        return xstream.toXML(o);
    }

    /**
     * Unmarshall an object back from an XML string
     * @param s String representation of the XML content
     * @param c expected class of the object
     * @return the created object. It can be casted to the correct class 
     */
    @Override
    public Object unmarshall(String s, Class c) {
        if (alias != null) {
            xstream.alias(alias, c);
        }
        return xstream.fromXML(s);
    }
}
