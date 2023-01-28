package me.skrilled.ui.menu.assembly;

import java.util.ArrayList;

public class WindowAssembly extends Assembly {
    BGAssembly bgAssembly;
    StringAssembly windowName;
    ArrayList<IconAssembly> icons = new ArrayList<>();
    ArrayList<WindowAssembly> subWindows = new ArrayList<>();
    ArrayList<Assembly> otherAssemblies = new ArrayList<>();

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
}
