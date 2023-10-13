package no.ntnu;

/**
 * Smart TV - the logic.
 */
public class TvLogic {
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
    // TODO
  }

  /**
   * Turn OFF the TV.
   */
  public void turnOff() {
    // TODO
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
   */
  public int getNumberOfChannels() {
    return numberOfChannels;
  }

  /**
   * Get the current channel of the TV.
   *
   * @return The current channel
   */
  public int getCurrentChannel() {
    return currentChannel;
  }

  /**
   * Set the channel for the TV.
   *
   * @param channel The desired channel
   * @throws IllegalArgumentException When the channel number is invalid
   */
  public void setChannel(int channel) throws IllegalArgumentException {
    // TODO Implement this
  }
}
