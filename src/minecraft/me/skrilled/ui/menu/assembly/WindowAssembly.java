package me.skrilled.ui.menu.assembly;

import java.util.ArrayList;

public class WindowAssembly extends Assembly {
    public ArrayList<IconAssembly> icons = new ArrayList<>();
    public ArrayList<WindowAssembly> subWindows;
    public ArrayList<Assembly> assemblies = new ArrayList<>();

    public WindowAssembly(float[] pos, WindowAssembly fatherWindow, String assemblyName) {
        this(pos, fatherWindow, new ArrayList<>(), assemblyName);
    }

    public WindowAssembly(float[] pos, WindowAssembly fatherWindow, ArrayList<WindowAssembly> subWindows, String assemblyName) {
        super(pos, fatherWindow);
        this.subWindows = subWindows;
        for (WindowAssembly subWindow : subWindows) {
            subWindow.fatherWindow = this;
        }
        this.assemblyName = assemblyName;
    }

    public static WindowAssembly getVoidWindow() {
        return new WindowAssembly(new float[4], null, "");
    }

    public void addAssembly(Assembly assembly) {
        if (assemblies == null) assemblies = new ArrayList<>();
        assemblies.add(assembly);
    }

    @Override
    public void updateRenderPos() {
        super.updateRenderPos();
        for (Assembly assembly : assemblies) {
            assembly.updateRenderPos();
        }
        for (IconAssembly icon : icons) {
            icon.updateRenderPos();
        }
        for (WindowAssembly subWindow : subWindows) {
            subWindow.updateRenderPos();
        }
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
        for (IconAssembly icon : this.icons) {
            currentUsedHeight += icon.draw();
        }
        for (Assembly otherAssembly : assemblies) {
            if (otherAssembly instanceof BGAssembly) otherAssembly.draw();
            else currentUsedHeight += otherAssembly.draw();
        }
        for (WindowAssembly windowAssembly : subWindows) {
            currentUsedHeight += windowAssembly.draw();
        }
        return Math.max(currentUsedHeight, deltaY());
    }

    @Override
    public void mouseEventHandle(int mouseX, int mouseY, int button) {

    }

    public void reset() {
        assemblies = new ArrayList<>();
        subWindows = new ArrayList<>();
    }

    public Assembly getAssemblyByName(String assemblyName) {
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

    public boolean cantBeSighted(Assembly assembly) {
        for (Assembly assembly1 : this.assemblies) {
            if (assembly == assembly1) {
                float deltaX = deltaX();
                float deltaY = deltaY();
                return (assembly.pos[2] < 0 || assembly.pos[3] < 0 || assembly.pos[0] > deltaX || assembly.pos[1] > deltaY);
            }
        }
        for (WindowAssembly subWindow : subWindows) {
            return subWindow.cantBeSighted(assembly);
        }
        return true;
    }

    public ArrayList<Assembly> getAssembliesByMousePos(int mouseX, int mouseY) {
        if (this instanceof Window_Values_Assembly) {
            Math.min(1, 2);
        }
        ArrayList<Assembly> result = new ArrayList<>();
        float[] absPos = this.calcAbsPos();
        //本窗口
        if (this.isMouseInside(this, mouseX, mouseY, absPos[0], absPos[1], absPos[2], absPos[3])) result.add(this);
        //本窗口组件
        for (Assembly assembly : assemblies) {
            if (cantBeSighted(assembly)) {
                continue;
            }
            absPos = assembly.calcAbsPos();
            if (assembly.isMouseInside(assembly, mouseX, mouseY, assembly.pos[0] < 0 ? assembly.fatherWindow.calcAbsX() : absPos[0], assembly.pos[1] < 0 ? assembly.fatherWindow.calcAbsY() : absPos[1], Math.min(absPos[2], assembly.fatherWindow.deltaX() + assembly.fatherWindow.calcAbsX()), Math.min(absPos[3], assembly.fatherWindow.deltaY() + assembly.fatherWindow.calcAbsY()))) {
                result.add(assembly);
            }
        }
        //子窗口的组件
        for (WindowAssembly subWindow : subWindows) {
            if (subWindow.fatherWindow instanceof Window_Values_Assembly) {
                Math.max(1, 2);
            }
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

    public ArrayList<Assembly> getAssembliesByClass(Class T) {
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

    public void posUpdateByDelta(float[] deltaPos) {
        this.pos[0] += deltaPos[0];
        this.pos[1] += deltaPos[1];
        this.pos[2] += deltaPos[2];
        this.pos[3] += deltaPos[3];
    }
}
