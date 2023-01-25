package me.skrilled.ui.menu;

import java.util.ArrayList;

public interface Window {
    /**
     * 这个（子）窗口的相对父窗口的坐标（x,y）
     */
    float[] pos = new float[2];
    /**
     * 这个窗口中已经用了的高度
     */
    float currentUsedHeight = 0;
    /**
     * 这个（子）窗口能用的最大高度
     */
    float maxHeight = 0;
    /**
     * 该组件类型
     */
    AssemblyType assemblyType = null;
    /**
     * 能否在父窗口中绘制该窗口
     */
    boolean canDraw = false;
    Window fatherWindow = null;
    ArrayList<Window> subWindow = null;

    /**
     * 调用父窗口坐标和自己相对坐标绘制
     */
    public abstract void draw();

}
