package no.ntnu.message;

/**
 * A message reporting the current channel of a TV.
 */
public class CurrentChannelMessage implements Message {
  private final int channel;

  /**
   * Create a message which reports the current channel of a TV.
   *
   * @param channel The current channel of the TV
   */
  public CurrentChannelMessage(int channel) {
    this.channel = channel;
  }

  /**
   * Get the current channel of the TV.
   *
   * @return The current channel of the TV
   */
  public int getChannel() {
    return channel;
  }
}
