package no.ntnu.run;

import no.ntnu.TrafficLight;
import no.ntnu.communication.TrafficLightServer;

/**
 * Runs the Traffic light, with a TCP server.
 */
public class TrafficLightRunner {
  /**
   * Entrypoint for the application.
   *
   * @param args Command line arguments, not used.
   */
  public static void main(String[] args) {
    TrafficLight trafficLight = new TrafficLight();
    TrafficLightServer server = new TrafficLightServer(trafficLight);
    server.run();
  }
}
