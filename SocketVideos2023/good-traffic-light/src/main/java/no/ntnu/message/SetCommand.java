package no.ntnu.message;

import no.ntnu.TrafficLight;
import no.ntnu.TrafficLightState;

/**
 * A command requesting to set a new state for the traffic light.
 */
public class SetCommand extends Command {
  private final TrafficLightState desiredState;

  /**
   * Create a new command.
   *
   * @param trafficLight The traffic light which will updated
   * @param desiredState The desired state to be set
   */
  public SetCommand(TrafficLight trafficLight, TrafficLightState desiredState) {
    super(trafficLight);
    this.desiredState = desiredState;
  }

  @Override
  public Message execute() {
    Message response;
    try {
      trafficLight.setState(desiredState);
      response = new StateMessage(trafficLight.getState());
    } catch (IllegalStateException e) {
      response = new ErrorMessage("Can't change state: " + e.getMessage());
    }
    return response;
  }

  /**
   * Get the desired state.
   *
   * @return The state that this command will set for the traffic light
   */
  public TrafficLightState getDesiredState() {
    return desiredState;
  }
}
