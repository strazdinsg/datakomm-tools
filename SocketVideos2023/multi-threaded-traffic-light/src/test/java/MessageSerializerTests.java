import static org.junit.Assert.*;

import no.ntnu.TrafficLight;
import no.ntnu.TrafficLightState;
import no.ntnu.communication.MessageSerializer;
import no.ntnu.message.ErrorMessage;
import no.ntnu.message.GetCommand;
import no.ntnu.message.Message;
import no.ntnu.message.SetCommand;
import no.ntnu.message.StateMessage;
import org.junit.Test;

public class MessageSerializerTests {
  @Test
  public void testFromString() {
    TrafficLight trafficLight = new TrafficLight();
    MessageSerializer serializer = new MessageSerializer(trafficLight);
    Message message = serializer.fromString("get");
    assertTrue(message instanceof GetCommand);

    message = serializer.fromString("error");
    assertNull(message);
    message = serializer.fromString("error ");
    assertNull(message);
    message = serializer.fromString("error Ddd");
    assertTrue(message instanceof ErrorMessage);
    assertEquals("Ddd", ((ErrorMessage) message).getError());

    message = serializer.fromString("state");
    assertNull(message);
    message = serializer.fromString("state ");
    assertNull(message);
    message = serializer.fromString("state Unknown");
    assertNull(message);
    message = serializer.fromString("state yellow");
    assertTrue(message instanceof StateMessage);
    assertEquals(TrafficLightState.YELLOW, ((StateMessage) message).getState());
    message = serializer.fromString("state green");
    assertTrue(message instanceof StateMessage);
    assertEquals(TrafficLightState.GREEN, ((StateMessage) message).getState());
  }

  @Test
  public void testToString() {
    TrafficLight trafficLight = new TrafficLight();
    MessageSerializer serializer = new MessageSerializer(trafficLight);
    Message message = new GetCommand(trafficLight);
    assertEquals("get", serializer.toString(message));

    message = new SetCommand(trafficLight, TrafficLightState.RED);
    assertEquals("set red", serializer.toString(message));

    message = new ErrorMessage("Samsing rong");
    assertEquals("error Samsing rong", serializer.toString(message));


    message = new StateMessage(trafficLight.getState());
    assertEquals("state " + trafficLight.getState(), serializer.toString(message));
  }
}
