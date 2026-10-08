// Lesson 0007 exercise: lesson 6's prime count, done with Fork/Join.
// Run with:  java ForkPrimes.java
// Your job: fill in the TODO in compute(). The program checks itself.

import java.util.concurrent.*;

class CountPrimes extends RecursiveTask<Integer> {  // like Deck 2 s88-90
    static final int BaseBlockSize = 1000;
    private final int lb, ub;                      // counts primes in [lb, ub)

    CountPrimes(int lb, int ub) { this.lb = lb; this.ub = ub; }

    protected Integer compute() {
        if (ub - lb <= BaseBlockSize) {            // small: just do it
            int c = 0;
            for (int n = lb; n < ub; n++) if (isPrime(n)) c++;
            return c;
        }
        // TODO: split [lb, ub) in half, fork both halves,
        //       join both, and return the two counts added up
        return 0;
    }

    static boolean isPrime(int n) {                // Deck 2 s75
        if (n < 2) return false;
        for (int k = 2; k <= (int) Math.sqrt(n); k++)
            if (n % k == 0) return false;
        return true;
    }
}

public class ForkPrimes {
    public static void main(String[] args) {
        int N = 10_000_000, expected = 664_579;

        long t0 = System.currentTimeMillis();       // one thread, no pool
        int serial = countSerial(N);
        long t1 = System.currentTimeMillis();

        // Two threads, like lesson 6 and the slides' dual-core machine.
        ForkJoinPool pool = new ForkJoinPool(2);
        int got = pool.invoke(new CountPrimes(0, N));
        long t2 = System.currentTimeMillis();
        pool.shutdown();

        System.out.printf("no threads: %d primes, %d ms%n", serial, t1 - t0);
        System.out.printf("fork/join:  %d primes, %d ms%n", got, t2 - t1);
        if (got != expected) {
            System.out.println("FAIL (expected " + expected + ")");
            return;
        }
        double speedup = (double) (t1 - t0) / (t2 - t1);
        System.out.printf("speed-up %.1fx on 2 threads%n", speedup);
        System.out.println(speedup >= 1.3 ? "PASS"
            : "FAIL (right count, but no faster: are your forks"
            + " running one after the other? See slide 87)");
    }

    static int countSerial(int n) {
        int c = 0;
        for (int i = 0; i < n; i++) if (CountPrimes.isPrime(i)) c++;
        return c;
    }
}
