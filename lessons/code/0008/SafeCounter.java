// Lesson 0008 exercise: make two shared classes thread-safe.
// Run with:  java SafeCounter.java
// Your job: fill in the two TODOs. The program checks itself.

class Counter {                      // Deck 3 s15: not thread safe
    private int x = 1;
    // TODO 1: make these three methods safe to call from many threads
    void inc() { x = x + 1; }
    void dec() { x = x - 1; }
    int get()  { return x; }
}

class Pair {                         // Deck 3 s28: x == y must always hold
    private int x = 0, y = 0;

    void incBoth() {
        // TODO 2: make the two updates below one atomic action
        x = x + 1;
        y = y + 1;
    }

    synchronized boolean consistent() { return x == y; }
}

public class SafeCounter {
    public static void main(String[] args) throws InterruptedException {
        // Part 1: one thread adds 1 a million times, another subtracts.
        Counter c = new Counter();
        Thread inc = new Thread(() -> {
            for (int i = 0; i < 1_000_000; i++) c.inc();
        });
        Thread dec = new Thread(() -> {
            for (int i = 0; i < 1_000_000; i++) c.dec();
        });
        inc.start(); dec.start();
        inc.join();  dec.join();
        System.out.println("counter = " + c.get() + " (should be 1)");

        // Part 2: one thread updates the pair, another keeps checking it.
        Pair p = new Pair();
        Thread writer = new Thread(() -> {
            for (int i = 0; i < 5_000_000; i++) p.incBoth();
        });
        int[] broken = {0};
        Thread checker = new Thread(() -> {
            while (writer.isAlive()) if (!p.consistent()) broken[0]++;
        });
        writer.start(); checker.start();
        writer.join();  checker.join();
        System.out.println("saw x != y " + broken[0] + " times (should be 0)");

        System.out.println(c.get() == 1 && broken[0] == 0 ? "PASS" : "FAIL");
    }
}
