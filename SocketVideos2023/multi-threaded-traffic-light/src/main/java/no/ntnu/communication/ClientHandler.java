package no.ntnu.communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import no.ntnu.message.Command;
import no.ntnu.message.ErrorMessage;
import no.ntnu.message.Message;

/**
 * Handles one client connection - all the communication with that client.
 */
public class ClientHandler extends Thread {
  private final MessageSerializer serializer;
  private final Socket socket;
  private BufferedReader socketReader;
  private PrintWriter socketWriter;

  /**
   * Create a handler for one client.
   *
   * @param socket     The socket associated with that client
   * @param serializer The message serializer to use
   */
  public ClientHandler(Socket socket, MessageSerializer serializer) {
    this.serializer = serializer;
    this.socket = socket;
  }

  private boolean initializeStreams() {
    boolean success = false;
    try {
      socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      socketWriter = new PrintWriter(socket.getOutputStream(), true);
      success = true;
    } catch (IOException e) {
      System.err.println("Could not initialize streams for client");
    }
    return success;
  }

  /**
   * Start the processing of one client.
   */
  @Override
  public void run() {
    if (!initializeStreams()) {
      return;
    }

    System.out.println("Processing client on " + Thread.currentThread().getName());

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
