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

public class Color_alpha_Assembly extends Assembly {
    public float h, s, b, a;

    public int[] rgba = new int[4];
    public ArrayList<ColorPoint> colorPoints;
    Color color;
    boolean init = false;
    CicleAssembly cicleAssembly;
    ColorAssembly colorAssembly;

    public Color_alpha_Assembly(float[] pos, WindowAssembly fatherWindow, float h, float s, float b, float a, ColorAssembly colorAssembly) {
        super(pos, fatherWindow);
        this.h = h;
        this.s = s;
        this.b = b;
        this.a = a;
        this.colorAssembly = colorAssembly;
        this.canDrag = true;
        color = Color.getHSBColor(h, s, b);
        rgba[0] = color.getRed();
        rgba[1] = color.getGreen();
        rgba[2] = color.getBlue();
        rgba[3] = (int) (a * 255);
        processCircleAssembly(a);
    }

    public void processCircleAssembly(float a) {
        float[] circlePos = new float[]{pos[0] + a * deltaX(), pos[1] + (deltaY() - 1) / 2f, pos[0] + a * deltaX(), pos[1] + (deltaY() - 1) / 2f};
        cicleAssembly = new CicleAssembly(circlePos, fatherWindow, 0.4f * deltaY(), Color.WHITE, false);
    }

    @Override
    public float draw() {
        if ((!init) || SecilyUserInterface.mainGUIClickDrag) initColorPoints();
        float absX, absY;
        absX = calcAbsX();
        absY = calcAbsY();
        RenderUtil.drawRect(absX, absY, absX + deltaX(), absY + deltaY() - 1, Color.black.getRGB());;
        RenderUtil.drawColorPointsWithYThickness(colorPoints, deltaY());
        cicleAssembly.draw();
        return deltaY();
    }

    @Override
    public void mouseEventHandle(int mouseX, int mouseY, int button) {
        System.out.println(onDrag);
        String[] valueInfo = colorAssembly.assemblyName.split("\\.");
        ModuleHeader moduleHeader = ModuleManager.getModuleByName(valueInfo[0]);
        ValueHeader valueHeader = moduleHeader.getValueByName(valueInfo[1]);
        float x = mouseX - calcAbsX();
        x = x < 0 ? 0 : x > deltaX() ? deltaX() : x;
        processCircleAssembly(x / deltaX());
        Color color = valueHeader.getColorValue();
        valueHeader.setColorValue(new Color(color.getRed(), color.getGreen(), color.getBlue(), (int) (255 * x / deltaX())));
    }

    public void initColorPoints() {
        colorPoints = new ArrayList<>();
        float absX, absY;
        absX = calcAbsX();
        absY = calcAbsY();
        for (int i = 0; i < 2f * deltaX(); i++) {
            Color tempColor = new Color(rgba[0] / 255f, rgba[1] / 255f, rgba[2] / 255f, i / (2f * (pos[2] - pos[0])));
            colorPoints.add(new ColorPoint(tempColor, new float[]{absX + (i / 2f), absY}));
        }
        init = true;
    }

    public void setH(float h) {
        this.h = h;
        float[] hsb = new float[3];
        Color.RGBtoHSB(rgba[0], rgba[1], rgba[2], hsb);
        Color color1 = new Color(Color.HSBtoRGB(h, hsb[1], hsb[2]));
        rgba[0] = color1.getRed();
        rgba[1] = color1.getGreen();
        rgba[2] = color1.getBlue();
        init = false;
    }

    public void setSB(float[] sb) {
        this.s = sb[0];
        this.b = sb[1];
        float[] hsb = new float[3];
        Color.RGBtoHSB(rgba[0], rgba[1], rgba[2], hsb);
        Color color1 = new Color(Color.HSBtoRGB(hsb[0], s, b));
        rgba[0] = color1.getRed();
        rgba[1] = color1.getGreen();
        rgba[2] = color1.getBlue();
        init = false;
    }
}
