package me.skrilled.ui.menu.assembly.color;

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

public class Color_h_Assembly extends Assembly {
    public Color_sb_Assembly color_sb_assembly;
    public float h;
    public ArrayList<ColorPoint> colorPoints;
    CicleAssembly circleAssembly;
    ColorAssembly colorAssembly;
    private boolean init;

    /**
     * 色相的选择的颜色的明度，饱和度默认全1.0f
     */
    public Color_h_Assembly(float[] pos, WindowAssembly fatherWindow, float h, ColorAssembly colorAssembly) {
        super(pos, fatherWindow);
        this.h = h;
        this.colorAssembly = colorAssembly;
        processCircleAssembly(h);
    }

    private void processCircleAssembly(float h) {
        float[] circlePos = new float[]{pos[0] + deltaX * h, pos[1] + (deltaY - 1) / 2f, pos[0] + deltaX * (h / 360), pos[1] + (deltaY - 1) / 2f};
        this.circleAssembly = new CicleAssembly(circlePos, fatherWindow, 0.4f * deltaY, Color.WHITE, false);
    }

    @Override
    public float draw() {
        if ((!init) || SecilyUserInterface.clickDrag) initColorPoints();
        RenderUtil.drawColorPointsWithYThickness(colorPoints, deltaY);
        circleAssembly.draw();
        return deltaY;
    }

    @Override
    public void mouseEventHandle(int mouseX, int mouseY, int button) {
        String[] valueInfo = colorAssembly.assemblyName.split("\\.");
        ModuleHeader moduleHeader = ModuleManager.getModuleByName(valueInfo[0]);
        ValueHeader valueHeader = moduleHeader.getValueByName(valueInfo[1]);
        float x = mouseX - calcAbsX();
        x = x < 0 ? 0 : x > deltaX ? deltaX : x;
        processCircleAssembly(x / deltaX);
        Color color = valueHeader.getColorValue();
        int alpha = color.getAlpha();
        float[] hsb = {};
        Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsb);
        color = Color.getHSBColor(x / deltaX, hsb[1], hsb[2]);
        valueHeader.setColorValue(new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha));
    }

    public void initColorPoints() {
        colorPoints = new ArrayList<>();
        float absX, absY;
        absX = calcAbsX();
        absY = calcAbsY();
        for (int i = 0; i < 2f * (pos[2] - pos[0]); i++) {
            colorPoints.add(new ColorPoint(Color.getHSBColor(i / (2f * (pos[2] - pos[0])), 1f, 1f), new float[]{absX + (i / 2f), absY}));
        }
        init = true;
    }

    public void SetH(float h) {
        this.h = h;
        float[] circlePos = new float[]{pos[0] + deltaX * h, pos[1] + (deltaY - 1) / 2f, pos[0] + deltaX * (h / 360), pos[1] + (deltaY - 1) / 2f};
        this.circleAssembly = new CicleAssembly(circlePos, fatherWindow, 0.4f * deltaY, Color.WHITE, false);
        init = false;
    }
}
