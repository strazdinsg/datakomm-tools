package no.ntnu;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import no.ntnu.command.AddCommand;
import no.ntnu.command.Command;
import no.ntnu.command.EchoCommand;
import no.ntnu.command.VersionCommand;

/**
 * Handle one TCP client connection.
 */
public class ClientHandler {
  private static final String VERSION_RESPONSE = "Server_V1.0";
  private final Socket clientSocket;
  private ObjectInputStream objectReader;
  private PrintWriter socketWriter;

  /**
   * Create a new client handler.
   *
   * @param clientSocket The TCP socket associated with this client
   */
  public ClientHandler(Socket clientSocket) {
    this.clientSocket = clientSocket;
    System.out.println("Client connected from " + clientSocket.getRemoteSocketAddress()
        + ", port " + clientSocket.getPort());
  }

  /**
   * Run the handling logic of this TCP client.
   */
  public void run() {
    if (establishStreams()) {
      handleClientRequests();
      closeSocket();
    }

    System.out.println("Exiting the handler of the client "
        + clientSocket.getRemoteSocketAddress());
  }

  /**
   * Establish the input and output streams of the socket.
   *
   * @return True on success, false on error
   */
  private boolean establishStreams() {
    boolean success = false;
    try {
      socketWriter = new PrintWriter(clientSocket.getOutputStream(), true);
      objectReader = new ObjectInputStream(clientSocket.getInputStream());
      success = true;
    } catch (IOException e) {
      System.err.println("Error while processing the client: " + e.getMessage());
    }
    return success;
  }

  private void handleClientRequests() {
    Command command;
    boolean shouldContinue;
    do {
      command = receiveClientCommand();
      shouldContinue = handleCommand(command);
    } while (shouldContinue);
  }

  /**
   * Receive one command from the client (over the TCP socket).
   *
   * @return The client command, null on error
   */
  private Command receiveClientCommand() {
    Command command = null;
    try {
      command = (Command) objectReader.readObject();
    } catch (IOException e) {
      System.err.println("Error while receiving data from the client: " + e.getMessage());
    } catch (ClassNotFoundException e) {
      System.err.println("An object of invalid class received: " + e.getMessage());
    }
    return command;
  }

  /**
   * Handle one command from the client.
   *
   * @param command A command sent by the client
   * @return True when the command is handled, and we should continue receiving next
   *     commands from the client.
   */
  private boolean handleCommand(Command command) {
    boolean shouldContinue = true;
    System.out.println("Command from the client: " + command);

    String response = null;

    if (command == null) {
      shouldContinue = false;
    } else {
      if (command instanceof VersionCommand) {
        response = VERSION_RESPONSE;
      } else if (command instanceof EchoCommand echoCommand) {
        response = echoCommand.getMessage();
      } else if (command instanceof AddCommand addCommand) {
        response = "" + addCommand.getSum();
      } else {
        response = "Unknown command";
      }
    }

    if (response != null) {
      sendToClient(response);
    }
    return shouldContinue;
  }

  private void sendToClient(String message) {
    try {
      socketWriter.println(message);
    } catch (Exception e) {
      System.err.println("Error while sending a message to the client: " + e.getMessage());
    }
  }

  private void closeSocket() {
    try {
      clientSocket.close();
    } catch (IOException e) {
      System.err.println("Error while closing socket for client "
          + clientSocket.getRemoteSocketAddress() + ", reason: " + e.getMessage());
    }
  }
}

