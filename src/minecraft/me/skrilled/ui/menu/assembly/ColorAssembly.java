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
        RenderUtil.drawHSBColorBox(this);
    }
}
