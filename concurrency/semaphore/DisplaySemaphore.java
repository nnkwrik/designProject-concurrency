package concurrency.semaphore;

import java.awt.*;
import concurrency.display.*;


class DisplaySemaphore extends Semaphore {

    NumberCanvas display_;
    int count_; //shadow value for display

    DisplaySemaphore(NumberCanvas t, int val) {
        super(val);
        count_=val;
        display_=t;
        display_.setColor(Color.cyan);
        display_.setValue(count_);
    }

    synchronized public void up() {
        super.up();
        ++count_;
        display_.setValue(count_);
        try {
	    Thread.sleep(200);
	} catch (InterruptedException e){
	    Thread.currentThread().interrupt();
	} //reassert
    }

    synchronized public void down() throws InterruptedException{
       super.down();
       --count_;
       display_.setValue(count_);
    }
}

