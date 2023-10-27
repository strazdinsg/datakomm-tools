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
import no.ntnu.message.TvStateMessage;

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
   * Receive incoming messages from the server, in another thread.
   */
  public void startListeningThread(ClientMessageListener listener) {
    new Thread(() -> {
      Message message = null;
      do {
        try {
          if (socketReader != null) {
            String rawMessage = socketReader.readLine();
            message = MessageSerializer.fromString(rawMessage);
            handleIncomingMessage(message, listener);
          } else {
            message = null;
          }
        } catch (IOException e) {
          System.err.println("Error while receiving incoming message: " + e.getMessage());
        }
      } while (message != null);
    }).start();
  }

  private void handleIncomingMessage(Message message, ClientMessageListener listener) {
    if (message instanceof TvStateMessage tvStateMessage) {
      listener.handleTvStateChange(tvStateMessage.isOn());
    }
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
   * @return True if the command was sent successfully, false on error
   */
  public boolean sendCommand(Command command) {
    boolean sent = false;
    if (socketWriter != null && socketReader != null) {
      try {
        socketWriter.println(MessageSerializer.toString(command));
        sent = true;
      } catch (Exception e) {
        System.err.println("Could not send a command: " + e.getMessage());
      }
    }
    return sent;
  }
}
