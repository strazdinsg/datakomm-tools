package no.ntnu.datakomm;

/**
 * A simple structure containing information for a TCP connection:
 * destination host address and TCP port
 *
 * Created by Girts Strazdins on 29/10/16.
 */
public class ConnectionInfo {
    private String host;
    private int port;

    public ConnectionInfo(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }
}
