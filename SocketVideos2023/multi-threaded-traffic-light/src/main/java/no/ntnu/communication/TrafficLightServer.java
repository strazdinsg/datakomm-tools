package no.ntnu.communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import no.ntnu.TrafficLight;
import no.ntnu.message.Command;
import no.ntnu.message.ErrorMessage;
import no.ntnu.message.Message;

/**
 * TCP server for the traffic light.
 */
public class TrafficLightServer {
  private final MessageSerializer serializer;
  ServerSocket serverSocket;
  private BufferedReader socketReader;
  private PrintWriter socketWriter;

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
        handleClient();
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
      socketReader = new BufferedReader(
          new InputStreamReader(clientSocket.getInputStream()));
      socketWriter = new PrintWriter(clientSocket.getOutputStream(), true);
      System.out.println("New client connected");
    } catch (IOException e) {
      System.err.println("Could not accept the next client: " + e.getMessage());
    }
    return clientSocket;
  }

  private void handleClient() {
    Message clientMessage;
    do {
      clientMessage = readClientMessage();

      Message response;
      if (clientMessage instanceof Command command) {
        response = command.execute();
      } else {
        response = new ErrorMessage("Invalid command received");
      }
      sendResponseToClient(response);

    } while (clientMessage != null);
  }

  private Message readClientMessage() {
    Message clientMessage = null;
    try {
      String rawClientMessage = socketReader.readLine();
      clientMessage = serializer.fromString(rawClientMessage);
    } catch (IOException e) {
      System.err.println("Client disconnected");
    }
    return clientMessage;
  }

  private void sendResponseToClient(Message response) {
    socketWriter.println(serializer.toString(response));
  }
}

