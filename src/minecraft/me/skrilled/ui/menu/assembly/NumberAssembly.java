/*
 *MCP-919-main
 *Code by SkrilledSense
 *20230126
 */
package me.skrilled.ui.menu.assembly;

import me.skrilled.utils.render.RenderUtil;
import me.surge.animation.Animation;

import java.awt.*;

public class NumberAssembly extends Assembly {
    /**
     * 0：最小值；1：现在值；2：最大值；3：间隔
     */
    double[] doubles;
    Animation anim;
    Color bgColor;
    Color ugColor;
    Color buttonColor;

    public NumberAssembly(float[] pos, WindowAssembly fatherWindow, double[] doubles, Animation anim, Color bgColor, Color ugColor, Color buttonColor) {
        super(pos, fatherWindow);
        this.doubles = doubles;
        this.anim = anim;
        this.bgColor = bgColor;
        this.ugColor = ugColor;
        this.buttonColor = buttonColor;
    }

    @Override
    public float draw() {
        float absX = calcAbsX(), absY = calcAbsY();
        return RenderUtil.drawNumberBar(absX,absY,deltaX,deltaY,(float) anim.getAnimationFactor(),bgColor.getRGB(),ugColor.getRGB(),buttonColor.getRGB());
    }

    @Override
    public void mouseEventHandle(int mouseX, int mouseY, int button) {

    }
}
