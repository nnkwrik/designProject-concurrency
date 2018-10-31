package concurrency.display;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ThreadPanel extends JPanel {

    JButton run;
    JButton pause;
    JScrollBar bar_;
    DisplayThread thread_;
    GraphicCanvas canvas_;
    boolean hasSlider;

    public ThreadPanel(String title, Color c) {
        this(title,c,false);
    }

    public ThreadPanel(String title, Color c, boolean hasSlider) {
        super();
        this.hasSlider=hasSlider;
        // Set up Buttons
        this.setFont(new Font("Helvetica", Font.BOLD,14));
        JPanel p = new JPanel();
        p.add(run=new JButton("Run"));
		
        run.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            if (thread_ != null) //added 2013
		thread_.activate();
          }
        });
		
        p.add(pause=new JButton("Pause"));
		
        pause.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            if (thread_ != null) //added 2013
		thread_.passivate();
          }
        });

        setLayout(new BorderLayout());
        add("South",p);
        canvas_ = new GraphicCanvas(title,c);
        canvas_.setColor(Color.red); //added 2013
        add("North", canvas_);
        bar_ = new JScrollBar(JScrollBar.HORIZONTAL, 30, 10, 2, 58);
        if (hasSlider) 
	    add("Center",bar_);
		
        bar_.addAdjustmentListener(new AdjustmentListener() {
              public void adjustmentValueChanged(AdjustmentEvent e) {
                if (thread_ != null) //added 2013
                  thread_.setSplit(bar_.getValue());
              }
            });
	setBackground(Color.lightGray);
    }

    public static boolean rotate() throws InterruptedException {
        return DisplayThread.rotate();
    }

    public static void rotate(int degrees) throws InterruptedException {
        for(int i=0;i<degrees;i+=6)
            DisplayThread.rotate();
    }

    public static void setSegmentColor(Color c) {
        DisplayThread.setSegmentColor(c);
    }

    public void start(Runnable r) {
        thread_ = new DisplayThread(canvas_, r, 100,
				    hasSlider?bar_.getValue():0, true);
        thread_.start();
    }

    public Thread start(Runnable r, boolean suspended) {
        thread_ = new DisplayThread(canvas_, r, 100,
				    hasSlider?bar_.getValue():0,suspended);
        thread_.start();
        return thread_;
    }

    public void stop() {
        thread_.interrupt();
    }
}
