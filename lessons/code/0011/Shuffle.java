// Lesson 0011 exercise: shuffle an array with one lock per element
// (Deck 3 s71-75, s89-91) without deadlocking.
// Run with:  java Shuffle.java
// Your job: fix the TODO in swap(). The program checks itself.

import java.util.*;
import java.util.concurrent.locks.*;

public class Shuffle {
    static final int N = 10;
    static final int[] data = new int[N];
    static final Lock[] locks = new Lock[N];

    // Swap data[g] and data[h] atomically, holding both elements' locks.
    static void swap(int g, int h) {
        // TODO: this takes the locks in the order the caller gave them.
        //       Two threads swapping (3, 7) and (7, 3) can each hold one
        //       lock and wait forever for the other. Take them in a
        //       fixed order instead (slides 70, 73).
        Lock first = locks[g], second = locks[h];

        first.lock();
        try {
            second.lock();
            try {
                int t = data[g]; data[g] = data[h]; data[h] = t;
            } finally { second.unlock(); }
        } finally { first.unlock(); }
    }

    public static void main(String[] args) throws InterruptedException {
        for (int j = 0; j < N; j++) {
            data[j] = j;
            locks[j] = new ReentrantLock();
        }
        Thread[] ts = new Thread[4];
        for (int i = 0; i < ts.length; i++) {
            ts[i] = new Thread(() -> {
                Random r = new Random();
                for (int k = 0; k < 200_000; k++) {
                    int g = r.nextInt(N), h = r.nextInt(N);
                    if (g != h) swap(g, h);
                }
            });
            ts[i].start();
        }
        long deadline = System.currentTimeMillis() + 5000;
        for (Thread t : ts)
            t.join(Math.max(1, deadline - System.currentTimeMillis()));

        boolean stuck = false;
        for (Thread t : ts) stuck |= t.isAlive();
        if (stuck) {
            System.out.println("still running after 5 s: deadlock");
            System.out.println("FAIL");
            System.exit(1);                  // the stuck threads never end
        }
        int[] sorted = data.clone();
        Arrays.sort(sorted);
        boolean same = Arrays.equals(sorted, new int[]{0,1,2,3,4,5,6,7,8,9});
        System.out.println("shuffled: " + Arrays.toString(data));
        System.out.println(same ? "PASS"
            : "FAIL (values lost or duplicated: a swap was not atomic)");
    }
}
