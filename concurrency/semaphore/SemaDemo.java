package concurrency.semaphore;

import javax.swing.*;
import java.awt.*;
import concurrency.display.*;

public class SemaDemo {

    ThreadPanel thread1;
    ThreadPanel thread2;
    ThreadPanel thread3;
    NumberCanvas semaDisplay;

    public static void main(String[] args) {
	JFrame frame = new JFrame("SemaDemo");
	SemaDemo app = new SemaDemo();
	JComponent contents = app.createComponents();
	frame.getContentPane().add(contents);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.pack();
	frame.setVisible(true);
	app.start();
    }

    public JComponent createComponents() {
	JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        semaDisplay = new NumberCanvas("Mutex");
        panel.add("Center",semaDisplay);
        Panel p = new Panel();
        p.add(thread1=new ThreadPanel("Thread 1",Color.blue,true));
        p.add(thread2=new ThreadPanel("Thread 2",Color.blue,true));
        p.add(thread3=new ThreadPanel("Thread 3",Color.blue,true));
        panel.add("South",p);
        panel.setBackground(Color.lightGray);
	return panel;
    }

    public void start() {
        Semaphore mutex = new DisplaySemaphore(semaDisplay,1);
        thread1.start(new MutexLoop(mutex));
        thread2.start(new MutexLoop(mutex));
        thread3.start(new MutexLoop(mutex));

    }

    public void stop() {
        thread1.stop();
        thread2.stop();
        thread3.stop();
    }
}

