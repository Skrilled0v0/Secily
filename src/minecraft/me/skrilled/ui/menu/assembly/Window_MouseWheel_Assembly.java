package me.skrilled.ui.menu.assembly;

import net.minecraft.client.main.Main;

import java.awt.*;
import java.util.ArrayList;

public class Window_MouseWheel_Assembly<T> extends WindowAssembly {
    public ArrayList<T> contents;
    private int skipAim;
    private boolean needInit = true;
    private int numOfContent2Render;

    private Window_MouseWheel_Assembly(float[] pos, Assembly fatherWindow) {
        super(pos, fatherWindow);
    }

    private Window_MouseWheel_Assembly(float[] pos, Assembly fatherWindow, ArrayList<WindowAssembly> subWindows) {
        super(pos, fatherWindow, subWindows);
    }

    public Window_MouseWheel_Assembly(float[] pos, Assembly fatherWindow, ArrayList<T> contents, int numOfContent2Render) {
        super(pos, fatherWindow);
        this.contents = contents;
        this.numOfContent2Render = numOfContent2Render;
    }

    public void mouseWheel(int delta) {
        if (skipAim - delta < contents.size() && skipAim - delta >= 0) {
            skipAim -= delta;
            this.needInit = true;
        }
    }

    @Override
    public void reset() {
        skipAim = 0;
        subWindows = new ArrayList<>();
        otherAssemblies = new ArrayList<>();
        needInit = true;
    }

    @Override
    public void draw() {
        if (needInit) Init();
        super.draw();
    }

    public void Init() {
        this.otherAssemblies = new ArrayList<>();
        int skipCount = 0;
        int renderCount = 0;
        float bgBoxHeight = this.deltaY / (numOfContent2Render + (numOfContent2Render - 1) * 0.21337989340194817129204190406175f);
        float udMargin = 0.21337989340194817129204190406175f * bgBoxHeight;
        for (int i = 0; i < contents.size(); i++) {
            if (skipCount < skipAim) {
                skipCount++;
                continue;
            }
            if (renderCount < numOfContent2Render) {
                String content = contents.get(i).toString();
                float yUsed = (i - skipAim) * (bgBoxHeight + udMargin);
                StringAssembly stringAssembly = new StringAssembly(new float[]{0, yUsed, deltaX, bgBoxHeight + yUsed}, this, content, false, new Color(255, 255, 255, 25), new Color(-1), Main.fontLoader.EN24,Main.fontLoader.EN24.getHeight()/2f);
                stringAssembly.assemblyName = content;
                this.addAssembly(stringAssembly);
                renderCount++;
            } else break;
        }
        needInit = false;
    }
}
