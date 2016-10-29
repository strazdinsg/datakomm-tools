package no.ntnu.datakomm;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * A TCP client that will run as a background HandlerThread - it will make a connection
 * with the TCP server, receive messages from the GUI thread and send them to the server
 * in background. In addition, it will forward all the messages coming from the server to
 * the GUI thread.
 *
 * Created by Girts Strazdins on 29/10/16.
 */
public class TcpClient extends HandlerThread {
    private static final String TAG = "TcpClient";
    // All the supported message types
    private static final int MSG_TYPE_CONNECT = 0; // Open TCP connection to the server
    private static final int MSG_TYPE_SEND = 1; // Send data to the server
    public static final int MSG_TYPE_FEEDBACK = 2; // Feedback from the background TCP thread to main GUI thread

    private Handler mHandler;
    private Handler responseHandler;
    private PrintWriter outputStream;
    private Socket socket;

    /**
     * The HandlerThread requires us to specify a name for this thread
     */
    public TcpClient(Handler responseHandler) {
        super(TAG);
        this.responseHandler = responseHandler;
    }

    /**
     * Initiate a connection to the server.
     * This method will be called from the main GUI thread. Yet the connection itself
     * will happen in the background. This method only enqueues a message saying "I would like
     * to open a connection".
     * The background thread will respond to the main GUI thread with a message stating whether
     * the connection is successful or not
     *
     * @param host
     * @param port
     */
    public void initiateConnectionToServer(String host, int port) {
        Log.i(TAG, "Initiating connection to server " + host + ":" + port);
        ConnectionInfo connInfo = new ConnectionInfo(host, port);
        if (mHandler != null) {
            mHandler.obtainMessage(MSG_TYPE_CONNECT, connInfo).sendToTarget();
        } else {
            sendErrorToGui("Error: mHandler was null, did not initiate connection");
        }
    }

    /**
     * Enqueue a new message that will be sent to the TCP server
     * This method will be called from the main GUI thread
     */
    public void enqueueMessage(String msg) {
        Log.i(TAG, "Enqueuing message for later transmission: " + msg);
        if (mHandler != null) {
            mHandler.obtainMessage(MSG_TYPE_SEND, msg).sendToTarget();
        } else {
            Log.i(TAG, "Error: mHandler was null, did not enqueue message");
        }
    }

    /**
     * Initialize a single message queue Handler
     * This method will be called in the background thread
     */
    @Override
    protected void onLooperPrepared() {
        Log.i(TAG, "onLooperPrepared");
        synchronized (this) {
            mHandler = new Handler() {
                public void handleMessage(Message msg) {
                    switch (msg.what) {
                        case MSG_TYPE_CONNECT:
                            ConnectionInfo connInfo = (ConnectionInfo) msg.obj;
                            connectToServer(connInfo);
                            break;
                        case MSG_TYPE_SEND:
                            // Extract message to send
                            String msgToSend = (String) msg.obj;
                            sendMessageToServer(msgToSend);
                            break;
                    }
                }
            };

            notifyAll(); // Notify everyone that the handler has been created
        }
    }

    /**
     * Wait until the handler is initialized.
     *
     * This method will be called in the main GUI thread. And it will wait until the
     * background thread finishes its job with handler initialization
     * @return true on success, false otherwise
     */
    public synchronized boolean waitForHandler() {
        Log.i(TAG, "waitForHandler");
        while (mHandler == null) {
            try {
                wait();
            } catch (InterruptedException e) {
                Log.i(TAG, "Could not wait for Handler initialization, interrupted");
                return false;
            }
        }
        return true;
    }

    /**
     * Start connection to the specified TCP server.
     * The result of the connection (success of failure) will be notified to the main GUI
     * thread by sending a message to its Handler
     *
     * This method will be called by the Handler in the background thread
     * @param connInfo connection info
     */
    private void connectToServer(ConnectionInfo connInfo) {
        String msg = "";
        boolean ok = true;
        Log.i(TAG, "Connecting to server " + connInfo.getHost() + ":" + connInfo.getPort());
        try {
            socket = new Socket(connInfo.getHost(), + connInfo.getPort());
            // The second parameter true means "auto flush"
            outputStream = new PrintWriter(socket.getOutputStream(), true);
            msg = "Connection to server successful";
        } catch (IOException e) {
            ok = false;
            msg = "Could not open socket to " + connInfo.getHost() + ":" + connInfo.getPort()
                    + ". Reason: " + e.getMessage();
        }

        sendFeedbackToGui(ok, msg);
    }

    /**
     * Send an enqueued message to the server. Add newline (\n) at the end
     * This method will fail if the TCP connection is not opened
     * On error the main GUI thread's Handler will be notified by a message. On success
     * there will be no notification.
     *
     * This method will be called by the Handler in the background thread
     * @param msg message as a JSON String
     */
    private void sendMessageToServer(String msg) {
        Log.i(TAG, "Sending message: " + msg);
        if (outputStream != null) {
            outputStream.println(msg);
        } else {
            sendErrorToGui("Error sending message: Stream not writable");
        }
    }

    /**
     * Send an error message to the main GUI thread
     * @param errMsg
     */
    private void sendErrorToGui(String errMsg) {
        sendFeedbackToGui(false, errMsg);
    }

    /**
     * Send feedback message to the main GUI thread
     * @param msg
     */
    private void sendFeedbackToGui(boolean ok, String msg) {
        if (ok) {
            Log.i(TAG, msg);
        } else {
            Log.e(TAG, msg);
        }
        if (responseHandler != null) {
            Feedback feedback = new Feedback(ok, msg);
            responseHandler.obtainMessage(MSG_TYPE_FEEDBACK, feedback).sendToTarget();
        }
    }

}
