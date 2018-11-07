/*
@author  j.n.magee
*/
package concurrency.buffer.workStealing;

import concurrency.display.ThreadPanel;
import concurrency.utils.Logger;

import java.util.Locale;

public class WorkStealingConsumer implements Runnable {

    WorkStealingBuffer<Character> buf;

    int id;

    public WorkStealingConsumer(WorkStealingBuffer<Character> buf, int id) {
        this.buf = buf;
        this.id = id;
    }


    public void run() {
        try {
            while (true) {
                ThreadPanel.rotate(180);
                Character c = buf.get(id);

                Logger.log("C" + id + " [" + c + "] <====");
                Logger.out();
                ThreadPanel.rotate(180);
            }
        } catch (InterruptedException e) {
        }
    }
}
