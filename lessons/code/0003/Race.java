// Lesson 0003 demo: non-determinism and a race condition (Deck 1 slides 24, 31).
// Run with:  java Race.java   (run it 3 times and compare)
// Two threads each add 1 to a shared counter, one million times.

public class Race {
    static int count = 0;   // static = shared by every thread (slide 41)

    public static void main(String[] args) throws InterruptedException {
        Runnable addMillion = () -> {
            for (int i = 0; i < 1_000_000; i++) count++;
        };
        Thread a = new Thread(addMillion);
        Thread b = new Thread(addMillion);
        a.start(); b.start();
        a.join();  b.join();

        System.out.println("expected 2000000, got " + count);
        System.out.println("lost updates: " + (2_000_000 - count));
    }
}
