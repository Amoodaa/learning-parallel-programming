// Lesson 0017 exercise: the array smoother (Deck 6 s18-22).
// Run with:  java Smoother.java
// One thread per element replaces data[k] by the average of itself
// and its neighbours. Every thread must read the OLD values, so no
// thread may write until all have read: that is the barrier's job.
// main must wait for all threads before checking: the latch's job.
// Your job: add the CyclicBarrier and the CountDownLatch.

import java.util.*;
import java.util.concurrent.*;

public class Smoother {

    static void smooth(double[] data) {
        int n = data.length;
        // TODO: create the barrier (how many parties?)
        // TODO: create the latch (count from what?)
        for (int j = 0; j < n; j++) {
            final int k = j;
            new Thread(() -> {
                double sum = data[k];
                int count = 1;
                if (k > 0)     { sum += data[k - 1]; count++; }
                if (k < n - 1) { sum += data[k + 1]; count++; }
                // TODO: wait here until every thread has read
                data[k] = sum / count;
                // TODO: tell main this thread is finished
            }).start();
        }
        // TODO: wait for all n threads to finish
    }

    // ---- checker: no need to change anything below ----
    public static void main(String[] args) {
        Random rnd = new Random(42);
        int bad = 0;
        for (int trial = 0; trial < 100; trial++) {
            double[] data = new double[40];
            for (int j = 0; j < data.length; j++)
                data[j] = rnd.nextInt(100);
            double[] expect = new double[data.length];
            for (int k = 0; k < data.length; k++) {
                double s = data[k]; int c = 1;
                if (k > 0) { s += data[k - 1]; c++; }
                if (k < data.length - 1) { s += data[k + 1]; c++; }
                expect[k] = s / c;
            }
            smooth(data);
            if (!Arrays.equals(data, expect)) bad++;
        }
        System.out.println("wrong results: " + bad + " of 100 trials");
        System.out.println(bad == 0 ? "PASS"
            : "FAIL (a thread read a value its neighbour had already"
              + " changed, or main checked before the threads finished)");
    }
}
