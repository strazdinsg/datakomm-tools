package no.ntnu;


import static no.ntnu.TvServer.PORT_NUMBER;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import no.ntnu.message.ChannelCountCommand;
import no.ntnu.message.Command;
import no.ntnu.message.CurrentChannelMessage;
import no.ntnu.message.ErrorMessage;
import no.ntnu.message.GetChannelCommand;
import no.ntnu.message.Message;
import no.ntnu.message.MessageSerializer;
import no.ntnu.message.SetChannelCommand;
import no.ntnu.message.TurnOffCommand;
import no.ntnu.message.TurnOnCommand;

/**
 * Remote control for a TV - a TCP client.
 */
public class RemoteControl {
  private BufferedReader socketReader;
  private PrintWriter socketWriter;
  private Socket socket;

  public static void main(String[] args) {
    RemoteControl remoteControl = new RemoteControl();
    remoteControl.run();
  }

  private void run() {
    if (connect()) {
      exchangeMessages();
    }
  }

  private void exchangeMessages() {
    sendAndExpectError(new GetChannelCommand());
    sendAndExpectError(new ChannelCountCommand());
    send(new TurnOnCommand());
    expectCurrentChannel(1);
    send(new ChannelCountCommand());
    send(new SetChannelCommand(12));
    expectCurrentChannel(12);
    sendAndExpectError(new SetChannelCommand(-1));
    sendAndExpectError(new SetChannelCommand(55));
    sendAndExpectError(new SetChannelCommand(0));
    expectCurrentChannel(12);
    send(new SetChannelCommand(4));
    expectCurrentChannel(4);
    send(new TurnOffCommand());
    sendAndExpectError(new GetChannelCommand());
    sendAndExpectError(new SetChannelCommand(2));
    send(new TurnOnCommand());
    expectCurrentChannel(4);
    send(new SetChannelCommand(1));
    expectCurrentChannel(1);
    send(new TurnOffCommand());
  }


  private boolean connect() {
    boolean connected = false;
    try {
      socket = new Socket("localhost", PORT_NUMBER);
      socketWriter = new PrintWriter(socket.getOutputStream(), true);
      socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      connected = true;
    } catch (IOException e) {
      System.err.println("Could not establish connection to the server: " + e.getMessage());
    }
    return connected;
  }

  private Message send(Command command) {
    Message response = sendCommandToServer(command);
    if (response == null) {
      System.err.println("Error: we expected a non-null response!");
    }
    return response;
  }

  private void sendAndExpectError(Command command) {
    Message response = sendCommandToServer(command);
    if (response instanceof ErrorMessage errorMessage) {
      System.out.println("  >>> Error as expected: " + errorMessage.getMessage());
    } else {
      System.err.println("Error: we expected an error, got " + response);
    }
  }

  private void expectCurrentChannel(int channel) {
    Message m = send(new GetChannelCommand());
    if (m instanceof CurrentChannelMessage currentChannelMessage) {
      if (currentChannelMessage.getChannel() == channel) {
        System.out.println("  Channel OK");
      } else {
        System.err.println("  Invalid channel!");
      }
    } else {
      System.err.println("  >> Expected current channel message, got something wrong!");
    }
  }

  private Message sendCommandToServer(Command command) {
    String serializedCommand = MessageSerializer.toString(command);
    System.out.println("Sending command: " + serializedCommand);
    Message serverResponse;
    try {
      socketWriter.println(serializedCommand);
      String rawServerResponse = socketReader.readLine();
      System.out.println("  >>> " + rawServerResponse);
      serverResponse = MessageSerializer.fromString(rawServerResponse);
      if (serverResponse == null) {
        throw new IllegalStateException("Could not deserialize message: " + rawServerResponse);
      }
    } catch (IOException e) {
      System.err.println("  >>> error while receiving response: " + e.getMessage());
      serverResponse = null;
    }
    return serverResponse;
  }
}
