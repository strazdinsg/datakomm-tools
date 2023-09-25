package no.ntnu.datakomm;

/**
 * Some helper functions for CPU threads.
 */
public class ThreadHelper {
  /**
   * Not allowed to create instances of this class.
   */
  private ThreadHelper() {
  }

  /**
   * Sleep the desired duration. This function simply makes the code
   * nicer by handling the InterruptedException.
   *
   * @param seconds Duration in seconds
   */
  public static void sleep(long seconds) {
    try {
      Thread.sleep(1000 * seconds); // Put the current thread to sleep for 1 second.
    } catch (InterruptedException e) {
      System.out.println("Thread interrupted from sleeping: " + e.getMessage());
      Thread.currentThread().interrupt();
    }
  }
}
