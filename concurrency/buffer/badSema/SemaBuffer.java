/*
@author  j.n.magee
//updated: daniel.sykes 2013
//updated: Kozo Okano 2015
*/
package concurrency.buffer.badSema;

import concurrency.buffer.Buffer;
import concurrency.utils.*;

/**
 * 这个Buffer会造成死锁：
 * 比如消费者调用get()，此时Semaphore发生了wait()。Semaphore会释放Semaphore的锁并阻塞消费者线程，但此时SemaBuffer的锁没有被释放。
 * 因此，生产者调用put()方法会因为获取不到SemaBuffer的锁被阻塞。
 * 所以切忌在synchronized中使用信号量
 * @param <E>
 */
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
