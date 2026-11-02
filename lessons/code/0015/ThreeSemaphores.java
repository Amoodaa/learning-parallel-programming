// Lesson 0015 exercise: the three semaphore patterns from
// Deck 5 s6-17. Run with:  java ThreeSemaphores.java
// Your job: fill in the 3 TODO parts. Each part only needs the
// right starting value and acquire()/release() calls.
// The checker runs each part 200 times with random delays.

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

public class ThreeSemaphores {

    // ---------- Part 1 (s6-9): A must run before B ----------
    static Semaphore aDone;
    static void resetPart1() {
        aDone = new Semaphore(0);            // TODO: starting value?
    }
    static void taskA() {
        log("A");
        // TODO: tell B that A is done
    }
    static void taskB() {
        // TODO: wait until A is done
        log("B");
    }

    // ---------- Part 2 (s10-13): rendezvous ----------
    // st2 must not run until st3 has; st4 not until st1 has.
    static Semaphore aArrived, bArrived;
    static void resetPart2() {
        aArrived = new Semaphore(0);         // TODO: starting values?
        bArrived = new Semaphore(0);
    }
    static void taskTA() {
        log("st1");
        // TODO: say "A has arrived", wait for B
        log("st2");
    }
    static void taskTB() {
        log("st3");
        // TODO: say "B has arrived", wait for A
        log("st4");
    }

    // ---------- Part 3 (s14-17): at most 3 cars in the tunnel ----
    static Semaphore control;
    static void resetPart3() {
        control = new Semaphore(0);          // TODO: starting value?
    }
    static void car() {
        // TODO: get a place in the tunnel
        inTunnel();      // the checker counts cars inside here
        // TODO: give the place back
    }

    // acquire() throws InterruptedException; this hides the try.
    static void acquire(Semaphore s) {
        try { s.acquire(); } catch (InterruptedException e) {}
    }

    // ---- checker: no need to change anything below ----
    static final List<String> events =
        Collections.synchronizedList(new ArrayList<>());
    static void log(String e) { jitter(); events.add(e); }
    static final AtomicInteger inside = new AtomicInteger();
    static final AtomicInteger most = new AtomicInteger();
    static void inTunnel() {
        most.accumulateAndGet(inside.incrementAndGet(), Math::max);
        jitter();
        inside.decrementAndGet();
    }
    static void jitter() {
        int r = ThreadLocalRandom.current().nextInt(3);
        try { Thread.sleep(0, r * 300_000); }
        catch (InterruptedException e) {}
        if (r == 0) Thread.yield();
    }

    static boolean run(Runnable... tasks) throws InterruptedException {
        List<Thread> ts = new ArrayList<>();
        for (Runnable r : tasks) ts.add(new Thread(r));
        Collections.shuffle(ts);
        for (Thread t : ts) { t.setDaemon(true); t.start(); }
        for (Thread t : ts) t.join(2000);
        return ts.stream().noneMatch(Thread::isAlive);
    }
    static int before(String a, String b) {
        return events.indexOf(a) < events.indexOf(b) ? 1 : 0;
    }

    public static void main(String[] args) throws InterruptedException {
        boolean all = true;

        int ok = 0, hung = 0;
        for (int i = 0; i < 200; i++) {
            events.clear(); resetPart1();
            if (!run(ThreeSemaphores::taskB, ThreeSemaphores::taskA))
                { hung++; break; }
            ok += before("A", "B");
        }
        all &= report("Part 1 (A before B)", ok, hung);

        ok = 0; hung = 0;
        for (int i = 0; i < 200; i++) {
            events.clear(); resetPart2();
            if (!run(ThreeSemaphores::taskTA, ThreeSemaphores::taskTB))
                { hung++; break; }
            ok += before("st3", "st2") & before("st1", "st4");
        }
        all &= report("Part 2 (rendezvous)", ok, hung);

        ok = 0; hung = 0;
        for (int i = 0; i < 200; i++) {
            most.set(0); resetPart3();
            Runnable[] cars = new Runnable[8];
            Arrays.fill(cars, (Runnable) ThreeSemaphores::car);
            if (!run(cars)) { hung++; break; }
            if (most.get() <= 3) ok++;
        }
        all &= report("Part 3 (tunnel, max 3)", ok, hung);
        System.out.println(all ? "PASS" : "FAIL");
        System.exit(0);
    }
    static boolean report(String name, int ok, int hung) {
        System.out.printf("%-24s %3d/200 correct%s%n", name, ok,
            hung > 0 ? ", then stuck: nobody released (part stopped)" : "");
        return ok == 200;
    }
}
