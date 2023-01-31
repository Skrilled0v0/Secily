package me.skrilled.ui.menu.assembly;

import me.skrilled.SenseHeader;

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
        if (otherAssemblies == null) otherAssemblies = new ArrayList<>();
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
    public void mouseEventHandle(int mouseX, int mouseY, int button) {

    }

    @Override
    public void reInit() {
        if (bgAssembly.canReInit) {
            bgAssembly.reInit();
        }
        if (windowName.canReInit) {
            windowName.reInit();
        }
        for (IconAssembly icon : icons) {
            if (icon.canReInit) {
                icon.reInit();
            }
        }
        for (WindowAssembly subWindow : subWindows) {
            if (subWindow.canReInit) {
                subWindow.reInit();
            }
        }
        for (Assembly assembly : otherAssemblies) {
            if (assembly.canReInit) {
                assembly.reInit();
            }
        }
    }

    public void reset() {
        otherAssemblies = new ArrayList<>();
        subWindows = new ArrayList<>();
    }

    public Assembly getAssemblyByName(String assemblyName) {
        if (bgAssembly != null) if (bgAssembly.assemblyName.equalsIgnoreCase(assemblyName)) return bgAssembly;
        if (windowName != null) if (windowName.assemblyName.equalsIgnoreCase(assemblyName)) return windowName;
        for (IconAssembly icon : icons) {
            if (icon.assemblyName.equalsIgnoreCase(assemblyName)) return icon;
        }
        for (WindowAssembly subWindow : subWindows) {
            if (subWindow.assemblyName.equalsIgnoreCase(assemblyName)) return subWindow;
            Assembly assembly = subWindow.getAssemblyByName(assemblyName);
            if (assembly != null) return assembly;
        }
        for (Assembly assembly : otherAssemblies) {
            if (assembly.assemblyName.equalsIgnoreCase(assemblyName)) return assembly;
        }
        return null;
    }

    public ArrayList<Assembly> getAssembliesByMousePos(int mouseX, int mouseY) {
        ArrayList<Assembly> result = new ArrayList<>();
        float[] absPos = this.calcAbsPos();
        //本窗口
        if (isMouseInside(mouseX, mouseY, absPos[0], absPos[1], absPos[2], absPos[3])) result.add(this);
        //本窗口组件
        for (Assembly assembly : otherAssemblies) {
            absPos = assembly.calcAbsPos();
            if (isMouseInside(mouseX, mouseY, absPos[0], absPos[1], absPos[2], absPos[3])) {
                result.add(assembly);
            }
        }
        //子窗口的组件
        for (WindowAssembly subWindow : subWindows) {
            for (Assembly assembly : subWindow.getAssembliesByMousePos(mouseX, mouseY)) {
                result.add(assembly);
            }
        }
        return result;
    }
}
