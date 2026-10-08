// Lesson 0009 exercise: two lock bugs from Deck 3 slides 35 and 41-43.
// Run with:  java TwoLocks.java
// Your job: fill in the two TODOs. The program checks itself.

class Ticket {
    private static int next = 0;             // shared by every Ticket

    synchronized static void take() { next++; }   // locks Ticket.class

    // TODO 1: this locks `this`, not the lock that take() uses.
    //         Make it use the same lock as take() (slide 43).
    synchronized void skip() { next++; }

    synchronized static int get() { return next; }
}

class Timer extends Thread {
    private boolean go = true;
    long ticks;

    // TODO 2: the loop may never see go change (slides 30-36).
    //         Fix it so terminate() always stops the loop.
    public void run() { while (go) ticks++; }

    synchronized void terminate() { go = false; }  // slide 35
}

public class TwoLocks {
    public static void main(String[] args) throws InterruptedException {
        // Part 1: one thread takes tickets, another skips them.
        Ticket t = new Ticket();
        Thread a = new Thread(() -> {
            for (int i = 0; i < 1_000_000; i++) Ticket.take();
        });
        Thread b = new Thread(() -> {
            for (int i = 0; i < 1_000_000; i++) t.skip();
        });
        a.start(); b.start();
        a.join();  b.join();
        int n = Ticket.get();
        System.out.println("next = " + n + " (should be 2000000)");

        // Part 2: stop a busy thread from main.
        Timer timer = new Timer();
        timer.start();
        Thread.sleep(300);
        timer.terminate();
        timer.join(2000);                  // wait at most 2 seconds
        boolean stopped = !timer.isAlive();
        System.out.println("timer stopped: " + stopped);

        System.out.println(n == 2_000_000 && stopped ? "PASS" : "FAIL");
        System.exit(0);                    // end even if timer is stuck
    }
}
