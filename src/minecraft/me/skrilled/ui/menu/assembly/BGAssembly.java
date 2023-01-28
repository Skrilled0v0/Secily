package me.skrilled.ui.menu.assembly;

import me.skrilled.SenseHeader;
import me.skrilled.ui.menu.assembly.bgType.BackGroundType;
import me.skrilled.utils.render.BlurUtil;
import me.skrilled.utils.render.RenderUtil;

import java.awt.*;

public class BGAssembly extends Assembly {
    Color color;
    BackGroundType bgType;
    boolean canBlur=false;
    float radius=2.5f;

    private BGAssembly(float[] pos, Assembly fatherWindow) {
        super(pos, fatherWindow);
    }

    /**
     * 默认圆角矩形
     */
    public BGAssembly(float[] pos, Assembly fatherWindow, Color color) {
        super(pos, fatherWindow);
        this.color = color;
        bgType = BackGroundType.RoundRect;
    }

    public BGAssembly(float[] pos, Assembly fatherWindow, Color color, BackGroundType bgType) {
        super(pos, fatherWindow);
        this.color = color;
        this.bgType = bgType;
    }

    public BGAssembly(float[] pos, Assembly fatherWindow, Color color, BackGroundType bgType, boolean canBlur) {
        super(pos, fatherWindow);
        this.color = color;
        this.bgType = bgType;
        this.canBlur = canBlur;
    }

    public BGAssembly(float[] pos, Assembly fatherWindow, Color color, BackGroundType bgType, boolean canBlur, float radius) {
        super(pos, fatherWindow);
        this.color = color;
        this.bgType = bgType;
        this.canBlur = canBlur;
        this.radius = radius;
    }

    @Override
    public void draw() {
        float absX = calcAbsX();
        float absY = calcAbsX();
        switch (bgType) {
            case Rect:
                if (canBlur) BlurUtil.blurArea(absX, absY, absX + deltaX, absY + deltaY, 10);
                RenderUtil.drawRect(absX, absY, absX + deltaX, absY + deltaY, color.getRGB());
                break;
            case RoundRect:
                if (canBlur) BlurUtil.blurAreaRounded(absX, absY, absX + deltaX, absY + deltaY, radius, 10);
//                SenseHeader.getSense.printINFO("x1 ["+absX+"] y1 ["+absY+"] x2 ["+(absX+ deltaX)+"] y2 ["+(absY + deltaY)+"]");
//                SenseHeader.getSense.printINFO("bbb"+deltaY);
                RenderUtil.drawRoundRect(absX, absY, absX + deltaX, absY + deltaY, radius, color.getRGB());
                break;
        }
    }
}
