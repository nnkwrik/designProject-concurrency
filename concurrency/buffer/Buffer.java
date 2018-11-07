package concurrency.buffer;

public interface Buffer<E> {
    void put(E o)
            throws InterruptedException; //put object into buffer

    E get()
            throws InterruptedException;       //get an object from buffer
}