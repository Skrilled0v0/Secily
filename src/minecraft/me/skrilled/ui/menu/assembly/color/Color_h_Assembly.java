package me.skrilled.ui.menu.assembly.color;

import me.skrilled.ui.menu.assembly.Assembly;

import java.awt.*;
import java.util.ArrayList;

public class Color_h_Assembly extends Assembly {
    public Color_sb_Assembly color_sb_assembly;
    public float h;
    public ArrayList<ColorPoint> colorPoints;
    private boolean init;

    private Color_h_Assembly(float[] pos, Assembly fatherWindow) {
        super(pos, fatherWindow);
    }

    /**
     * 色相的选择的颜色的明度，饱和度默认全1.0f
     */
    public Color_h_Assembly(float[] pos, Assembly fatherWindow, float h) {
        super(pos, fatherWindow);
        this.h = h;
    }

    @Override
    public void draw() {

    }

    public void InitColorPoints() {
        colorPoints = new ArrayList<>();
        for (int i = 0; i < 2f * (pos[2] - pos[0]); i++) {
            colorPoints.add(new ColorPoint(Color.getHSBColor(i / (2f * (pos[2] - pos[0])), 1f, 1f), new float[]{}));
        }
    }
}
