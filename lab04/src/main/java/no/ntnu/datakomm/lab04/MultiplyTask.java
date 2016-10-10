package no.ntnu.datakomm.lab04;

import java.util.Random;

/**
 * A task requiring to extract all the arguments as numbers, multiply them, and
 * send the result to the server as an HTTP POST with parameter result=X , where
 * X is the multiplication product
 *
 * @author Girts Strazdins, 2016-10-09
 */
public class MultiplyTask extends Task {

    private static final int MAX_ARGS = 5;
    private static final int MAX_ARG_VALUE = 100;
    
    public MultiplyTask() {
        super();
        this.type = Tasks.TASK_MULTIPLY.ordinal();
        this.description = "Extract all the arguments as numbers, multiply\n"
                + " them, and send the result to the server as an"
                + " HTTP POST with parameter result=X, where X is the "
                + "multiplication product";
        
        // Generate 1 to 5 arguments, randomly
        Random r = new Random();
        int argCount = r.nextInt(MAX_ARGS) + 1;
        for (int i = 1; i <= argCount; ++i) {
            this.arguments.add(Integer.toString(r.nextInt(MAX_ARG_VALUE)));
        }
    }
}
