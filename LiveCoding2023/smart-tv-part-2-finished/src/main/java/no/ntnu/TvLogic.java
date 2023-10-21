package no.ntnu;

/**
 * Smart TV - the logic.
 */
public class TvLogic {
  private static final String ERR_MUST_TURN_ON = "Must turn on the TV first";
  private boolean isTvOn;
  private final int numberOfChannels;
  private int currentChannel;

  /**
   * Create a new Smart TV.
   *
   * @param numberOfChannels The total number of channels the TV has
   */
  public TvLogic(int numberOfChannels) {
    if (numberOfChannels < 1) {
      throw new IllegalArgumentException("Number of channels must be a positive number");
    }

    this.numberOfChannels = numberOfChannels;
    isTvOn = false;
    currentChannel = 1;
  }

  /**
   * Turn ON the TV.
   */
  public void turnOn() {
    isTvOn = true;
  }

  /**
   * Turn OFF the TV.
   */
  public void turnOff() {
    isTvOn = false;
  }

  /**
   * Check whether the TV is ON or OFF.
   *
   * @return True when the TV is ON, false when OFF.
   */
  public boolean isTvOn() {
    return isTvOn;
  }

  /**
   * Get the number of channels this TV has.
   *
   * @return The total number of channels
   * @throws IllegalStateException When the TV is OFF
   */
  public int getNumberOfChannels() throws IllegalStateException {
    if (!isTvOn) {
      throw new IllegalStateException(ERR_MUST_TURN_ON);
    }
    return numberOfChannels;
  }

  /**
   * Get the current channel of the TV.
   *
   * @return The current channel
   * @throws IllegalStateException When the TV is OFF
   */
  public int getCurrentChannel() throws IllegalStateException {
    if (!isTvOn) {
      throw new IllegalStateException(ERR_MUST_TURN_ON);
    }
    return currentChannel;
  }

  /**
   * Set the channel for the TV.
   *
   * @param channel The desired channel
   * @throws IllegalArgumentException When the channel number is invalid
   * @throws IllegalStateException When the TV is OFF
   */
  public void setChannel(int channel) throws IllegalArgumentException, IllegalStateException {
    if (!isTvOn) {
      throw new IllegalStateException(ERR_MUST_TURN_ON);
    }
    if (channel <= 0 || channel > numberOfChannels) {
      throw new IllegalArgumentException("Invalid channel number");
    }
    currentChannel = channel;
  }
}
