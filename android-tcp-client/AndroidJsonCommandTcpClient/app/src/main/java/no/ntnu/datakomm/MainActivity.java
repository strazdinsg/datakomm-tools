package no.ntnu.datakomm;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VerticalSeekBar;

import datakomm.ntnu.no.androidjsoncommandtcpclient.R;

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";

    // Default server's address and port
    private static final String DEFAULT_SERVER_HOST = "192.168.1.57";
    private static final int DEFAULT_SERVER_PORT = 5000;

    // States of the application
    private static enum State { STARTED, CONN_INITIATED, CONN_OPENED };
    private State state;

    private TcpClient tcpClient;
    private SeekBar seekBar1;
    private SeekBar seekBar2;
    private EditText etHost;
    private EditText etPort;
    private Button btConnect;
    private TextView tvSensorTitle;
    private TextView tvSensorVal;

    /**
     * Initialize all the necessary components
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        state = State.STARTED;

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
                        handleFeedback(feedback);
                        break;
                    case TcpClient.MSG_TYPE_SERVER_RESPONSE:
                        JsonMessage serverMsg = (JsonMessage) msg.obj;
                        handleServerResponse(serverMsg);
                }
            }
        };
        tcpClient = new TcpClient(responseHandler);
        tcpClient.start();

        // Ensure that the whole looper system is initialized before we start sending requests to it
        if (!tcpClient.waitForHandler()) {
            return;
        }

    }

    /**
     * Handle a message received from the TCP server
     * @param serverMsg
     */
    private void handleServerResponse(JsonMessage serverMsg) {
        switch (serverMsg.getType()) {
            case MsgType.SERVER_INFO:
                // Server reports its info. Currently we expect only version to be reported
                String version = serverMsg.getArgumentByIndex(0);
                if (version != null) {
                    Toast t = Toast.makeText(this, "Connected to Json Server " + version, Toast.LENGTH_LONG);
                    t.show();
                }
                break;
            case MsgType.SENSOR_VALUES:
                // The server reports some sensor values
                // Currently we expect a single simulated sensor
                String sensorVal = serverMsg.getArgumentByIndex(0);
                if (sensorVal != null) {
                    tvSensorVal.setText(sensorVal);
                }
        }
    }

    /**
     * Initialize the GUI: seekbars, listeners etc
     */
    private void initGui() {
        // Initialize widget references
        seekBar1 = (SeekBar) findViewById(R.id.seekBar1);
        seekBar2 = (SeekBar) findViewById(R.id.seekBar2);
        etHost = (EditText) findViewById(R.id.serverHost);
        etPort = (EditText) findViewById(R.id.serverPort);
        btConnect = (Button) findViewById(R.id.connect);
        tvSensorTitle = (TextView) findViewById(R.id.sensorTitle);
        tvSensorVal = (TextView) findViewById(R.id.sensorVal);

        // Set some default values
        etHost.setText(DEFAULT_SERVER_HOST);
        etPort.setText("" + DEFAULT_SERVER_PORT);
        seekBar1.setEnabled(false);
        seekBar2.setEnabled(false);

        // React on Connect button
        btConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectToServer();
            }
        });


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
     * Initiate connection to server, Background thread will handle it, so we wait for answer
     */
    private void connectToServer() {
        // Disable the Connect button until we get a response
        btConnect.setEnabled(false);

        // Try to open connection
        String host = etHost.getText().toString();
        try {
            int port = Integer.valueOf(etPort.getText().toString());
            tcpClient.initiateConnectionToServer(host, port);
            state = State.CONN_INITIATED;

        } catch (NumberFormatException ex) {
            Toast.makeText(this, "Invalid port, should be a number", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleFeedback(Feedback feedback) {
        Log.i(TAG, "Got feedback in the main GUI thread: success="
                + feedback.isSuccess() + ", msg=" + feedback.getErrMsg());
        switch (this.state) {
            case CONN_INITIATED:
                // This should be feedback for the connection open try
                if (feedback.isSuccess()) {
                    onSuccessfulConnect();
                } else {
                    Log.i(TAG, "Connection failed");
                    Toast.makeText(this,  feedback.getErrMsg(), Toast.LENGTH_LONG).show();
                    btConnect.setEnabled(true);
                }
                break;
            default:
                break;
        }
    }

    private void onSuccessfulConnect() {
        // Connection established, can enable the controls
        seekBar1.setEnabled(true);
        seekBar2.setEnabled(true);
        tvSensorTitle.setVisibility(View.VISIBLE);
        tvSensorVal.setVisibility(View.VISIBLE);

        btConnect.setText("Disconnect");
        // TODO - implement disconnect
    }

    /**
     * Send the new value of a seekbar to the TCP server - enqueue a message that will be
     * handled in the background Looper thread
     * @param seekbarId
     * @param position
     */
    private void sendSeekbarPos(int seekbarId, int position) {
//        String msg = "New position for seekbar " + seekbarId + ": " + position;
        // Send a Command message. First - command type (seekbarId), then value
        JsonMessage msg = new JsonMessage();
        msg.setType(MsgType.SEND_COMMAND);
        msg.addArgument("" + seekbarId);
        msg.addArgument("" + position);
        tcpClient.enqueueMessage(msg.toJson());
    }


}
