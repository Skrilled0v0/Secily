package me.skrilled.ui.menu.assembly;

import me.skrilled.utils.render.RenderUtil;
import me.surge.animation.Animation;

import java.awt.*;
import java.util.ArrayList;

public class BGAssembly extends Assembly{
    Color color;
    private BGAssembly(float[] pos, Assembly fatherWindow) {
        super(pos, fatherWindow);
    }
    public BGAssembly(float[] pos, Assembly fatherWindow,Color color) {
        super(pos, fatherWindow);
        this.color = color;
    }

    @Override
    public void draw() {
        float absX = calcAbsX();
        float absY = calcAbsX();
        RenderUtil.drawRoundRect(absX+pos[0],absY+pos[1],absX+pos[2],absY+pos[3],2.5f,color.getRGB());
    }
}
