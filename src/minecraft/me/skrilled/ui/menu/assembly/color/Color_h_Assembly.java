package me.skrilled.ui.menu.assembly.color;

import me.skrilled.ui.menu.assembly.Assembly;
import me.skrilled.ui.menu.assembly.SolidCicleAssembly;
import me.skrilled.utils.render.RenderUtil;

import java.awt.*;
import java.util.ArrayList;

public class Color_h_Assembly extends Assembly {
    public Color_sb_Assembly color_sb_assembly;
    SolidCicleAssembly solidCircleAssembly;
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
        float[] solidCirclePos = new float[]{pos[0]+deltaX*(h/360),pos[1]+deltaY/2f,pos[0]+deltaX*(h/360),pos[1]+deltaY/2f};
        this.solidCircleAssembly = new SolidCicleAssembly(solidCirclePos,fatherWindow,deltaY*0.6f,Color.blue);
    }

    @Override
    public void draw() {
        if (!init) InitColorPoints();
        RenderUtil.drawColorPointsWithYThickness(colorPoints,0.8f*deltaY);
        solidCircleAssembly.draw();
    }

    public void InitColorPoints() {
        colorPoints = new ArrayList<>();
        float absX, absY;
        absX = calcAbsX();
        absY = calcAbsY();
        for (int i = 0; i < 2f * (pos[2] - pos[0]); i++) {
            colorPoints.add(new ColorPoint(Color.getHSBColor(i / (2f * (pos[2] - pos[0])), 1f, 1f), new float[]{absX + (i / 2f), absY}));
        }
    }
}
