package me.skrilled.ui.menu.assembly;

public abstract class Assembly {
    /**
     * 这个（子）窗口的相对父窗口的坐标（x,y）
     */
    public float[] pos;
    /**
     * 这个窗口中已经用了的高度,最大值参考currentUsedHeight
     */
    float currentUsedHeight = 0;
    /**
     * 这个（子）窗口中能用的最大高度（留出背景框上下左右边距）
     */
    float maxHeight = 0;
    Assembly fatherWindow;

    public Assembly(float[] pos, Assembly fatherWindow) {
        this.pos = pos;
        this.fatherWindow = fatherWindow;
    }

    /**
     * 调用父窗口坐标和自己相对坐标绘制
     */
    public abstract void draw();

    public float calcAbsX() {
        float x = pos[0];
        x += fatherWindow == null ? 0f : fatherWindow.calcAbsX();
        return x;
    }

    public float calcAbsY() {
        float y = pos[1];
        y += fatherWindow == null ? 0f : fatherWindow.calcAbsY();
        return y;
    }

}
