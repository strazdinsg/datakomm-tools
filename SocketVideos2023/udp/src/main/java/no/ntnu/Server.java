package no.ntnu;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * A UDP server, handles multiple clients.
 */
public class Server {
  public static final int UDP_PORT = 1235;
  private boolean isRunning;
  private DatagramSocket udpSocket;

  public static void main(String[] args) {
    Server server = new Server();
    server.run();
  }

  private void run() {
    if (openListeningSocket()) {
      isRunning = true;
      while (isRunning) {
        DatagramPacket clientDatagram = receiveClientDatagram();
        if (clientDatagram != null) {
          DatagramHandler datagramHandler = new DatagramHandler(udpSocket, clientDatagram);
          datagramHandler.run();
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
      udpSocket = new DatagramSocket(UDP_PORT);
      System.out.println("Server listening on port " + UDP_PORT);
      success = true;
    } catch (IOException e) {
      System.err.println("Could not open a listening socket on port " + UDP_PORT
          + ", reason: " + e.getMessage());
    }
    return success;
  }

  private DatagramPacket receiveClientDatagram() {
    byte[] receivedData = new byte[200];
    DatagramPacket clientDatagram = new DatagramPacket(receivedData, receivedData.length);
    try {
      udpSocket.receive(clientDatagram);
    } catch (IOException e) {
      System.err.println("Error while receiving client datagram: " + e.getMessage());
      clientDatagram = null;
    }
    return clientDatagram;
  }

}
