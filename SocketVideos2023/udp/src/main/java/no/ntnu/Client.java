package no.ntnu;

import static no.ntnu.Server.UDP_PORT;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

/**
 * UDP Client.
 */
public class Client {
  private static final String SERVER_HOST = "localhost";

  DatagramSocket udpSocket;
  private InetAddress serverIp;


  /**
   * Run the client application.
   *
   * @param args Command line arguments. Not used.
   */
  public static void main(String[] args) {
    Client client = new Client();
    client.run();
  }

  private void run() {
    if (initializeDatagramSocket()) {
      sendAndReceive("version");
      sendAndReceive("echo My name is Chuck Norris");
      sendAndReceive("echo");
      sendAndReceive("add 2 3");
      sendAndReceive("add 12 32");
      sendAndReceive("add 12 32 45");
      sendAndReceive("add ab 32");
      sendAndReceive("add 12 c");
      sendAndReceive("add 12c 44");
    }
    System.out.println("Exiting...");
  }

  private void sendAndReceive(String command) {
    if (sendToServer(command)) {
      String response = receiveFromServer();
      if (response != null) {
        System.out.println("Server's response: " + response);
      }
    }
  }

  /**
   * Prepare UDP socket for communication.
   *
   * @return True on success, false on error.
   */
  private boolean initializeDatagramSocket() {
    boolean success = false;
    try {
      udpSocket = new DatagramSocket();
      serverIp = InetAddress.getByName(SERVER_HOST);
      success = true;
    } catch (IOException e) {
      System.err.println("Could not create a UDP socket: " + e.getMessage());
    }
    return success;
  }

  /**
   * Send a message to the UDP server.
   *
   * @param message The message to send
   * @return True when the message is successfully sent, false on error.
   */
  private boolean sendToServer(String message) {
    boolean sent = false;
    try {
      byte[] dataToSend = message.getBytes();
      DatagramPacket packetToSend = new DatagramPacket(dataToSend, 0, dataToSend.length,
          serverIp, UDP_PORT);
      udpSocket.send(packetToSend);
      sent = true;
    } catch (IOException e) {
      System.err.println("Error while sending the message: " + e.getMessage());
    }
    return sent;
  }

  /**
   * Receive one line of text from the server (the TCP socket).
   *
   * @return The received line or null on error.
   */
  private String receiveFromServer() {
    String response = null;
    try {
      byte[] dataBuffer = new byte[200];
      DatagramPacket receivedPacket = new DatagramPacket(dataBuffer, dataBuffer.length);
      udpSocket.receive(receivedPacket);
      response = new String(receivedPacket.getData(), 0, receivedPacket.getLength(),
          StandardCharsets.UTF_8);
    } catch (IOException e) {
      System.err.println("Error while receiving data from the server: " + e.getMessage());
    }
    return response;
  }

}