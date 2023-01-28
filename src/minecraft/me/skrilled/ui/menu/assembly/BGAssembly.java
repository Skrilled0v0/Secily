package me.skrilled.ui.menu.assembly;

import me.skrilled.ui.menu.assembly.bgType.BackGroundType;
import me.skrilled.utils.render.RenderUtil;

import java.awt.*;

public class BGAssembly extends Assembly {
    Color color;
    BackGroundType bgType;

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

    @Override
    public void draw() {
        float absX = calcAbsX();
        float absY = calcAbsX();
        switch (bgType) {
            case Rect:
                RenderUtil.drawRect(absX + pos[0], absY + pos[1], absX + pos[2], absY + pos[3], color.getRGB());
                break;
            case RoundRect:
                RenderUtil.drawRoundRect(absX + pos[0], absY + pos[1], absX + pos[2], absY + pos[3], 2.5f, color.getRGB());
                break;
        }
    }
}
