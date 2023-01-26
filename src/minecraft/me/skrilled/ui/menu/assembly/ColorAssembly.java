/*
 *MCP-919-main
 *Code by SkrilledSense
 *20230126
 */
package me.skrilled.ui.menu.assembly;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

public class ColorAssembly extends Assembly {


    public ColorAssembly(float[] pos, Assembly fatherWindow) {
        super(pos, fatherWindow);
    }

    @Override
    public void draw() {
        float deltaX = pos[2] - pos[0];
        float deltaY = pos[3] - pos[1];
        float h = 0f;
        float s = 0f;//x
        float b = 0f;//y
        glEnable(GL_POINTS);
        for (int i = 0; i < deltaX; i++) {
            for (int j = 1; j <= deltaY; j++) {
                s = i / deltaX;
                b = j / deltaY;
                Color color = Color.getHSBColor(h, s, b);
                float red, green, blue;
                red = color.getRed() / 255f;
                green = color.getGreen() / 255f;
                blue = color.getBlue() / 255f;
                glColor3f(red,green,blue);
                glVertex2f(calcAbsX(),calcAbsY());
            }
        }
    }
}
