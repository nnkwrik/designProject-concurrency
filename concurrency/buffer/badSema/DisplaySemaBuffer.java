package concurrency.buffer.badSema;


import concurrency.buffer.swing.BufferCanvas;

class DisplaySemaBuffer extends SemaBuffer<Character> {
    BufferCanvas disp_;
    char[] tmp;

    DisplaySemaBuffer(BufferCanvas disp, int size) {
        super(size);
        disp_ = disp;
        tmp = new char[size];
    }

    synchronized public void put(Character c) throws InterruptedException {
        int oldin = in;
        super.put(c);
        tmp[oldin] = c;
        disp_.setValue(tmp, in, out);
        Thread.sleep(400);
    }

    synchronized public Character get() throws InterruptedException {
        int oldout = out;
        Character c = super.get();
        tmp[oldout] = ' ';
        disp_.setValue(tmp, in, out);
        return (c);
    }
}
