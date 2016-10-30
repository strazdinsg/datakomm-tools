package no.ntnu.datakomm;

import android.os.Handler;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * A class that will receive data from the TCP server and pass it to the TcpClient handler
 * Created by Girts Strazdins on 30/10/16.
 */
public class ReceivingThread extends Thread {
    private static final String TAG = "ReceivingThread";

    private Handler handler;
    private BufferedReader input;
    private boolean running;

    public ReceivingThread(InputStream inputStream, Handler handler) {
        this.handler = handler;
        this.input = new BufferedReader(new InputStreamReader(inputStream));
    }

    @Override
    public void run() {
        if (handler == null) {
            Log.e(TAG, "Can not start - the responseHandler is null!");
            return;
        }

        running = true;
        // Read one line of input, parse it as a message, pass it to the TcpClient handler
        while (running) {
            try {
                String line = input.readLine();
                JsonMessage msg = JsonMessage.fromJson(line);
                if (msg != null) {
                    handler.obtainMessage(TcpClient.MSG_TYPE_SERVER_RESPONSE, msg).sendToTarget();
                } else {
                    Log.i(TAG, "Received message from server that was not proper JSON: " + line);
                }
            } catch (IOException e) {
                Log.e(TAG, "Error while reading input from the server: " + e.getMessage());
                running = false;
            }
        }
    }

    public void stopRunning() {
        running = false;
    }
}
