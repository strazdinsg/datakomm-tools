package no.ntnu.alesund;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Encodes a string (password or other) as a hash
 * @author Girts Strazdins, 2015-11-08 (modified 2016-10-12)
 */
public class Encoder {

    // Expected number of characters in every hash
    private static final int MD5_NUM_CHARS = 32; 
    private static final int SHA1_NUM_CHARS = 40; 
    
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
        return this.genHash(plaintext, md5Cript, MD5_NUM_CHARS);
    }

    /**
     * Generates human-readable SHA1 hash
     *
     * @param plaintext
     * @return
     */
    public String sha1(String plaintext) {
        return this.genHash(plaintext, shaCript, SHA1_NUM_CHARS);
    }
    
    /**
     * Generate a hash from a plaintext, using given cryptography algorithm/digest
     * @param plaintext
     * @param digest
     * @return 
     */
    private String genHash(String plaintext, MessageDigest digest, 
            int expectedChars) {
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
        // Girts' edit: The actual code was like this:
//        while (hashText.length() < 32) {
        // But it does not work correctly for SHA1. To make it correct,
        // Check if the returned hash had expected number of characters
        while (hashText.length() < expectedChars) {
            hashText = "0" + hashText;
        }
        return hashText;
    }
}
