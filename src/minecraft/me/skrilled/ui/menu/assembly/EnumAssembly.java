/*
 *MCP-919-main
 *Code by SkrilledSense
 *20230126
 */
package me.skrilled.ui.menu.assembly;

import me.fontloader.FontDrawer;
import me.skrilled.utils.render.RenderUtil;
import me.surge.animation.Animation;

import java.awt.*;
import java.util.ArrayList;

public class EnumAssembly extends Assembly {


    FontDrawer font;
    Color bgColor;
    Color fontColor;
    ArrayList<String> allChoice;
    ArrayList<String> restChoice;
    String currentValue;
    Animation animation;

    public EnumAssembly(float[] pos, WindowAssembly fatherWindow, FontDrawer font, Color bgColor, Color fontColor, ArrayList<String> allChoice, String currentValue, Animation animation) {
        super(pos, fatherWindow);
        this.font = font;
        this.bgColor = bgColor;
        this.fontColor = fontColor;
        this.allChoice = allChoice;
        this.currentValue = currentValue;
        this.animation = animation;
        restChoice = (ArrayList<String>) allChoice.clone();
        restChoice.removeIf(s -> s.equalsIgnoreCase(currentValue));
    }

    @Override
    public float draw() {
        float absX = calcAbsX(), absY = calcAbsY();
        return RenderUtil.drawEnumTypeBox(font, currentValue, restChoice, absX, absY, (float) animation.getAnimationFactor(), bgColor.getRGB(), fontColor.getRGB());
    }

    @Override
    public void mouseEventHandle(int mouseX, int mouseY, int button) {

    }
}
