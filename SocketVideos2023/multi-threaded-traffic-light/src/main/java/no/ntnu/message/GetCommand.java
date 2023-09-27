package no.ntnu.message;

import no.ntnu.TrafficLight;

/**
 * A command requesting to get (read) the current state of the traffic light.
 */
public class GetCommand extends Command {
  public GetCommand(TrafficLight trafficLight) {
    super(trafficLight);
  }

  @Override
  public Message execute() {
    return new StateMessage(trafficLight.getState());
  }
}
