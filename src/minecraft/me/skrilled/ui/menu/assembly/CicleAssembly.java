package me.skrilled.ui.menu.assembly;

import me.skrilled.utils.render.RenderUtil;

import java.awt.*;

public class CicleAssembly extends Assembly {
    Color color;
    float r;
    boolean filled;

    private CicleAssembly(float[] pos, Assembly fatherWindow) {
        super(pos, fatherWindow);
    }

    /**
     * 默认实心
     */
    public CicleAssembly(float[] pos, Assembly fatherWindow, float r, Color color) {
        super(pos, fatherWindow);
        this.color = color;
        this.r = r;
        filled = true;
    }

    public CicleAssembly(float[] pos, Assembly fatherWindow, float r, Color color, boolean filled) {
        super(pos, fatherWindow);
        this.color = color;
        this.r = r;
        this.filled = filled;
    }

    @Override
    public void draw() {
        float absX = calcAbsX();
        float absY = calcAbsY();
        if (filled) RenderUtil.drawCircle(absX, absY, r, color.getRGB());
        else RenderUtil.drawAngleCirque(absX, absY, r, 0, 360, 1, color.getRGB());
    }
}
