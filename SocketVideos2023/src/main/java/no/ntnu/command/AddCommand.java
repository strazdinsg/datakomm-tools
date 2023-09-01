package no.ntnu.command;

/**
 * A command where the client asks the server to calculate sum of x and y and return it.
 */
public class AddCommand extends Command {
  private final int x;
  private final int y;

  /**
   * Create an addition command.
   *
   * @param x the first argument to be added to the sum
   * @param y the second argument to be added to the sum
   */
  public AddCommand(int x, int y) {
    this.x = x;
    this.y = y;
  }

  /**
   * Get the sum x + y.
   *
   * @return x + y
   */
  public int getSum() {
    return x + y;
  }
}
