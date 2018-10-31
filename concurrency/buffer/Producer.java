/*
@author  j.n.magee
*/
package concurrency.buffer;

import concurrency.display.*;

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
                buf.put(alphabet.charAt(ai));
                ai = (ai + 1) % alphabet.length();
                ThreadPanel.rotate(348);
            }
        } catch (InterruptedException e) {
        }
    }
}
