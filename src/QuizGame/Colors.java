package QuizGame;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Colors {

    Random random = new Random();


    //colormixes rgb,
    //(R-low, R-up, G-low, G-up, B-low, B-up, R-Dark, G-Dark, B-Dark, R-Light, G-Light, B-Light)
    int[] pinkmix = {195, 236, 24, 64, 91, 122,173, 20, 87,252, 228, 236};
    int[] violetmix = {123, 171, 31, 71, 162, 188,106, 27, 154,225, 190, 231};
    int[] turcoisemix = {0, 38, 151, 198, 167, 218,0, 105, 92,178, 223, 219};
    int[] redmix = {211, 244, 47, 83, 47, 80,183, 28, 28,255, 205, 210};
    int[] greenmix = {45, 120, 140, 230, 60, 120,27, 94, 32,200, 230, 201};
    int[] bluemix = {25, 100, 100, 180, 200, 253,13, 71, 161,187, 222, 251};
    int[] yellowmix = {251, 255, 195, 255, 45, 100,255, 143, 0,255, 245, 157};
    int[] orangemix = {240, 255, 108, 183, 0, 50,239, 108, 0,255, 204, 128};


    List<int[]> colormixList = new ArrayList<>();
    public Colors(){
        colormixList.add(pinkmix);
        colormixList.add(violetmix);
        colormixList.add(turcoisemix);
        colormixList.add(redmix);
        colormixList.add(greenmix);
        colormixList.add(bluemix);
        colormixList.add(yellowmix);
        colormixList.add(orangemix);
        Collections.shuffle(colormixList);
    }

    public Color randColor(int[] colormix) {
        int r = random.nextInt(colormix[0], colormix[1]);
        int g = random.nextInt(colormix[2], colormix[3]);
        int b = random.nextInt(colormix[4], colormix[5]);
        return new Color(r, g, b);
    }



    }
