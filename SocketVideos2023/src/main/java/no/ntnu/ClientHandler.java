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
  private final Server server;
  private final Socket clientSocket;
  private BufferedReader socketReader;
  private PrintWriter socketWriter;

  /**
   * Create a new client handler.
   *
   * @param clientSocket The TCP socket associated with this client
   */
  public ClientHandler(Server server, Socket clientSocket) {
    this.clientSocket = clientSocket;
    this.server = server;
    System.out.println("Client connected from " + clientSocket.getRemoteSocketAddress()
        + ", port " + clientSocket.getPort());
  }

  /**
   * Run the handling logic of this TCP client.
   */
  public void run() {
    if (establishStreams()) {
      sendVersionToClient();
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

  private void sendVersionToClient() {
    sendToClient("Server_V1.0");
  }

  private void handleClientRequests() {
    String command;
    boolean shouldContinue = true;
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

    String response;

    if (command == null) {
      response = null;
    } else if (command.contains("Chuck Norris")) {
      response = "At your service, master!";
    } else if ("".equals(command)) {
      shouldContinue = false;
      response = "Closing the connection...";
    } else if ("shutdown".equals(command)) {
      response = "Server is shutting down...";
      server.shutdown();
      shouldContinue = false;
    } else {
      response = command.toUpperCase();
    }

    if (response != null) {
      sendToClient(response);
    }
    return shouldContinue;
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

