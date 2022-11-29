package QuizGamev2;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

public class Canvas1 extends JComponent  {
    int x;
    int y;
    private int width;
    private int height;
    Shapes s1;
    Colors c1 = new Colors();

    public Canvas1(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        //Ange shape-class + index på colormix här:
        s1 = new Shapes6(c1.colormixList.get(7));
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

        s1.drawShapes(g2d);
    }

}
