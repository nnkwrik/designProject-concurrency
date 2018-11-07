package concurrency.display;

import java.awt.Color;

class DisplayThread extends Thread {

    GraphicCanvas display_;
    boolean suspended = true;
    int angle_=0;
    int segStart_=9999;
    int segEnd_=9999;
    int rate_;
    final static int step = 6;
    int barValue_;
    Color segColor_=Color.cyan;

    Runnable target_;

    DisplayThread(GraphicCanvas g, Runnable target, 
		  int rate, int split, boolean susp) {
        display_ = g;
        target_=target;
        rate_=rate;
        setSplit(split);
        suspended = susp;
        if (suspended)
            display_.setColor(Color.red);
        else
            display_.setColor(Color.green);
    }

    synchronized void mysuspend() throws InterruptedException{
        while (suspended) 
	    wait();
    }


    void passivate() {
        if (!suspended) {
            suspended = true;
            display_.setColor(Color.red);
	}
    }

    void activate() {
        if (suspended) {
            suspended = false;
            display_.setColor(Color.green);
            synchronized(this) {
		notify();
	    }
        }
    }

    static boolean rotate() throws InterruptedException {
        DisplayThread d = (DisplayThread)Thread.currentThread();
        synchronized(d) {
            d.mysuspend();
            d.angle_=(d.angle_+step)%360;
            d.display_.setAngle(d.angle_);
            Thread.sleep(d.rate_);
            return (d.angle_>=d.segStart_ && d.angle_<=d.segEnd_);
        }
    }

    static void setSegmentColor(Color c) {
        DisplayThread d = (DisplayThread)Thread.currentThread();
        synchronized(d) {
            d.segColor_=c;
            d.display_.setSegment(d.segStart_,d.segEnd_,d.segColor_);
        }
    }


    synchronized void setSplit(int i) {
        barValue_ = i;
        segStart_=(60-i)*3;//+90;
        segEnd_ = segStart_+i*6;
        display_.setSegment(segStart_, segEnd_, segColor_);
    }

    public void run() {
        try {
          mysuspend();
          target_.run();
          display_.setColor(Color.red); //white... changed 2013
          display_.setAngle(0);
        } catch (InterruptedException e){}
     }
}
