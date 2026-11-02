// Lesson 0013 exercise: the readers/writers control (Deck 4 s32-37).
// Run with:  java ReadWrite.java
// Your job: write the four methods of ReadersWriters. The rules:
//   - one writer at a time, and no readers while it writes;
//   - any number of readers at once;
//   - waiting writers go before new readers.

import java.util.*;
import java.util.concurrent.atomic.*;
import java.util.concurrent.locks.*;

public class ReadWrite {

    static class ReadersWriters {
        private int readers = 0;          // reading right now
        private int waitingWriters = 0;   // blocked in startWrite
        private boolean writing = false;
        private final Lock lock = new ReentrantLock();
        private final Condition wCond = lock.newCondition();
        private final Condition rCond = lock.newCondition();

        void startRead() {
            // TODO (slide 36): wait while someone is writing or a
            //      writer is waiting; then count yourself in.
        }

        void stopRead() {
            // TODO (slide 37): count yourself out; the last reader
            //      out wakes one writer.
        }

        void startWrite() {
            // TODO (slide 34): wait while someone is writing or
            //      reading, counting yourself in waitingWriters
            //      while you wait; then set writing.
        }

        void stopWrite() {
            // TODO (slide 35): clear writing; wake one waiting
            //      writer if there is one, otherwise ALL readers.
        }
    }

    // ---- checker: no need to change anything below ----
    static final AtomicInteger inReaders = new AtomicInteger();
    static final AtomicInteger inWriters = new AtomicInteger();
    static final AtomicInteger clashes = new AtomicInteger();
    static final AtomicInteger maxReaders = new AtomicInteger();

    static void pause(int ms) {
        try { Thread.sleep(ms); } catch (InterruptedException e) {}
    }

    public static void main(String[] args) throws InterruptedException {
        ReadersWriters rw = new ReadersWriters();
        int[] data = new int[2];       // writers keep data[0] == data[1]
        List<Thread> ts = new ArrayList<>();
        for (int r = 0; r < 6; r++) ts.add(new Thread(() -> {
            for (int i = 0; i < 150; i++) {
                rw.startRead();
                int n = inReaders.incrementAndGet();
                maxReaders.accumulateAndGet(n, Math::max);
                if (inWriters.get() > 0) clashes.incrementAndGet();
                int a = data[0];
                pause(1);
                if (data[1] != a) clashes.incrementAndGet();
                inReaders.decrementAndGet();
                rw.stopRead();
            }
        }));
        for (int w = 0; w < 2; w++) ts.add(new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                rw.startWrite();
                if (inWriters.incrementAndGet() > 1
                        || inReaders.get() > 0) clashes.incrementAndGet();
                data[0]++;
                pause(1);
                data[1]++;
                inWriters.decrementAndGet();
                rw.stopWrite();
                pause(2);
            }
        }));
        for (Thread t : ts) { t.setDaemon(true); t.start(); }
        long deadline = System.currentTimeMillis() + 20_000;
        for (Thread t : ts)
            t.join(Math.max(1, deadline - System.currentTimeMillis()));
        if (ts.stream().anyMatch(Thread::isAlive)) {
            System.out.println("still running after 20 s: a thread is"
                + " waiting for a signal that never comes");
            System.out.println("FAIL");
            System.exit(1);
        }
        System.out.println("clashes: " + clashes
            + ", most readers at once: " + maxReaders
            + ", writes: " + data[0]);
        if (clashes.get() > 0)
            System.out.println("FAIL (a writer overlapped a reader"
                + " or another writer)");
        else if (maxReaders.get() < 2)
            System.out.println("FAIL (readers never read at the same"
                + " time: they should be allowed to)");
        else if (data[0] != 200)
            System.out.println("FAIL (writes were lost)");
        else System.out.println("PASS");
    }
}
