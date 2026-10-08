// Lesson 0004 exercise: split an array fairly over one thread per core.
// Run with:  java FairSplit.java
// Your job: fill in the three TODOs. The program checks itself.

class Summer extends Thread {             // same class as lesson 1
    private final int[] data;
    private final int from, to;           // sums data[from..to)
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

public class FairSplit {

    // Boundaries for splitting n items over k threads (Deck 2 s24).
    // Thread j gets [index[j], index[j+1]).
    static int[] boundaries(int n, int k) {
        int[] index = new int[k + 1];
        for (int j = 0; j <= k; j++) {
            // TODO 1: index[j] = ... (use long so j*n can't overflow)
        }
        return index;
    }

    public static void main(String[] args) throws InterruptedException {
        // Check 1: the slide's own example, N = 103 and k = 4.
        int[] small = boundaries(103, 4);
        String got = java.util.Arrays.toString(small);
        if (!got.equals("[0, 25, 51, 77, 103]")) {
            System.out.println("boundaries(103, 4) = " + got);
            System.out.println("FAIL (slide 24 says [0, 25, 51, 77, 103])");
            return;
        }

        // Check 2: a real sum with one thread per core.
        int k = Runtime.getRuntime().availableProcessors();
        int[] data = new int[10_000_003];  // not a multiple of k
        for (int i = 0; i < data.length; i++) data[i] = i % 10;

        int[] index = boundaries(data.length, k);
        Summer[] workers = new Summer[k];
        for (int j = 0; j < k; j++)
            workers[j] = new Summer(data, index[j], index[j + 1]);

        // TODO 2: start every worker

        // TODO 3: wait for every worker

        long total = 0;
        for (Summer w : workers) total += w.getResult();
        long expected = 45_000_003L;   // 0+1+...+9 per 10, plus 0+1+2
        System.out.println(k + " threads, total = " + total);
        System.out.println(total == expected
            ? "PASS" : "FAIL (expected " + expected + ")");
    }
}
