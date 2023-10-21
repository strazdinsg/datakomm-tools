import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;

import no.ntnu.message.ChannelCountCommand;
import no.ntnu.message.Message;
import no.ntnu.message.MessageSerializer;
import no.ntnu.message.TurnOnCommand;
import org.junit.Test;

public class MessageSerializerTests {

  @Test
  public void testChannelCountCommand() {
    Message m = MessageSerializer.fromString("c");
    assertNotNull(m);
    assertTrue(m instanceof ChannelCountCommand);
  }

  @Test
  public void testTurnOnCommand() {
    Message m = MessageSerializer.fromString("1");
    assertNotNull(m);
    assertTrue(m instanceof TurnOnCommand);
  }
}
