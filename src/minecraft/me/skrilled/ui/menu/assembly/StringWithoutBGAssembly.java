/*
 *MCP-919-main
 *Code by SkrilledSense
 *20230126
 */
package me.skrilled.ui.menu.assembly;

import me.fontloader.FontDrawer;
import me.skrilled.SenseHeader;
import me.skrilled.api.modules.ModuleHeader;
import me.skrilled.api.value.ValueHeader;
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
        if (centered[1]) absPos[1] += (deltaY - font.getHeight()) / 2f + 1;
        if (centered[0]) return RenderUtil.drawCenteredString(calcAbsPos(), font, value, fontColor.getRGB());
        else return RenderUtil.drawLeftedString(calcAbsPos(), font, value, fontColor.getRGB());
    }

    @Override
    public void mouseEventHandle(int mouseX, int mouseY, int button) {
        if (fatherWindow != null) {
            if (fatherWindow.assemblyName.equalsIgnoreCase("windowOfEnumAssembly")) {
                if (this.value.equals("H") || this.value.equals("I")) {
                    EnumAssembly enumAssembly = ((EnumAssembly) (fatherWindow.fatherWindow));
                    enumAssembly.setDropDown(this.value.equals("H"));
                } else if (fatherWindow.fatherWindow.assemblyName.startsWith("enumAssembly")) {
                    String s = fatherWindow.fatherWindow.assemblyName;
                    String[] valueInfo = s.split("\\.");
                    ModuleHeader module = SenseHeader.getSense.getModuleManager().getModuleByName(valueInfo[1]);
                    ValueHeader value = module.getValueByName(valueInfo[2]);
                    value.setCurrentEnumType(this.value);
                }
            }
        }
    }
}
