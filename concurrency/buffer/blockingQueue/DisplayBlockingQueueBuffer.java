package concurrency.buffer.blockingQueue;

import concurrency.buffer.swing.BufferCanvas;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DisplayBlockingQueueBuffer extends BlockingQueueBuffer<Character> {
    private final Lock lock = new ReentrantLock();
    BufferCanvas disp_;
    char[] tmp;

    public DisplayBlockingQueueBuffer(BufferCanvas disp, int size) {
        super(size);
        disp_ = disp;
        tmp = new char[size];
    }

    public void put(Character c) throws InterruptedException {
        super.put(c);

        lock.lock();
        try {
            in = (in + 1) % size;
            int oldin = (in - 1 + size) % size;
            tmp[oldin] = c;
            disp_.setValue(tmp, in, out);
            Thread.sleep(400);
        } finally {
            lock.unlock();
        }
    }

    public Character get() throws InterruptedException {
        Character c = super.get();
        lock.lock();
        try {
            out = (out + 1) % size;
            int oldout = (out - 1 + size) % size;
            tmp[oldout] = ' ';
            disp_.setValue(tmp, in, out);
        } finally {
            lock.unlock();
        }
        return (c);
    }
}

