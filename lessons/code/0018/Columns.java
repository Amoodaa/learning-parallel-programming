// Lesson 0018 exercise: one producer, two consumers and an
// ArrayBlockingQueue (Deck 6 s23-32).
// Run with:  java Columns.java
// The producer puts one job per column of a matrix in the queue.
// Each consumer takes jobs and fills that column. A job with
// col == -1 is a "sentinel": it means "no more work, stop".
// Your job: make both consumers stop, and fill every column once.

import java.util.*;
import java.util.concurrent.*;

public class Columns {
    static final int ROWS = 200, COLS = 500, CONSUMERS = 2;
    static final int[][] data = new int[ROWS][COLS];
    static final int[] filled = new int[COLS];   // times each col filled

    record Job(int col) {}                       // slide 27's BufferData
    static final ArrayBlockingQueue<Job> queue =
        new ArrayBlockingQueue<>(8);

    static void producer() throws InterruptedException {
        for (int c = 0; c < COLS; c++) queue.put(new Job(c));
        // TODO (slide 30): one sentinel for each consumer.
    }

    static void consumer() throws InterruptedException {
        // TODO (slides 31-32): take jobs until you get a sentinel.
        while (true) {
            Job job = queue.take();
            fill(job.col());
        }
    }

    static void fill(int col) {
        for (int r = 0; r < ROWS; r++) data[r][col] = col + 1;
        synchronized (filled) { filled[col]++; }
    }

    // ---- checker: no need to change anything below ----
    public static void main(String[] args) throws InterruptedException {
        List<Thread> ts = new ArrayList<>();
        ts.add(new Thread(() -> run(Columns::producer)));
        for (int i = 0; i < CONSUMERS; i++)
            ts.add(new Thread(() -> run(Columns::consumer)));
        for (Thread t : ts) { t.setDaemon(true); t.start(); }
        for (Thread t : ts) t.join(5000);
        if (ts.stream().anyMatch(Thread::isAlive)) {
            System.out.println("still running after 5 s: a consumer is"
                + " blocked in take() on an empty queue");
            System.out.println("FAIL");
            System.exit(1);
        }
        int wrong = 0;
        for (int c = 0; c < COLS; c++) {
            if (filled[c] != 1) wrong++;
            else for (int r = 0; r < ROWS; r++)
                if (data[r][c] != c + 1) { wrong++; break; }
        }
        System.out.println("columns filled wrongly: " + wrong
            + " of " + COLS + ", jobs left in queue: " + queue.size());
        System.out.println(wrong == 0 && queue.isEmpty() ? "PASS"
            : "FAIL (some column was skipped or filled twice)");
    }
    interface Work { void go() throws InterruptedException; }
    static void run(Work w) {
        try { w.go(); } catch (InterruptedException e) {}
    }
}
