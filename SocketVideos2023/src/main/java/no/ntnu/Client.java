package no.ntnu;

import static no.ntnu.Server.TCP_PORT;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * TCP Client.
 */
public class Client {
  private static final String SERVER_HOST = "localhost";
  private Socket socket;
  private PrintWriter socketWriter;
  private BufferedReader socketReader;


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
    if (connect()) {
      sendAndReceive("version");
      sendAndReceive("echo My name is Chuck Norris");
      sendAndReceive("echo");
      sendAndReceive("add 2 3");
      sendAndReceive("add 12 32");
      sendAndReceive("add 12 32 45");
      sendAndReceive("add ab 32");
      sendAndReceive("add 12 c");
      sendAndReceive("add 12c 44");
      disconnect();
    }
    System.out.println("Exiting...");
  }

  private void receiveVersion() {
    String version = receiveOneLineFromServer();
    System.out.println("Version: " + version);
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
   * Establish a connection to a TCP server (web server).
   *
   * @return True on success, false on error.
   */
  private boolean connect() {
    boolean success = false;
    try {
      socket = new Socket(SERVER_HOST, TCP_PORT);
      socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      socketWriter = new PrintWriter(socket.getOutputStream(), true);
      System.out.println("Connection established");
      success = true;
    } catch (IOException e) {
      System.err.println("Could not connect to the server: " + e.getMessage());
    }
    return success;
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
      socketWriter.println(message);
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
      response = socketReader.readLine();
    } catch (IOException e) {
      System.err.println("Error while receiving data from the server: " + e.getMessage());
    }
    return response;
  }

  /**
   * Close the TCP connection.
   */
  private void disconnect() {
    try {
      if (socket != null) {
        socket.close();
        System.out.println("Socket closed");
      } else {
        System.err.println("Can't close a socket which has not been open");
      }
    } catch (IOException e) {
      System.err.println("Could not close the socket: " + e.getMessage());
    }
  }
}