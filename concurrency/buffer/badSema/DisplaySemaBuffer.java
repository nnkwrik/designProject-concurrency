package concurrency.buffer.badSema;


import concurrency.buffer.swing.BufferCanvas;

public class DisplaySemaBuffer extends SemaBuffer<Character> {
    BufferCanvas disp_;
    char[] tmp;

    public DisplaySemaBuffer(BufferCanvas disp, int size) {
        super(size);
        disp_ = disp;
        tmp = new char[size];
    }

    synchronized public void put(Character c) throws InterruptedException {
        super.put(c);
        int oldin = (in - 1 + size) % size;
        tmp[oldin] = c;
        disp_.setValue(tmp, in, out);
        Thread.sleep(400);
    }

    synchronized public Character get() throws InterruptedException {
        Character c = super.get();
        int oldout = (out - 1 + size) % size;
        tmp[oldout] = ' ';
        disp_.setValue(tmp, in, out);
        return (c);
    }
}
