package concurrency.buffer.waitnotifyAll;


import concurrency.buffer.Buffer;

public class BufferImpl<E> implements Buffer<E> {

    protected E[] buf;
    protected int in = 0;
    protected int out= 0;
    protected int count= 0;
    protected int size;

    public BufferImpl(int size) {
        this.size = size;
	//buf =  new  E[size];
	buf = (E[]) new  Object[size];
    }

    public synchronized void put(E o) throws InterruptedException {
        while (count==size) 
	    wait();
        buf[in] = o;
        ++count;
        in=(in+1) % size;
        notifyAll();
    }

    public synchronized E get() throws InterruptedException {
        while (count==0) 
	    wait();
        E o = buf[out];
        buf[out]=null;
        --count;
        out=(out+1) % size;
        notifyAll();
        return (o);
    }
}
