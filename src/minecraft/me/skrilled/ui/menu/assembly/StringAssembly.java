/*
 *MCP-919-main
 *Code by SkrilledSense
 *20230126
 */
package me.skrilled.ui.menu.assembly;

import me.fontloader.FontDrawer;
import me.skrilled.utils.render.RenderUtil;

import java.awt.*;

public class StringAssembly extends Assembly {
    String value;
    boolean centered;
    Color bgColor;
    Color fontColor;
    boolean border;
    FontDrawer font;
    float radius;

    public StringAssembly(float[] pos, Assembly fatherWindow, String value, boolean centered, Color bgColor, Color fontColor, FontDrawer font,float radius) {
        super(pos, fatherWindow);
        this.value = value;
        this.centered = centered;
        this.bgColor = bgColor;
        this.fontColor = fontColor;
        this.font = font;
        this.radius = radius;
    }

    public StringAssembly(float[] pos, Assembly fatherWindow, String value, boolean centered, Color bgColor, Color fontColor, boolean border, FontDrawer font) {
        super(pos, fatherWindow);
        this.value = value;
        this.centered = centered;
        this.bgColor = bgColor;
        this.fontColor = fontColor;
        this.border = border;
        this.font = font;
    }

    @Override
    public float draw() {
        if (centered) return RenderUtil.drawCenteredStringBox_P(calcAbsPos(), font, value, bgColor.getRGB(), fontColor.getRGB(),radius);
        else return RenderUtil.drawStringBox_P(calcAbsPos(), font, value, bgColor.getRGB(), fontColor.getRGB());
    }

    @Override
    public void mouseEventHandle(int mouseX, int mouseY, int button) {

    }

    @Override
    public void reInit() {

    }
}
