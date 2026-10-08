// Lesson 0005 exercise: lesson 4's fair sum, rewritten with a thread pool.
// Run with:  java PoolSum.java
// Your job: fill in the three TODOs in main(). The program checks itself.

import java.util.*;
import java.util.concurrent.*;

class Sum implements Callable<Long> {     // like Deck 2 s71-72
    private final int[] data;
    private final int from, to;           // sums data[from..to)

    Sum(int[] data, int from, int to) {
        this.data = data; this.from = from; this.to = to;
    }

    public Long call() {                  // returns the result
        long s = 0;
        for (int i = from; i < to; i++) s += data[i];
        return s;
    }
}

public class PoolSum {
    public static void main(String[] args) throws Exception {
        int k = Runtime.getRuntime().availableProcessors();
        int[] data = new int[10_000_003];
        for (int i = 0; i < data.length; i++) data[i] = i % 10;

        int[] index = new int[k + 1];
        for (int j = 0; j <= k; j++)
            index[j] = (int) ((long) j * data.length / k);

        ExecutorService pool = Executors.newFixedThreadPool(k);
        List<Future<Long>> futures = new ArrayList<>();

        for (int j = 0; j < k; j++) {
            // TODO 1: submit a new Sum for segment j to the pool,
            //         and add the Future it returns to futures
        }

        long total = 0;
        for (Future<Long> f : futures) {
            // TODO 2: wait for f's result and add it to total
        }

        // TODO 3: shut the pool down

        long expected = 45_000_003L;
        System.out.println(futures.size() + " tasks, total = " + total);
        if (!pool.isShutdown()) {
            System.out.println("FAIL (pool still running: the program"
                + " would never exit)");
            System.exit(1);
        }
        System.out.println(total == expected
            ? "PASS" : "FAIL (expected " + expected + ")");
    }
}
