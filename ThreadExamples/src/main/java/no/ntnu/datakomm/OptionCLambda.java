package no.ntnu.datakomm;

import java.util.Random;

/**
 * This example shows one option to run code in a new thread: create runnable
 * methods on the fly with lambda expressions. This approach is usable if you
 * have very custom needs to launch different operations in another thread, or
 * you don't want your class to have yet another interface to implement.
 */
public class OptionCLambda {
    Random random = new Random();

    public static void main(String[] args) throws InterruptedException {
        // We create an object first to get access to it's methods
        // We could use static methods as well in this example
        OptionCLambda obj = new OptionCLambda();
        obj.process();
    }

    public void process() throws InterruptedException {
        // Create three threads, each of them will call a 
        // different method of this class
        Thread t1 = new Thread(() -> {
            loopFive();
        });
        Thread t2 = new Thread(() -> {
            loopThree();
        });
        Thread t3 = new Thread(() -> {
            loop(10);
        });
        // Start all the threads in parallel
        t1.start();
        t2.start();
        t3.start();
        for (int i = 0; i < 5; ++i) {
            System.out.println("Main thread is still running");
            Thread.sleep(1000); // Put the current thread to sleep for 1 second.
        }
        System.out.println("Main thread finished work");
    }

    /**
     * Simulate some long calculations
     *
     * @param iterationCount How many times to repeat the calculation
     */
    public void loop(int iterationCount) {
        long currentThreadId = Thread.currentThread().getId();
        for (int i = 1; i <= iterationCount; ++i) {
            System.out.println("Thread #" + currentThreadId + " iteration " + i);
            // Sleep some random time
            int sleepTime = 1000 + random.nextInt(2000);
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException ex) {
                System.out.println("Someone interrupted sleeping for thread"
                        + currentThreadId);
            }
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
