package no.ntnu.tv;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import no.ntnu.message.Message;

/**
 * Handles the TCP server socket(s).
 */
public class TvServer {
  public static final int PORT_NUMBER = 10025;
  private final TvLogic logic;

  boolean isTcpServerRunning;
  private final List<ClientHandler> connectedClients = new ArrayList<>();

  public TvServer(TvLogic logic) {
    this.logic = logic;
  }

  /**
   * Start TCP server for this TV.
   */
  public void startServer() {
    ServerSocket listeningSocket = openListeningSocket();
    System.out.println("Server listening on port " + PORT_NUMBER);
    if (listeningSocket != null) {
      isTcpServerRunning = true;
      while (isTcpServerRunning) {
        ClientHandler clientHandler = acceptNextClientConnection(listeningSocket);
        if (clientHandler != null) {
          connectedClients.add(clientHandler);
          clientHandler.start();
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

  private ClientHandler acceptNextClientConnection(ServerSocket listeningSocket) {
    ClientHandler clientHandler = null;
    try {
      Socket clientSocket = listeningSocket.accept();
      System.out.println("New client connected from " + clientSocket.getRemoteSocketAddress());
      clientHandler = new ClientHandler(clientSocket, this);
    } catch (IOException e) {
      System.err.println("Could not accept client connection: " + e.getMessage());
    }
    return clientHandler;
  }

  /**
   * Get the associated TV logic.
   *
   * @return The TV logic
   */
  public TvLogic getTvLogic() {
    return logic;
  }

  /**
   * Send a message to all currently connected clients.
   *
   * @param message The message to send
   */
  public void sendResponseToAllClients(Message message) {
    for (ClientHandler clientHandler : connectedClients) {
      clientHandler.sendToClient(message);
    }
  }

  public void clientDisconnected(ClientHandler clientHandler) {
    connectedClients.remove(clientHandler);
  }
}
