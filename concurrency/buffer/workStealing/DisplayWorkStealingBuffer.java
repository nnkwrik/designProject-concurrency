package concurrency.buffer.workStealing;

import concurrency.buffer.swing.BufferCanvas;

import java.util.Arrays;
import java.util.OptionalInt;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class DisplayWorkStealingBuffer extends WorkStealingBuffer<Character> {
    private final Lock lock = new ReentrantLock();
    BufferCanvas disp_;
    char[] tmp;

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
        Character c = super.get(id);
        lock.lock();
        try {
            out = (out + 1) % size;
            OptionalInt oldout = IntStream.range(0, tmp.length)
                    .filter(i -> c.charValue() == tmp[i])
                    .findFirst();

            // 可能已经被put进去的新值替换掉了，会找不到
            if (oldout.isPresent()) {
                tmp[oldout.getAsInt()] = ' ';
            }
            disp_.setValue(tmp, in, out);   //in`和out只是圆点的位置

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return (c);
    }
}

