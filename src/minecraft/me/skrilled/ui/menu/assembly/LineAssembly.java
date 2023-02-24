package me.skrilled.ui.menu.assembly;

import me.skrilled.ui.menu.assembly.enums.FillingMode;
import me.skrilled.ui.menu.assembly.enums.SideOfBoundedWindow;
import me.skrilled.utils.math.Vec3f;
import org.lwjgl.input.Keyboard;

import java.awt.*;

import static java.lang.Math.abs;

public class LineAssembly extends Assembly {
    public float width;
    /**
     * 不同颜色模式的colors数目可能有区别
     */
    public Color color;
    FillingMode mode;
    int lastMouseX = -1;
    int lastMouseY = -1;
    /**
     * 不另外处理，默认拖动父窗口
     */
    WindowAssembly boundedWindow;
    SideOfBoundedWindow sideOfBoundedWindow;

    public LineAssembly(float[] pos, WindowAssembly fatherWindow, float width, Color color, SideOfBoundedWindow sideOfBoundedWindow) {
        this(pos, fatherWindow, width, color, FillingMode.SIMPLE, sideOfBoundedWindow);
    }

    public LineAssembly(float[] pos, WindowAssembly fatherWindow, float width, Color color, FillingMode mode, SideOfBoundedWindow sideOfBoundedWindow) {
        super(pos, fatherWindow);
        this.width = width;
        this.color = color;
        this.mode = mode;
        this.sideOfBoundedWindow = sideOfBoundedWindow;
        boundedWindow = fatherWindow;
    }

    /**
     * x1,y1,z1,z2在本组件该方法中不用
     *
     * @param Mx 鼠标X
     * @param My 鼠标Y
     * @return true if the mouse is inside the zone of this line (with width
     */
    @Override
    public boolean isMouseInside(Assembly assembly, int Mx, int My, float x1, float y1, float x2, float y2) {
        Vec3f mouse = new Vec3f(Mx, My);
        Vec3f[] points = mode.calcPoints(this);

        double LU2MouseCrossLD2Mouse = points[0].sub(mouse).cross(points[1].sub(mouse)).getZ();
        double LD2MouseCrossRD2Mouse = points[1].sub(mouse).cross(points[2].sub(mouse)).getZ();
        double RD2MouseCrossRU2Mouse = points[2].sub(mouse).cross(points[3].sub(mouse)).getZ();
        double RU2MouseCrossLU2Mouse = points[3].sub(mouse).cross(points[0].sub(mouse)).getZ();

        if (LU2MouseCrossLD2Mouse == 0) return true;
        if (LD2MouseCrossRD2Mouse == 0) return true;
        if (RD2MouseCrossRU2Mouse == 0) return true;
        if (RU2MouseCrossLU2Mouse == 0) return true;

        boolean isPositive1 = (LU2MouseCrossLD2Mouse / abs(LU2MouseCrossLD2Mouse)) == 1;
        boolean isPositive2 = (LD2MouseCrossRD2Mouse / abs(LD2MouseCrossRD2Mouse)) == 1;
        boolean isPositive3 = (RD2MouseCrossRU2Mouse / abs(RD2MouseCrossRU2Mouse)) == 1;
        boolean isPositive4 = (RU2MouseCrossLU2Mouse / abs(RU2MouseCrossLU2Mouse)) == 1;
        boolean result = isPositive1 == isPositive2 && isPositive2 == isPositive3 && isPositive3 == isPositive4;
        return result;
    }

    @Override
    public float draw() {
        return mode.draw(this);
    }

    /**
     * 对拖拽做处理,摁住rShift等比例缩放
     */
    @Override
    public void mouseEventHandle(int mouseX, int mouseY, int button) {
        if (!onDrag) {
            //刚点
        } else {
            //点了的之后
            boolean shift = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);
            boundedWindow.posUpdateByDelta(sideOfBoundedWindow.calcDeltaPos(mouseX - lastMouseX, mouseY - lastMouseY, shift, new float[]{boundedWindow.deltaX(), boundedWindow.deltaY()}));
            boundedWindow.updateRenderPos();
        }
        this.lastMouseX = mouseX;
        this.lastMouseY = mouseY;
    }
}