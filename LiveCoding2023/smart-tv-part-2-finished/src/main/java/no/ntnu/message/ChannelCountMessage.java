package no.ntnu.message;

/**
 * A message containing the number of channels for the TV.
 */
public class ChannelCountMessage implements Message {
  private final int channelCount;

  public ChannelCountMessage(int channelCount) {
    this.channelCount = channelCount;
  }

  public int getChannelCount() {
    return channelCount;
  }
}
