package no.ntnu.remote;

import no.ntnu.remote.gui.GuiApp;

/**
 * Run one TV remote, with Graphical User Interface (GUI).
 */
public class RemoteRunner {
  /**
   * Main entrypoint of the application.
   *
   * @param args Command line arguments, not used
   */
  public static void main(String[] args) {
    TcpClient tcpClient = new TcpClient();
    if (tcpClient.start()) {
      GuiApp.startApp(tcpClient);
      // This code is reached when the GUI is stopped
      tcpClient.stop();
    } else {
      System.err.println("Could not connect to a smart TV!");
    }
  }
}
