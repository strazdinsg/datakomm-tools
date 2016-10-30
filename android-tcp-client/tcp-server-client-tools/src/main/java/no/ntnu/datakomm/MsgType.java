package no.ntnu.datakomm;

/**
 * Message types used in the test application
 * @author Girts Strazdins, 2016-10-30
 */
public class MsgType {
    // Sent from the client application
    public static int SEND_COMMAND; // The client sends an actuation command
    public static int READ_SENSORS; // The client requests to read specific sensors
    
    // Sent from the server
    public static int SENSOR_VALUES; // Server returns requested sensor values
    public static int SERVER_STATE_CHANGED; // Server reports some state change
    
}
