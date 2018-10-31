package concurrency.buffer;

public interface Buffer<E> {
    public void put(E o)
       throws InterruptedException; //put object into buffer
    public E get()
       throws InterruptedException;       //get an object from buffer
}