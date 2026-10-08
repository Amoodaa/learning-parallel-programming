// Lesson 0010 exercise: coarse- vs fine-grained locking (Deck 3 s62-66).
// Run with:  java FineLocks.java
// Your job: fill in the two TODOs in FineArray. The program checks itself.

import java.util.Random;

class CoarseArray {                     // slide 63: one lock, the object
    private final int[] data;
    CoarseArray(int n) { data = new int[n]; }

    synchronized void inc(int j, int x) {
        data[j] = Slow.add(data[j], x);
    }
    synchronized int get(int j) { return data[j]; }
}

class FineArray {                       // slides 64-66: a lock per element
    private final int[] data;
    private final Object[] locks;

    FineArray(int n) {
        data = new int[n];
        locks = new Object[n];
        for (int j = 0; j < n; j++) locks[j] = new Object();
    }

    void inc(int j, int x) {
        // TODO 1: do the same update as CoarseArray.inc,
        //         but lock only element j's lock
    }

    int get(int j) {
        // TODO 2: return data[j], under element j's lock
        return 0;
    }
}

class Slow {   // an update that takes a little while, like real work
    static int add(int v, int x) {
        for (int k = 0; k < 300; k++) Thread.onSpinWait();
        return v + x;
    }
}

public class FineLocks {
    static final int N = 1000, THREADS = 8, OPS = 10_000;

    interface Inc { void inc(int j, int x); }

    static long run(Inc target) throws InterruptedException {
        Thread[] ts = new Thread[THREADS];
        long t0 = System.currentTimeMillis();
        for (int i = 0; i < THREADS; i++) {
            ts[i] = new Thread(() -> {
                Random r = new Random();
                for (int k = 0; k < OPS; k++) target.inc(r.nextInt(N), 1);
            });
            ts[i].start();
        }
        for (Thread t : ts) t.join();
        return System.currentTimeMillis() - t0;
    }

    public static void main(String[] args) throws InterruptedException {
        CoarseArray coarse = new CoarseArray(N);
        FineArray fine = new FineArray(N);
        long tc = run(coarse::inc);
        long tf = run(fine::inc);

        long sc = 0, sf = 0;
        for (int j = 0; j < N; j++) { sc += coarse.get(j); sf += fine.get(j); }
        long expected = (long) THREADS * OPS;
        System.out.printf("coarse: total %d, %d ms%n", sc, tc);
        System.out.printf("fine:   total %d, %d ms%n", sf, tf);

        if (sf != expected)
            System.out.println("FAIL (fine total should be " + expected + ")");
        else if (tf * 2 > tc)
            System.out.println("FAIL (right total, but not much faster:"
                + " are you locking the whole array?)");
        else
            System.out.println("PASS");
    }
}
