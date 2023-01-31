package me.skrilled.ui.menu.assembly;

import java.util.ArrayList;

public class WindowAssembly extends Assembly {
    public BGAssembly bgAssembly;
    public Assembly windowName;
    public ArrayList<IconAssembly> icons = new ArrayList<>();
    public ArrayList<WindowAssembly> subWindows = new ArrayList<>();
    public ArrayList<Assembly> otherAssemblies = new ArrayList<>();

    public WindowAssembly(float[] pos, Assembly fatherWindow) {
        super(pos, fatherWindow);
    }

    public WindowAssembly(float[] pos, Assembly fatherWindow, ArrayList<WindowAssembly> subWindows) {
        super(pos, fatherWindow);
        this.subWindows = subWindows;
    }

    public void addAssembly(Assembly assembly) {
        if(otherAssemblies==null)
            otherAssemblies=new ArrayList<>();
        otherAssemblies.add(assembly);
    }

    public void addWindow(WindowAssembly windowAssembly) {
        subWindows.add(windowAssembly);
    }

    public void delAssembly(Assembly assembly) {
        otherAssemblies.removeIf(a -> a.equals(assembly));
    }

    public void delWindow(WindowAssembly windowAssembly) {
        subWindows.removeIf(a -> a.equals(windowAssembly));
    }

    @Override
    public void draw() {
        if (bgAssembly != null) {
            this.bgAssembly.draw();
        }
        if (windowName != null) {
            this.windowName.draw();
        }
        for (IconAssembly icon : this.icons) {
            icon.draw();
        }
        for (Assembly otherAssembly : otherAssemblies) {
            otherAssembly.draw();
        }
        for (WindowAssembly windowAssembly : subWindows) {
            windowAssembly.draw();
        }
    }

    @Override
    public void MouseClicked(int mouseX,int mouseY,int button) {

    }

    public static boolean isMouseInside(int Mx, int My, float x1, float y1, float x2, float y2) {
        return Mx > x1 && My > y1 && Mx < x2 && My < y2;
    }
    public ArrayList<Assembly> getAssembliesClicked(int mouseX,int mouseY) {
        ArrayList<Assembly> result = new ArrayList<>();
        float[] absPos = null;
        //本窗口组件
        for (Assembly assembly : otherAssemblies) {
            absPos = assembly.calcAbsPos();
            if (isMouseInside(mouseX,mouseY,absPos[0],absPos[1],absPos[2],absPos[3])){
                result.add(assembly);
            }
        }
        //子窗口的组件
        for (WindowAssembly subWindow : subWindows) {
            for (Assembly assembly : subWindow.getAssembliesClicked(mouseX, mouseY)) {
                result.add(assembly);
            }
        }
        return result;
    }
}
