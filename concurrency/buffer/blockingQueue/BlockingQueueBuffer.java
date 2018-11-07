package concurrency.buffer.blockingQueue;


import concurrency.buffer.Buffer;
import concurrency.utils.BlockingQueue;


public class BlockingQueueBuffer<E> implements Buffer<E> {

    protected int in = 0;
    protected int out = 0;
    protected int size;

    private BlockingQueue<E> blockingQueue;

    public BlockingQueueBuffer(int size) {
        this.size = size;
        blockingQueue = new BlockingQueue<>(size);
    }

    public void put(E o) throws InterruptedException {
        blockingQueue.put(o);
    }

    public E get() throws InterruptedException {
        return blockingQueue.take();
    }
}
