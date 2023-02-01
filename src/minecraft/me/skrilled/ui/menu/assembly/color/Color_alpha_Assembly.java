package me.skrilled.ui.menu.assembly.color;

import me.skrilled.ui.menu.assembly.Assembly;
import me.skrilled.ui.menu.assembly.CicleAssembly;
import me.skrilled.utils.render.RenderUtil;

import java.awt.*;
import java.util.ArrayList;

public class Color_alpha_Assembly extends Assembly {
    public float h, s, b, a;

    public float[] rgba = new float[4];
    public ArrayList<ColorPoint> colorPoints;
    Color color;
    boolean init = false;
    CicleAssembly cicleAssembly;


    private Color_alpha_Assembly(float[] pos, Assembly fatherWindow) {
        super(pos, fatherWindow);
    }

    public Color_alpha_Assembly(float[] pos, Assembly fatherWindow, float h, float s, float b, float a) {
        super(pos, fatherWindow);
        this.h = h;
        this.s = s;
        this.b = b;
        this.a = a;
        color = Color.getHSBColor(h, s, b);
        rgba[0] = color.getRed();
        rgba[1] = color.getGreen();
        rgba[2] = color.getBlue();
        rgba[3] = a;
        float[] circlePos = new float[]{pos[0] + a * deltaX, pos[1] + (deltaY - 1) / 2f, pos[0] + a * deltaX, pos[1] + (deltaY - 1) / 2f};
        cicleAssembly = new CicleAssembly(circlePos, fatherWindow, 0.4f * deltaY, Color.WHITE, false);
    }

    @Override
    public float draw() {
        if (!init) InitColorPoints();
        float absX, absY;
        absX = calcAbsX();
        absY = calcAbsY();
        RenderUtil.drawRect(absX, absY, absX + deltaX, absY + deltaY - 1, -1);
        RenderUtil.drawColorPointsWithYThickness(colorPoints, deltaY);
//        SenseHeader.getSense.printINFO("alpha: "+deltaY);
        cicleAssembly.draw();
        return deltaY;
    }

    @Override
    public void mouseEventHandle(int mouseX, int mouseY, int button) {

    }

    @Override
    public void reInit() {

    }

    public void InitColorPoints() {
        colorPoints = new ArrayList<>();
        float absX, absY;
        absX = calcAbsX();
        absY = calcAbsY();
        for (int i = 0; i < 2f * deltaX; i++) {
            Color tempColor = new Color(rgba[0] / 255f, rgba[1] / 255f, rgba[2] / 255f, i / (2f * (pos[2] - pos[0])));
            colorPoints.add(new ColorPoint(tempColor, new float[]{absX + (i / 2f), absY}));
        }
        init = true;
    }
    public void SetH(float h){
        this.h = h;
        init = false;
    }
}
