package concurrency.buffer.waitnotifyAll;

import concurrency.buffer.swing.BoundedBuffer;
import concurrency.buffer.Buffer;
import concurrency.buffer.Consumer;
import concurrency.buffer.Producer;

public class WaitNotifyAllStarter extends BoundedBuffer {

    public static void main(String[] args) {
        setupSwing(new WaitNotifyAllStarter(), 1,1);
    }

    @Override
    public void start() {
        Buffer<Character> b = new DisplayBuffer(buffDisplay,5);
        // Create Thread
        prod.start(new Producer(b));
        cons.start(new Consumer(b));
    }

    @Override
    public void stop() {
        prod.stop();
        cons.stop();
    }
}
