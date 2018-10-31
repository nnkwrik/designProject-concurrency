/*
@author  j.n.magee
//updated: daniel.sykes 2013
//updated: Kozo Okano 2015
*/
package concurrency.buffer.badSema;


import concurrency.buffer.Consumer;
import concurrency.buffer.Producer;
import concurrency.buffer.swing.BoundedBuffer;
import concurrency.buffer.Buffer;

public class BadSemaStarter extends BoundedBuffer {

    public static void main(String[] args) {
        setupSwing(new BadSemaStarter(), 1,1);
    }

    @Override
    public void start() {
        Buffer<Character> b = new DisplaySemaBuffer(buffDisplay,5);
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

