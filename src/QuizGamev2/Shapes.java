package QuizGamev2;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.util.Random;

public class Shapes {

    Random random = new Random();
    Colors mycolors = new Colors();
    int[] mixcolor;

    Color darkcolor;
    Color lightcolor;

    Border border1;
    Border border2;
    Border border1Up;
    Border border1Down;
    Border border1Left;
    Border border1Right;

    Border border1UpLeft;
    Border border1UpRight;
    Border border1DownLeft;
    Border border1DownRight;


    public Shapes(int[] mixcolor){
        this.mixcolor = mixcolor;
        darkcolor = new Color(mixcolor[6],mixcolor[7],mixcolor[8]);
        lightcolor = new Color(mixcolor[9],mixcolor[10],mixcolor[11]);
        border1 = BorderFactory.createLineBorder(darkcolor,4);
        border2 = BorderFactory.createLineBorder(darkcolor,3);
        border1Up = BorderFactory.createMatteBorder(4,0,0,0,darkcolor);
        border1Down = BorderFactory.createMatteBorder(0,0,4,0,darkcolor);
        border1Left = BorderFactory.createMatteBorder(0,4,0,0,darkcolor);
        border1Right = BorderFactory.createMatteBorder(0,0,0,4,darkcolor);
        border1UpLeft = BorderFactory.createMatteBorder(4,4,0,0,darkcolor);
        border1UpRight = BorderFactory.createMatteBorder(4,0,0,4,darkcolor);
        border1DownLeft = BorderFactory.createMatteBorder(0,4,4,0,darkcolor);
        border1DownRight = BorderFactory.createMatteBorder(0,0,4,4,darkcolor);
    }

    public void drawShapes(Graphics2D g2d){

        for (int i = 0; i < 30; i++) {
            Path2D.Double p = new Path2D.Double();
            p.moveTo(randx(),randy());
            p.lineTo(randx(),randy());
            p.lineTo(randx(),randy());
            p.closePath();
            g2d.setColor(mycolors.randColor(mixcolor));
            g2d.fill(p);
        }
    }


    public int randx(){
        int xr = random.nextInt(1,320);
        return xr;
    }
    public int randy(){
        int yr = random.nextInt(1,450);
        return yr;
    }


    public int randwidth() {
        return 0;
    }


    public int randheight() {
        return 0;
    }


}



class Shapes1 extends Shapes {


    public Shapes1(int[] mixcolor) {
        super(mixcolor);
    }

    public void drawShapes(Graphics2D g2d){

        for (int i = 0; i < 30; i++) {
            Path2D.Double p = new Path2D.Double();
            p.moveTo(randx(),randy());
            p.lineTo(randx(),randy());
            p.lineTo(randx(),randy());
            p.closePath();
            g2d.setColor(mycolors.randColor(mixcolor));
            g2d.fill(p);
        }
    }


    public int randx(){
        int xr = random.nextInt(1,320);
        return xr;
    }
    public int randy(){
        int yr = random.nextInt(1,450);
        return yr;
    }

    @Override
    public int randwidth() {
        return 0;
    }

    @Override
    public int randheight() {
        return 0;
    }


}



class Shapes2 extends Shapes {
//Filled triangles multitone


    public Shapes2(int[] mixcolor) {
        super(mixcolor);
    }

    public void drawShapes(Graphics2D g2d){

        for (int i = 0; i < 30; i++) {
            Path2D.Double p = new Path2D.Double();
            p.moveTo(randx(),randy());
            p.lineTo(randx(),randy());
            p.lineTo(randx(),randy());
            p.closePath();
            g2d.setColor(mycolors.randColor(mixcolor));
            g2d.fill(p);
        }
    }

    public int randx(){
        int xr = random.nextInt(1,320);
        return xr;
    }
    public int randy(){
        int yr = random.nextInt(1,450);
        return yr;
    }

    @Override
    public int randwidth() {
        return 0;
    }

    @Override
    public int randheight() {
        return 0;
    }
}



class Shapes3 extends Shapes {

    //sketched triangles multitone


    public Shapes3(int[] mixcolor) {
        super(mixcolor);
    }

