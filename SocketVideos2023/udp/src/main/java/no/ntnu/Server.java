package no.ntnu;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * A UDP server, handles multiple clients.
 */
public class Server {
  public static final int UDP_PORT = 1235;
  private DatagramSocket serverSocket;
  private boolean isRunning;

  public static void main(String[] args) {
    Server server = new Server();
    server.run();
  }

  private void run() {
    if (openListeningSocket()) {
      isRunning = true;
      while (isRunning) {
        DatagramPacket clientPacket = receiveNextClientPacket();
        if (clientPacket != null) {
          ClientHandler clientHandler = new ClientHandler(clientPacket, serverSocket);
          clientHandler.run();
        }
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
      serverSocket = new DatagramSocket(UDP_PORT);
      System.out.println("Server listening on port " + UDP_PORT);
      success = true;
    } catch (IOException e) {
      System.err.println("Could not open a listening socket on port " + UDP_PORT
          + ", reason: " + e.getMessage());
    }
    return success;
  }

  private DatagramPacket receiveNextClientPacket() {
    byte[] dataBuffer = new byte[1024];
    DatagramPacket packet = new DatagramPacket(dataBuffer, dataBuffer.length);
    try {
      serverSocket.receive(packet);
    } catch (IOException e) {
      System.err.println("Could not receive data from a client: " + e.getMessage());
      packet = null;
    }
    return packet;
  }

  /**
   * Shut down the server.
   */
  public void shutdown() {
    isRunning = false;
  }
}
