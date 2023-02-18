package me.skrilled.ui.menu.assembly;

public abstract class Assembly {
    public boolean autoPushPopMatrix = true;

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
    public WindowAssembly fatherWindow;
    public String assemblyName = "defaultName";
    public boolean onDrag = false;
    public boolean canDrag = false;

    public Assembly(float[] pos, WindowAssembly fatherWindow) {
        this.pos = pos;
        this.fatherWindow = fatherWindow;
        this.assemblyName = this.getClass().getSimpleName();
    }

    public static boolean isMouseInside(int Mx, int My, float x1, float y1, float x2, float y2) {
        return Mx > x1 && My > y1 && Mx < x2 && My < y2;
    }

    public float deltaX() {
        return pos[2] - pos[0];
    }

    public float deltaY() {
        return pos[3] - pos[1];
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
        return new float[]{calcAbsX(), calcAbsY(), calcAbsX() + deltaX(), calcAbsY() + deltaY()};
    }

    public abstract void mouseEventHandle(int mouseX, int mouseY, int button);


    public void onDrag(float mouseDeltaX, float mouseDeltaY) {
        this.pos[0] += mouseDeltaX;
        this.pos[1] += mouseDeltaY;
        this.pos[2] += mouseDeltaX;
        this.pos[3] += mouseDeltaY;
    }

    @Override
    public String toString() {
        return "Assembly{" + ", assemblyName='" + assemblyName + '\'' + '}';
    }
}
