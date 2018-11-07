package concurrency.buffer.workStealing;


import concurrency.buffer.Buffer;
import concurrency.utils.BlockingQueue;

import java.util.Arrays;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.stream.IntStream;


public class WorkStealingBuffer<E> implements Buffer<E> {

    protected int in = 0;
    protected int out = 0;
    protected int size;

    private final BlockingDeque<E>[] managedQueues;
    private final WorkStealingChannel<E> channel;

    public WorkStealingBuffer(int size,int consumerSize) {
        this.size = size;
        managedQueues = new LinkedBlockingDeque[consumerSize];
        channel = new WorkStealingChannel(managedQueues);

        IntStream.range(0,consumerSize)
                .forEach(i->managedQueues[i] = new LinkedBlockingDeque());
    }

    public void put(E o) throws InterruptedException {
        channel.put(o);
    }

    public E get() throws InterruptedException {
        return channel.take(null);
    }

    public E get(Integer id) throws InterruptedException {
        return channel.take(managedQueues[id]);
    }
}
