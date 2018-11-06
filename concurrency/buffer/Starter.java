package concurrency.buffer;

import concurrency.buffer.badSema.DisplaySemaBuffer;
import concurrency.buffer.fixedSema.DisplayFixedSemaBuffer;
import concurrency.buffer.swing.BoundedBuffer;
import concurrency.buffer.waitnotifyAll.DisplayWaitBuffer;

/**
 * @author nnkwrik
 * @date 18/11/06 18:16
 */
public class Starter {

    public static void main(String[] args) {
        fixedSema(1,1);
    }

    public static void waitNotifyAll(int consumerNum, int producerNum){
        BoundedBuffer boundedBuffer = BoundedBuffer.create(consumerNum,producerNum);
        Buffer<Character> b = new DisplayWaitBuffer(boundedBuffer.getBuffDisplay(),BoundedBuffer.SLOT);
        boundedBuffer.start(b);
    }

    public static void badSema(int consumerNum, int producerNum){
        BoundedBuffer boundedBuffer = BoundedBuffer.create(consumerNum,producerNum);
        Buffer<Character> b = new DisplaySemaBuffer(boundedBuffer.getBuffDisplay(),BoundedBuffer.SLOT);
        boundedBuffer.start(b);
    }

    public static void fixedSema(int consumerNum, int producerNum){
        BoundedBuffer boundedBuffer = BoundedBuffer.create(consumerNum,producerNum);
        Buffer<Character> b = new DisplayFixedSemaBuffer(boundedBuffer.getBuffDisplay(),BoundedBuffer.SLOT);
        boundedBuffer.start(b);
    }


}
