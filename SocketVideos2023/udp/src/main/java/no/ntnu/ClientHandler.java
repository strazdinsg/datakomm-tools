package no.ntnu;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.charset.StandardCharsets;

/**
 * Handle one UDP client packet.
 */
public class ClientHandler {
  private final DatagramPacket clientPacket;
  private final DatagramSocket serverSocket;

  /**
   * Handler for one UDP client-packet.
   *
   * @param clientPacket The packet received from a client
   * @param serverSocket The server socket used to send a reply
   */
  public ClientHandler(DatagramPacket clientPacket, DatagramSocket serverSocket) {
    this.clientPacket = clientPacket;
    this.serverSocket = serverSocket;
    System.out.println(clientPacket.getAddress()
        + ":" + clientPacket.getPort() + " >");
  }

  /**
   * Run the handling logic of this TCP client.
   */
  public void run() {
    String command = extractClientCommand();
    handleCommand(command);
  }

  private String extractClientCommand() {
    return new String(clientPacket.getData(), 0, clientPacket.getLength(), StandardCharsets.UTF_8);
  }

  private String getVersionResponse() {
    return "Server_V1.0";
  }

  /**
   * Handle one command from the client.
   *
   * @param command A command sent by the client
   */
  private void handleCommand(String command) {
    System.out.println("    " + command);

    String response = null;

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

    if (response != null) {
      sendToClient(response);
    }
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
      byte[] responseData = message.getBytes();
      DatagramPacket responsePacket = new DatagramPacket(responseData, responseData.length,
          clientPacket.getAddress(), clientPacket.getPort());
      serverSocket.send(responsePacket);
    } catch (IOException e) {
      System.err.println("Error while sending a message to the client: " + e.getMessage());
    }
  }
}

