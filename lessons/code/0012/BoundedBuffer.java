// Lesson 0012 exercise: a bounded buffer with a lock and two
// condition variables (Deck 4 s13-16).
// Run with:  java BoundedBuffer.java
// Your job: write put() and get(). The program checks itself.

import java.util.*;
import java.util.concurrent.atomic.*;
import java.util.concurrent.locks.*;

public class BoundedBuffer {

    static class Buffer<E> {
        private final int max;
        private final ArrayList<E> items = new ArrayList<>();
        private final Lock lock = new ReentrantLock();
        private final Condition notFull = lock.newCondition();
        private final Condition notEmpty = lock.newCondition();

        Buffer(int max) { this.max = max; }

        // Add x at the end. If the buffer is full, wait until it isn't.
        void put(E x) {
            // TODO (slide 15): lock; while full, await notFull;
            //      add x; signal notEmpty; unlock in finally.
            //      Signal on EVERY put, not only when the buffer
            //      was empty (see the errata for slide 15).
        }

        // Remove and return the oldest item. If empty, wait.
        E get() {
            // TODO (slide 16): the mirror image of put().
            return null;
        }

        // Used by the checker only.
        int size() {
            lock.lock();
            try { return items.size(); } finally { lock.unlock(); }
        }
    }

    // ---- checker: no need to change anything below ----
    public static void main(String[] args) throws InterruptedException {
        final int MAX = 3, PRODUCERS = 4, CONSUMERS = 4, EACH = 20_000;
        Buffer<Integer> buf = new Buffer<>(MAX);
        AtomicLong sum = new AtomicLong();
        AtomicInteger got = new AtomicInteger(), nulls = new AtomicInteger();
        AtomicInteger tooBig = new AtomicInteger();
        List<Thread> ts = new ArrayList<>();
        for (int p = 0; p < PRODUCERS; p++) {
            final int base = p * EACH;
            ts.add(new Thread(() -> {
                for (int i = 1; i <= EACH; i++) {
                    buf.put(base + i);
                    if (buf.size() > MAX) tooBig.incrementAndGet();
                }
            }));
        }
        for (int c = 0; c < CONSUMERS; c++) {
            ts.add(new Thread(() -> {
                for (int i = 0; i < EACH; i++) {
                    Integer x = buf.get();
                    if (x == null) { nulls.incrementAndGet(); continue; }
                    sum.addAndGet(x);
                    got.incrementAndGet();
                }
            }));
        }
        for (Thread t : ts) { t.setDaemon(true); t.start(); }
        long deadline = System.currentTimeMillis() + 10_000;
        for (Thread t : ts)
            t.join(Math.max(1, deadline - System.currentTimeMillis()));

        if (ts.stream().anyMatch(Thread::isAlive)) {
            System.out.println("still running after 10 s: some thread"
                + " is waiting for a signal that never comes");
            System.out.println("FAIL");
            System.exit(1);
        }
        long n = (long) PRODUCERS * EACH;
        long expected = n * (n + 1) / 2;
        System.out.println("items taken: " + got + " of " + n
            + ", nulls: " + nulls + ", over capacity: " + tooBig);
        boolean ok = got.get() == n && nulls.get() == 0
            && sum.get() == expected && tooBig.get() == 0;
        System.out.println(ok ? "PASS"
            : "FAIL (items lost, duplicated, null, or buffer overfilled)");
    }
}
