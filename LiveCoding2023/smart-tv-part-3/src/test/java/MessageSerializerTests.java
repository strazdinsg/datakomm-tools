import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import static no.ntnu.message.MessageSerializer.CHANNEL_COUNT_COMMAND;
import static no.ntnu.message.MessageSerializer.CHANNEL_COUNT_MESSAGE;
import static no.ntnu.message.MessageSerializer.CURRENT_CHANNEL_MESSAGE;
import static no.ntnu.message.MessageSerializer.ERROR_MESSAGE;
import static no.ntnu.message.MessageSerializer.GET_CHANNEL_COMMAND;
import static no.ntnu.message.MessageSerializer.OK_RESPONSE;
import static no.ntnu.message.MessageSerializer.SET_CHANNEL_COMMAND;
import static no.ntnu.message.MessageSerializer.TURN_OFF_COMMAND;
import static no.ntnu.message.MessageSerializer.TURN_ON_COMMAND;
import static no.ntnu.message.MessageSerializer.TV_STATE_OFF_MESSAGE;
import static no.ntnu.message.MessageSerializer.TV_STATE_ON_MESSAGE;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import no.ntnu.message.ChannelCountCommand;
import no.ntnu.message.ChannelCountMessage;
import no.ntnu.message.CurrentChannelMessage;
import no.ntnu.message.ErrorMessage;
import no.ntnu.message.GetChannelCommand;
import no.ntnu.message.Message;
import no.ntnu.message.MessageSerializer;
import no.ntnu.message.OkMessage;
import no.ntnu.message.SetChannelCommand;
import no.ntnu.message.TurnOffCommand;
import no.ntnu.message.TurnOnCommand;
import no.ntnu.message.TvStateMessage;
import org.junit.Test;

/**
 * Tests for message serialization: both to and from the string format.
 */
public class MessageSerializerTests {
  @Test
  public void testNull() {
    assertNull(MessageSerializer.fromString(null));
    assertNull(MessageSerializer.toString(null));
  }

  @Test
  public void testChannelCountCommand() {
    Message m = MessageSerializer.fromString(CHANNEL_COUNT_COMMAND);
    assertNotNull(m);
    assertTrue(m instanceof ChannelCountCommand);
  }

  @Test
  public void testTurnOnCommand() {
    Message m = MessageSerializer.fromString(TURN_ON_COMMAND);
    assertNotNull(m);
    assertTrue(m instanceof TurnOnCommand);
  }

  @Test
  public void testSetChannelCommand() {
    Message m = MessageSerializer.fromString(SET_CHANNEL_COMMAND);
    assertNull(m);
    expectSetChannelCommand(SET_CHANNEL_COMMAND + "2", 2);
    expectSetChannelCommand(SET_CHANNEL_COMMAND + "13", 13);
    expectSetChannelCommand(SET_CHANNEL_COMMAND + "0", 0);
    expectSetChannelCommand(SET_CHANNEL_COMMAND + "-5", -5);
  }

  private void expectSetChannelCommand(String messageString, int expectedChannel) {
    Message m = MessageSerializer.fromString(messageString);
    assertTrue(m instanceof SetChannelCommand);
    SetChannelCommand scc = (SetChannelCommand) m;
    assertEquals(expectedChannel, scc.getChannel());
  }

  @Test
  public void testTurnOffCommand() {
    Message m = MessageSerializer.fromString(TURN_OFF_COMMAND);
    assertNotNull(m);
    assertTrue(m instanceof TurnOffCommand);
  }

  @Test
  public void testGetChannelCommand() {
    Message m = MessageSerializer.fromString(GET_CHANNEL_COMMAND);
    assertNotNull(m);
    assertTrue(m instanceof GetChannelCommand);
  }

  @Test
  public void testCurrentChannelMessage() {
    assertCurrentChannelMessage("", null);
    assertCurrentChannelMessage("ddd", null);
    assertCurrentChannelMessage("1", 1);
    assertCurrentChannelMessage("15", 15);
  }

  private void assertCurrentChannelMessage(String channelString, Integer expectedChannel) {
    Message m = MessageSerializer.fromString(CURRENT_CHANNEL_MESSAGE + channelString);
    if (expectedChannel != null) {
      assertNotNull(m);
      assertTrue(m instanceof CurrentChannelMessage);
      CurrentChannelMessage ccm = (CurrentChannelMessage) m;
      assertEquals((long) expectedChannel, ccm.getChannel());
    } else {
      assertNull(m);
    }
  }

  @Test
  public void testErrorMessage() {
    assertErrorMessage(ERROR_MESSAGE, null);
    assertErrorMessage(ERROR_MESSAGE + "_", "_");
    assertErrorMessage(ERROR_MESSAGE + "Samsing rang", "Samsing rang");
  }

