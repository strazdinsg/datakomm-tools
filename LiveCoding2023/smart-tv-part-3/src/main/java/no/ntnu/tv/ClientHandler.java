package no.ntnu.tv;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import no.ntnu.message.Command;
import no.ntnu.message.Message;
import no.ntnu.message.MessageSerializer;

/**
 * Handler for one specific client connection (TCP).
 */
public class ClientHandler extends Thread {
  private final TvLogic logic;
  private final Socket socket;
  private final BufferedReader socketReader;
  private final PrintWriter socketWriter;

  /**
   * Create a new client handler.
   *
   * @param socket Socket associated with this client
   * @param logic  The TV logic which will process the commands
   * @throws IOException When something goes wrong with establishing the input or output streams
   */
  public ClientHandler(Socket socket, TvLogic logic) throws IOException {
    this.logic = logic;
    this.socket = socket;
    socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    socketWriter = new PrintWriter(socket.getOutputStream(), true);
  }

  /**
   * Run the client handling logic.
   */
  @Override
  public void run() {
    Message response;
    do {
      Command clientCommand = readClientRequest();
      if (clientCommand != null) {
        System.out.println("Received a " + clientCommand.getClass().getSimpleName());
        response = clientCommand.execute(logic);
        if (response != null) {
          sendResponseToClient(response);
        }
      } else {
        response = null;
      }
    } while (response != null);
    System.out.println("Client " + socket.getRemoteSocketAddress() + " leaving");
  }

  /**
   * Read one message from the TCP socket - from the client.
   *
   * @return The received client message, or null on error
   */
  private Command readClientRequest() {
    Message clientCommand = null;
    try {
      String rawClientRequest = socketReader.readLine();
      clientCommand = MessageSerializer.fromString(rawClientRequest);
      if (!(clientCommand instanceof Command)) {
        if (clientCommand != null) {
          System.err.println("Wrong message from the client: " + clientCommand);
        }
        clientCommand = null;
      }
    } catch (IOException e) {
      System.err.println("Could not receive client request: " + e.getMessage());
    }
    return (Command) clientCommand;
  }

  /**
   * Send a response from the server to the client, over the TCP socket.
   *
   * @param response The response to send to the client, NOT including the newline
   */
  private void sendResponseToClient(Message response) {
    socketWriter.println(MessageSerializer.toString(response));
  }

}
