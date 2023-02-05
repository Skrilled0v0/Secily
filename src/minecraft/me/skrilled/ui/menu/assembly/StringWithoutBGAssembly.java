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
    public String value;
    /**
     * centered 0 -> xCentered
     * centered 1 -> yCentered
     */
    boolean[] centered = new boolean[]{true, false};
    Color fontColor;
    boolean border;
    FontDrawer font;

    public StringWithoutBGAssembly(float[] pos, WindowAssembly fatherWindow, String value, FontDrawer font, Color fontColor) {
        super(pos, fatherWindow);
        this.value = value;
        this.font = font;
        this.fontColor = fontColor;
    }

    public StringWithoutBGAssembly(float[] pos, WindowAssembly fatherWindow, String value, FontDrawer font, Color fontColor, boolean[] centered) {
        super(pos, fatherWindow);
        this.value = value;
        this.font = font;
        this.fontColor = fontColor;
        this.centered = centered;
    }

    @Override
    public float draw() {
        float[] absPos = calcAbsPos();
        if (centered[1]) absPos[1] += (deltaY() - font.getHeight()) / 2f + 1;
        if (centered[0]) return RenderUtil.drawCenteredString(calcAbsPos(), font, value, fontColor.getRGB());
        else return RenderUtil.drawLeftedString(calcAbsPos(), font, value, fontColor.getRGB());
    }

    @Override
    public void mouseEventHandle(int mouseX, int mouseY, int button) {
        if (fatherWindow != null) {
            //Enum组件下的点击
            if (fatherWindow.fatherWindow != null) {
                if (fatherWindow.fatherWindow instanceof EnumAssembly) {
                    //展开的点击判定
                    EnumAssembly enumAssembly = ((EnumAssembly) (fatherWindow.fatherWindow));
                    if (this.value.equals("H") || this.value.equals("I")) {
                        enumAssembly.setDropDown(this.value.equals("H"));
                    } else {
                        //Enum文字的点击判定
                        enumAssembly.setCurrentValue(this.value);
                    }
                }
            }
        }
    }
}
