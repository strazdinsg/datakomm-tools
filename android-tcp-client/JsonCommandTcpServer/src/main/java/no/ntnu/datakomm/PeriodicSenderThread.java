package no.ntnu.datakomm;

import java.util.Random;

/**
 * A thread that will simulate periodic sensor reading
 * and will send periodic messages to the client 
 * (output stream of the client socket)
 * @author Girts Strazdins, 2016-10-30
 */
class PeriodicSenderThread extends Thread {
    private final ClientThread client;
    
    private static final int SLEEP_PERIOD = 2000;    
    private static final Random RAND = new Random();
    private boolean running; // Flag used to stop the thread externally
    
    public PeriodicSenderThread(ClientThread client) {
        this.client = client;
        running = false;
    }
    
    @Override
    public void run() {
        JsonMessage msg = new JsonMessage();
        msg.setType(MsgType.SENSOR_VALUES);
        
        running = true;
        while (running) {
            int sensorValue = RAND.nextInt(1000);
            msg.clearArguments();
            msg.addArgument("" + sensorValue);
            System.out.println("Sending sensor value: " + sensorValue);
            client.sendResponse(msg.toJson());
            try {
                Thread.sleep(SLEEP_PERIOD);
            } catch (InterruptedException ex) {
                System.out.println("Thread interrupted");
                break;
            }
        }
    }

    void stopRunning() {
        running = false;
    }
}
