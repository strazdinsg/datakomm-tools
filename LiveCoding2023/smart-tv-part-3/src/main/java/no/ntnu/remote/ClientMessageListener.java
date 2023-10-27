package no.ntnu.remote;

/**
 * A listener who will handle incoming TCP messages from the server.
 */
public interface ClientMessageListener {
  /**
   * This event is fired when the state of the TV has changed.
   *
   * @param isOn True when the TV is on, false when it is off
   */
  void handleTvStateChange(boolean isOn);

  /**
   * This event lets the listener know the number of channels the TV supports.
   *
   * @param channelCount The total number of channels for the TV
   */
  void handleChannelCount(int channelCount);

  /**
   * This event is fired when the channel is changed on the TV.
   *
   * @param channel The current channel on the TV
   */
  void handleChannelChange(int channel);
}
