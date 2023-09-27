package no.ntnu.message;

import no.ntnu.TrafficLightState;

/**
 * A message reporting a state of the traffic light.
 */
public class StateMessage implements Message {
  private final TrafficLightState state;

  public StateMessage(TrafficLightState state) {
    this.state = state;
  }

  public TrafficLightState getState() {
    return state;
  }
}
