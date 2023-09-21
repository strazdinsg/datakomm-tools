package no.ntnu;

/**
 * The allowed traffic light states.
 */
public enum TrafficLightState {
  GREEN, YELLOW, RED;

  /**
   * Create a state enum from a lower-case string.
   *
   * @param s The string representation
   * @return The state enum, or null if the string is invalid
   */
  public static TrafficLightState from(String s) {
    return switch (s) {
      case "green" -> GREEN;
      case "yellow" -> YELLOW;
      case "red" -> RED;
      default -> null;
    };
  }

  @Override
  public String toString() {
    return switch (this) {
      case RED -> "red";
      case YELLOW -> "yellow";
      case GREEN -> "green";
    };
  }
}
