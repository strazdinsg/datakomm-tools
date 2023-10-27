package no.ntnu.message;

/**
 * A message telling whether the TV is ON or off.
 */
public class TvStateMessage implements Message {
  private final boolean isOn;

  /**
   * Create a TV state message.
   *
   * @param isOn The TV is ON if this is true, the TV is off if this is false.
   */
  public TvStateMessage(boolean isOn) {
    this.isOn = isOn;
  }

  /**
   * Check whether the TV is ON.
   *
   * @return ON if true, OFF if false
   */
  public boolean isOn() {
    return isOn;
  }
}
