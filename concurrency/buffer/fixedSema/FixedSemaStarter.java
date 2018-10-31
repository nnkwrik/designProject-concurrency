package concurrency.buffer.fixedSema;


import concurrency.buffer.swing.BoundedBuffer;
import concurrency.buffer.Buffer;
import concurrency.buffer.Consumer;
import concurrency.buffer.Producer;

/**
 * use semaphore and lock version
 */
public class FixedSemaStarter extends BoundedBuffer {

    public static void main(String[] args) {

        setupSwing(new FixedSemaStarter(), 1, 1);

    }


    public void start() {
        Buffer<Character> b = new DisplayFixedSemaBuffer(buffDisplay, 5);
        // Create Thread
        prod.start(new Producer(b));
        cons.start(new Consumer(b));
    }


    public void stop() {
        prod.stop();
        cons.stop();
    }
}
