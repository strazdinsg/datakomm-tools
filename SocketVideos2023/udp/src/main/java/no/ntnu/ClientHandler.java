package no.ntnu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Handle one TCP client connection.
 */
public class ClientHandler {
  private final Socket clientSocket;
  private BufferedReader socketReader;
  private PrintWriter socketWriter;

  /**
   * Create a new client handler.
   *
   * @param clientSocket The TCP socket associated with this client
   */
  public ClientHandler(Socket clientSocket) {
    this.clientSocket = clientSocket;
    System.out.println("Client connected from " + clientSocket.getRemoteSocketAddress()
        + ", port " + clientSocket.getPort());
  }

  /**
   * Run the handling logic of this TCP client.
   */
  public void run() {
    if (establishStreams()) {
      handleClientRequests();
      closeSocket();
    }

    System.out.println("Exiting the handler of the client "
        + clientSocket.getRemoteSocketAddress());
  }

  /**
   * Establish the input and output streams of the socket.
   *
   * @return True on success, false on error
   */
  private boolean establishStreams() {
    boolean success = false;
    try {
      socketReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
      socketWriter = new PrintWriter(clientSocket.getOutputStream(), true);
      success = true;
    } catch (IOException e) {
      System.err.println("Error while processing the client: " + e.getMessage());
    }
    return success;
  }

  private String getVersionResponse() {
    return "Server_V1.0";
  }

  private void handleClientRequests() {
    String command;
    boolean shouldContinue;
    do {
      command = receiveClientCommand();
      shouldContinue = handleCommand(command);
    } while (shouldContinue);
  }

  /**
   * Receive one command from the client (over the TCP socket).
   *
   * @return The client command, null on error
   */
  private String receiveClientCommand() {
    String command = null;
    try {
      command = socketReader.readLine();
    } catch (IOException e) {
      System.err.println("Error while receiving data from the client: " + e.getMessage());
    }
    return command;
  }

  /**
   * Handle one command from the client.
   *
   * @param command A command sent by the client
   * @return True when the command is handled, and we should continue receiving next
   *     commands from the client.
   */
  private boolean handleCommand(String command) {
    boolean shouldContinue = true;
    System.out.println("Command from the client: " + command);

    String response = null;

    if (command == null) {
      shouldContinue = false;
    } else {
      String[] commandParts = command.split(" ", 2);
      if (commandParts.length >= 1) {
        String commandType = commandParts[0];
        switch (commandType) {
          case "version":
            response = getVersionResponse();
            break;
          case "echo":
            response = handleEchoCommand(commandParts);
            break;
          case "add":
            response = handleAddCommand(commandParts);
            break;
          default:
            response = "Unknown command";
        }
      }
    }

    if (response != null) {
      sendToClient(response);
    }
    return shouldContinue;
  }

  private String handleAddCommand(String[] commandParts) {
    String response;
    if (commandParts.length >= 2) {
      String[] xy = commandParts[1].split(" ");
      if (xy.length == 2) {
        Integer x = parseInteger(xy[0]);
        Integer y = parseInteger(xy[1]);
        if (x != null && y != null) {
          response = "" + (x + y);
        } else {
          response = "Invalid add-command: the arguments are not integers";
        }
      } else {
        response = "Invalid number of arguments for the add-command, two expected";
      }
    } else {
      response = "Invalid add-command format: the x and y arguments are missing!";
    }
    return response;
  }

  private static String handleEchoCommand(String[] commandParts) {
    String response;
    if (commandParts.length >= 2) {
      response = commandParts[1]; // Repeat the original message from the client
    } else {
      response = "Invalid echo-command format: the message is missing!";
    }
    return response;
  }

  /**
   * Interpret the parameter s as an integer.
   *
   * @param s The string containing an integer
   * @return The integer value of s, or null if it is not a valid integer
   */
  private Integer parseInteger(String s) {
    Integer result = null;
    try {
      result = Integer.valueOf(s);
    } catch (NumberFormatException e) {
      System.err.println("Invalid number format: " + s);
    }
    return result;
  }

  private void sendToClient(String message) {
    try {
      socketWriter.println(message);
    } catch (Exception e) {
      System.err.println("Error while sending a message to the client: " + e.getMessage());
    }
  }

  private void closeSocket() {
    try {
      clientSocket.close();
    } catch (IOException e) {
      System.err.println("Error while closing socket for client "
          + clientSocket.getRemoteSocketAddress() + ", reason: " + e.getMessage());
    }
  }
}

