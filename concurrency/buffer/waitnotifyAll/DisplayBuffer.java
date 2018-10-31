package concurrency.buffer.waitnotifyAll;

import concurrency.buffer.swing.BufferCanvas;

public class DisplayBuffer extends BufferImpl<Character> {
    BufferCanvas disp_;
    char[] tmp;

    public DisplayBuffer(BufferCanvas disp, int size) {
        super(size);
        disp_ = disp;
        tmp = new char[size];
    }

    synchronized public void put(Character c) throws InterruptedException {
        int oldin = in;
        super.put(c);
        tmp[oldin]= c;
        disp_.setValue(tmp,in,out);
        Thread.sleep(400);
    }

    synchronized public Character get() throws InterruptedException {
        int oldout = out;
        Character c = super.get();
        tmp[oldout]=' ';
        disp_.setValue(tmp,in,out);
        return (c);
    }
 }

