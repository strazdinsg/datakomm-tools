package no.ntnu.datakomm;

import java.util.Random;

/**
 * This example shows one option to run code in a new thread: create a class
 * that extends the Thread class.
 * We simulate some long calculations to demonstrate parallel execution.
 */
public class MyThreadClass extends Thread {
  Random random = new Random();

  /**
   * Entrypoint for the application.
   *
   * @param args Command line arguments, not used
   */
  public static void main(String[] args) {
    // Start three calculation threads in parallel
    Thread t1 = new MyThreadClass();
    Thread t2 = new MyThreadClass();
    Thread t3 = new MyThreadClass();
    t1.start();
    t2.start();
    t3.start();
    for (int i = 0; i < 5; ++i) {
      System.out.println("The main thread is still running");
      ThreadHelper.sleep(1);
    }
    System.out.println("Main thread finished work");
  }

  @Override
  public void run() {
    long currentThreadId = Thread.currentThread().getId();
    for (int i = 0; i < 5; ++i) {
      System.out.println("Thread #" + currentThreadId + " calculating something...");
      int randomSleepTime = 1 + random.nextInt(2);
      ThreadHelper.sleep(randomSleepTime);
    }
    System.out.println("Thread #" + currentThreadId + " finished work");
  }
}
