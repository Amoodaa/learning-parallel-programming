// Lesson 0002 exercise: stop a busy thread from main, the EggTimer way (Deck 1 slide 73).
// Run with:  java StopMe.java
// Your job: two TODOs in the Worker class. The program checks itself.

class Worker extends Thread {
    // TODO 1: something is missing from this declaration (look at slide 73)
    private boolean running = true;
    private long count = 0;

    public void run() {
        while (running) {
            count++;            // busy work: no sleep, no printing
        }
    }

    public void terminate() {
        // TODO 2: tell the loop to finish
    }

    long getCount() { return count; }
}

public class StopMe {
    public static void main(String[] args) throws InterruptedException {
        Worker w = new Worker();
        w.start();
        Thread.sleep(300);          // let it work for a moment
        w.terminate();
        w.join(2000);               // wait at most 2 seconds

        if (w.isAlive()) {
            System.out.println("FAIL: worker is still running 2s after terminate()");
            System.exit(1);         // stop the program, otherwise the JVM waits forever
        }
        System.out.println("worker counted to " + w.getCount());
        System.out.println("PASS");
    }
}
