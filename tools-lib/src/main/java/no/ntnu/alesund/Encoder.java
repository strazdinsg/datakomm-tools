package no.ntnu.alesund;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Encodes a string (password or other) as a hash
 * @author Girts Strazdins, 2015-11-08 (modified 2016-10-12)
 */
public class Encoder {

    MessageDigest shaCript;
    MessageDigest md5Cript;

    public Encoder() throws NoSuchAlgorithmException {
        shaCript = MessageDigest.getInstance("SHA-1");
        md5Cript = MessageDigest.getInstance("MD5");
    }

    /**
     * Generates human-readable MD5 hash Code from
     * http://stackoverflow.com/questions/415953/how-can-i-generate-an-md5-hash
     *
     * @param plaintext
     * @return
     */
    public String md5(String plaintext) {
        return this.genHash(plaintext, md5Cript);
    }

    /**
     * Generates human-readable SHA1 hash
     *
     * @param plaintext
     * @return
     */
    public String sha1(String plaintext) {
        return this.genHash(plaintext, shaCript);
    }
    
    /**
     * Generate a hash from a plaintext, using given cryptography algorithm/digest
     * @param plaintext
     * @param digest
     * @return 
     */
    private String genHash(String plaintext, MessageDigest digest) {
        if (plaintext == null) {
            return null;
        }
        // Generate binary hash
        digest.reset();
        digest.update(plaintext.getBytes());
        byte[] d = digest.digest();
        // Convert it to human-readable
        BigInteger bigInt = new BigInteger(1, d);
        String hashText = bigInt.toString(16);
        // Now we need to zero pad it if you actually want the full 32 chars.
        while (hashText.length() < 32) {
            hashText = "0" + hashText;
        }
        return hashText;
    }
}
