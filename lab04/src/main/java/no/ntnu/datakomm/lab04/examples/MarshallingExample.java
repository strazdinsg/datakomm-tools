package no.ntnu.datakomm.lab04.examples;

import java.util.Random;
import no.ntnu.alesund.JsonMarshalling;
import no.ntnu.datakomm.lab04.HelloTask;
import no.ntnu.alesund.XMLMarshalling;
import no.ntnu.datakomm.lab04.MultiplyTask;

/**
 * Example showing how to marshall/unmarshall the Task object
 * @author Girts Strazdins, 2016-10-09
 */
public class MarshallingExample {
    public static void main(String[] args) {
        // Create tasks
        Random r = new Random();
        int sessionId = r.nextInt(100000);
        HelloTask helloTask = new HelloTask();
        MultiplyTask multiplyTask = new MultiplyTask();
        helloTask.setSessionId(sessionId);
        multiplyTask.setSessionId(sessionId);

        // Convert task objects to XML and JSON strings
        XMLMarshalling xmlMarshalling = new XMLMarshalling();
        xmlMarshalling.setAlias("task");
        String helloXml = xmlMarshalling.marshall(helloTask, HelloTask.class);
        String multiplyXml = xmlMarshalling.marshall(multiplyTask, MultiplyTask.class);
        JsonMarshalling jsonMarshalling = new JsonMarshalling();
        jsonMarshalling.setAlias("task");
        String helloJson = jsonMarshalling.marshall(helloTask, HelloTask.class);
        String multiplyJson = jsonMarshalling.marshall(multiplyTask, MultiplyTask.class);
        
        System.out.println("Example of how you will receive Hello task:");
        System.out.println(helloXml);
        System.err.println("");
        System.out.println("Example of how you will receive Multiply task:");
        System.out.println(multiplyXml);
        
        System.err.println("");
        System.err.println("");
        System.err.println("If you choose the JSON format, then you would receive:");
        System.err.println("");
        System.out.println(helloJson);
        System.err.println("");
        System.out.println(multiplyJson);
        System.err.println("");
        
        
    }
}
