package no.ntnu.datakomm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

/**
 * A thread handling user commands
 * 
 * @author Girts Strazdins, girts.strazdins@gmail.com, 2016-10-30
 */
public class ClientThread implements Runnable {

    Socket clientSocket;
    PrintStream out = null;
    BufferedReader in = null;    
    
    JsonServer server;
    PeriodicSenderThread senderThread;

    /**
     * Initialize Client connection, do not start it
     * @param clientSocket
     * @param server
     * @throws IOException 
     */
    public void init(Socket clientSocket, JsonServer server) throws IOException {
        this.clientSocket = clientSocket;
        // Get Input and Output streams
        out = new PrintStream(clientSocket.getOutputStream(), true);        
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        this.server = server;
        
        // Warning: do not use the "out" variable directly. 
        // Always use this.sendResponse()!
        
        // Create another thread that will simulate periodic sensor reading
        // and will send periodic messages to the client
        senderThread = new PeriodicSenderThread(this);
        senderThread.start();
    }

    /**
     * Get Welcome message
     * @return 
     */
    public String getWelcomeMessage() {
        JsonMessage msg = new JsonMessage();
        msg.setType(MsgType.SERVER_INFO);
        msg.addArgument(JsonServer.SERVER_VERSION);
        return msg.toJson();
    }
    
    @Override
    public void run() {
        sendResponse(getWelcomeMessage());
        
        // We will send a response with static type, echo back received parameters
        JsonMessage responseMsg = new JsonMessage();
        responseMsg.setType(MsgType.CMD_EXECUTED);
        
        // Process commands one by one
        String line;
        while ((line = readLine()) != null) {
            System.out.println("Got line: " + line);
            JsonMessage cmd = JsonMessage.fromJson(line);
            if (cmd != null) {
                System.out.println("Cmd type = " + cmd.getType() + ", args:");
                for (String arg : cmd.getArguments()) {
                    System.out.println(arg);
                }
                
                // Send back response with the same arguments
                responseMsg.setArguments(cmd.getArguments());
                out.println(responseMsg.toJson());
                
            } else {
                System.out.println("Something wrong with parsing client input: "
                    + line);
            }
        }
        
        // Terminate the connection
        shutdown();
    }

    /**
     * Read a line of input from the client
     * @return 
     */
    private String readLine() {
        try {
            return in.readLine();            
        } catch (IOException ex) {
            return null;
        }
    }

    /**
     * A thread-safe method to send a response to the client (output stream
     * of the socket)
     * @param response 
     */
    public synchronized void sendResponse(String response) {
        if (out != null) {
            out.println(response);
        }
    }
    
    private void shutdown() {
        System.out.println("Closing connection to "
                + clientSocket.getInetAddress().getHostName()
                + " : " + clientSocket.getPort());
        try {
            if (in != null) {
                in.close();
                in = null;
            }
            if (out != null) {
                out.close();
                out = null;
            }
            if (clientSocket != null) {
                clientSocket.close();
                clientSocket = null;
            }
        } catch (IOException ex) {
            System.out.println("Unable to close connection: " + ex.getMessage());
        }
        System.out.println("Connection successfully closed");
        senderThread.stopRunning();
    }

}
