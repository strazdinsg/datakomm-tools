package no.ntnu.message;

import no.ntnu.TvLogic;

/**
 * A command requesting to turn on the TV.
 */
public class TurnOnCommand extends Command {
  @Override
  public Message execute(TvLogic logic) {
    logic.turnOn();
    return new OkMessage();
  }
}
