package no.ntnu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * A remote control sending commands to the traffic light. Version 0.1 - very bad design. :(
 */
public class RemoteControl {
  /**
   * Entrypoint for the application.
   *
   * @param args Command line arguments, not used.
   */
  public static void main(String[] args) {
    try {
      Socket socket = new Socket("localhost", 1236);
      PrintWriter socketWriter = new PrintWriter(socket.getOutputStream(), true);
      BufferedReader socketReader = new BufferedReader(
          new InputStreamReader(socket.getInputStream()));

      socketWriter.println("get");
      String response = socketReader.readLine();
      String color = response.substring(6);
      System.out.println("Traffic light is " + color);

      socketWriter.println("set blabla");
      response = socketReader.readLine();
      System.out.println("Tried to set blabla, got " + response);

      socketWriter.println("set green");
      response = socketReader.readLine();
      System.out.println("Tried to set green, got " + response);

      socketWriter.println("set yellow");
      response = socketReader.readLine();
      System.out.println("Tried to set yellow, got " + response);

      socketWriter.println("set green");
      response = socketReader.readLine();
      System.out.println("Tried to set green, got " + response);

    } catch (IOException e) {
      System.err.println("Something went wrong: " + e.getMessage());
    }
  }
}
