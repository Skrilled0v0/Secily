package me.skrilled.ui.menu.assembly;

import java.util.ArrayList;

public class WindowAssembly extends Assembly {
    public BGAssembly bgAssembly;
    public StringWithoutBGAssembly windowName;
    public ArrayList<IconAssembly> icons = new ArrayList<>();
    public ArrayList<WindowAssembly> subWindows = new ArrayList<>();
    public ArrayList<Assembly> assemblies = new ArrayList<>();

    public WindowAssembly(float[] pos, WindowAssembly fatherWindow) {
        super(pos, fatherWindow);
    }

    public WindowAssembly(float[] pos, WindowAssembly fatherWindow, ArrayList<WindowAssembly> subWindows) {
        super(pos, fatherWindow);
        this.subWindows = subWindows;
    }

    public void addAssembly(Assembly assembly) {
        if (assemblies == null) assemblies = new ArrayList<>();
        assemblies.add(assembly);
    }

    public void addWindow(WindowAssembly windowAssembly) {
        subWindows.add(windowAssembly);
    }

    public void delAssembly(Assembly assembly) {
        assemblies.removeIf(a -> a.equals(assembly));
    }

    public void delWindow(WindowAssembly windowAssembly) {
        subWindows.removeIf(a -> a.equals(windowAssembly));
    }

    @Override
    public float draw() {
        currentUsedHeight = 0f;
        if (bgAssembly != null) {
            bgAssembly.pos = new float[]{0, 0, deltaX(), deltaY()};
            currentUsedHeight += this.bgAssembly.draw();
        }
        if (windowName != null) {
            currentUsedHeight += this.windowName.draw();
        }
        for (IconAssembly icon : this.icons) {
            currentUsedHeight += icon.draw();
        }
        for (Assembly otherAssembly : assemblies) {
            currentUsedHeight += otherAssembly.draw();
        }
        for (WindowAssembly windowAssembly : subWindows) {
            currentUsedHeight += windowAssembly.draw();
        }
        return currentUsedHeight > deltaY() ? currentUsedHeight : deltaY();
    }

    @Override
    public void mouseEventHandle(int mouseX, int mouseY, int button) {

    }

    public void reset() {
        assemblies = new ArrayList<>();
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
        for (Assembly assembly : assemblies) {
            if (assembly.assemblyName.equalsIgnoreCase(assemblyName)) return assembly;
        }
        return null;
    }

    public boolean canBeSighted(Assembly assembly) {
        for (Assembly assembly1 : this.assemblies) {
            if (assembly == assembly1) {
                return (assembly.pos[2] <= 0 || assembly.pos[3] <= 0 || assembly.pos[0] >= deltaX() || assembly.pos[1] >= deltaY());
            }
        }
        for (WindowAssembly subWindow : subWindows) {
            return subWindow.canBeSighted(assembly);
        }
        return false;
    }

    public ArrayList<Assembly> getAssembliesByMousePos(int mouseX, int mouseY) {
        ArrayList<Assembly> result = new ArrayList<>();
        float[] absPos = this.calcAbsPos();
        //本窗口
        if (isMouseInside(mouseX, mouseY, absPos[0], absPos[1], absPos[2], absPos[3])) result.add(this);
        //本窗口组件
        for (Assembly assembly : assemblies) {
            if (canBeSighted(assembly)) {
                continue;
            }
            absPos = assembly.calcAbsPos();
            if (isMouseInside(mouseX, mouseY, assembly.pos[0] < 0 ? assembly.fatherWindow.calcAbsX() : absPos[0], assembly.pos[1] < 0 ? assembly.fatherWindow.calcAbsY() : absPos[1], absPos[2], absPos[3])) {
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

    public ArrayList<Assembly> getAssembliesCanDrag() {
        ArrayList<Assembly> result = new ArrayList<>();
        //mainGui不采用这里的拖动方案
        if (this.fatherWindow != null) {
            if (this.canDrag) result.add(this);
        }
        for (Assembly assembly : assemblies) {
            if (assembly.canDrag) result.add(assembly);
        }
        for (WindowAssembly subWindow : subWindows) {
            for (Assembly assembly : subWindow.getAssembliesCanDrag()) {
                result.add(assembly);
            }
        }
        return result;
    }

    public ArrayList<Assembly> getAssembliesByClass(Class<? extends Assembly> T) {
        ArrayList<Assembly> result = new ArrayList<>();
        if (this.getClass().getName().equals(T.getName())) result.add(this);
        for (Assembly assembly : this.assemblies) {
            if (assembly.getClass().getName().equals(T.getName())) result.add(assembly);
        }
        for (WindowAssembly subWindow : subWindows) {
            for (Assembly assembly : subWindow.getAssembliesByClass(T)) {
                result.add(assembly);
            }
        }
        return result;
    }
}
