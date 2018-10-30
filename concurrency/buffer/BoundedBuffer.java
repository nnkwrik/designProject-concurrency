/*
@author  j.n.magee
//updated: daniel.sykes 2013
*/
package concurrency.buffer;

import java.awt.*;
import java.applet.*;
import concurrency.display.*;



/****************************APPLET**************************/

public class BoundedBuffer extends Applet {


    ThreadPanel prod;
    ThreadPanel cons;
    BufferCanvas buffDisplay;

    public void init() {
        super.init();
        // Set up Display
        prod = new ThreadPanel("Producer",Color.blue);
        cons = new ThreadPanel("Consumer",Color.yellow);
        buffDisplay =  new BufferCanvas("Buffer",5);
        GridBagLayout gridbag = new GridBagLayout();
        setLayout(gridbag);
        GridBagConstraints gc = new GridBagConstraints();
        gc.anchor = GridBagConstraints.NORTH;
        gridbag.setConstraints(buffDisplay, gc);
        gridbag.setConstraints(prod, gc);
        gridbag.setConstraints(cons, gc);
        add(prod);
        add(buffDisplay);
        add(cons);
        setBackground(Color.lightGray);
    }

    public void start() {
        Buffer<Character> b = new DisplayBuffer(buffDisplay,5);
        // Create Thread
        prod.start(new Producer(b));
        cons.start(new Consumer(b));
    }


    public void stop() {
        prod.stop();
        cons.stop();
    }
}

/**************************************************************/

class DisplayBuffer extends BufferImpl<Character> {
    BufferCanvas disp_;
    char[] tmp;

    DisplayBuffer(BufferCanvas disp,int size) {
        super(size);
        disp_ = disp;
        tmp = new char[size];
    }

    synchronized public void put(Character c) throws InterruptedException {
        int oldin = in;
        super.put(c);
        tmp[oldin]= c;
        disp_.setvalue(tmp,in,out);
        Thread.sleep(400);
    }

    synchronized public Character get() throws InterruptedException {
        int oldout = out;
        Character c = super.get();
        tmp[oldout]=' ';
        disp_.setvalue(tmp,in,out);
        return (c);
    }

}
