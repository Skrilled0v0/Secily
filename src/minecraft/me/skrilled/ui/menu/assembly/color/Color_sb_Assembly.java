/*
 *MCP-919-main
 *Code by SkrilledSense
 *20230126
 */
package me.skrilled.ui.menu.assembly.color;

import me.skrilled.ui.menu.assembly.Assembly;
import me.skrilled.ui.menu.assembly.SolidCicleAssembly;
import me.skrilled.utils.render.RenderUtil;

import java.awt.*;
import java.util.ArrayList;

public class Color_sb_Assembly extends Assembly {
    public Color_h_Assembly color_h_assembly;
    public SolidCicleAssembly solidCicleAssembly;
    public float h;
    public ArrayList<ArrayList<ColorPoint>> colorPointLists;
    private boolean init = false;


    private Color_sb_Assembly(float[] pos, Assembly fatherWindow) {
        super(pos, fatherWindow);
    }

    public Color_sb_Assembly(float[] pos, Assembly fatherWindow, float h, float s, float b) {
        super(pos, fatherWindow);
        this.h = h;
        float circleR = 5.0f;
        float x = pos[0] + (pos[2] - pos[0]) * s;
        float y = pos[1] + (pos[3] - pos[1]) * b;
        float[] solidCirclePos = new float[]{x - circleR, y - circleR, x + circleR, y + circleR};
        this.solidCicleAssembly = new SolidCicleAssembly(solidCirclePos, fatherWindow);
    }

    /**
     * 含有sb选框的颜色更新
     */
    public void SetH(float h) {
        this.h = h;
        //更新sb选框
        for (ArrayList<ColorPoint> colorPoints : colorPointLists) {
            for (ColorPoint colorPointUpdating : colorPoints) {
                colorPointUpdating.SetH(h);
            }
        }
    }

    @Override
    public void draw() {
        if (!init) {
            InitColorPointLists();
        }
        RenderUtil.drawColorPointLists(colorPointLists);
        this.solidCicleAssembly.draw();
    }

    public void InitColorPointLists() {
        colorPointLists = new ArrayList<>();
        float s, b;
        float absX, absY;
        absX = calcAbsX();
        absY = calcAbsY();
        for (int i = 0; i < 2f * (pos[2] - pos[0]); i++) {
            colorPointLists.add(new ArrayList<>());
            s = i / (2f * (pos[2] - pos[0]));
            for (int j = 0; j < 2f * (pos[3] - pos[1]); i++) {
                b = j / (2f * (pos[3] - pos[1]));
                colorPointLists.get(i).add(new ColorPoint(Color.getHSBColor(h, s, b), new float[]{absX + (i / 2f), absY + (j / 2f)}));
            }
        }
    }
}
