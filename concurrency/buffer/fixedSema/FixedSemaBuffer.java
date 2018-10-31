/*
@author  j.n.magee
//updated: daniel.sykes 2013
//updated: Kozo Okano 2015
*/
package concurrency.buffer.fixedSema;

import concurrency.buffer.Buffer;
import concurrency.semaphore.Semaphore;

class FixedSemaBuffer<E> implements Buffer<E> {
    protected E[] buf;
    protected int in = 0;
    protected int out = 0;
    protected int count = 0; //only used for display purposes
    protected int size;

    Semaphore full;  //counts number of items
    Semaphore empty; //counts number of spaces

    FixedSemaBuffer(int size) {
        this.size = size;
        buf = (E[]) new Object[size];
        full = new Semaphore(0);
        empty = new Semaphore(size);
    }

    public void put(E o) throws InterruptedException {
        empty.down();
        synchronized (this) {
            buf[in] = o;
            ++count;
            in = (in + 1) % size;
        }
        full.up();
    }

    public E get() throws InterruptedException {
        full.down();
        E o;
        synchronized (this) {
            o = buf[out];
            buf[out] = null;
            --count;
            out = (out + 1) % size;
        }
        empty.up();
        return (o);
    }
}


