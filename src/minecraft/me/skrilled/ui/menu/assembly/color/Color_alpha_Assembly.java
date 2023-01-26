package me.skrilled.ui.menu.assembly.color;

import me.skrilled.ui.menu.assembly.Assembly;

import java.util.ArrayList;

public class Color_alpha_Assembly extends Assembly {
    public float h,s,b,a;
    public ArrayList<ColorPoint> colorPoints;


    private Color_alpha_Assembly(float[] pos, Assembly fatherWindow) {
        super(pos, fatherWindow);
    }

    public Color_alpha_Assembly(float[] pos, Assembly fatherWindow, float h, float s, float b, float a) {
        super(pos, fatherWindow);
        this.h = h;
        this.s = s;
        this.b = b;
        this.a = a;
    }

    @Override
    public void draw() {

    }
}
