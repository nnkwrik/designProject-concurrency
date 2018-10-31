package concurrency.display;

import java.awt.*;
import javax.swing.*;

public class NumberCanvas extends JPanel {
    int value_ = 0;
    String title_;
    Color c;

    Font f1 = new Font("Helvetica",Font.BOLD,36);
    Font f2 = new Font("Times",Font.ITALIC+Font.BOLD,24);

    public NumberCanvas(String title) {
        this(title, Color.cyan);
    }

    public NumberCanvas(String title, Color c) {
        super();
        this.title_ = title;
        this.c = c;	
    }

    public void setColor(Color c){
        this.c = c;
        //setBackground(c);
        repaint();
    }

    public void setValue(int newval){
        value_ = newval;
        repaint();
    }

    public Dimension getPreferredSize() {
	return new Dimension(100, 100);
    }

    public void paintComponent(Graphics g){
	super.paintComponent(g);
	Graphics2D gc2 = (Graphics2D) g;
	setOpaque(true);
        setBackground(c);
	gc2.setColor(Color.black);

         // Display the title
	gc2.setFont(f2);
	FontMetrics fm = g.getFontMetrics();
	int w = fm.stringWidth(title_);
	int h = fm.getHeight();
	int x = (getSize().width - w)/2;
	int y = h;
	gc2.drawString(title_, x, y);
	gc2.drawLine(x,y+3,x+w,y+3);


         // Display the value
	gc2.setFont(f1);
	fm = g.getFontMetrics();
	String s1 = String.valueOf(value_);
	w = fm.stringWidth(s1);
	h = fm.getHeight();
	x = (getSize().width - w)/2;
	y = (getSize().height+ h)/2;
	gc2.drawString(s1, x, y);
    }
}
