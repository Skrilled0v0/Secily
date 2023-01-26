/*
 *MCP-919-main
 *Code by SkrilledSense
 *20230126
 */
package me.skrilled.ui.menu.assembly;

import me.skrilled.SenseHeader;
import me.skrilled.utils.render.RenderUtil;

import java.awt.*;

public class ColorAssembly extends Assembly {


    public ColorAssembly(float[] pos, Assembly fatherWindow) {
        super(pos, fatherWindow);
    }

    @Override
    public void draw() {
        float deltaX = pos[2] - pos[0];
        float deltaY = pos[3] - pos[1];
        float h = 0f;
        float s;//x
        float b;//y
        for (int i = 0; i < deltaX; i++) {
            for (int j = 1; j <= deltaY; j++) {
                s = i / deltaX;
                b = j / deltaY;
                Color color = Color.getHSBColor(h, s, b);
                SenseHeader.getSense.printINFO(calcAbsX() + " " + calcAbsY());
                RenderUtil.drawPoint(calcAbsX(), calcAbsY(), color.getRGB());

            }
        }
    }
}
