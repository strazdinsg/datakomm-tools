package no.ntnu.datakomm.lab04.examples;

import no.ntnu.alesund.XMLMarshalling;
import no.ntnu.datakomm.lab04.Feedback;

/**
 * Just an example feedback for a submission
 * @author Girts
 */
public class FeedbackExample {
    public static void main(String[] args) {
        Feedback feedback = new Feedback();
        feedback.addResult("Successfully authorized", 20);
        feedback.addResult("Sent request for task 1", 10);
        feedback.addResult("Correct response to task 1", 10);
        feedback.addResult("Correct response to task 2", 10);
        feedback.addResult("Correct response to task 3", 10);
        feedback.addResult("Correct response to task 4", 20);
        feedback.addResult("Correct response to secret task", 0);
        feedback.addResult("Asked these feedback results", 10);
        
        XMLMarshalling marshalling = new XMLMarshalling();
        marshalling.setAlias("feedback");
        String xml = marshalling.marshall(feedback, Feedback.class);
        
        System.out.println("Example feedback:");
        System.out.println(xml);
        
        // Just for testing - try to reconstruct the feedback
        Feedback f2 = (Feedback) marshalling.unmarshall(xml, Feedback.class);
        if (f2 != null) {
            System.out.println("Tried to restore the feedback object, it works!");
        }
    }
}
