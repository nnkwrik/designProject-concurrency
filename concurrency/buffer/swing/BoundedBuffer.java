/*
@author  j.n.magee 
//updated: daniel.sykes 2013
//updated: kozo okano 2015
*/
package concurrency.buffer.swing;

import java.awt.*;

import concurrency.display.*;

import javax.swing.*;

public abstract class BoundedBuffer {

    public ThreadPanel prod;
    public ThreadPanel cons;
    public BufferCanvas buffDisplay;


    public JComponent createComponents() {
        JPanel panel = new JPanel();

        // Set up Display
        prod = new ThreadPanel("Producer", Color.blue);
        cons = new ThreadPanel("Consumer", Color.yellow);
        buffDisplay = new BufferCanvas("Buffer", 5);
        GridBagLayout gridbag = new GridBagLayout();
        panel.setLayout(gridbag);
        GridBagConstraints gc = new GridBagConstraints();
        gc.anchor = GridBagConstraints.NORTH;
        gridbag.setConstraints(buffDisplay, gc);
        gridbag.setConstraints(prod, gc);
        gridbag.setConstraints(cons, gc);
        panel.add(prod);
        panel.add(buffDisplay);
        panel.add(cons);
        panel.setBackground(Color.lightGray);
        return panel;
    }

    public static void setupSwing(BoundedBuffer boundedBuffer, int consumer, int produser){
        JFrame frame = new JFrame("BoundedBuffer");
        BoundedBuffer app = boundedBuffer;
        JComponent contents = app.createComponents();
        frame.getContentPane().add(contents);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        app.start();
    }

    public abstract void start();

    public abstract void stop();
}
