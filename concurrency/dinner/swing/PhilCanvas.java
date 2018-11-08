package concurrency.dinner.swing;

import java.awt.*;
import java.awt.geom.AffineTransform;
import javax.swing.*;

public class PhilCanvas extends JPanel {

    public static final int NUMPHILS = 5;
    public static final int THINKING = 0;
    public static final int HUNGRY = 1;
    public static final int GOTRIGHT = 2;
    public static final int EATING = 3;
    public static final int GOTLEFT = 4;

    Image[] imgs = new Image[5];

    AffineTransform[] philPlace = new AffineTransform[NUMPHILS];

    int[] state = new int[NUMPHILS];

    double[] chopX = new double[NUMPHILS];
    double[] chopY = new double[NUMPHILS];
    boolean[] untable = new boolean[NUMPHILS];

    boolean frozen = false;

    public PhilCanvas() {
        super();

        MediaTracker mt;
        mt = new MediaTracker(this);

        Toolkit toolkit = getToolkit();

        imgs[0] = toolkit.getImage("image/thinking.gif");
        mt.addImage(imgs[0], 0);
        imgs[1] = toolkit.getImage("image/hungry.gif");
        mt.addImage(imgs[1], 1);
        imgs[2] = toolkit.getImage("image/gotright.gif");
        mt.addImage(imgs[2], 2);
        imgs[3] = toolkit.getImage("image/eating.gif");
        mt.addImage(imgs[3], 3);
        imgs[4] = toolkit.getImage("image/gotleft.gif");
        mt.addImage(imgs[4], 4);

        try {
            mt.waitForID(0);
            mt.waitForID(1);
            mt.waitForID(2);
            mt.waitForID(3);
            mt.waitForID(4);
        } catch (InterruptedException e) {
            System.out.println("Couldn't load one of the images");
        }
        initPlacing();
    }

    Image offscreen;
    Dimension offscreensize;
    Graphics offgraphics;

    public void backdrop() {
        Dimension d = getSize();
        if ((offscreen == null) || (d.width != offscreensize.width)
                || (d.height != offscreensize.height)) {
            offscreen = createImage(d.width, d.height);
            offscreensize = d;
            offgraphics = offscreen.getGraphics();
            offgraphics.setFont(new Font("Helvetica", Font.BOLD, 18));
            Graphics2D g2D = (Graphics2D) offgraphics;
            g2D.translate(d.width / 2, d.height / 2); //set origin to centre
        }
        offgraphics.setColor(Color.lightGray);
        offgraphics.fillRect(-d.width / 2, -d.height / 2, d.width, d.height);
    }

    public void drawtable() {
        offgraphics.setColor(Color.red);
        offgraphics.fillOval(-45, -45, 90, 90);
        offgraphics.setColor(Color.black);
        for (int i = 0; i < NUMPHILS; i++) {
            if (untable[i]) offgraphics.fillOval((int) chopX[i], (int) chopY[i], 10, 10);
//            offgraphics.drawString(String.valueOf(i),(int)chopX[i],(int)chopY[i]);
        }
    }

    public Dimension getPreferredSize() {
        return new Dimension(400, 400);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D gc2 = (Graphics2D) g;
        setOpaque(true);
        backdrop();
        for (int i = 0; i < NUMPHILS; i++) {
            philPaint(offgraphics, i);
        }
        drawtable();
        if (deadlocked()) {
            offgraphics.setColor(Color.black);
            offgraphics.drawString("DEADLOCKED", -60, 0);
        }
        g.drawImage(offscreen, 0, 0, null);
    }

    public void philPaint(Graphics g, int i) {
        Graphics2D g2D = (Graphics2D) g;
        g2D.drawImage(imgs[state[i]], philPlace[i], this);
    }

    public synchronized void setPhil(int id, int s) throws InterruptedException {
        while (frozen) wait();
        state[id] = s;
        repaint();
    }

    public synchronized void freeze() {
        frozen = true;
    }

    public synchronized void thaw() {
        frozen = false;
        notifyAll();
    }

    public synchronized void setFork(int id, boolean taken) {
        untable[id] = !taken;
    }

    public boolean deadlocked() {
        int i = 0;
        while (i < NUMPHILS && state[i] == GOTRIGHT) ++i;
        return i == NUMPHILS;
    }

    public void initPlacing() {

        double radius = 100.0;
        double philWidth = imgs[0].getWidth(this);
        double philHeight = imgs[0].getHeight(this);
        double radians;

        for (int i = 0; i < NUMPHILS; i++) {
            philPlace[i] = new AffineTransform();
            radians = 2.0 * Math.PI * (1.0 - (double) i / (double) NUMPHILS);
            philPlace[i].rotate(radians);
            philPlace[i].translate(0, -radius);
            philPlace[i].translate(-philWidth / 2, -philHeight / 2);
        }

        radius = 35;
        for (int i = 0; i < NUMPHILS; i++) {
            radians = (double) i * 2.0 * Math.PI / (double) NUMPHILS + Math.PI / (double) NUMPHILS;
            chopX[i] = -Math.sin(radians) * radius - 5;
            chopY[i] = -Math.cos(radians) * radius - 5;
            untable[i] = true;
        }
    }
}
