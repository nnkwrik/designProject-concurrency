/*
@author  j.n.magee
*/
package concurrency.buffer;

import concurrency.display.*;
import concurrency.utils.Logger;


/*******************PRODUCER************************/

public class Producer implements Runnable {

    Buffer<Character> buf;
    String alphabet = "abcdefghijklmnopqrstuvwxyz";

    public Producer(Buffer<Character> b) {
        buf = b;
    }

    public void run() {
        try {
            int ai = 0;
            while (true) {
                ThreadPanel.rotate(12);

                Logger.log("Producer      ====> [" + alphabet.charAt(ai) + "]");
                buf.put(alphabet.charAt(ai));
                Logger.out();
                ai = (ai + 1) % alphabet.length();
                ThreadPanel.rotate(348);
            }
        } catch (InterruptedException e) {
        }
    }
}
