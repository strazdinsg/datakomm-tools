package no.ntnu.datakomm.lab04.examples;

import no.ntnu.alesund.XMLMarshalling;
import no.ntnu.datakomm.lab04.Feedback;
import no.ntnu.datakomm.lab04.TaskResult;

/**
 * Just an example feedback for a submission
 * @author Girts
 */
public class FeedbackExample {
    public static void main(String[] args) {
        XMLMarshalling marshalling = new XMLMarshalling();
        String xml;

        // Feedback for one task
        TaskResult result = new TaskResult();
        result.setSuccess(false);
        result.setComment("No such email found in the database");
        marshalling.setAlias("result");
        xml = marshalling.marshall(result, TaskResult.class);
        System.out.println("Example result for one task:");
        System.out.println(xml);
        System.out.println("");
        
        // Feedback for the whole assignment
        Feedback feedback = new Feedback();
        feedback.setStudent("Ole Einar Bjørndalen");
        feedback.addResult("Successfully authorized", 20);
        feedback.addResult("Sent request for task 1", 10);
        feedback.addResult("Correct response to task 1", 10);
        feedback.addResult("Correct response to task 2", 10);
        feedback.addResult("Correct response to task 3", 10);
        feedback.addResult("Correct response to task 4", 20);
        feedback.addResult("Correct response to secret task", 0);
        feedback.addResult("Asked these feedback results", 10);
        
        marshalling.setAlias("feedback");
        xml = marshalling.marshall(feedback, Feedback.class);
        
        System.out.println("Example feedback for the whole assignment:");
        System.out.println(xml);
        System.out.println("");
        
        // Just for testing - try to reconstruct the feedback
        Feedback f2 = (Feedback) marshalling.unmarshall(xml, Feedback.class);
        if (f2 != null) {
            System.out.println("Tried to restore the feedback object, it works!");
        }
    }
}
