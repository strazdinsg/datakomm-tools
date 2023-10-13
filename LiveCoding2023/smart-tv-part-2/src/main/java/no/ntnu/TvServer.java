package no.ntnu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Handles the TCP server socket(s).
 */
public class TvServer {
  public static final int PORT_NUMBER = 10025;
  public static final String CHANNEL_COUNT_COMMAND = "c";
  public static final String TURN_ON_COMMAND = "1";
  public static final String TURN_OFF_COMMAND = "0";
  public static final String GET_CHANNEL_COMMAND = "g";
  public static final String SET_CHANNEL_COMMAND = "s";
  public static final String OK_RESPONSE = "o";

  boolean isTcpServerRunning;
  private BufferedReader socketReader;
  private PrintWriter socketWriter;

  /**
   * Start TCP server for this TV.
   */
  private void startServer() {
    ServerSocket listeningSocket = openListeningSocket();
    System.out.println("Server listening on port " + PORT_NUMBER);
    if (listeningSocket != null) {
      isTcpServerRunning = true;
      while (isTcpServerRunning) {
        Socket clientSocket = acceptNextClientConnection(listeningSocket);
        if (clientSocket != null) {
          System.out.println("New client connected from " + clientSocket.getRemoteSocketAddress());
          handleClient(clientSocket);
        }
      }
    }
  }

  private ServerSocket openListeningSocket() {
    ServerSocket listeningSocket = null;
    try {
      listeningSocket = new ServerSocket(PORT_NUMBER);
    } catch (IOException e) {
      System.err.println("Could not open server socket: " + e.getMessage());
    }
    return listeningSocket;
  }

  private Socket acceptNextClientConnection(ServerSocket listeningSocket) {
    Socket clientSocket = null;
    try {
      clientSocket = listeningSocket.accept();
      socketReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
      socketWriter = new PrintWriter(clientSocket.getOutputStream(), true);

    } catch (IOException e) {
      System.err.println("Could not accept client connection: " + e.getMessage());
    }
    return clientSocket;
  }

  private void handleClient(Socket clientSocket) {
    String response;
    do {
      String clientRequest = readClientRequest();
      System.out.println("Received from client: " + clientRequest);
      response = handleClientRequest(clientRequest);
      if (response != null) {
        sendResponseToClient(response);
      }
    } while (response != null);
  }

  /**
   * Read one message from the TCP socket - from the client.
   *
   * @return The received client message, or null on error
   */
  private String readClientRequest() {
    String clientRequest = null;
    try {
      clientRequest = socketReader.readLine();
    } catch (IOException e) {
      System.err.println("Could not receive client request: " + e.getMessage());
    }
    return clientRequest;
  }


  private String handleClientRequest(String clientRequest) {
    String response = null;

    if (clientRequest != null) {
      switch (clientRequest) {
        case TURN_ON_COMMAND:
          response = handleTurnOnCommand();
          break;
        case TURN_OFF_COMMAND:
          response = handleTurnOffCommand();
          break;
        case CHANNEL_COUNT_COMMAND:
          response = handleChannelCountCommand();
          break;
        case GET_CHANNEL_COMMAND:
          response = handleGetChannelCommand();
          break;
        default:
          if (clientRequest.startsWith(SET_CHANNEL_COMMAND)) {
            String desiredChannel = clientRequest.substring(1);
            response = handleSetChannelCommand(desiredChannel);
          }
      }
    }

    return response;
  }

  private String handleTurnOnCommand() {
    isTvOn = true;
    return OK_RESPONSE;
  }

  private String handleTurnOffCommand() {
    isTvOn = false;
    return OK_RESPONSE;
  }

  private String handleChannelCountCommand() {
    String response;
    if (isTvOn) {
      response = "c" + numberOfChannels;
    } else {
      response = "eMust turn the TV on first";
    }
    return response;
  }

  private String handleGetChannelCommand() {
    String response;
    if (isTvOn) {
      response = "C" + currentChannel;
    } else {
      response = "eMust turn the TV on first";
    }
    return response;
  }

  private String handleSetChannelCommand(String desiredChannelString) {
    String response;
    Integer desiredChannel = parseInteger(desiredChannelString);
    if (desiredChannel != null && desiredChannel > 0 && desiredChannel <= numberOfChannels) {
      if (isTvOn) {
        currentChannel = desiredChannel;
        response = OK_RESPONSE;
      } else {
        response = "eMust turn the TV on first";
      }
    } else {
      response = "eInvalid channel number";
    }
    return response;
  }

  private Integer parseInteger(String s) {
    Integer number = null;
    try {
      number = Integer.parseInt(s);
    } catch (NumberFormatException e) {
      System.out.println("Can't parse string as int: " + s);
    }
    return number;
  }

  /**
   * Send a response from the server to the client, over the TCP socket.
   *
   * @param response The response to send to the client, NOT including the newline
   */
  private void sendResponseToClient(String response) {
    socketWriter.println(response);
  }

}
