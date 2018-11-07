package concurrency.dinner;

import concurrency.dinner.philosopher.Philosopher;

import java.awt.BorderLayout;
import java.lang.reflect.Constructor;
import java.util.stream.IntStream;
import javax.swing.*;

public class Diners {

    public PhilCanvas display;
    Thread[] phil = new Thread[PhilCanvas.NUMPHILS];
    Fork[] fork = new Fork[PhilCanvas.NUMPHILS];
    JScrollBar slider;
    JButton restart;
    JButton freeze;
    Class<? extends Philosopher> philosopherClazz;

    private Diners() {
    }

    public JComponent createComponents() {
        JPanel p0 = new JPanel();
        p0.setLayout(new BorderLayout());
        display = new PhilCanvas();
        display.setSize(300, 320);
        p0.add("Center", display);
        slider = new JScrollBar(JScrollBar.HORIZONTAL, 50, 5, 0, 100);

        restart = new JButton("Restart");
        restart.addActionListener(e -> {
            if (display.deadlocked()) {
                stop();
                slider.setValue(50);
                start();
            }
            display.thaw();
        });

        freeze = new JButton("Freeze");
        freeze.addActionListener(e -> display.freeze());


        JPanel p1 = new JPanel();
        p1.setLayout(new BorderLayout());
        p1.add("Center", slider);
        p1.add("East", restart);
        p1.add("West", freeze);
        p0.add("South", p1);
        return p0;
    }



    public static Diners create(Class<? extends Philosopher> philosopherClazz) {
        JFrame frame = new JFrame("Diners");
        Diners app = new Diners();
        app.philosopherClazz = philosopherClazz;
        JComponent contents = app.createComponents();
        frame.getContentPane().add(contents);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        return app;
    }

    public int sleepTime() {
        return (slider.getValue() * (int) (100 * Math.random()));
    }

    public int eatTime() {
        return (slider.getValue() * (int) (50 * Math.random()));
    }

    public void start() {
        Constructor<? extends Philosopher> constructor = null;
        try {
            constructor = philosopherClazz.getConstructor(Diners.class, Integer.TYPE, Fork.class, Fork.class);

            IntStream.range(0,display.NUMPHILS).forEach(i -> fork[i] = new Fork(display, i));

            for (int i = 0; i < display.NUMPHILS; i++) {
                phil[i] = constructor.newInstance(this, i,
                        fork[(i - 1 + display.NUMPHILS) % display.NUMPHILS],
                        fork[i]);
                phil[i].start();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void stop() {
        for (int i = 0; i < display.NUMPHILS; i++) {
            phil[i].interrupt();
        }
    }

}
