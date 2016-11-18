package no.ntnu.datakomm;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A server that receives commands in Json Format, and can send also responses
 * in Json format
 * 
 * @author Girts Strazdins, girts.strazdins@gmail.com, 2016-10-30
 */
public class JsonServer {

    private static final int DEFAULT_PORT = 5000;
    public static final String SERVER_VERSION = "v1.0";
    private static final int MAX_CLIENT_THREADS = 10;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int port = DEFAULT_PORT;
        if (args.length > 0) {
            // Use first argument as port
            try {
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException e) {
                System.out.println("Invalid port: " + args[0]);
                port = DEFAULT_PORT;
            }
        }
        
        // Create new server
        JsonServer server = new JsonServer();
        
        // Start the server
        server.run(port);
    }

    public void run(int port) {
        ServerSocket s;
        Socket clientSocket;
        // Thread pool handler
        ExecutorService executor = Executors.newFixedThreadPool(MAX_CLIENT_THREADS);
        
        try {
            // 1) Create server socket
            s = new ServerSocket(port);
            System.out.println("Json Command Server started on port " + port 
                    + ". Waiting for connection...");

            while (true) {
                // 2) Accept client connection
                clientSocket = s.accept();
                System.out.println("Connection received from "
                        + clientSocket.getInetAddress().getHostName()
                        + " : " + clientSocket.getPort());

                // 3) Handle client in new thread
                ClientThread thread = new ClientThread();
                thread.init(clientSocket, this);
                executor.submit(thread);
            }

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }        
    }
}
