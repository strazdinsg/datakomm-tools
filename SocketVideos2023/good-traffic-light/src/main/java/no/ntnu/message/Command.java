package no.ntnu.message;

import no.ntnu.TrafficLight;

/**
 * A generic command - sent from a remote client to the traffic light server.
 */
public abstract class Command implements Message {
  protected final TrafficLight trafficLight;

  protected Command(TrafficLight trafficLight) {
    this.trafficLight = trafficLight;
  }

  /**
   * Execute the command - perform necessary logic calls.
   *
   * @return A message containing the response of the command execution
   */
  public abstract Message execute();
}
