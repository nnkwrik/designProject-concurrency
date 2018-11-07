package concurrency.utils;

/**
 * a simple BlockingDeque implement by Array
 *
 * @param <E>
 */
public class BlockingDeque<E> extends BlockingQueue<E> {


    public BlockingDeque(int capacity) {
        super(capacity);
    }

    public E poll() throws InterruptedException {
        lock.lock();
        try {
            return arrayFirst();
        } finally {
            lock.unlock();
        }
    }

    public E pollLast() throws InterruptedException {
        lock.lock();
        try {
            return arrayLast();
        } finally {
            lock.unlock();
        }
    }


    private E arrayFirst() {
        E x = (E) items[takeIndex];
        if (x != null) {
            items[takeIndex] = null;
            if (++takeIndex == items.length)
                takeIndex = 0;
            count--;
            putCondition.signal();
        }
        return x;
    }

    private E arrayLast() {
        //putIndex 下一个要放的位置，目前为空
        int lastIndex = putIndex - 1 < 0 ? items.length - 1 : putIndex - 1;

        E x = (E) items[lastIndex];
        if (x != null) {
            items[lastIndex] = null;
            putIndex = lastIndex;
            count--;
            putCondition.signal();
        }
        return x;
    }
}
