package concurrency.dinner;

import concurrency.dinner.swing.PhilCanvas;
import concurrency.utils.Logger;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Fork {

    private volatile boolean taken = false;
    private PhilCanvas display;
    private int identity;

    private int value;

    private final Lock lock;
    private Condition getCondition;


    public Fork(PhilCanvas disp, int id) {
        display = disp;
        identity = id;
        lock = new ReentrantLock();
        getCondition = lock.newCondition();
        value = id;
    }


    public void put() {
        lock.lock();
        try {
            Logger.logout("put");
            taken = false;
            display.setFork(identity, taken);
            getCondition.signal();
        } finally {
            lock.unlock();
        }
    }

    public void get()
            throws InterruptedException {
        lock.lock();
        try {
            while (taken) getCondition.await();
            Logger.logout("got");
            taken = true;
            display.setFork(identity, taken);
        } finally {
            lock.unlock();
        }
    }

    //for TimeoutPhilosopher
    public boolean get(long timeout, TimeUnit unit)
            throws InterruptedException {

        boolean isSignaled = true;
        lock.lock();
        try {
            while (isSignaled && taken){
                //Condition.await() can return a boolean ,but wait() can't
                isSignaled = getCondition.await(timeout, unit);
            }

            if (isSignaled) {
                Logger.logout("got");
                taken = true;
                display.setFork(identity, taken);
            }else {
                Logger.logout("timeout to get");
            }
        } finally {
            lock.unlock();
        }

        return isSignaled;
    }

    //for ForkValuePhilosopher
    public static synchronized void shuffleValue(Fork f1, Fork f2) {
        if (f1.hashCode() % 2 == 1) {
            int tmp = f1.value;
            f1.value = f2.value;
            f2.value = tmp;
        }
    }


    public int getValue() {
        return value;
    }

}
