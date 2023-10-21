package no.ntnu.message;

/**
 * Serializes messages to protocol-defined strings and vice versa.
 */
public class MessageSerializer {
  public static final String CHANNEL_COUNT_COMMAND = "c";
  public static final String TURN_ON_COMMAND = "1";
  public static final String TURN_OFF_COMMAND = "0";
  public static final String GET_CHANNEL_COMMAND = "g";
  public static final String SET_CHANNEL_COMMAND = "s";
  public static final String OK_RESPONSE = "o";
  private static final String CHANNEL_COUNT_MESSAGE = "c";
  private static final String ERROR_MESSAGE = "e";
  private static final String CURRENT_CHANNEL_MESSAGE = "C";

  /**
   * Not allowed to instantiate this utility class.
   */
  private MessageSerializer() {
  }

  /**
   * Create message from a string, according to the communication protocol.
   *
   * @param s The string sent over the communication channel
   * @return The logical message, as interpreted according to the protocol
   */
  public static Message fromString(String s) {
    Message m;
    switch (s) {
      case CHANNEL_COUNT_COMMAND:
        m = new ChannelCountCommand();
        break;
      case TURN_ON_COMMAND:
        m = new TurnOnCommand();
        break;
      default:
        m = null;
    }
    return m;
  }

  public static String toString(Message response) {
    // TODO - implement
    return "TODO";
  }
}
