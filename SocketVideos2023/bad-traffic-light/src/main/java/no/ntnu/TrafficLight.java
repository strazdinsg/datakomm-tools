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
  /**
   * Entrypoint for the application.
   *
   * @param args Command line arguments, not used.
   */
  public static void main(String[] args) {
    String currentState = "red";

    try {
      // Open a socket
      ServerSocket serverSocket = new ServerSocket(1236);
      System.out.println("Running traffic light, listening to port 1236");

      while (true) {
        // Accept the next client
        Socket clientSocket = serverSocket.accept();
        BufferedReader socketReader = new BufferedReader(
            new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter socketWriter = new PrintWriter(clientSocket.getOutputStream(), true);
        System.out.println("New client connected");

        // Read client message
        String response;
        String clientMessage;
        do {
          try {
            clientMessage = socketReader.readLine();
          } catch (IOException e) {
            System.err.println("Client disconnected");
            clientMessage = null;
          }

          if (clientMessage != null) {
            if (clientMessage.equals("get")) {
              // GET message
              response = "state " + currentState;
            } else if (clientMessage.startsWith("set ")) {
              // SET message, check the logic

              String desiredState = clientMessage.substring(4);
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
            } else {
              response = "error Unknown command";
            }

            // Send response to the client
            socketWriter.println(response);
          }

        } while (clientMessage != null);
      }

    } catch (IOException e) {
      System.err.println("Something went wrong with socket communication: " + e.getMessage());
    }
  }
}

