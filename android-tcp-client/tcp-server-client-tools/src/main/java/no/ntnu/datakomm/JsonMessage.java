package no.ntnu.datakomm;

import java.util.LinkedList;
import java.util.List;
import no.ntnu.alesund.JsonMarshalling;

/**
 * A message that can be exchanged between TCP server and client
 * @author Girts Strazdins, 2016-10-29
 */
public class JsonMessage {
    private int type;
    private List<String> arguments = new LinkedList<>();
    private static JsonMarshalling marshalling = null;
    
    public JsonMessage() {
        // lazy-init the marshalling when the first message is created
        if (marshalling == null) {
            marshalling = new JsonMarshalling();
            marshalling.setAlias("message");
        }
    }
    
    /**
     * Get type of the message. Application-specific
     * @return 
     */
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
    
    public List<String> getArguments() {
        return this.arguments;
    }
    
    public void setArguments(List<String> arguments) {
        this.arguments = arguments;
    }
    
    /**
     * Take this object, serialize it as a Json String
     * @return 
     */
    public String toJson() {
        String json = marshalling.marshall(this, JsonMessage.class);
        // Remove newline characters
        return json.replace("\n", "");
    }
    
    /**
     * Try to parse a Json String and extract JsonMessage object from it
     * Return null on error
     * 
     * @param jsonString
     * @return 
     */
    public static JsonMessage fromJson(String jsonString) {
        JsonMessage msg = (JsonMessage) marshalling.unmarshall(jsonString, 
                JsonMessage.class);
        return msg;
    }
    
    /**
     * Add a new argument value
     * @param arg
     */
    public void addArgument(String arg) {
        arguments.add(arg);
    }
    
    /**
     * Return value of a specific argument or null if it does not exist
     * @param index
     * @return 
     */
    public String getArgumentByIndex(int index) {
        if (index < 0 || index >= arguments.size()) return null;
        return arguments.get(index);
    }
    
    /**
     * Get number of arguments currently stored in the message
     * @return 
     */
    public int getArgumentCount() {
        return arguments.size();
    }

    /**
     * Return true if this object is equal to a given one
     * @param m
     * @return 
     */
    public boolean equals(JsonMessage m) {
        if (m == null) return false;
        return m.type == this.type && m.arguments.equals(this.arguments);
    }
}
