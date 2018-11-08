package concurrency.buffer.fixedSema;


import concurrency.buffer.swing.BufferCanvas;

public class DisplayFixedSemaBuffer extends FixedSemaBuffer<Character> {
    BufferCanvas disp_;
    char[] tmp;

    public DisplayFixedSemaBuffer(BufferCanvas disp, int size) {
        super(size);
        disp_ = disp;
        tmp = new char[size];
    }

    public void put(Character c) throws InterruptedException {
        super.put(c);
        synchronized (this) {
            int oldin = (in - 1 + size) % size;
            tmp[oldin] = c;
            disp_.setValue(tmp, in, out);
            Thread.sleep(400);
        }
    }

    public Character get() throws InterruptedException {
        Character c = super.get();
        synchronized (this) {
            int oldout = (out - 1 + size) % size;
            tmp[oldout] = ' ';
            disp_.setValue(tmp, in, out);
        }
        return (c);
    }
}

