package no.ntnu;

/**
 * Run the TCP server.
 */
public class ServerRunner {
  public static void main(String[] args) {
    SmartTv tv = new SmartTv(13);
    tv.startServer();
  }
}
