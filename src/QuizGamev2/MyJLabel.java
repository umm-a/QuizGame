package QuizGamev2;

import javax.swing.*;
import java.awt.*;

public class MyJLabel extends JLabel {



    Canvas1 canvas;

    public MyJLabel(String text, int horizontalAlignment,Canvas1 canvas) {
        super(text, horizontalAlignment);
        this.setBackground(canvas.shapes.lightcolor);
        this.setBorder(canvas.shapes.border1);
        this.setOpaque(true);

    }
}

class MyJPanel extends JPanel{
    Canvas1 canvas;

    public MyJPanel(LayoutManager layout, Canvas1 canvas) {
        super(layout);
        this.canvas = canvas;
        this.setBackground(canvas.shapes.lightcolor);
        this.setBorder(canvas.shapes.border1);
    }
}