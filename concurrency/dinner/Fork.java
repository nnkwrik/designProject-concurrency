package concurrency.dinner;

public class Fork {

    private boolean taken = false;
    private PhilCanvas display;
    private int identity;

    public Fork(PhilCanvas disp, int id) {
        display = disp;
        identity = id;
    }

    public synchronized void put() {
        taken = false;
        display.setFork(identity, taken);
        notify();
    }

    public synchronized void get()
            throws InterruptedException {
        while (taken) wait();
        taken = true;
        display.setFork(identity, taken);
    }
}
