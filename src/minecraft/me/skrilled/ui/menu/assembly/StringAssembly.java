/*
 *MCP-919-main
 *Code by SkrilledSense
 *20230126
 */
package me.skrilled.ui.menu.assembly;

import me.fontloader.FontDrawer;
import me.skrilled.api.manager.ModuleManager;
import me.skrilled.api.modules.ModuleHeader;
import me.skrilled.ui.menu.ui.KeyBindingGui;
import me.skrilled.utils.IMC;
import me.skrilled.utils.render.RenderUtil;

import java.awt.*;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class StringAssembly extends Assembly implements IMC {
    final byte mode;
    String value;
    boolean[] centered;
    Color bgColor;
    Color onSelectedBGColor;
    Color fontColor;
    boolean border;
    FontDrawer font;
    float radius;
    /**
     * for mode:1
     */
    StringWithoutBGAssembly stringWithoutBGAssembly;
    /**
     * for mode:1
     */
    BGAssembly bgAssembly;

    public StringAssembly(float[] pos, WindowAssembly fatherWindow, String value, boolean[] centered, Color bgColor, Color fontColor, FontDrawer font, float radius) {
        super(pos, fatherWindow);
        this.value = value;
        this.centered = centered;
        this.bgColor = bgColor;
        this.fontColor = fontColor;
        this.font = font;
        this.radius = radius;
        mode = 0;
    }

    public StringAssembly(float[] pos, WindowAssembly fatherWindow, String value, boolean[] centered, Color bgColor, Color onSelectedBGColor, Color fontColor, FontDrawer font, float radius) {
        super(pos, fatherWindow);
        this.value = value;
        this.centered = centered;
        this.bgColor = bgColor;
        this.onSelectedBGColor = onSelectedBGColor;
        this.fontColor = fontColor;
        this.font = font;
        this.radius = radius;
        mode = 0;
    }

    public StringAssembly(float[] pos, WindowAssembly fatherWindow, String value, boolean[] centered, Color bgColor, Color fontColor, boolean border, FontDrawer font) {
        super(pos, fatherWindow);
        this.value = value;
        this.centered = centered;
        this.bgColor = bgColor;
        this.fontColor = fontColor;
        this.border = border;
        this.font = font;
        mode = 0;
    }

    public StringAssembly(StringWithoutBGAssembly s, BGAssembly bg) {
        super(new float[]{min(s.pos[0], bg.pos[0]), min(s.pos[1], bg.pos[1]), max(s.pos[2], bg.pos[2]), max(s.pos[3], bg.pos[3])}, s.fatherWindow);
        this.stringWithoutBGAssembly = s;
        this.bgAssembly = bg;
        mode = 1;
    }

    @Override
    public float draw() {
        switch (mode) {
            case 0:
                return RenderUtil.drawCenteredStringBox_P(calcAbsPos(), font, value, bgColor.getRGB(), fontColor.getRGB(), radius, centered);
            case 1:
                bgAssembly.draw();
                stringWithoutBGAssembly.draw();
                return Math.max(stringWithoutBGAssembly.pos[3], bgAssembly.pos[3]) - Math.min(stringWithoutBGAssembly.pos[1], bgAssembly.pos[1]);
        }
        return 0f;
    }

    @Override
    public void mouseEventHandle(int mouseX, int mouseY, int button) {
        switch (mode) {
            case 0:
                if (assemblyName.startsWith("bindAssembly")) {
                    String moduleName = assemblyName.split("\\.")[1];
                    ModuleHeader module = ModuleManager.getModuleByName(moduleName);
                    mc.displayGuiScreen(new KeyBindingGui(module));
                } else if (assemblyName.startsWith("renderedInArrayListAssembly")) {
                    String moduleName = assemblyName.split("\\.")[1];
                    ModuleHeader module = ModuleManager.getModuleByName(moduleName);
                    module.setCanView(this.value.equals("K"));
                    if (this.value.equals("K")) {
                        value = "J";
                    } else {
                        value = "K";
                    }
                }
            case 1:
                stringWithoutBGAssembly.mouseEventHandle(mouseX, mouseY, button);
                bgAssembly.mouseEventHandle(mouseX, mouseY, button);
        }
    }
}
