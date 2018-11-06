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
        int oldin = in;
        super.put(c);
        synchronized (this) {
            tmp[oldin] = c;
            disp_.setValue(tmp, in, out);
            Thread.sleep(400);
        }
    }

    public Character get() throws InterruptedException {
        int oldout = out;
        Character c = super.get();
        synchronized (this) {
            tmp[oldout] = ' ';
            disp_.setValue(tmp, in, out);
        }
        return (c);
    }
}

