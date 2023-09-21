import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import no.ntnu.TrafficLight;
import no.ntnu.TrafficLightState;
import no.ntnu.communication.MessageSerializer;
import no.ntnu.message.GetCommand;
import no.ntnu.message.Message;
import no.ntnu.message.SetCommand;
import org.junit.Test;

public class MessageSerializerTests {
  @Test
  public void testFromString() {
    TrafficLight trafficLight = new TrafficLight();
    MessageSerializer serializer = new MessageSerializer(trafficLight);
    Message message = serializer.fromString("get");
    assertTrue(message instanceof GetCommand);
  }

  @Test
  public void testToString() {
    TrafficLight trafficLight = new TrafficLight();
    MessageSerializer serializer = new MessageSerializer(trafficLight);
    Message message = new GetCommand(trafficLight);
    assertEquals("get", serializer.toString(message));

    message = new SetCommand(trafficLight, TrafficLightState.RED);
    assertEquals("set red", serializer.toString(message));
  }
}
