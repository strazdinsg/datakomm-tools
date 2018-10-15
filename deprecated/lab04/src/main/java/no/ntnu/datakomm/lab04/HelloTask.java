package no.ntnu.datakomm.lab04;

/**
 * A simple task requiring to send a static message "Hello" to the server
 * @author Girts Strazdins, 2016-10-09
 */
public class HelloTask extends Task {
    
    public HelloTask() {
        super();
        this.type = Tasks.TASK_HELLO.ordinal();
        this.description = "You should send an HTTP post with a static "
                + "message as a response to this task: include "
                + "parameter msg=Hello in the HTTP POST";
        // Leave the arguments as an empty array
    }
}
