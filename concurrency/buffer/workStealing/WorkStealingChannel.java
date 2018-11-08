package concurrency.buffer.workStealing;

import concurrency.utils.BlockingDeque;
import concurrency.utils.Logger;



/**
 * @author nnkwrik
 * @date 18/11/06 21:20
 */
public class WorkStealingChannel<E> {

    //double-ended queue , can get element from both side
    private final BlockingDeque<E>[] managedQueues;

    public WorkStealingChannel(BlockingDeque<E>[] managedQueues) {
        this.managedQueues = managedQueues;
    }

    public void put(final E product) throws InterruptedException {
            int targetIndex = (product.hashCode() % managedQueues.length);
            BlockingDeque<E> targetQueue = managedQueues[targetIndex];
            targetQueue.put(product);
            Logger.log(" -> " + "C" + targetIndex);
    }

    public E take(final BlockingDeque<E> preferredQueue) throws InterruptedException {
        E product = null;
        BlockingDeque<E> targetQueue = preferredQueue;

        // try poll element from mine queue
        if (targetQueue != null) {
            product = targetQueue.poll();
        }

        if (product == null) {   //mine queue is empty
            int queueIndex = (int) (System.currentTimeMillis() % managedQueues.length);
            int tmp = queueIndex;
            while (product == null) {
                //random to steal element from other queue .
                targetQueue = managedQueues[queueIndex];
                product = targetQueue.pollLast();

                queueIndex = (queueIndex + 1) % managedQueues.length;
                if (queueIndex == tmp) break;
            }

            if (product != null && preferredQueue != targetQueue) {
                int stealedIntex = (queueIndex - 1 >= 0 ? queueIndex - 1 : managedQueues.length - 1);
                Logger.log("C" + stealedIntex + " <- ");
            } else if (product == null) { // other queue are empty too
                targetQueue = preferredQueue;
                product = targetQueue.take();   //block
            }
        }

        if (preferredQueue == targetQueue) Logger.log("      ");
        return product;
    }


}
