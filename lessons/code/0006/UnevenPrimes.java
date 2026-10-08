// Lesson 0006 exercise: count primes below 10 million, first with one
// big piece per thread, then with many small pieces.
// Run with:  java UnevenPrimes.java
// Your job: fill in the TODO in countPrimes(). The program checks itself.

import java.util.*;
import java.util.concurrent.*;

class Count implements Callable<Integer> {   // counts primes in [from, to)
    private final int from, to;
    Count(int from, int to) { this.from = from; this.to = to; }

    public Integer call() {
        int c = 0;
        for (int n = from; n < to; n++) if (isPrime(n)) c++;
        return c;
    }

    static boolean isPrime(int n) {          // Deck 2 s75
        if (n < 2) return false;
        for (int k = 2; k <= (int) Math.sqrt(n); k++)
            if (n % k == 0) return false;
        return true;
    }
}

public class UnevenPrimes {
    static final int N = 10_000_000;

    // Split [0, N) into pieces of size `chunk` (the last may be shorter),
    // run them all on the pool, and return the total count.
    // If `show` is true, print when each piece finishes.
    static int countPrimes(ExecutorService pool, int chunk, boolean show)
            throws Exception {
        long t0 = System.currentTimeMillis();
        List<Future<Integer>> futures = new ArrayList<>();

        // TODO: one Count task per piece [from, from + chunk),
        //       covering 0..N with no gaps; the last piece stops at N

        int total = 0;
        for (int i = 0; i < futures.size(); i++) {
            total += futures.get(i).get();
            if (show) System.out.printf("  piece %d done at %d ms%n",
                i, System.currentTimeMillis() - t0);
        }
        return total;
    }

    public static void main(String[] args) throws Exception {
        int expected = 664_579;  // primes below 10 million
        // Two threads, like the slides' dual-core machine.
        ExecutorService pool = Executors.newFixedThreadPool(2);

        // Check: pieces that don't divide N evenly (3M, 3M, 3M, 1M).
        int c = countPrimes(pool, 3_000_000, false);
        if (c != expected) {
            pool.shutdown();
            System.out.println("pieces of 3,000,000: " + c + " primes");
            System.out.println("FAIL (expected " + expected + ")");
            return;
        }

        System.out.println("2 pieces of 5,000,000 (slides 76-77):");
        long t0 = System.currentTimeMillis();
        int a = countPrimes(pool, N / 2, true);
        long t1 = System.currentTimeMillis();
        int b = countPrimes(pool, 1000, false);   // slide 78
        long t2 = System.currentTimeMillis();
        pool.shutdown();

        System.out.printf("  total %d ms%n", t1 - t0);
        System.out.printf("10,000 pieces of 1000 (slide 78): %d ms%n",
            t2 - t1);
        System.out.println(a == expected && b == expected
            ? "PASS" : "FAIL (expected " + expected + " both times)");
    }
}
