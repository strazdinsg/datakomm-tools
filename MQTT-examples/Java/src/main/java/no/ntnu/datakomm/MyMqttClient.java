package no.ntnu.datakomm;

import java.io.IOException;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * An example MQTT client application. It connects to an MQTT server, subscribes
 * to a specific channel and then sends a message to that channel. It will
 * receive it's own message back as well. And if during the lifetime of this
 * applications some other MQTT client publishes message on the same channel,
 * this client will receive it as well.
 *
 * @author Thomas S. Mjåland
 */
public class MyMqttClient implements MqttCallback {

    // The protocol, address and port of the server
    private static final String SERVER_ADDRESS = "tcp://129.241.152.12:1883";
    // Name used to identify yourself with the server, should be unique
    private static final String MY_NAME = "Darth Waiter";

    public static void main(String[] args) {
        MyMqttClient clientInstance = new MyMqttClient();
        clientInstance.run();
    }

    private void run() {
        MqttClient client = null;

        // First we try to create a mqtt client with the server's address and our selected name
        try {
            client = new MqttClient(SERVER_ADDRESS, MY_NAME);

            // We give the client an object implementing the "MqttCallback"-interface,
            // This object has the functions that the client calls when something happens:
            // the is connection lost, a message is sent or a message is received
            client.setCallback(this);

            // Then we try to connect to the server
            client.connect();

            // We subscribe to a topic - any topic which has ntnu/datakomm in the beginning
            client.subscribe("ntnu/datakomm/#");

            // The reception of incomming messages will happen in another thread,
            // the messageArrived() method will be called. 
            // Meanwhile, we can do other stuff in this thread
            // We publish to the same topic
            String messageString = "Hello MQTT";
            MqttMessage message = new MqttMessage(messageString.getBytes());
            client.publish("ntnu/datakomm/ping", message);
            System.out.println("Sending message 'Hello!' (in the main thread)");

            System.out.println("Waiting for incomming messages...");
            System.out.println("Press <Enter> to stop this client application.");

            // This will block until user presses <Enter>
            System.in.read();

            // We then disconnect from the server, and close the client
            client.disconnect();
            client.close();

        } catch (MqttException ex) {
            System.out.println("Something went wrong with the MQTT client.\n  "
                    + "Errormessage: " + ex.getMessage());
            System.exit(-1);
        } catch (IOException ex) {
            System.out.println("Something went wrong with the <Enter> press: "
                    + ex.getMessage());
        }
    }

    /**
     * This method is called (in another child thread) when MQTT connection is
     * lost
     *
     * @param cause Reason for closing the connection
     */
    @Override
    public void connectionLost(Throwable cause) {
        System.out.println("Lost connection with server: " + cause.getMessage());
    }

    /**
     * This method is called (in another child thread) when a message is
     * received from the MQTT server
     *
     * @param topic   the topic on which the message was received
     * @param message the received message
     */
    @Override
    public void messageArrived(String topic, MqttMessage message) {
        System.out.println("Received message from server (in child thread):");
        System.out.println("    Topic:   " + topic);
        System.out.println("    Message: " + new String(message.getPayload()));
    }

    /**
     * This method is called, when we get an ACK from the MQTT server that the
     * message was received.
     *
     * @param token
     */
    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        System.out.println("A message was just successfully delivered to the server. "
                + "(we got this notification in a child thread)");
    }

}
