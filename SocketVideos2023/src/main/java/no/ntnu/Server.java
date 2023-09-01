package no.ntnu;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * A TCP server, handles multiple clients.
 */
public class Server {
  public static final int TCP_PORT = 1235;
  private ServerSocket serverSocket;
  private boolean isRunning;

  public static void main(String[] args) {
    Server server = new Server();
    server.run();
  }

  private void run() {
    if (openListeningSocket()) {
      isRunning = true;
      while (isRunning) {
        Socket clientSocket = acceptNextClient();
        ClientHandler clientHandler = new ClientHandler(this, clientSocket);
        clientHandler.run();
      }
    }

    System.out.println("Server exiting...");
  }


  /**
   * Open a listening TCP socket.
   *
   * @return True on success, false on error.
   */
  private boolean openListeningSocket() {
    boolean success = false;
    try {
      serverSocket = new ServerSocket(TCP_PORT);
      System.out.println("Server listening on port " + TCP_PORT);
      success = true;
    } catch (IOException e) {
      System.err.println("Could not open a listening socket on port " + TCP_PORT
          + ", reason: " + e.getMessage());
    }
    return success;
  }

  private Socket acceptNextClient() {
    Socket clientSocket = null;
    try {
      clientSocket = serverSocket.accept();
    } catch (IOException e) {
      System.err.println("Could not accept the next client: " + e.getMessage());
    }
    return clientSocket;
  }

  /**
   * Shut down the server.
   */
  public void shutdown() {
    isRunning = false;
  }
}
