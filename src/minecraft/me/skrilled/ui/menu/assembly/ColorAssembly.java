package me.skrilled.ui.menu.assembly;

import me.skrilled.SenseHeader;
import me.skrilled.ui.menu.assembly.color.ColorPoint;
import me.skrilled.ui.menu.assembly.color.Color_alpha_Assembly;
import me.skrilled.ui.menu.assembly.color.Color_h_Assembly;
import me.skrilled.ui.menu.assembly.color.Color_sb_Assembly;

import java.awt.*;
import java.util.ArrayList;

public class ColorAssembly extends Assembly {
    public Color_h_Assembly color_h_assembly;
    public Color_sb_Assembly color_sb_assembly;
    public Color_alpha_Assembly color_alpha_assembly;
    public ArrayList<ColorPoint> h_colors;
    public ArrayList<ColorPoint> sb_colors;
    public ArrayList<ColorPoint> alpha_colors;
    WindowAssembly windowAssembly;
    boolean withAlpha = false;
    BGAssembly bgAssembly;

    /**
     * 创建一个加载初始有默认选择点hsb,alpha值的颜色选区
     * 推荐长宽比5：6
     */
    public ColorAssembly(float[] pos, WindowAssembly fatherWindow, float h, float s, float b, float a) {
        super(pos, fatherWindow);
        withAlpha = true;
        windowAssembly = new WindowAssembly(pos, fatherWindow);
        fatherWindow.addWindow(windowAssembly);
        //构建sb明度饱和度选框,h拖动条,alpha拖动条,pos待设计（注意留出勾勒边框的位置）
        float margin = 0.05f * deltaX;
        float[] bg_pos = new float[]{0, 0, deltaX, deltaY};
        float[] sb_pos = new float[]{margin, margin, deltaX - margin, deltaX - margin};
        //y坐标计算：(dY-2*margin-0.9dX)/2
        float height = 0.08f * deltaX;
        float[] h_pos = new float[]{margin, deltaX, deltaX - margin, deltaX + height};
        float[] alpha_pos = new float[]{margin, 1.1f * deltaX, deltaX - margin, 1.1f * deltaX + height};
        bgAssembly = new BGAssembly(bg_pos, windowAssembly, Color.darkGray);
        windowAssembly.addAssembly(bgAssembly);
        color_h_assembly = new Color_h_Assembly(h_pos, windowAssembly, h, this);
        windowAssembly.addAssembly(color_h_assembly);
        color_sb_assembly = new Color_sb_Assembly(sb_pos, windowAssembly, h, s, b, this);
        windowAssembly.addAssembly(color_sb_assembly);
        color_alpha_assembly = new Color_alpha_Assembly(alpha_pos, windowAssembly, h, s, b, a, this);
        windowAssembly.addAssembly(color_alpha_assembly);
    }

    @Override
    public float draw() {
        return 0f;
    }

    @Override
    public void mouseEventHandle(int mouseX, int mouseY, int button) {

    }

    public Color getColorClicked_hsb(int[] mouseABSPos) {
        for (ArrayList<ColorPoint> colorPoints : color_sb_assembly.colorPointLists) {
            if (mouseABSPos[0] == Math.floor(colorPoints.get(0).pos[0])) {
                for (ColorPoint colorPoint : colorPoints) {
                    if (mouseABSPos[1] == Math.floor(colorPoint.pos[1])) {
                        return colorPoint.color;
                    }
                }
            }
        }
        SenseHeader.getSense.printINFO("鼠标超出选区？？？//hsb");
        return null;
    }

    public Color getColorClicked_h(int[] mouseABSPos) {
        for (ColorPoint colorPoint : color_h_assembly.colorPoints) {
            if (mouseABSPos[0] == Math.floor(colorPoint.pos[0])) {
                color_sb_assembly.SetH(colorPoint.GetH());
                return colorPoint.color;
            }
        }
        SenseHeader.getSense.printINFO("鼠标超出选区？？？//h");
        return null;
    }

    public Color getColorClicked_alpha(int[] mouseABSPos) {
        for (ColorPoint colorPoint : color_alpha_assembly.colorPoints) {
            if (mouseABSPos[0] == Math.floor(colorPoint.pos[0])) {
                return colorPoint.color;
            }
        }
        SenseHeader.getSense.printINFO("鼠标超出选区？？？//alpha");
        return null;
    }

    public void SetH(float h) {
        color_sb_assembly.SetH(h);
        color_h_assembly.SetH(h);
        color_alpha_assembly.SetH(h);
    }
}