    public void drawShapes(Graphics2D g2d){
        g2d.setStroke(new BasicStroke(3));

        for (int i = 0; i < 30; i++) {
            Path2D.Double p = new Path2D.Double();
            p.moveTo(randx(),randy());
            p.lineTo(randx(),randy());
            p.lineTo(randx(),randy());
            p.closePath();
            g2d.setColor(mycolors.randColor(mixcolor));
            g2d.draw(p);
        }
    }


    public int randx(){
        int xr = random.nextInt(1,320);
        return xr;
    }
    public int randy(){
        int yr = random.nextInt(1,450);
        return yr;
    }

    @Override
    public int randwidth() {
        return 0;
    }

    @Override
    public int randheight() {
        return 0;
    }
}



class Shapes4 extends Shapes {

    public Shapes4(int[] mixcolor) {
        super(mixcolor);
    }

    public void drawShapes(Graphics2D g2d){
        g2d.setStroke(new BasicStroke(3));

        for (int i = 0; i < 100; i++) {
            Ellipse2D.Double e = new Ellipse2D.Double(randx(),randy(), randwidth(), randheight());
            g2d.setColor(mycolors.randColor(mixcolor));
            g2d.fill(e);
        }
    }

    public int randx(){
        int xr = random.nextInt(1,320);
        return xr;
    }
    public int randy(){
        int yr = random.nextInt(1,450);
        return yr;
    }

    public int randwidth(){
        int randw = random.nextInt(50,150);
        return randw;
    }
    public int randheight(){
        int randh = random.nextInt(50,150);
        return randh;
    }

}



class Shapes5 extends Shapes {

    public Shapes5(int[] mixcolor) {
        super(mixcolor);
    }

    public void drawShapes(Graphics2D g2d){
        g2d.setStroke(new BasicStroke(4));

        for (int i = 0; i < 100; i++) {
            Ellipse2D.Double e = new Ellipse2D.Double(randx(),randy(), randwidth(), randheight());
            g2d.setColor(mycolors.randColor(mixcolor));
            g2d.draw(e);
        }
    }


    public int randx(){
        int xr = random.nextInt(1,320);
        return xr;
    }
    public int randy(){
        int yr = random.nextInt(1,450);
        return yr;
    }

    public int randwidth(){
        int randw = random.nextInt(50,150);
        return randw;
    }
    public int randheight(){
        int randh = random.nextInt(50,150);
        return randh;
    }


}



class Shapes6 extends Shapes {

    public Shapes6(int[] mixcolor) {
        super(mixcolor);
    }

    public void drawShapes(Graphics2D g2d){
        g2d.setStroke(new BasicStroke(4));

        for (int i = 0; i < 100; i++) {
            Rectangle2D.Double e = new Rectangle2D.Double(randx(),randy(), randwidth(), randheight());
            g2d.setColor(mycolors.randColor(mixcolor));
            g2d.draw(e);
        }
    }


    public int randx(){
        int xr = random.nextInt(1,320);
        return xr;
    }
    public int randy(){
        int yr = random.nextInt(1,450);
        return yr;
    }

    public int randwidth(){
        int randw = random.nextInt(50,150);
        return randw;
    }
    public int randheight(){
        int randh = random.nextInt(50,150);
        return randh;
    }



}



class Shapes7 extends Shapes {

    public Shapes7(int[] mixcolor) {
        super(mixcolor);
    }

    public void drawShapes(Graphics2D g2d){
        g2d.setStroke(new BasicStroke(4));

        for (int i = 0; i < 100; i++) {
            Rectangle2D.Double e = new Rectangle2D.Double(randx(),randy(), randwidth(), randheight());
            g2d.setColor(mycolors.randColor(mixcolor));
            g2d.fill(e);
        }
    }

    public int randx(){
        int xr = random.nextInt(1,320);
        return xr;
    }
    public int randy(){
        int yr = random.nextInt(1,450);
        return yr;
    }

    public int randwidth(){
        int randw = random.nextInt(30,60);
        return randw;
    }
    public int randheight(){
        int randh = random.nextInt(30,60);
        return randh;
    }



}
