import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

import no.ntnu.tv.TvLogic;
import no.ntnu.message.ChannelCountCommand;
import no.ntnu.message.ChannelCountMessage;
import no.ntnu.message.ErrorMessage;
import no.ntnu.message.Message;
import no.ntnu.message.OkMessage;
import no.ntnu.message.SetChannelCommand;
import no.ntnu.message.TurnOffCommand;
import no.ntnu.message.TurnOnCommand;
import org.junit.Test;

/**
 * Tests for the command logic - checks whether the command execution have the right impact on
 * the logic.
 */
public class CommandTests {
  private static final int CHANNEL_COUNT = 5;

  @Test
  public void testChannelCountWhenTvIsOff() {
    ChannelCountCommand c = new ChannelCountCommand();
    TvLogic logic = new TvLogic(CHANNEL_COUNT);
    Message response = c.execute(logic);
    assertTrue(response instanceof ErrorMessage);
  }

  @Test
  public void testChannelCount() {
    TvLogic logic = new TvLogic(CHANNEL_COUNT);
    logic.turnOn();
    ChannelCountCommand c = new ChannelCountCommand();
    Message response = c.execute(logic);
    assertTrue(response instanceof ChannelCountMessage);
    ChannelCountMessage channelCountMessage = (ChannelCountMessage) response;
    assertEquals(CHANNEL_COUNT, channelCountMessage.getChannelCount());
  }

  @Test
  public void testTurnOn() {
    TvLogic logic = new TvLogic(CHANNEL_COUNT);
    TurnOnCommand c = new TurnOnCommand();
    Message response = c.execute(logic);
    assertTrue(response instanceof OkMessage);
    assertTrue(logic.isTvOn());
  }

  @Test
  public void testTurnOff() {
    TvLogic logic = new TvLogic(CHANNEL_COUNT);
    logic.turnOn();
    TurnOffCommand c = new TurnOffCommand();
    Message response = c.execute(logic);
    assertTrue(response instanceof OkMessage);
    assertTrue(!logic.isTvOn());
  }

  @Test
  public void testSetChannel() {
    TvLogic logic = new TvLogic(CHANNEL_COUNT);
    logic.turnOn();
    assertSetChannel(logic, 2);
    assertSetChannel(logic, CHANNEL_COUNT - 1);
  }

  private static void assertSetChannel(TvLogic logic, int channel) {
    SetChannelCommand c = new SetChannelCommand(channel);
    Message response = c.execute(logic);
    assertTrue(response instanceof OkMessage);
    assertEquals(channel, logic.getCurrentChannel());
  }

  @Test
  public void testSetInvalidChannel() {
    TvLogic logic = new TvLogic(CHANNEL_COUNT);
    logic.turnOn();
    assertInvalidChannel(logic, 0);
    assertInvalidChannel(logic, -1);
    assertInvalidChannel(logic, CHANNEL_COUNT + 1);
  }

  private void assertInvalidChannel(TvLogic logic, int channel) {
    int oldChannel = logic.getCurrentChannel();
    SetChannelCommand c = new SetChannelCommand(channel);
    Message response = c.execute(logic);
    assertTrue(response instanceof ErrorMessage);
    assertEquals(oldChannel, logic.getCurrentChannel());
  }

}
