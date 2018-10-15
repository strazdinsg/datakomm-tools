package no.ntnu.alesund;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver;
import java.io.IOException;
import java.util.Iterator;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A class that can marshall an object to Json string and vice versa
 *
 * @author Girts Strazdins, 2016-10-09
 */
public class JsonMarshalling extends Marshalling {

    private final XStream xstream;
    private final ObjectMapper mapper;

    public JsonMarshalling() {
        xstream = new XStream(new JsonHierarchicalStreamDriver());
        mapper = new ObjectMapper();
    }
    
    
    
    /**
     * Marshal an object to Json string
     *
     * @param o object to be marshalled
     * @param c class of the object
     * @return
     */
    @Override
    public String marshall(Object o, Class c) {
        if (alias != null) {
            xstream.alias(alias, c);
        }
        return xstream.toXML(o);
    }

    /**
     * Unmarshall an object back from an XML string
     *
     * @param c expected class of the object
     * @param s String representation of the serialized content
     * @return the created object. It can be casted to the correct class
     */
    @Override
    public Object unmarshall(String s, Class c) {
        if (alias != null) {
            xstream.alias(alias, c);
        }
        if (s == null) {
            return null;
        }
        
        // Try to extract the object directly
        Object o = tryExtractObject(s, c);
        if (o != null) {
            return o;
        }

        // If direct extraction failed, try to get the first parameter of 
        // the whole object. Sometimes object->json parsers (also this one)
        // enclose the object as an internal field in the global object
        // E.g., object "car" with attribute "name"="Audi" would not be simply
        // { "name": "Audi" }, instead it would be
        // { "car" : { "name": "Audi" } }
        // Then we first take the "car" field and look at it as an object
        String internalJson = getFirstField(s);
        if (internalJson != null) {
            o = tryExtractObject(internalJson, c);
            if (o != null) {
                return o;
            }
        }
        
        // Give up
        return null;
    }

    private Object tryExtractObject(String s, Class c) {
        try {
            debugOut("Trying to extract object from string " + s);
            Object o = mapper.readValue(s, c);
            return o;
        } catch (IOException ex) {
            debugOut("Error while extracting object: " + ex.getMessage());
            return null;
        }
    }

    /**
     * Try to get the first field inside a given JSON string
     *
     * @param json
     * @return String representing the whole content of the first field
     */
    private String getFirstField(String json) {
        try {
            JSONObject mainObj = new JSONObject(json);
            Iterator<String> it = mainObj.keys();
            if (it != null && it.hasNext()) {
                // First field found
                String firstKey = it.next();
                Object internalObj = mainObj.get(firstKey);
                if (internalObj != null) {
                    return internalObj.toString();
                } else {
                    // Error while getting the internal object
                    return null;
                }
            } else {
                // no fields found inside the JSON object
                return null;
            }
        } catch (JSONException ex) {
            debugOut("Error while extracting first field: " + ex.getMessage());
            // Error while parsing the json string
            return null;
        }
    }
}
