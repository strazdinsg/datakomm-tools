import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import no.ntnu.TrafficLight;
import no.ntnu.TrafficLightState;
import no.ntnu.message.*;
import org.junit.Test;

public class CommandTests {

  @Test
  public void testSetCommand() {
    TrafficLight trafficLight = new TrafficLight();
    assertEquals(TrafficLightState.RED, trafficLight.getState());

    Command command = new SetCommand(trafficLight, TrafficLightState.GREEN);
    Message response = command.execute();
    assertTrue(response instanceof ErrorMessage);

    command = new SetCommand(trafficLight, TrafficLightState.YELLOW);
    response = command.execute();
    assertTrue(response instanceof StateMessage);
    assertEquals(TrafficLightState.YELLOW, ((StateMessage) response).getState());
  }
}
