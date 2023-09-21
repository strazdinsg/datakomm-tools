import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import no.ntnu.TrafficLight;
import no.ntnu.TrafficLightState;
import org.junit.Test;

public class TrafficLightTests {
  @Test
  public void testDefaultState() {
    TrafficLight trafficLight = new TrafficLight();
    assertEquals(TrafficLightState.RED, trafficLight.getState());
  }

  @Test
  public void testProhibitedStateChange() {
    TrafficLight trafficLight = new TrafficLight();
    assertEquals(TrafficLightState.RED, trafficLight.getState());
    assertThrows(IllegalStateException.class, () -> trafficLight.setState(TrafficLightState.GREEN));
    assertEquals(TrafficLightState.RED, trafficLight.getState());
  }
}
