package no.ntnu.command;

/**
 * An echo-command where the client is requesting to echo the same message back to it.
 */
public class EchoCommand extends Command {
  private final String message;

  public EchoCommand(String message) {
    this.message = message;
  }

  /**
   * Get the client's message.
   *
   * @return The clients message sent with this command
   */
  public String getMessage() {
    return message;
  }
}
