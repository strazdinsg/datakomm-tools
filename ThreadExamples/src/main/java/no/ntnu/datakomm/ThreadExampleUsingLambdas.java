package no.ntnu.datakomm;

import java.util.Random;

/**
 * This example shows one option to run code in a new thread: create runnable
 * methods on the fly with lambda expressions. This approach is usable if you
 * have very custom needs to launch different operations in another thread, or
 * you don't want your class to have yet another interface to implement.
 */
public class ThreadExampleUsingLambdas {
  Random random = new Random();

  /**
   * Entrypoint for the application.
   *
   * @param args Command line arguments, not used
   */
  public static void main(String[] args) {
    // We create an object first to get access to its methods
    ThreadExampleUsingLambdas obj = new ThreadExampleUsingLambdas();
    obj.process();
  }

  /**
   * Simulate long processing, in multiple threads.
   */
  public void process() {
    // Create three threads, each of them will call a
    // different method of this class
    // Pay attention to the different syntactic ways to specifying what to run inside the thread
    Thread t1 = new Thread(this::loopFive);
    Thread t2 = new Thread(() -> {
      loopThree();
    });
    Thread t3 = new Thread(() -> loop(10));
    // Start all the threads in parallel
    t1.start();
    t2.start();
    t3.start();

    for (int i = 0; i < 5; ++i) {
      System.out.println("Main thread is still running");
      ThreadHelper.sleep(1);
    }
    System.out.println("Main thread finished work");
  }

  /**
   * Simulate some long calculations inside the current thread.
   *
   * @param iterationCount How many times to repeat the calculation
   */
  public void loop(int iterationCount) {
    long currentThreadId = Thread.currentThread().getId();
    for (int i = 1; i <= iterationCount; ++i) {
      System.out.println("Thread #" + currentThreadId + " iteration " + i);
      // Sleep some random time
      int randomSleepTime = 1 + random.nextInt(2);
      ThreadHelper.sleep(randomSleepTime);
    }
    System.out.println("Thread #" + currentThreadId + " finished work");
  }

  public void loopFive() {
    loop(5);
  }

  public void loopThree() {
    loop(3);
  }
}
