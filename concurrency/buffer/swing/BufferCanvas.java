/*
@author  j.n.magee
*/
package concurrency.buffer.swing;

import java.awt.*;
import javax.swing.*;


/**************************************************************/

public class BufferCanvas extends JPanel {
    String title_;
    int slots_;
    int in_=0;
    int out_=0;
    char[] buf_;

    Font f1 = new Font("Helvetica",Font.ITALIC+Font.BOLD,24);
    Font f2 = new Font("TimesRoman",Font.BOLD,36);

    BufferCanvas(String title, int slots) {
        super();
        title_=title;
        slots_=slots;
        setBackground(Color.cyan);
        buf_ = new char[slots];
    }

    public void setValue(char[] buf,int in, int out){
        buf_=buf;
        in_=in;
        out_=out;
        repaint();
    }

    public Dimension getPreferredSize() {
	return new Dimension(20+50*slots_,150);
//	return new Dimension(20+50*slots_,150);
    }


    public synchronized void update(Graphics g){
	paintComponent(g);
    }
	
    Image offscreen;
    Dimension offscreensize;
    Graphics offgraphics;

    public void paintComponent(Graphics g){
	super.paintComponent(g);
	Graphics2D gc2 = (Graphics2D) g;
	setOpaque(true);

        Dimension d = getSize();
	if ((offscreen == null) || (d.width != offscreensize.width)
	    || (d.height != offscreensize.height)) {
	    offscreen = createImage(d.width, d.height);
	    offscreensize = d;
	    offgraphics = offscreen.getGraphics();
	    offgraphics.setFont(getFont());
	}

	offgraphics.setColor(getBackground());
	offgraphics.fillRect(0, 0, d.width, d.height);

	// Display the title
        offgraphics.setColor(Color.black);
        offgraphics.setFont(f1);
        FontMetrics fm = offgraphics.getFontMetrics();
        int w = fm.stringWidth(title_);
        int h = fm.getHeight();
        int x = (getSize().width - w)/2;
        int y = h;
        offgraphics.drawString(title_, x, y);
        offgraphics.drawLine(x,y+3,x+w,y+3);
        // Buffer Boxes
        y = getSize().height/2 - 15;
        offgraphics.setColor(Color.white);
        offgraphics.fillRect(10,y,50*slots_,50);
        offgraphics.setColor(Color.black);
        offgraphics.setFont(f2);
        for(int i=0; i<slots_; i++) {
            offgraphics.drawRect(10+50*i,y,50,50);
            offgraphics.drawChars(buf_,i,1,25+50*i,y+35);
        }
        //Input and output Pointers
        offgraphics.setColor(Color.blue);
        offgraphics.fillOval(35+50*in_,y-20,15,15);
        offgraphics.setColor(Color.yellow);
        offgraphics.fillOval(35+50*out_,y+55,15,15);
        g.drawImage(offscreen, 0, 0, null);
    }
}
