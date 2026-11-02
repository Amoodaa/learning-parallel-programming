// Lesson 0019 exercise: a lock-free stack (Deck 7 s30-35).
// Run with:  java LockFreeStack.java
// No locks, no synchronized: only AtomicReference.compareAndSet.
// Your job: rewrite push() and pop() as compare-and-set loops.

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

public class LockFreeStack {

    static class Node<E> {
        final E item;
        final Node<E> next;
        Node(E item, Node<E> next) { this.item = item; this.next = next; }
    }

    static class Stack<E> {
        private final AtomicReference<Node<E>> head =
            new AtomicReference<>();

        void push(E x) {
            // TODO (slide 32): this read-then-write is NOT atomic.
            //      Another thread can change head in between, and
            //      its push is lost. Retry with compareAndSet.
            Node<E> oldHead = head.get();
            head.set(new Node<>(x, oldHead));
        }

        // Returns null if the stack is empty.
        E pop() {
            // TODO (slide 33): same problem; two threads can pop
            //      the same node. Retry with compareAndSet.
            Node<E> oldHead = head.get();
            if (oldHead == null) return null;
            head.set(oldHead.next);
            return oldHead.item;
        }
    }

    // ---- checker: no need to change anything below ----
    public static void main(String[] args) throws Exception {
        final int T = 4, EACH = 100_000;
        Stack<Integer> stack = new Stack<>();
        ConcurrentHashMap<Integer, Integer> popped =
            new ConcurrentHashMap<>();
        AtomicInteger dupes = new AtomicInteger();
        CyclicBarrier together = new CyclicBarrier(T);
        Thread[] ts = new Thread[T];
        for (int t = 0; t < T; t++) {
            final int base = t * EACH;
            ts[t] = new Thread(() -> {
                try { together.await(); } catch (Exception e) {}
                // push and pop mixed, then empty the stack
                for (int i = 0; i < EACH; i++) {
                    stack.push(base + i);
                    if (i % 3 == 0) take(stack, popped, dupes);
                }
                while (take(stack, popped, dupes)) {}
            });
            ts[t].start();
        }
        for (Thread t : ts) t.join();
        int total = T * EACH;
        System.out.println("pushed " + total + ", popped "
            + popped.size() + " distinct, popped twice: " + dupes);
        System.out.println(popped.size() == total && dupes.get() == 0
            ? "PASS" : "FAIL (pushes were lost or a node was"
                       + " popped by two threads)");
    }
    static boolean take(Stack<Integer> s,
            ConcurrentHashMap<Integer, Integer> seen, AtomicInteger d) {
        Integer x = s.pop();
        if (x == null) return false;
        if (seen.putIfAbsent(x, 1) != null) d.incrementAndGet();
        return true;
    }
}
