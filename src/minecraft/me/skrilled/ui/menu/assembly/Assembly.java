package me.skrilled.ui.menu.assembly;

public abstract class Assembly {

    /**
     * 拖动时重新计算坐标的比值(相对fatherWindow的deltaPos
     */
    public float[] positionArgs;
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
    public String assemblyName;
    public boolean onDrag = false;
    public boolean canDrag = false;

    public Assembly(float[] pos, WindowAssembly fatherWindow) {
        this.fatherWindow = fatherWindow;
        if (fatherWindow == null) {
            this.pos = pos;
            this.positionArgs = null;
        } else {
            this.positionArgs = pos;
            float dX = fatherWindow.deltaX();
            float dY = fatherWindow.deltaY();
            this.pos = new float[]{dX * pos[0], dY * pos[1], dX * pos[2], dY * pos[3]};
        }
        this.assemblyName = this.getClass().getSimpleName() + (fatherWindow == null ? "" : " father: " + fatherWindow.assemblyName);
    }

    public boolean isMouseInside(Assembly assembly, int Mx, int My, float x1, float y1, float x2, float y2) {
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
        return "Assembly{" + "assemblyName='" + assemblyName + '\'' + '}';
    }

    public void updateRenderPos() {
        if (fatherWindow == null) return;
        float dX = fatherWindow.deltaX();
        float dY = fatherWindow.deltaY();
        this.pos = new float[]{dX * positionArgs[0], dY * positionArgs[1], dX * positionArgs[2], dY * positionArgs[3]};
    }

    public WindowAssembly getOldestFatherWindow(int[] count) {
        if (fatherWindow != null) {
            count[0]++;
            return fatherWindow.getOldestFatherWindow(count);
        } else {
            return (WindowAssembly) this;
        }
    }
}
