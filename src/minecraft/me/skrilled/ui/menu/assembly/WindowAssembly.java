package me.skrilled.ui.menu.assembly;

import java.util.ArrayList;

public class WindowAssembly extends Assembly{
    BGAssembly bgAssembly;
    StringAssembly windowName;
    ArrayList<IconAssembly> icons;
    ArrayList<WindowAssembly> windowAssemblies;
    ArrayList<Assembly> otherAssemblies;
    public WindowAssembly(float[] pos, Assembly fatherWindow) {
        super(pos, fatherWindow);
    }

    @Override
    public void draw() {
        this.bgAssembly.draw();
        this.windowName.draw();
        for (IconAssembly icon : this.icons) {
            icon.draw();
        }
        for (Assembly otherAssembly : otherAssemblies) {
            otherAssembly.draw();
        }
        for (WindowAssembly windowAssembly : windowAssemblies) {
            windowAssembly.draw();
        }
    }
}