  private void assertErrorMessage(String message, String expectedError) {
    Message m = MessageSerializer.fromString(message);
    if (expectedError != null) {
      assertNotNull(m);
      assertTrue(m instanceof ErrorMessage);
      ErrorMessage em = (ErrorMessage) m;
      assertEquals(expectedError, em.getMessage());
    } else {
      assertNull(m);
    }
  }

  @Test
  public void testTurnOffCommandToString() {
    assertEquals(TURN_OFF_COMMAND, MessageSerializer.toString(new TurnOffCommand()));
  }

  @Test
  public void testErrorMessageToString() {
    assertErrorMessageToString("E");
    assertErrorMessageToString("Samsing rang");
  }

  private void assertErrorMessageToString(String errorMessage) {
    assertEquals(ERROR_MESSAGE + errorMessage,
        MessageSerializer.toString(new ErrorMessage(errorMessage)));
  }

  @Test
  public void testTurnOnCommandToString() {
    assertEquals(TURN_ON_COMMAND, MessageSerializer.toString(new TurnOnCommand()));
  }

  @Test
  public void testChannelCountCommandToString() {
    assertEquals(CHANNEL_COUNT_COMMAND, MessageSerializer.toString(new ChannelCountCommand()));
  }

  @Test
  public void testChannelCountMessageToString() {
    assertChannelCountMessage(1);
    assertChannelCountMessage(5);
    assertChannelCountMessage(15);
  }

  private void assertChannelCountMessage(int channelCount) {
    assertEquals(CHANNEL_COUNT_MESSAGE + channelCount,
        MessageSerializer.toString(new ChannelCountMessage(channelCount)));
  }

  @Test
  public void testStringToChannelCountMessage() {
    Message m = MessageSerializer.fromString(CHANNEL_COUNT_MESSAGE + 12);
    assertTrue(m instanceof ChannelCountMessage);
    ChannelCountMessage ccm = (ChannelCountMessage) m;
    assertEquals(12, ccm.getChannelCount());
  }

  @Test
  public void testSetChannelCommandToString() {
    assertSetChannelToString(1);
    assertSetChannelToString(3);
    assertSetChannelToString(12);
    assertSetChannelToString(667);
  }

  private void assertSetChannelToString(int channel) {
    assertEquals(SET_CHANNEL_COMMAND + channel,
        MessageSerializer.toString(new SetChannelCommand(channel)));
  }

  @Test
  public void testCurrentChannelCommandToString() {
    assertEquals(GET_CHANNEL_COMMAND, MessageSerializer.toString(new GetChannelCommand()));
  }

  @Test
  public void testCurrentChannelMessageToString() {
    assertCurrentChannelMessageToString(1);
    assertCurrentChannelMessageToString(2);
    assertCurrentChannelMessageToString(23);
    assertCurrentChannelMessageToString(667);
  }

  private void assertCurrentChannelMessageToString(int channel) {
    assertEquals(CURRENT_CHANNEL_MESSAGE + channel,
        MessageSerializer.toString(new CurrentChannelMessage(channel)));
  }

  @Test
  public void testOkMessage() {
    assertTrue(MessageSerializer.fromString(OK_RESPONSE) instanceof OkMessage);
  }

  @Test
  public void testOkMessageToString() {
    assertEquals(OK_RESPONSE, MessageSerializer.toString(new OkMessage()));
  }

  @Test
  public void testInvalidCommand() {
    assertNull(MessageSerializer.fromString("Chuck iz da best"));
    assertNull(MessageSerializer.fromString("Zis iz insein"));
    assertNull(MessageSerializer.fromString("+"));
    assertNull(MessageSerializer.fromString("G"));
  }

  @Test
  public void testTvOnStateMessageToString() {
    TvStateMessage m = new TvStateMessage(true);
    assertEquals(TV_STATE_ON_MESSAGE, MessageSerializer.toString(m));
  }

  @Test
  public void testTvOffStateMessageToString() {
    TvStateMessage m = new TvStateMessage(false);
    assertEquals(TV_STATE_OFF_MESSAGE, MessageSerializer.toString(m));
  }

  @Test
  public void testTvOnStateMessageFromString() {
    Message m = MessageSerializer.fromString(TV_STATE_ON_MESSAGE);
    assertTrue(m instanceof TvStateMessage);
    TvStateMessage tsm = (TvStateMessage) m;
    assertTrue(tsm.isOn());
  }

  @Test
  public void testTvOffStateMessageFromString() {
    Message m = MessageSerializer.fromString(TV_STATE_OFF_MESSAGE);
    assertTrue(m instanceof TvStateMessage);
    TvStateMessage tsm = (TvStateMessage) m;
    assertFalse(tsm.isOn());
  }
}
