package no.ntnu.datakomm.lab04.examples;

import java.security.NoSuchAlgorithmException;
import no.ntnu.alesund.Encoder;

/**
 * Test how the SHA1 is working. Can be used as an example for authorization
 * @author Girts Strazdins, 2016-10-12
 */
public class Sha1Test {
    public static void main(String[] args) {
        // The specification of L04 says: generate a random salt and
        // sha(phone + salt). Here is how it works:
        String phone = "95595555";
        String salt = "12345";
        Encoder encoder;
        try {
            encoder = new Encoder();
            String sha1 = encoder.sha1(phone + salt);
            System.out.println("SHA1 for phone " + phone + " and salt " + salt
                + " is: " + sha1);
        } catch (NoSuchAlgorithmException ex) {
            System.out.println("SHA1 algorithm not available: " + ex.getMessage());
        }
    }
}
