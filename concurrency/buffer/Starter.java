package concurrency.buffer;

import concurrency.buffer.swing.BoundedBuffer;

/**
 * @author nnkwrik
 * @date 18/11/06 18:16
 */
public class Starter {

    public static void main(String[] args) {
        int consumerSize = 3;
        int producerSize = 2;

        //Object.wait() と Object.notify()　による実装
//        BoundedBuffer.create(BufferEnum.WAIT_NOTIFYALL).start();
//        BoundedBuffer.create(BufferEnum.WAIT_NOTIFYALL, consumerSize, producerSize).start();

        //2つのSemaphoreで実装,空の状態でput(),あるいはフルの状態でget()するとDeadLockになる
//        BoundedBuffer.create(BufferEnum.BAD_SEMAPHORE).start();
//        BoundedBuffer.create(BufferEnum.BAD_SEMAPHORE, consumerSize, producerSize).start();

        //2つのSemaphoreで実装, 修正済み
//        BoundedBuffer.create(BufferEnum.FIXED_SEMAPHORE).start();
//        BoundedBuffer.create(BufferEnum.FIXED_SEMAPHORE, consumerSize, producerSize).start();

        //Lock と Conditionで実装. Object.wait() と Object.notify()の実装とほぼ同様
//        BoundedBuffer.create(BufferEnum.LOCK).start();
//        BoundedBuffer.create(BufferEnum.LOCK, consumerSize, producerSize).start();


        //配列ベースのBlocking Queueで実装
//        BoundedBuffer.create(BufferEnum.BLOCKING_QUEUE).start();
//        BoundedBuffer.create(BufferEnum.BLOCKING_QUEUE, consumerSize, producerSize).start();

        //work stealing algorithm　,配列ベースのBlocking Dequeで実装
//        BoundedBuffer.create(BufferEnum.WORK_STEALING).start();
//        BoundedBuffer.create(BufferEnum.WORK_STEALING, consumerSize, producerSize,10).start();

    }

}
