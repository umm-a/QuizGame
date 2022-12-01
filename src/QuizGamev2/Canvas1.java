package QuizGamev2;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;;

public class Canvas1 extends JComponent  {
    int x;
    int y;
    private int width;
    private int height;
    Shapes shapes;

    GraphicsChooser graphicsChooser;

    public Canvas1(GraphicsChooser graphicsChooser,int framewidth, int frameheight) {
        int posx = (framewidth-320)/2;
        int posy = (frameheight-450)/2;
        this.setLayout(null);

        //panelens position på JFrame
        this.setBounds(posx,posy,320,450);
        this.setBorder(new EtchedBorder());

        //"Ritblockets" position på panelen
        this.x = 0;
        this.y = 0;
        this.width = 320;
        this.height = 450;
        this.graphicsChooser = graphicsChooser;
        shapes = this.graphicsChooser.myshapes;

    }

    protected void paintComponent(Graphics g){
        Graphics2D g2d = (Graphics2D) g;

        RenderingHints rh = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHints(rh);

        Rectangle2D.Double r = new Rectangle2D.Double(x,x,width,height);
        g2d.setColor(new Color(66,66,66));
        g2d.fill(r);

        shapes.drawShapes(g2d);
    }

}
