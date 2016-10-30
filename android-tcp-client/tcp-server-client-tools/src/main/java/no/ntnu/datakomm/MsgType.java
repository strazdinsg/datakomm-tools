package no.ntnu.datakomm;

/**
 * Message types used in the test application
 * @author Girts Strazdins, 2016-10-30
 */
public class MsgType {
    // Sent from the client application
    public static final int SEND_COMMAND = 1; // The client sends an actuation command
    public static final int READ_SENSORS = 2; // The client requests to read specific sensors
    
    // Sent from the server
    public static final int SERVER_INFO = 101; // Server returns some info regarding what version it is running, etc
    public static final int SENSOR_VALUES = 102; // Server returns requested sensor values
    public static final int SERVER_STATE_CHANGED = 103; // Server reports some state change
    public static final int CMD_EXECUTED = 104; // Server returns requested sensor values
    
}
