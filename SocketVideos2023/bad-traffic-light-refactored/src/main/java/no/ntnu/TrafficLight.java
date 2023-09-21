package no.ntnu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * An all-in-one Traffic light. Version 0.1 - all in one method. Very bad idea :(
 */
public class TrafficLight {
  String currentState = "red";
  ServerSocket serverSocket;
  private BufferedReader socketReader;
  private PrintWriter socketWriter;

  /**
   * Entrypoint for the application.
   *
   * @param args Command line arguments, not used.
   */
  public static void main(String[] args) {
    TrafficLight trafficLight = new TrafficLight();
    trafficLight.run();
  }

  private void run() {
    serverSocket = openListeningSocket();
    if (serverSocket == null) {
      return;
    }
    System.out.println("Running traffic light, listening to port 1236");

    while (true) {
      Socket socket = acceptNextClient();
      if (socket != null) {
        handleClient(socket);
      }
    }
  }

  private void handleClient(Socket socket) {
    String clientMessage;
    do {
      clientMessage = readClientMessage();
      String response;

      if (clientMessage != null) {
        if (clientMessage.equals("get")) {
          response = "state " + currentState;
        } else if (clientMessage.startsWith("set ")) {
          response = handleSetMessage(clientMessage);
        } else {
          response = "error Unknown command";
        }
        sendResponseToClient(response);
      }

    } while (clientMessage != null);
  }

  private ServerSocket openListeningSocket() {
    ServerSocket serverSocket = null;
    try {
      serverSocket = new ServerSocket(1236);
    } catch (IOException e) {
      System.err.println("Could not open the listening socket: " + e.getMessage());
    }
    return serverSocket;
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

  private String handleSetMessage(String clientMessage) {
    String desiredState = clientMessage.substring(4);
    String response;
    switch (desiredState) {
      case "yellow" -> {
        currentState = "yellow";
        response = "state yellow";
      }
      case "red" -> {
        if (!currentState.equals("green")) {
          currentState = "red";
          response = "state red";
        } else {
          response = "error Can't switch from red to green, must switch to yellow first";
        }
      }
      case "green" -> {
        if (!currentState.equals("red")) {
          currentState = "green";
          response = "state green";
        } else {
          response = "error Can't switch from green to red, must switch to yellow first";
        }
      }
      default -> response = "error Unknown color";
    }
    return response;
  }

  private String readClientMessage() {
    String clientMessage = null;
    try {
      clientMessage = socketReader.readLine();
    } catch (IOException e) {
      System.err.println("Client disconnected");
    }
    return clientMessage;
  }

  private void sendResponseToClient(String response) {
    socketWriter.println(response);
  }
}

