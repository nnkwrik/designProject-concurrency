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
        //TODO 多个时擦除有bug？
//        testWaitNotifyAll(3,2);
        testWorkStealing(3,2,10);

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
        testWorkStealing(consumerNum,producerNum,BoundedBuffer.SLOT);
    }

    public static void testWorkStealing(int consumerNum, int producerNum, int slot){
        BoundedBuffer boundedBuffer = BoundedBuffer.create(consumerNum,producerNum,slot);
        WorkStealingBuffer<Character> b = new DisplayWorkStealingBuffer(boundedBuffer.getBuffDisplay(),slot,consumerNum);
        boundedBuffer.startWorkStealing(b);
    }

}
