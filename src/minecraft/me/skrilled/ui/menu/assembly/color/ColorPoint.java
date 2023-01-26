package me.skrilled.ui.menu.assembly.color;

import java.awt.*;

public class ColorPoint {
    public Color color;
    public float[] pos;

    public ColorPoint(Color color, float[] pos) {
        this.color = color;
        this.pos = pos;
    }
    public void SetH(float h){
        float[] hsb = new float[3];
        Color.RGBtoHSB(color.getRed(),color.getGreen(),color.getBlue(),hsb);
        color = Color.getHSBColor(h,hsb[1],hsb[2]);
    }
    public float GetH(){
        float[] hsb = new float[3];
        Color.RGBtoHSB(color.getRed(),color.getGreen(),color.getBlue(),hsb);
        return hsb[0];
    }
}
