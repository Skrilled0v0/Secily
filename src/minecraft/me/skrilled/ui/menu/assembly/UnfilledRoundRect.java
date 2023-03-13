package me.skrilled.ui.menu.assembly;

import me.skrilled.ui.menu.assembly.enums.FillingMode;
import me.skrilled.ui.menu.assembly.enums.SideOfBoundedWindow;
import me.skrilled.utils.render.RenderUtil;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class UnfilledRoundRect extends Assembly {
    float lineWidth;
    Set<LineAssembly> lineAssemblySet;
    Color color;
    float radius;

    public UnfilledRoundRect(float[] pos, WindowAssembly fatherWindow, Color color, float radius, float lineWidth) {
        super(pos, fatherWindow);
        this.canDrag = true;
        this.lineWidth = lineWidth;
        this.color = color;
        this.radius = radius;
        this.lineAssemblySet = new HashSet<>();
        lineAssemblySet.add(new LineAssembly(new float[]{pos[0], pos[1], pos[0], pos[3]}, fatherWindow, lineWidth, new Color(0, 0, 0, 0), FillingMode.ROUNDED_SIDE, SideOfBoundedWindow.LEFT));
        lineAssemblySet.add(new LineAssembly(new float[]{pos[0], pos[3], pos[2], pos[3]}, fatherWindow, lineWidth, new Color(0, 0, 0, 0), FillingMode.ROUNDED_SIDE, SideOfBoundedWindow.DOWN));
        lineAssemblySet.add(new LineAssembly(new float[]{pos[2], pos[1], pos[2], pos[3]}, fatherWindow, lineWidth, new Color(0, 0, 0, 0), FillingMode.ROUNDED_SIDE, SideOfBoundedWindow.RIGHT));
        lineAssemblySet.add(new LineAssembly(new float[]{pos[0], pos[1], pos[2], pos[1]}, fatherWindow, lineWidth, new Color(0, 0, 0, 0), FillingMode.ROUNDED_SIDE, SideOfBoundedWindow.UP));
    }

    @Override
    public float draw() {
        float[] aP = calcAbsPos();
        RenderUtil.drawRoundRect(aP[0], aP[1], aP[2], aP[3], radius, color.getRGB(), false);
        return 0;
    }

    @Override
    public float getDrawHeight() {
        return 0;
    }

    @Override
    public void mouseEventHandle(int mouseX, int mouseY, int button) {
        for (LineAssembly lineAssembly : lineAssemblySet) {
            if (lineAssembly.isOnDrag()) {
                lineAssembly.mouseEventHandle(mouseX, mouseY, button);
                return;
            }
        }
        for (LineAssembly lineAssembly : lineAssemblySet) {
            if (lineAssembly.isMouseInside(null, mouseX, mouseY, 0, 0, 0, 0)) {
                lineAssembly.mouseEventHandle(mouseX, mouseY, button);
                lineAssembly.setOnDrag(true);
            }
        }
    }

    @Override
    public void setOnDrag(boolean onDrag) {
        super.setOnDrag(onDrag);
        for (LineAssembly lineAssembly : lineAssemblySet) {
            lineAssembly.setOnDrag(false);
        }
    }

    @Override
    public void updateRenderPos() {
        super.updateRenderPos();
        for (LineAssembly lineAssembly : lineAssemblySet) {
            lineAssembly.updateRenderPos();
        }
    }

    @Override
    public boolean isMouseInside(Assembly assembly, int Mx, int My, float x1, float y1, float x2, float y2) {
        return true;
    }
}
