// Lesson 0014 exercise: the shared bathroom (Deck 4 s52).
// Run with:  java Bathroom.java
// Your job: write enter() and leave() with a lock and a condition
// variable. The rules (slide 52):
//   - the bathroom is empty, or used by women only, or men only;
//   - anyone may enter an empty bathroom;
//   - while women are inside, more women may enter but no men,
//     and the other way round;
//   - capacity is unlimited.

import java.util.*;
import java.util.concurrent.atomic.*;
import java.util.concurrent.locks.*;

public class Bathroom {

    static class Room {
        private int women = 0, men = 0;   // people inside now
        private final Lock lock = new ReentrantLock();
        private final Condition changed = lock.newCondition();

        void enter(boolean woman) {
            // TODO: lock; while someone of the OTHER gender is
            //       inside, await; then count yourself in.
        }

        void leave(boolean woman) {
            // TODO: lock; count yourself out; if the room is now
            //       empty, wake EVERYONE waiting (why everyone?).
        }
    }

    // ---- checker: no need to change anything below ----
    public static void main(String[] args) throws InterruptedException {
        Room room = new Room();
        AtomicInteger womenIn = new AtomicInteger();
        AtomicInteger menIn = new AtomicInteger();
        AtomicInteger clashes = new AtomicInteger();
        AtomicInteger maxTogether = new AtomicInteger();
        List<Thread> ts = new ArrayList<>();
        for (int p = 0; p < 16; p++) {
            boolean woman = p % 2 == 0;
            AtomicInteger mine = woman ? womenIn : menIn;
            AtomicInteger other = woman ? menIn : womenIn;
            ts.add(new Thread(() -> {
                Random rnd = new Random();
                for (int i = 0; i < 100; i++) {
                    room.enter(woman);
                    maxTogether.accumulateAndGet(
                        mine.incrementAndGet(), Math::max);
                    if (other.get() > 0) clashes.incrementAndGet();
                    try { Thread.sleep(rnd.nextInt(2)); }
                    catch (InterruptedException e) {}
                    if (other.get() > 0) clashes.incrementAndGet();
                    mine.decrementAndGet();
                    room.leave(woman);
                }
            }));
        }
        for (Thread t : ts) { t.setDaemon(true); t.start(); }
        long deadline = System.currentTimeMillis() + 20_000;
        for (Thread t : ts)
            t.join(Math.max(1, deadline - System.currentTimeMillis()));
        if (ts.stream().anyMatch(Thread::isAlive)) {
            System.out.println("still running after 20 s: someone is"
                + " waiting for a signal that never comes");
            System.out.println("FAIL");
            System.exit(1);
        }
        System.out.println("clashes: " + clashes
            + ", most people of one gender inside at once: "
            + maxTogether);
        if (clashes.get() > 0)
            System.out.println("FAIL (women and men were inside"
                + " together)");
        else if (maxTogether.get() < 2)
            System.out.println("FAIL (only one person at a time:"
                + " people of the same gender may share)");
        else System.out.println("PASS");
    }
}
