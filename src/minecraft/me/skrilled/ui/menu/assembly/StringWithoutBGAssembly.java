/*
 *MCP-919-main
 *Code by SkrilledSense
 *20230126
 */
package me.skrilled.ui.menu.assembly;

import me.fontloader.FontDrawer;
import me.skrilled.utils.render.RenderUtil;

import java.awt.*;

public class StringWithoutBGAssembly extends Assembly {
    String value;
    Color fontColor;
    boolean border;
    FontDrawer font;

    private StringWithoutBGAssembly(float[] pos, Assembly fatherWindow) {
        super(pos, fatherWindow);
    }

    public StringWithoutBGAssembly(float[] pos, Assembly fatherWindow, String value, FontDrawer font, Color fontColor) {
        super(pos, fatherWindow);
        this.value = value;
        this.font = font;
        this.fontColor = fontColor;
    }

    @Override
    public void draw() {
        RenderUtil.drawCenteredString(calcAbsPos(),font,value,fontColor.getRGB());
    }

    @Override
    public void mouseEventHandle(int mouseX, int mouseY, int button) {

    }

    @Override
    public void reInit() {

    }
}
