package no.ntnu.alesund;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Encodes a string (password or other) as a MD5 hash
 * @author Girts Strazdins 
 * girts.strazdins@gmail.com
 * 2015-11-08 
 */
public class MD5Encoder {

    /**
     * Generates human-readable MD5 hash
     * Code from http://stackoverflow.com/questions/415953/how-can-i-generate-an-md5-hash
     * @param plaintext
     * @return 
     */
    public String encode(String plaintext) {
        if (plaintext == null) return null;
        try {
            // Generate binary hash
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.reset();
            m.update(plaintext.getBytes());
            byte[] digest = m.digest();
            // Convert it to human-readable
            BigInteger bigInt = new BigInteger(1, digest);
            String hashText = bigInt.toString(16);
            // Now we need to zero pad it if you actually want the full 32 chars.
            while (hashText.length() < 32) {
                hashText = "0" + hashText;
            }
            return hashText;
            
        } catch (NoSuchAlgorithmException ex) {
            // MD5 encoder not supported by the OS/libraries
            return null;
        }
    }

}
