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
        int consumerSize = 3;
        int producerSize = 2;


        //TODO 3,2时擦除有bug

        //use Object.wait() and Object.notify()
//        BoundedBuffer.create(BufferEnum.WAIT_NOTIFYALL).start();
        BoundedBuffer.create(BufferEnum.WAIT_NOTIFYALL, consumerSize, producerSize).start();

        //use 2 semaphore but will be deadlock
//        BoundedBuffer.create(BufferEnum.BAD_SEMAPHORE).start();
//        BoundedBuffer.create(BufferEnum.BAD_SEMAPHORE, consumerSize, producerSize).start();

        //use 2 semaphore
//        BoundedBuffer.create(BufferEnum.FIXED_SEMAPHORE).start();
//        BoundedBuffer.create(BufferEnum.FIXED_SEMAPHORE, consumerSize, producerSize).start();

        //use lock and condition()
//        BoundedBuffer.create(BufferEnum.LOCK).start();
//        BoundedBuffer.create(BufferEnum.LOCK, consumerSize, producerSize).start();


        //use blocking queue base array
//        BoundedBuffer.create(BufferEnum.BLOCKING_QUEUE).start();
//        BoundedBuffer.create(BufferEnum.BLOCKING_QUEUE, consumerSize, producerSize).start();

        //work stealing algorithm, use blocking deque base array
//        BoundedBuffer.create(BufferEnum.WORK_STEALING).start();
//        BoundedBuffer.create(BufferEnum.WORK_STEALING, consumerSize, producerSize,10).start();

    }

}
