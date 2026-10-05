// Lesson 0001 exercise: split an array in two, sum each half on its own thread.
// Run with:  java SumHalves.java
// Your job: fill in the two TODOs in main(). The program checks itself.

class Summer extends Thread {
    private final int[] data;
    private final int from, to;   // sums data[from..to)
    private long result;

    Summer(int[] data, int from, int to) {
        this.data = data; this.from = from; this.to = to;
    }

    public void run() {
        long s = 0;
        for (int i = from; i < to; i++) s += data[i];
        result = s;
    }

    long getResult() { return result; }
}

public class SumHalves {
    public static void main(String[] args) throws InterruptedException {
        int[] data = new int[10_000_000];
        for (int i = 0; i < data.length; i++) data[i] = i % 10;

        int mid = data.length / 2;
        Summer left  = new Summer(data, 0, mid);
        Summer right = new Summer(data, mid, data.length);

        // TODO 1: make both threads begin working (in parallel, not one after the other)

        // TODO 2: make main wait until both threads have finished

        long total = left.getResult() + right.getResult();
        long expected = 45_000_000L;
        System.out.println("total = " + total);
        System.out.println(total == expected ? "PASS" : "FAIL (expected " + expected + ")");
    }
}
