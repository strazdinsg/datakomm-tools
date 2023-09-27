package no.ntnu;

/**
 * A smart traffic light - the business logic for it.
 */
public class TrafficLight {
  private TrafficLightState state = TrafficLightState.RED;

  /**
   * Get the current state of the traffic light.
   *
   * @return The current state.
   */
  public TrafficLightState getState() {
    return state;
  }


  /**
   * Switch the state of the traffic light.
   *
   * @param state The desired state
   * @throws IllegalStateException If the traffic light can't switch to the desired state.
   */
  public void setState(TrafficLightState state) throws IllegalStateException {
    if (state == null) {
      throw new IllegalStateException("State can't be null");
    }
    if (this.state.equals(TrafficLightState.RED) && state.equals(TrafficLightState.GREEN)) {
      throw new IllegalStateException("Can't switch from red to green");
    }
    if (this.state.equals(TrafficLightState.GREEN) && state.equals(TrafficLightState.RED)) {
      throw new IllegalStateException("Can't switch from green to red");
    }
    this.state = state;
  }
}
