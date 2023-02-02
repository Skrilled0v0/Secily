/*
 *MCP-919-main
 *Code by SkrilledSense
 *20230126
 */
package me.skrilled.ui.menu.assembly.color;

import me.skrilled.SenseHeader;
import me.skrilled.api.manager.ModuleManager;
import me.skrilled.api.modules.ModuleHeader;
import me.skrilled.api.value.ValueHeader;
import me.skrilled.ui.menu.assembly.Assembly;
import me.skrilled.ui.menu.assembly.CicleAssembly;
import me.skrilled.ui.menu.assembly.ColorAssembly;
import me.skrilled.ui.menu.assembly.WindowAssembly;
import me.skrilled.ui.menu.ui.SecilyUserInterface;
import me.skrilled.utils.render.RenderUtil;

import java.awt.*;
import java.util.ArrayList;

public class Color_sb_Assembly extends Assembly {
    public Color_h_Assembly color_h_assembly;
    public CicleAssembly circleAssembly;
    public float h;
    public ArrayList<ArrayList<ColorPoint>> colorPointLists;
    public boolean init = false;
    ColorAssembly colorAssembly;

    public Color_sb_Assembly(float[] pos, WindowAssembly fatherWindow, float h, float s, float b, ColorAssembly colorAssembly) {
        super(pos, fatherWindow);
        this.h = h;
        this.colorAssembly = colorAssembly;
        processCircleAssembly(s, b);
    }

    public void processCircleAssembly(float s, float b) {
        float circleR = 3.2f;
        float x = pos[0] + (pos[2] - pos[0]) * s;
        float y = pos[1] + (pos[3] - pos[1]) * b;
        float[] circlePos = new float[]{x, y, x, y};
        this.circleAssembly = new CicleAssembly(circlePos, fatherWindow, circleR, Color.WHITE, false);
    }

    /**
     * 含有sb选框的颜色更新
     */
    public void setH(float h) {
        this.h = h;
        init = false;
    }

    @Override
    public float draw() {
        if ((!init) || SecilyUserInterface.clickDrag) initColorPointLists();
        RenderUtil.drawColorPointLists(colorPointLists);
        this.circleAssembly.draw();
        return deltaY;
    }

    @Override
    public void mouseEventHandle(int mouseX, int mouseY, int button) {
        String[] valueInfo = colorAssembly.assemblyName.split("\\.");
        SenseHeader.getSense.getModuleManager();
        ModuleHeader moduleHeader = ModuleManager.getModuleByName(valueInfo[0]);
        ValueHeader valueHeader = moduleHeader.getValueByName(valueInfo[1]);
        float x = mouseX - calcAbsX();
        float y = mouseY - calcAbsY();
        x = x < 0 ? 0 : x > deltaX ? deltaX : x;
        y = y < 0 ? 0 : y > deltaY ? deltaY : y;
        processCircleAssembly(x / deltaX, y / deltaY);
        colorAssembly.color_alpha_assembly.setSB(new float[]{x / deltaX, y / deltaY});
        Color color = valueHeader.getColorValue();
        int alpha = color.getAlpha();
        float[] hsb = new float[4];
        Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsb);
        color = Color.getHSBColor(hsb[0], x / deltaX, y / deltaY);
        valueHeader.setColorValue(new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha));
    }

    public void initColorPointLists() {
        colorPointLists = new ArrayList<>();
        float s, b;
        float absX, absY;
        absX = calcAbsX();
        absY = calcAbsY();
        for (int i = 0; i < 2f * (pos[2] - pos[0]); i++) {
            colorPointLists.add(new ArrayList<>());
            s = i / (2f * (pos[2] - pos[0]));
            for (int j = 0; j < 2f * (pos[3] - pos[1]); j++) {
                b = j / (2f * (pos[3] - pos[1]));
                colorPointLists.get(i).add(new ColorPoint(Color.getHSBColor(h, s, b), new float[]{absX + (i / 2f), absY + (j / 2f)}));
            }
        }
        init = true;
    }
}
