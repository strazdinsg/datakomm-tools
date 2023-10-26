import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertThrows;

import no.ntnu.tv.TvLogic;
import org.junit.Test;

/**
 * Tests for the TV logic.
 */
public class LogicTests {
  private static final int CHANNEL_COUNT = 10;

  @Test
  public void testOffByDefault() {
    TvLogic logic = new TvLogic(CHANNEL_COUNT);
    assertFalse(logic.isTvOn());
  }

  @Test
  public void testTurnOn() {
    TvLogic logic = new TvLogic(CHANNEL_COUNT);
    logic.turnOn();
    assertTrue(logic.isTvOn());
  }

  @Test
  public void testTurnOff() {
    TvLogic logic = new TvLogic(CHANNEL_COUNT);
    logic.turnOn();
    assertTrue(logic.isTvOn());
    logic.turnOff();
    assertFalse(logic.isTvOn());
  }

  @Test
  public void testGetNumberOfChannels() {
    TvLogic logic = new TvLogic(CHANNEL_COUNT);
    logic.turnOn();
    assertEquals(CHANNEL_COUNT, logic.getNumberOfChannels());
  }

  @Test
  public void testChannelCountFailsWhenTvIsOff() {
    final TvLogic logic = new TvLogic(CHANNEL_COUNT);
    assertThrows(IllegalStateException.class, () -> logic.getNumberOfChannels());
  }

  @Test
  public void testSetChannelFailsWhenTvIsOff() {
    final TvLogic logic = new TvLogic(CHANNEL_COUNT);
    assertThrows(IllegalStateException.class, () -> logic.setChannel(3));
  }

  @Test
  public void testSetChannel() {
    TvLogic logic = new TvLogic(CHANNEL_COUNT);
    logic.turnOn();
    logic.setChannel(2);
    assertEquals(2, logic.getCurrentChannel());
  }

  @Test
  public void testTrySetInvalidChannel() {
    TvLogic logic = new TvLogic(CHANNEL_COUNT);
    logic.turnOn();
    assertThrows(IllegalArgumentException.class, () -> logic.setChannel(-1));
    assertThrows(IllegalArgumentException.class, () -> logic.setChannel(0));
    assertThrows(IllegalArgumentException.class, () -> logic.setChannel(CHANNEL_COUNT + 1));
  }
}
