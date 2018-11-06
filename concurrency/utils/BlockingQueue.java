package concurrency.utils;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author nnkwrik
 * @date 18/11/06 19:33
 * Simple ArrayBlockingQueue
 */
public class BlockingQueue<E> {

    private final ReentrantLock lock;
    private final Condition takeCondition;
    private final Condition putCondition;

    final Object[] items;

    int count;
    int takeIndex;
    int putIndex;


    public BlockingQueue(int capacity) {
        this.items = new Object[capacity];
        lock = new ReentrantLock();
        takeCondition = lock.newCondition();
        putCondition =  lock.newCondition();
    }

    public void put(E e) throws InterruptedException {
        lock.lock();
        try {
            while (count == items.length)
                putCondition.await();
            enqueue(e);
        } finally {
            lock.unlock();
        }
    }

    public E take() throws InterruptedException {
        lock.lock();
        try {
            while (count == 0)
                takeCondition.await();
            return dequeue();
        } finally {
            lock.unlock();
        }
    }

    private void enqueue(E x) {
        items[putIndex] = x;
        if (++putIndex == items.length)
            putIndex = 0;
        count++;
        takeCondition.signal();
    }

    private E dequeue() {
        E x = (E) items[takeIndex];
        items[takeIndex] = null;
        if (++takeIndex == items.length)
            takeIndex = 0;
        count--;
        putCondition.signal();
        return x;
    }
}
