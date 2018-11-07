package concurrency.display;

import javax.swing.*;
import java.awt.*;

class GraphicCanvas extends JPanel {
    int angle_ = 0;
    String title_;
    Color arcColor_ = Color.blue;
    int segStart_ = 9999;
    int segEnd_ = 9999;
    Color segColor_ = Color.yellow;

    Font f1 = new Font("Times", Font.ITALIC+Font.BOLD, 24);

    final static int Cx = 30;
    final static int Cy = 45;

    GraphicCanvas(String title, Color c) {
        super();
        title_=title;
        arcColor_=c;
    }


    public Dimension getPreferredSize() {
	return new Dimension(150, 150);
    }

    public void setColor(Color c){
        setBackground(c);
        repaint();
    }

    public void setAngle(int a){
        angle_ = a;
        repaint();
    }

    public void setSegment(int start, int end, Color c) {
        segStart_ = start;
        segEnd_ = end;
        segColor_ = c;
    }

    Image offscreen;
    Dimension offscreensize;
    Graphics offgraphics;

    public synchronized void update(Graphics g){
	paintComponent(g);
    }
    
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
	// Display the arc
	if (angle_>0) {
	    if (angle_<segStart_ || segStart_==segEnd_) {
                offgraphics.setColor(arcColor_);
                //offgraphics.fillArc(Cx, Cy, 90, 90, 0, angle_);
                offgraphics.fillArc(Cx, Cy, 90, 90, 90, angle_);
	    } else if ( angle_>=segStart_ && angle_<segEnd_) {
                offgraphics.setColor(arcColor_);
                //offgraphics.fillArc(Cx, Cy, 90, 90, 0, segStart_);
                offgraphics.fillArc(Cx, Cy, 90, 90, 90, segStart_);
                if (angle_-segStart_>0) {
                    offgraphics.setColor(segColor_);
                    //offgraphics.fillArc(Cx,Cy, 90, 90, segStart_, angle_-segStart_);
                    offgraphics.fillArc(Cx,Cy, 90, 90, segStart_+90, angle_-segStart_);
                }
	    } else  {
                offgraphics.setColor(arcColor_);
                //offgraphics.fillArc(Cx,Cy,90,90,0, segStart_);
                offgraphics.fillArc(Cx, Cy, 90, 90, 90, segStart_);
                offgraphics.setColor(segColor_);
                //offgraphics.fillArc(Cx,Cy,90,90, segStart_, segEnd_-segStart_);
                offgraphics.fillArc(Cx,Cy,90,90, segStart_ +90, segEnd_-segStart_);
                if (angle_-segEnd_>0){
                    offgraphics.setColor(arcColor_);
                    //offgraphics.fillArc(Cx,Cy,90,90, segEnd_, angle_-segEnd_);
                    offgraphics.fillArc(Cx,Cy,90,90, segEnd_+90, angle_-segEnd_);
                }
	    }
	}
	g.drawImage(offscreen, 0, 0, null);
    }
}
