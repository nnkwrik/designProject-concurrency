package concurrency.buffer.workStealing;

import concurrency.buffer.swing.BufferCanvas;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DisplayWorkStealingBuffer extends WorkStealingBuffer<Character> {
    private final Lock lock = new ReentrantLock();
    BufferCanvas disp_;
    char[] tmp;

    //TODO 发生工作密取时索引就不是连续的了 in = (in + 1) % size;不可用
    public DisplayWorkStealingBuffer(BufferCanvas disp, int size, int consumerSize) {
        super(size, consumerSize);
        disp_ = disp;
        tmp = new char[size];
    }

    public void put(Character c) throws InterruptedException {
        int oldin = in;
        super.put(c);

        lock.lock();
        try {
            in = (in + 1) % size;
            tmp[oldin] = c;
            disp_.setValue(tmp, in, out);
            Thread.sleep(400);
        } finally {
            lock.unlock();
        }
    }

    public Character get() throws InterruptedException {
        return get(null);
    }

    public Character get(Integer id) throws InterruptedException {
        int oldout = out;
        Character c = super.get(id);
        lock.lock();
        try {
            out = (out + 1) % size;
            tmp[oldout] = ' ';
            disp_.setValue(tmp, in, out);
        } finally {
            lock.unlock();
        }
        return (c);
    }
}

