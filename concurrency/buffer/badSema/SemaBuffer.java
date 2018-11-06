/*
@author  j.n.magee
//updated: daniel.sykes 2013
//updated: Kozo Okano 2015
*/
package concurrency.buffer.badSema;

import concurrency.buffer.Buffer;
import concurrency.utils.*;

class SemaBuffer<E> implements Buffer<E> {
    protected E[] buf;
    protected int in = 0;
    protected int out = 0;
    protected int count = 0;
    protected int size;

    Semaphore full; //counts number of items
    Semaphore empty;//counts number of spaces

    SemaBuffer(int size) {
        this.size = size;
        buf = (E[]) new Object[size];
        full = new Semaphore(0);
        empty = new Semaphore(size);
    }

//  当没有多的信号量时，Semaphore中会wait，所以Semaphore的锁会被释放。此时该方法也因此处于阻塞状态，但是SemaBuffer的对象锁没有被释放。
//  所以比如当get处于这种阻塞状态时，时进不了put的。也就是说队列为空后，生产者无法放入对象
//  反过来也一样。队列变满时，消费者无法取出对象。
//  所以切忌在synchronized中使用信号量
//https://blog.csdn.net/coslay/article/details/45176063 Semaphore源码解析，shouldParkAfterFailedAcquire()阻塞线程。
//然后，该方法中调用parkAndCheckInterrupt()，其中使用LockSupport.park(this);来挂起线程，这个this当然就是指当前的Semaphore对象
//其实就是想说，不要在  synchronized 方法中使用信号量。
    synchronized public void put(E o) throws InterruptedException {
        empty.down();
        buf[in] = o;
        ++count;
        in = (in + 1) % size;
        full.up();
    }

    synchronized public E get() throws InterruptedException {
        full.down();
        E o = buf[out];
        buf[out] = null;
        --count;
        out = (out + 1) % size;
        empty.up();
        return (o);
    }
}
