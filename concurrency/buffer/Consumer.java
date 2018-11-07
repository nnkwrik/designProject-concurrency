/*
@author  j.n.magee
*/
package concurrency.buffer;

import concurrency.display.ThreadPanel;
import concurrency.utils.Logger;

public class Consumer implements Runnable {

    Buffer<Character> buf;

    public Consumer(Buffer<Character> b) {
        buf = b;
    }

    public void run() {
        try {
            while (true) {
                ThreadPanel.rotate(180);
                Character c = buf.get();
                Logger.log("Consumer [" + c + "] <====");
                Logger.out();
                ThreadPanel.rotate(180);
            }
        } catch (InterruptedException e) {
        }
    }
}
