package no.ntnu.message;

import no.ntnu.TvLogic;

/**
 * A command requesting to turn off the TV.
 */
public class TurnOffCommand extends Command {
  @Override
  public Message execute(TvLogic logic) {
    logic.turnOff();
    return new OkMessage();
  }
}
