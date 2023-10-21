package no.ntnu.message;

/**
 * An error message after a command execution which failed.
 */
public class ErrorMessage implements Message {
  private final String message;

  public ErrorMessage(String message) {
    this.message = message;
  }

  /**
   * Get the error message.
   *
   * @return Human-readable error message
   */
  public String getMessage() {
    return message;
  }
}
