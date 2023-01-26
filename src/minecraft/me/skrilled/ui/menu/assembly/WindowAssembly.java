package me.skrilled.ui.menu.assembly;

import java.util.ArrayList;

public class WindowAssembly extends Assembly{
    BGAssembly bgAssembly;
    StringAssembly windowName;
    ArrayList<IconAssembly> icons;
    ArrayList<WindowAssembly> subWindows;
    ArrayList<Assembly> otherAssemblies;
    public WindowAssembly(float[] pos, Assembly fatherWindow) {
        super(pos, fatherWindow);
    }

    public WindowAssembly(float[] pos, Assembly fatherWindow,ArrayList<WindowAssembly> subWindows) {
        super(pos, fatherWindow);
        this.subWindows = subWindows;
    }

    @Override
    public void draw() {
        this.bgAssembly.draw();
        if (windowName!=null){
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
