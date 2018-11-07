package concurrency.buffer;

import concurrency.buffer.badSema.DisplaySemaBuffer;
import concurrency.buffer.blockingQueue.DisplayBlockingQueueBuffer;
import concurrency.buffer.fixedSema.DisplayFixedSemaBuffer;
import concurrency.buffer.lock.DisplayLockBuffer;
import concurrency.buffer.swing.BoundedBuffer;
import concurrency.buffer.waitnotifyAll.DisplayWaitBuffer;
import concurrency.buffer.workStealing.DisplayWorkStealingBuffer;
import concurrency.buffer.workStealing.WorkStealingBuffer;

/**
 * @author nnkwrik
 * @date 18/11/06 18:16
 */
public class Starter {

    public static void main(String[] args) {
        //TODO 多个时擦除有bug
        testWaitNotifyAll(2,2);
//        testWorkStealing(2,1);

    }

    public static void testWaitNotifyAll(int consumerNum, int producerNum){
        BoundedBuffer boundedBuffer = BoundedBuffer.create(consumerNum,producerNum);
        Buffer<Character> b = new DisplayWaitBuffer(boundedBuffer.getBuffDisplay(),BoundedBuffer.SLOT);
        boundedBuffer.start(b);
    }

    public static void testBadSema(int consumerNum, int producerNum){
        BoundedBuffer boundedBuffer = BoundedBuffer.create(consumerNum,producerNum);
        Buffer<Character> b = new DisplaySemaBuffer(boundedBuffer.getBuffDisplay(),BoundedBuffer.SLOT);
        boundedBuffer.start(b);
    }

    public static void testFixedSema(int consumerNum, int producerNum){
        BoundedBuffer boundedBuffer = BoundedBuffer.create(consumerNum,producerNum);
        Buffer<Character> b = new DisplayFixedSemaBuffer(boundedBuffer.getBuffDisplay(),BoundedBuffer.SLOT);
        boundedBuffer.start(b);
    }

    public static void testLock(int consumerNum, int producerNum){
        BoundedBuffer boundedBuffer = BoundedBuffer.create(consumerNum,producerNum);
        Buffer<Character> b = new DisplayLockBuffer(boundedBuffer.getBuffDisplay(),BoundedBuffer.SLOT);
        boundedBuffer.start(b);
    }

    public static void testBlockingQueue(int consumerNum, int producerNum){
        BoundedBuffer boundedBuffer = BoundedBuffer.create(consumerNum,producerNum);
        Buffer<Character> b = new DisplayBlockingQueueBuffer(boundedBuffer.getBuffDisplay(),BoundedBuffer.SLOT);
        boundedBuffer.start(b);
    }

    public static void testWorkStealing(int consumerNum, int producerNum){
        BoundedBuffer boundedBuffer = BoundedBuffer.create(consumerNum,producerNum);
        WorkStealingBuffer<Character> b = new DisplayWorkStealingBuffer(boundedBuffer.getBuffDisplay(),BoundedBuffer.SLOT,consumerNum);
        boundedBuffer.startWorkStealing(b);
    }

}
