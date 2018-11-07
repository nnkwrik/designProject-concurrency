package concurrency.buffer.lock;


import concurrency.buffer.Buffer;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockBuffer<E> implements Buffer<E> {

    protected E[] buf;
    protected int in = 0;
    protected int out = 0;
    protected int count = 0;
    protected int size;

    private final Lock lock = new ReentrantLock();
    private Condition consumerCondition;
    private Condition producerCondition;

    public LockBuffer(int size) {
        this.size = size;
        buf = (E[]) new Object[size];
        consumerCondition = lock.newCondition();
        producerCondition = lock.newCondition();
    }

    public void put(E o) throws InterruptedException {
        lock.lock();
        try {
            while (count == size) {
                producerCondition.await();
            }
            buf[in] = o;
            ++count;
            in = (in + 1) % size;
            consumerCondition.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public E get() throws InterruptedException {
        lock.lock();
        try {
            while (count == 0) {
                consumerCondition.await();
            }
            E o = buf[out];
            buf[out] = null;
            --count;
            out = (out + 1) % size;
            producerCondition.signalAll();
            return (o);
        } finally {
            lock.unlock();
        }
    }
}
