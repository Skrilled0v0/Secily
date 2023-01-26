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
    boolean withAlpha = false;
    BGAssembly bgAssembly;


    private ColorAssembly(float[] pos, Assembly fatherWindow) {
        super(pos, fatherWindow);
    }

    /**
     * 创建一个加载初始有默认选择点hsb,alpha值的颜色选区
     */
    public ColorAssembly(float[] pos, Assembly fatherWindow, float h, float s, float b, float a) {
        super(pos, fatherWindow);
        withAlpha = true;
        //构建sb明度饱和度选框,h拖动条,alpha拖动条,pos待设计（注意留出勾勒边框的位置）
        float[] bg_pos = new float[]{};
        float[] h_pos = new float[]{};
        float[] sb_pos = new float[]{};
        float[] alpha_pos = new float[]{};
        bgAssembly = new BGAssembly(bg_pos, fatherWindow);
        color_h_assembly = new Color_h_Assembly(h_pos, fatherWindow, h);
        color_sb_assembly = new Color_sb_Assembly(sb_pos, fatherWindow, h, s, b);
        color_alpha_assembly = new Color_alpha_Assembly(alpha_pos, fatherWindow, h, s, b, a);
    }

    /**
     * 创建一个加载初始有默认选择点hsb值的颜色选区
     * 无alpha
     */
    public ColorAssembly(float[] pos, Assembly fatherWindow, float h, float s, float b) {
        super(pos, fatherWindow);
        //构建sb明度饱和度选框,h拖动条,alpha拖动条,pos待设计（注意留出勾勒边框的位置）
        float[] bg_pos = new float[]{};
        float[] h_pos = new float[]{};
        float[] sb_pos = new float[]{};
        bgAssembly = new BGAssembly(bg_pos, fatherWindow);
        color_h_assembly = new Color_h_Assembly(h_pos, fatherWindow, h);
        color_sb_assembly = new Color_sb_Assembly(sb_pos, fatherWindow, h, s, b);
    }

    @Override
    public void draw() {
        //待设计边框和背景，这两个先画
        //有透明值时白色背景凸显透明效果？

        //color选区等
        this.color_sb_assembly.draw();
        this.color_h_assembly.draw();
        if (this.color_alpha_assembly != null) color_alpha_assembly.draw();
    }

    public Color getColorClicked_hsb(int[] mouseABSPos) {
        for (ArrayList<ColorPoint> colorPoints : color_sb_assembly.colorPointLists) {
            if (mouseABSPos[0] == Math.floor(colorPoints.get(0).pos[0])){
                for (ColorPoint colorPoint : colorPoints) {
                    if (mouseABSPos[1]==Math.floor(colorPoint.pos[1])){
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
            if (mouseABSPos[0]==Math.floor(colorPoint.pos[0])){
                color_sb_assembly.SetH(colorPoint.GetH());
                return colorPoint.color;
            }
        }
        SenseHeader.getSense.printINFO("鼠标超出选区？？？//h");
        return null;
    }

    public Color getColorClicked_alpha(int[] mouseABSPos) {
        for (ColorPoint colorPoint : color_alpha_assembly.colorPoints) {
            if (mouseABSPos[0]==Math.floor(colorPoint.pos[0])){
                return colorPoint.color;
            }
        }
        SenseHeader.getSense.printINFO("鼠标超出选区？？？//alpha");
        return null;
    }
}
