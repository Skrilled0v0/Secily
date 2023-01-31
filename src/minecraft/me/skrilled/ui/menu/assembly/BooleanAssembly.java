/*
 *MCP-919-main
 *Code by SkrilledSense
 *20230126
 */
package me.skrilled.ui.menu.assembly;

import me.skrilled.utils.render.RenderUtil;
import me.surge.animation.Animation;

import java.awt.*;

public class BooleanAssembly extends Assembly {
    boolean value;
    Animation animation;
    Color bgColor;
    Color tureColor;
    Color falseColor;


    private BooleanAssembly(float[] pos, Assembly fatherWindow) {
        super(pos, fatherWindow);
    }

    public BooleanAssembly(float[] pos, Assembly fatherWindow, boolean value, Animation animation, Color bgColor, Color tureColor, Color falseColor) {
        super(pos, fatherWindow);
        this.value = value;
        this.animation = animation;
        this.bgColor = bgColor;
        this.tureColor = tureColor;
        this.falseColor = falseColor;
    }

    @Override
    public void draw() {
        float absX = calcAbsX(), absY = calcAbsY();
        RenderUtil.drawBooleanButton(absX, absY, deltaX, deltaY, (float) animation.getAnimationFactor(), bgColor.getRGB(), tureColor.getRGB()
                , falseColor.getRGB());
    }

    @Override
    public void MouseClicked(int mouseX,int mouseY,int button) {

    }

    public void SetAnimState(boolean in) {
        animation.setState(in);
    }
}
