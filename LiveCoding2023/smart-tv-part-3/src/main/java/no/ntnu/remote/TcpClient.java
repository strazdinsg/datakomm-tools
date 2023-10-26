package no.ntnu.remote;

import static no.ntnu.tv.TvServer.PORT_NUMBER;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import no.ntnu.message.Command;
import no.ntnu.message.Message;
import no.ntnu.message.MessageSerializer;

/**
 * TCP client logic for a remote control.
 */
public class TcpClient {
  private static final String SERVER_HOST = "localhost";
  private Socket socket;
  private PrintWriter socketWriter;
  private BufferedReader socketReader;

  /**
   * Start the TCP client - connect to the TV (server).
   */
  public boolean start() {
    boolean connected = false;
    try {
      socket = new Socket(SERVER_HOST, PORT_NUMBER);
      socketWriter = new PrintWriter(socket.getOutputStream(), true);
      socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      connected = true;
    } catch (IOException e) {
      System.err.println("Could not connect to the server: " + e.getMessage());
    }
    return connected;
  }

  /**
   * Stop the TCP client - close connection to the TV (server).
   */
  public void stop() {
    if (socket != null) {
      try {
        socket.close();
        socket = null;
        socketReader = null;
        socketWriter = null;
      } catch (IOException e) {
        System.err.println("Could not close the socket: " + e.getMessage());
      }
    }
  }

  /**
   * Send a command to the TV.
   *
   * @param command The command to send
   * @return The response from the TV
   */
  public Message sendCommand(Command command) {
    Message response = null;
    if (socketWriter != null && socketReader != null) {
      try {
        socketWriter.println(MessageSerializer.toString(command));
        String rawResponse = socketReader.readLine();
        response = MessageSerializer.fromString(rawResponse);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
    return response;
  }
}
