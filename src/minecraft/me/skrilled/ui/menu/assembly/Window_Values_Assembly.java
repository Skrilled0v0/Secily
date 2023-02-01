package me.skrilled.ui.menu.assembly;

import me.skrilled.api.modules.ModuleHeader;
import me.skrilled.ui.menu.assembly.bgType.BackGroundType;
import net.minecraft.client.main.Main;

import java.awt.*;
import java.util.ArrayList;

public class Window_Values_Assembly extends WindowAssembly {
    ModuleHeader module;
    boolean needInit = true;
    ArrayList<StringAssembly> pageBars;


    public Window_Values_Assembly(float[] pos, Assembly fatherWindow, ModuleHeader module) {
        super(pos, fatherWindow);
        this.module = module;
    }

    @Override
    public void draw() {
        if (needInit) Init();
        super.draw();
    }

    public void Init() {
        //初始化背景，标题
        BGAssembly thisBG = new BGAssembly(new float[]{0, 0, this.deltaX, this.deltaY}, this, new Color(204, 204, 204, 82), BackGroundType.RoundRect, false, 8.23f);
        this.addAssembly(thisBG);
        StringAssembly valuesTitleAssembly = new StringAssembly(new float[]{0, 0, this.deltaX, this.deltaY * 0.09516098897677025596313134458492f}, this, this.module.toString(), true, new Color(255, 255, 255, 61), Color.white, Main.fontLoader.EN22, 8.28f);
        this.addAssembly(valuesTitleAssembly);
        //计算并添加编辑区窗口
        float[] valuesEditZoneWindowPos = {this.deltaX * 0.04032185676732705007373212797333f, this.deltaY * 0.14363413672126383093562516866995f, this.deltaX * 0.95967814323267294992626787202667f, this.deltaY * 0.94615017334080670943098544767599f};
        WindowAssembly valuesEditZoneWindow = new WindowAssembly(valuesEditZoneWindowPos, this);
        valuesEditZoneWindow.assemblyName = "valuesEditZoneWindow";
        this.addWindow(valuesEditZoneWindow);
        //添加编辑区背景
        BGAssembly valuesEditZoneBG = new BGAssembly(new float[]{0, 0, valuesEditZoneWindow.deltaX, valuesEditZoneWindow.deltaY}, valuesEditZoneWindow, new Color(161, 161, 161, 64), BackGroundType.RoundRect, false, 9.2f);
        valuesEditZoneWindow.addAssembly(valuesEditZoneBG);
        //计算，初始化并添加分割线组件
        float[] linePos = {valuesEditZoneWindow.deltaX / 2f - 0.5f, valuesEditZoneWindow.deltaY * 0.02348802317760877438046458689017f, valuesEditZoneWindow.deltaX / 2f + 0.5f, valuesEditZoneWindow.deltaY * 0.94071084898339282942728542604377f};
        BGAssembly valuesEditZoneLine = new BGAssembly(linePos, valuesEditZoneWindow, new Color(204, 204, 204), BackGroundType.Rect);
        valuesEditZoneWindow.addAssembly(valuesEditZoneLine);
        needInit = false;
    }

    public void setModule(ModuleHeader module) {
        reset();
        this.module = module;
        needInit = true;
    }
}
