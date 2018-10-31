/*
@author  j.n.magee
*/
package concurrency.buffer;

import concurrency.display.ThreadPanel;

public class Consumer implements Runnable {

    Buffer<Character> buf;

    public Consumer(Buffer<Character> b) {
	buf = b;
    }

    public void run() {
      try {
        while(true) {
            ThreadPanel.rotate(180);
            Character c = buf.get();
            ThreadPanel.rotate(180);
        }
      } catch(InterruptedException e ){
      }
    }
}
