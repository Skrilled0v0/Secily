package me.skrilled.ui.menu.assembly;

import me.skrilled.utils.render.RenderUtil;

import java.awt.*;

public class CicleAssembly extends Assembly {
    Color color;
    float r;
    boolean filled;

    private CicleAssembly(float[] pos, WindowAssembly fatherWindow) {
        super(pos, fatherWindow);
    }

    /**
     * 默认实心
     */
    public CicleAssembly(float[] pos, WindowAssembly fatherWindow, float r, Color color) {
        super(pos, fatherWindow);
        this.color = color;
        this.r = r;
        filled = true;
    }

    public CicleAssembly(float[] pos, WindowAssembly fatherWindow, float r, Color color, boolean filled) {
        super(pos, fatherWindow);
        this.color = color;
        this.r = r;
        this.filled = filled;
    }

    @Override
    public float draw() {
        float absX = calcAbsX();
        float absY = calcAbsY();
        if (filled) RenderUtil.drawCircle(absX, absY, r, color.getRGB());
        else RenderUtil.drawAngleCirque(absX, absY, r, 0, 360, 1, color.getRGB());
        return r * 2;
    }

    @Override
    public float getDrawHeight() {
        return deltaY();
    }

    @Override
    public void mouseEventHandle(int mouseX, int mouseY, int button) {

    }
}
