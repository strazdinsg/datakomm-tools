package no.ntnu.datakomm;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for JsonMessage class
 * @author Girts Strazdins, 2016-10-30
 */
public class JsonMessageTest {
    @Test
    public void testJsonMessage() {
        // Empty message
        JsonMessage msg = new JsonMessage();
        String expected = 
                "{\"message\": {"
                + "  \"type\": 0,"
                + "  \"arguments\": []"
                + "}}";
        String real = msg.toJson();
        assertEquals(expected, real); 
        
        // Type = 3
        int expectedType = 3;
        msg.setType(expectedType);
        assertEquals(expectedType, msg.getType());
        expected = 
                "{\"message\": {"
                + "  \"type\": 3,"
                + "  \"arguments\": []"
                + "}}";
        String json = msg.toJson();
        assertEquals(expected, json); 
        
        JsonMessage msg2 = JsonMessage.fromJson(json);
        assertEquals(expectedType, msg2.getType());
        assertEquals(0, msg2.getArgumentCount());
        
        // Add one argument
        String arg1 = "lapsa";
        msg.addArgument(arg1);
        assertEquals(1, msg.getArgumentCount());
        assertEquals(arg1, msg.getArgumentByIndex(0));

        // Add another argument
        String arg2 = "Santa Claus";
        msg.addArgument(arg2);
        assertEquals(2, msg.getArgumentCount());
        assertEquals(arg1, msg.getArgumentByIndex(0));
        assertEquals(arg2, msg.getArgumentByIndex(1));
        expected = 
                "{\"message\": {"
                + "  \"type\": 3,"
                + "  \"arguments\": ["
                + "    \"" + arg1 + "\","
                + "    \"" + arg2 + "\""
                + "  ]"
                + "}}";
        json = msg.toJson();
        assertEquals(expected, json); 
        msg2 = JsonMessage.fromJson(json);
        assertTrue(msg.equals(msg2));
    }
}
