package no.ntnu.message;

/**
 * Serializes messages to protocol-defined strings and vice versa.
 */
public class MessageSerializer {
  /**
   * Create message from a string, according to the communication protocol.
   *
   * @param s The string sent over the communication channel
   * @return The logical message, as interpreted according to the protocol
   */
  public static Message fromString(String s) {
    Message m;
    switch (s) {
      case "c":
        m = new ChannelCountCommand();
        break;
      case "1":
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
