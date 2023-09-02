package no.ntnu;

import static no.ntnu.Server.UDP_PORT;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

/**
 * TCP Client.
 */
public class Client {
  private DatagramSocket clientSocket;
  private InetAddress serverAddress;


  /**
   * Run the web client.
   *
   * @param args Command line arguments. Not used.
   */
  public static void main(String[] args) {
    Client client = new Client();
    client.run();
  }

  private void run() {
    if (initializeSocket()) {
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

  private boolean initializeSocket() {
    boolean success = false;
    try {
      clientSocket = new DatagramSocket();
      serverAddress = InetAddress.getByName("localhost");
      success = true;
    } catch (SocketException e) {
      System.err.println("Could not open UDP socket: " + e.getMessage());
    } catch (UnknownHostException e) {
      System.err.println("Server address unknown: " + e.getMessage());
    }
    return success;
  }

  private void sendAndReceive(String command) {
    if (sendToServer(command)) {
      String response = receiveOneLineFromServer();
      if (response != null) {
        System.out.println("Server's response: " + response);
      }
    }
  }

  /**
   * Send a message to the TCP server.
   * We assume that the connection is already established.
   *
   * @param message The message to send
   * @return True when the message is successfully sent, false on error.
   */
  private boolean sendToServer(String message) {
    boolean sent = false;
    try {
      byte[] dataToSend = message.getBytes();
      DatagramPacket clientPacket = new DatagramPacket(dataToSend, dataToSend.length,
          serverAddress, UDP_PORT);
      clientSocket.send(clientPacket);
      sent = true;
    } catch (Exception e) {
      System.err.println("Error while sending the message: " + e.getMessage());
    }
    return sent;
  }

  /**
   * Receive one line of text from the server (the TCP socket).
   *
   * @return The received line or null on error.
   */
  private String receiveOneLineFromServer() {
    String response = null;
    try {
      byte[] responseData = new byte[100];
      DatagramPacket responsePacket = new DatagramPacket(responseData, responseData.length);
      clientSocket.receive(responsePacket);
      response = new String(responseData, 0, responsePacket.getLength(), StandardCharsets.UTF_8);
    } catch (IOException e) {
      System.err.println("Error while receiving data from the server: " + e.getMessage());
    }
    return response;
  }

}