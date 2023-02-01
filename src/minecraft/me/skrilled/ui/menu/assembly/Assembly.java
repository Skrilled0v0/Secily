package me.skrilled.ui.menu.assembly;

public abstract class Assembly {
    public boolean autoPushPopMatrix = true;
    /**
     * 被选中后赋值true，自动绘制背景
     */
    public boolean onSelected = false;

    /**
     * 这个（子）窗口的相对父窗口的坐标（x,y）
     */
    public float[] pos;
    /**
     * 这个窗口中已经用了的高度,最大值参考currentUsedHeight
     */
    public float currentUsedHeight = 0;
    /**
     * 这个（子）窗口中能用的最大高度（留出背景框上下左右边距）
     */
    public float maxHeight = 0;
    public Assembly fatherWindow;
    public float deltaX;
    public float deltaY;
    public String assemblyName = "defaultName";
    public boolean canReInit = false;

    public Assembly(float[] pos, Assembly fatherWindow) {
        this.pos = pos;
        deltaX = pos[2] - pos[0];
        deltaY = pos[3] - pos[1];
        this.fatherWindow = fatherWindow;
    }

    public static boolean isMouseInside(int Mx, int My, float x1, float y1, float x2, float y2) {
        return Mx > x1 && My > y1 && Mx < x2 && My < y2;
    }


    /**
     * 调用父窗口坐标和自己相对坐标绘制
     * 返回用去的高度
     */
    public abstract float draw();

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

    public float[] calcAbsPos() {
        return new float[]{calcAbsX(), calcAbsY(), calcAbsX() + deltaX, calcAbsY() + deltaY};
    }

    public abstract void mouseEventHandle(int mouseX, int mouseY, int button);

    public abstract void reInit();

    public void drag(float mouseDeltaX, float mouseDeltaY) {
        this.pos[0] += mouseDeltaX;
        this.pos[1] += mouseDeltaY;
        this.pos[2] += mouseDeltaX;
        this.pos[3] += mouseDeltaY;
    }

}
