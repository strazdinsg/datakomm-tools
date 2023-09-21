package no.ntnu.communication;

import no.ntnu.TrafficLight;
import no.ntnu.TrafficLightState;
import no.ntnu.message.GetCommand;
import no.ntnu.message.Message;
import no.ntnu.message.SetCommand;

/**
 * Can translate Message objects to Strings and vice versa.
 */
public class MessageSerializer {
  private final TrafficLight trafficLight;

  /**
   * Create a message serializer.
   *
   * @param trafficLight The traffic light logic
   */
  public MessageSerializer(TrafficLight trafficLight) {
    this.trafficLight = trafficLight;
  }


  /**
   * Translate a string received over a communication channel (socket) to a Message object.
   *
   * @param messageString The raw message string
   * @return The corresponding message object or null if it is invalid
   */
  public Message fromString(String messageString) {
    if (messageString == null) {
      return null;
    }

    if (messageString.equals("get")) {
      return new GetCommand(trafficLight);
    } else if (messageString.startsWith("set ")) {
      String desiredColor = messageString.substring(4);
      TrafficLightState desiredState = TrafficLightState.from(desiredColor);
      return new SetCommand(trafficLight, desiredState);
    }
    return null;
  }

  /**
   * Translate a message to a string that can be sent over the communication channel (Socket).
   *
   * @param message The message to translate
   * @return A corresponding string or null if it can't be translated
   */
  public String toString(Message message) {
    String result = null;
    if (message instanceof GetCommand) {
      result = "get";
    } else if (message instanceof SetCommand setCommand) {
      result = "set " + setCommand.getDesiredState();
    }
    return result;
  }
}
