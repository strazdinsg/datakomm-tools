package no.ntnu.tv;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import no.ntnu.message.Command;
import no.ntnu.message.CurrentChannelMessage;
import no.ntnu.message.Message;
import no.ntnu.message.MessageSerializer;
import no.ntnu.message.TvStateMessage;

/**
 * Handler for one specific client connection (TCP).
 */
public class ClientHandler extends Thread {
  private final Socket socket;
  private final BufferedReader socketReader;
  private final PrintWriter socketWriter;
  private final TvServer server;

  /**
   * Create a new client handler.
   *
   * @param socket Socket associated with this client
   * @param server Reference to the main TCP server class
   * @throws IOException When something goes wrong with establishing the input or output streams
   */
  public ClientHandler(Socket socket, TvServer server) throws IOException {
    this.server = server;
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
        response = clientCommand.execute(server.getTvLogic());
        if (response != null) {
          if (isBroadcastMessage(response)) {
            server.sendResponseToAllClients(response);
          } else {
            sendToClient(response);
          }
        }
      } else {
        response = null;
      }
    } while (response != null);
    System.out.println("Client " + socket.getRemoteSocketAddress() + " leaving");
    server.clientDisconnected(this);
  }

  private boolean isBroadcastMessage(Message response) {
    return response instanceof TvStateMessage
        || response instanceof CurrentChannelMessage;
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
   * @param message The message to send to the client
   */
  public void sendToClient(Message message) {
    socketWriter.println(MessageSerializer.toString(message));
  }

}
