package no.ntnu.datakomm.lab04;

import java.security.NoSuchAlgorithmException;
import java.util.Random;
import no.ntnu.alesund.Encoder;

/**
 * A simple task requiring to crack a md5 hash of four digit PIN code
 * and send the response as result field in the HTTP POST
 *
 * @author Girts Strazdins, 2016-10-09
 */
public class CrackTask extends Task {

    public CrackTask() {
        super();
        this.type = Tasks.TASK_CRACK.ordinal();
        this.description = "You should crack the four-digit PIN code, from"
                + "the given md5 hash of it, which is in the argument[0]"
                + " and send an HTTP post with a "
                + "parameter pin=XXXX";
        String pin = generateRandomPin(4);
        Encoder encoder;
        try {
            encoder = new Encoder();
            String hash = encoder.md5(pin);
            this.arguments.add(hash);
        } catch (NoSuchAlgorithmException ex) {
            System.out.println("Could not generate MD5 hash: " + ex.getMessage());
        }
    }

    /**
     * Generate a random PIN code, with given number of digits
     * @return 
     */
    private String generateRandomPin(int numberOfDigits) {
        if (numberOfDigits < 1) {
            return "";
        }
        Random r = new Random();
        String pin = "";
        for (int i = 0; i < numberOfDigits; ++i) {
            int digit = r.nextInt(10);
            pin += Integer.toString(digit);
        }
        return pin;
    }
}
