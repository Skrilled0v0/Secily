package me.skrilled.ui.menu.assembly;

import me.skrilled.utils.render.RenderUtil;

import java.awt.*;

public class SolidCicleAssembly extends Assembly {
    Color color;
    float r;

    private SolidCicleAssembly(float[] pos, Assembly fatherWindow) {
        super(pos, fatherWindow);
    }

    public SolidCicleAssembly(float[] pos, Assembly fatherWindow,float r, Color color) {
        super(pos, fatherWindow);
        this.color = color;
        this.r = r;
    }

    @Override
    public void draw() {
        float absX = calcAbsX();
        float absY = calcAbsY();
        RenderUtil.drawCircle(absX + pos[0], absY + pos[1], r, color.getRGB());
    }
}
