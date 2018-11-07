package concurrency.buffer.workStealing;

import concurrency.buffer.Producer;
import concurrency.utils.Logger;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;

/**
 * @author nnkwrik
 * @date 18/11/06 21:20
 */
public class WorkStealingChannel<E> {

    // 双端队列，可以从两端插入值或获取值，继承了BlockingQueue
    private final BlockingDeque<E>[] managedQueues;

    public WorkStealingChannel(BlockingDeque<E>[] managedQueues) {
        this.managedQueues = managedQueues;
    }

    public void put(final E product) throws InterruptedException {
            int targetIndex = (product.hashCode() % managedQueues.length);
            BlockingQueue<E> targetQueue = managedQueues[targetIndex];
            targetQueue.put(product);
            Logger.log(" -> " + "C" + targetIndex);
    }

    public E take(final BlockingDeque<E> preferredQueue) throws InterruptedException {
        E product = null;
        BlockingDeque<E> targetQueue = preferredQueue;

        // 优先从指定的队列获取值
        if (targetQueue != null) {
            product = targetQueue.poll();
        }

        if (product == null) {   //自己的工作队列为空
            int queueIndex = (int) (System.currentTimeMillis() % managedQueues.length);
            int tmp = queueIndex;
            while (product == null) {      //防止倒霉 随机取的是空的Queue
                //随机窃取 其他受管队列的产品，这里应该遍历一周
                targetQueue = managedQueues[queueIndex];
                product = targetQueue.pollLast();

                queueIndex = (queueIndex + 1) % managedQueues.length;
                if (queueIndex == tmp) break;
            }

            if (product != null && preferredQueue != targetQueue) {
                int stealedIntex = (queueIndex - 1 >= 0 ? queueIndex - 1 : managedQueues.length - 1);
                Logger.log("C" + stealedIntex + " <- ");
            } else if (product == null) {
                targetQueue = preferredQueue;
                product = targetQueue.take();   //阻塞
            }
        }

        if (preferredQueue == targetQueue) Logger.log("      ");
        return product;
    }


}
