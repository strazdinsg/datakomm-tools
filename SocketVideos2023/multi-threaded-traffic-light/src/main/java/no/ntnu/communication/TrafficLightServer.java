package no.ntnu.communication;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import no.ntnu.TrafficLight;

/**
 * TCP server for the traffic light.
 */
public class TrafficLightServer {
  private final MessageSerializer serializer;
  ServerSocket serverSocket;

  public TrafficLightServer(TrafficLight trafficLight) {
    this.serializer = new MessageSerializer(trafficLight);
  }


  /**
   * Run the TCP server.
   */
  public void run() {
    serverSocket = openListeningSocket();
    if (serverSocket == null) {
      return;
    }
    System.out.println("Running traffic light, listening to port 1236");

    while (true) {
      Socket socket = acceptNextClient();
      if (socket != null) {
        ClientHandler clientHandler = new ClientHandler(socket, serializer);
        clientHandler.start();
      }
    }
  }

  private ServerSocket openListeningSocket() {
    ServerSocket socket = null;
    try {
      socket = new ServerSocket(1236);
    } catch (IOException e) {
      System.err.println("Could not open the listening socket: " + e.getMessage());
    }
    return socket;
  }

  private Socket acceptNextClient() {
    Socket clientSocket = null;
    try {
      clientSocket = serverSocket.accept();
      System.out.println("New client connected");
    } catch (IOException e) {
      System.err.println("Could not accept the next client: " + e.getMessage());
    }
    return clientSocket;
  }
}

