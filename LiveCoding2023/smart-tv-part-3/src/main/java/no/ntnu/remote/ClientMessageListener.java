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
}
