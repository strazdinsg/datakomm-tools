package no.ntnu.datakomm;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.VerticalSeekBar;

import datakomm.ntnu.no.androidjsoncommandtcpclient.R;

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";

    private static final String SERVER_HOST = "192.168.1.57";
    private static final int SERVER_PORT = 5000;

    private TcpClient tcpClient;
    private VerticalSeekBar seekBar1;
    private VerticalSeekBar seekBar2;

    /**
     * Initialize all the necessary components
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initGui();
        initBackgroundLooper();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        tcpClient.quit();
        Log.i(TAG, "Background TCP client terminated");
    }

    /**
     * Initialize the background thread that will wait for messages from the GUI thread and
     * send them to the TCP server in background
     */
    private void initBackgroundLooper() {
        // Create an object that will handle responses from the background thread
        Handler responseHandler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case TcpClient.MSG_TYPE_FEEDBACK:
                        Feedback feedback = (Feedback) msg.obj;
                        // TODO - handle feedback
                        Log.i(TAG, "Got feedback in the main GUI thread: success="
                                + feedback.isSuccess() + ", msg=" + feedback.getErrMsg());
                        break;
                }
            }
        };
        tcpClient = new TcpClient(responseHandler);
        tcpClient.start();

        // Ensure that the whole looper system is initialized before we start sending requests to it
        if (!tcpClient.waitForHandler()) {
            return;
        }

        tcpClient.initiateConnectionToServer(SERVER_HOST, SERVER_PORT);
    }

    /**
     * Initialize the GUI: seekbars, listeners etc
     */
    private void initGui() {
        seekBar1 = (VerticalSeekBar) findViewById(R.id.seekbar1);
        seekBar2 = (VerticalSeekBar) findViewById(R.id.seekbar2);

        // React on seekbar changes
        SeekBar.OnSeekBarChangeListener seekbarListener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {}

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            /**
             * This method is called when the user releases a seekbar
             * @param seekBar
             */
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int position = seekBar.getProgress();
                Log.i(TAG, "Seekbar drag stopped, position = " + position);
                int seekbarId = seekBar == seekBar1 ? 1 : 2; // Check which seekbar this is
                sendSeekbarPos(seekbarId, position);
            }
        };

        seekBar1.setOnSeekBarChangeListener(seekbarListener);
        seekBar2.setOnSeekBarChangeListener(seekbarListener);
    }

    /**
     * Send the new value of a seekbar to the TCP server - enqueue a message that will be
     * handled in the background Looper thread
     * @param seekbarId
     * @param position
     */
    private void sendSeekbarPos(int seekbarId, int position) {
        String msg = "New position for seekbar " + seekbarId + ": " + position;
        tcpClient.enqueueMessage(msg);
    }
}
