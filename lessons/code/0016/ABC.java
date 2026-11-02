// Lesson 0016 exercise: the a/b/c printing puzzle (Deck 5 s24-30).
// Run with:  java ABC.java
// Three threads print a, b and c. The output must obey (slide 24):
//   1. a b comes before any c;
//   2. b's and c's alternate: b c b c ...;
//   3. at every point, (number of b's + c's) <= number of a's.
// Your job: choose the starting values of the three semaphores and
// add acquire()/release() calls to the three loops.

import java.util.concurrent.*;

public class ABC {
    static final int A = 2000, B = 1000, C = 1000;
    static final StringBuffer out = new StringBuffer();   // the "screen"

    // semA counts a's not yet "used" by a b or c (slide 28).
    // semB / semC: whose turn it is, b or c (slides 26-27).
    static Semaphore semA = new Semaphore(0);   // TODO: starting values
    static Semaphore semB = new Semaphore(0);
    static Semaphore semC = new Semaphore(0);

    static void printA() {
        for (int i = 0; i < A; i++) {
            out.append('a');
            // TODO: announce one more unused a
        }
    }
    static void printB() {
        for (int i = 0; i < B; i++) {
            // TODO: wait for your turn, and for an unused a
            out.append('b');
            // TODO: hand the turn to c
        }
    }
    static void printC() {
        for (int i = 0; i < C; i++) {
            // TODO: wait for your turn, and for an unused a
            out.append('c');
            // TODO: hand the turn to b
        }
    }

    static void acquire(Semaphore s) {
        try { s.acquire(); } catch (InterruptedException e) {}
    }

    // ---- checker: no need to change anything below ----
    public static void main(String[] args) throws InterruptedException {
        Thread[] ts = { new Thread(ABC::printC), new Thread(ABC::printB),
                        new Thread(ABC::printA) };
        for (Thread t : ts) { t.setDaemon(true); t.start(); }
        for (Thread t : ts) t.join(5000);
        String s = out.toString();
        System.out.println("first 60: " + s.substring(0, Math.min(60, s.length())));
        for (Thread t : ts) if (t.isAlive()) {
            System.out.println("still running after 5 s: a thread is"
                + " waiting for a permit nobody will release");
            System.out.println("FAIL");
            System.exit(1);
        }
        int a = 0, bc = 0;
        char last = 'c';             // so the first b/c must be a b
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if (ch == 'a') { a++; continue; }
            bc++;
            String where = " at position " + i;
            if (ch == last) { fail("two " + ch + "'s in a row (rule 2)"
                + (bc == 1 ? ": the first must be a b (rule 1)" : "") + where); }
            if (bc > a) fail("more b's+c's than a's (rule 3)" + where);
            last = ch;
        }
        if (s.length() != A + B + C) fail("wrong number of letters");
        System.out.println("PASS");
    }
    static void fail(String why) {
        System.out.println("FAIL (" + why + ")");
        System.exit(1);
    }
}
