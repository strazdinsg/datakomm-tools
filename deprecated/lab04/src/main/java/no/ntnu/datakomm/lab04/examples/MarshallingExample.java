package no.ntnu.datakomm.lab04.examples;

import java.util.Random;
import no.ntnu.alesund.JsonMarshalling;
import no.ntnu.datakomm.lab04.HelloTask;
import no.ntnu.alesund.XMLMarshalling;
import no.ntnu.datakomm.lab04.CrackTask;
import no.ntnu.datakomm.lab04.EchoTask;
import no.ntnu.datakomm.lab04.MultiplyTask;
import no.ntnu.datakomm.lab04.Task;

/**
 * Example showing how to marshall/unmarshall the Task object
 * @author Girts Strazdins, 2016-10-09
 */
public class MarshallingExample {
    private final XMLMarshalling xmlMarshalling;
    private final JsonMarshalling jsonMarshalling;
    private final int sessionId;
    
    public MarshallingExample() {
        xmlMarshalling = new XMLMarshalling();
        xmlMarshalling.setAlias("task");
        jsonMarshalling = new JsonMarshalling();
        jsonMarshalling.setAlias("task");
        Random r = new Random();
        sessionId = r.nextInt(100000);
    }
    
    public static void main(String[] args) {
        MarshallingExample example = new MarshallingExample();
        example.run(new HelloTask());
        example.run(new EchoTask());
        example.run(new MultiplyTask());
        example.run(new CrackTask());
    }
    
    public void run(Task task) {
        task.setSessionId(sessionId);
        String xml = xmlMarshalling.marshall(task, task.getClass());
        String json = jsonMarshalling.marshall(task, task.getClass());
        
        System.out.println("Example of how you will receive " + task.getClass() + ":");
        System.out.println(xml);
        System.err.println("");
        System.out.println("Example of how you will receive it as JSON:");
        System.out.println(json);
        System.err.println("");
        System.err.println("");
    }

    public static void debugTask() {
        
    }

}
