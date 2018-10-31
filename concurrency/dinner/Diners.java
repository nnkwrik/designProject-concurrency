package concurrency.dinner;

import java.awt.BorderLayout;
import java.awt.event.*;
import javax.swing.*;

public class Diners {

    PhilCanvas display;
    Thread[] phil= new Thread[PhilCanvas.NUMPHILS];
    Fork[] fork = new Fork[PhilCanvas.NUMPHILS];
    JScrollBar slider;
    JButton restart;
    JButton freeze;
    JCheckBox fixit; 
    boolean fixed = false;


    public static void main(String[] args) {
	JFrame frame = new JFrame("Diners");
	Diners app = new Diners();
	JComponent contents = app.createComponents();
	frame.getContentPane().add(contents);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.pack();
	frame.setVisible(true);
	app.start();
    }

    public JComponent createComponents() {
        JPanel p0 = new JPanel();
        p0.setLayout(new BorderLayout());
	fixit = new JCheckBox("Fixed It");
        display = new PhilCanvas();
        display.setSize(300,320);
        p0.add("Center", display);
        slider = new JScrollBar(JScrollBar.HORIZONTAL, 50, 5, 0, 100);
		
        restart = new JButton("Restart");	
	restart.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    if (display.deadlocked()) {
			stop();
			slider.setValue(50);
			start();
		    }
		    display.thaw();
		}
	    });

        freeze = new JButton("Freeze");		
	freeze.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    display.freeze();
		}
	    });


        JPanel p1 = new JPanel();
        p1.setLayout(new BorderLayout());
        p1.add("Center", slider);
        p1.add("East", restart);
        p1.add("West", freeze);
	p1.add("South", fixit);
        p0.add("South", p1);
	return p0;
    }

    Thread makePhilosopher(Diners d, int id, Fork left, Fork right) {
	fixed = fixit.isSelected(); 
        if (fixed)
            return new FixedPhilosopher(d,id,left,right);
	else
	    return new Philosopher(d,id,left,right);
    }

    public int sleepTime() {
        return (slider.getValue()*(int)(100*Math.random()));
    }

    public int eatTime() {
        return (slider.getValue()*(int)(50*Math.random()));
    }

    public void start() {

       for (int i =0; i<display.NUMPHILS; ++i)
            fork[i] = new Fork(display,i);
       for (int i =0; i<display.NUMPHILS; ++i){
            phil[i] = makePhilosopher(this,i,
                        fork[(i-1+display.NUMPHILS)% display.NUMPHILS],
                        fork[i]);
            phil[i].start();
       }
    }

    public void stop() {
        for (int i =0; i<display.NUMPHILS; ++i) {
            phil[i].interrupt();
        }
    }

}
