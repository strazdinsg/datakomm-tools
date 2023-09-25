package no.ntnu.communication;

import no.ntnu.TrafficLight;
import no.ntnu.TrafficLightState;
import no.ntnu.message.ErrorMessage;
import no.ntnu.message.GetCommand;
import no.ntnu.message.Message;
import no.ntnu.message.SetCommand;
import no.ntnu.message.StateMessage;

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

    Message message = null;

    if (messageString.equals("get")) {
      message = new GetCommand(trafficLight);
    } else if (messageString.startsWith("set ")) {
      String desiredColor = messageString.substring(4);
      TrafficLightState desiredState = TrafficLightState.from(desiredColor);
      message = new SetCommand(trafficLight, desiredState);
    } else if (messageString.startsWith("state ") && messageString.length() > 6) {
      String stateString = messageString.substring(6);
      TrafficLightState state = TrafficLightState.from(stateString);
      if (state != null) {
        message = new StateMessage(state);
      }
    } else if (messageString.startsWith("error ") && messageString.length() > 6) {
      String error = messageString.substring(6);
      message = new ErrorMessage(error);
    }

    return message;
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
    } else if (message instanceof ErrorMessage errorMessage) {
      result = "error " + errorMessage.getError();
    } else if (message instanceof StateMessage stateMessage) {
      result = "state " + stateMessage.getState();
    }
    return result;
  }
}
