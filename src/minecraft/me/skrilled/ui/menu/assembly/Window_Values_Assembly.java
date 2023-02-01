package me.skrilled.ui.menu.assembly;

import me.fontloader.FontDrawer;
import me.skrilled.api.modules.ModuleHeader;
import me.skrilled.api.value.ValueHeader;
import me.skrilled.ui.menu.assembly.bgType.BackGroundType;
import me.surge.animation.Animation;
import me.surge.animation.Easing;
import net.minecraft.client.main.Main;

import java.awt.*;
import java.util.ArrayList;

public class Window_Values_Assembly extends WindowAssembly {
    public BGAssembly bgAssembly;
    public StringWithoutBGAssembly windowName;
    public ArrayList<IconAssembly> icons = new ArrayList<>();
    public ArrayList<WindowAssembly> subWindows = new ArrayList<>();
    public ArrayList<Assembly> otherAssemblies = new ArrayList<>();
    WindowAssembly valuesEditZoneWindow;
    ModuleHeader module;
    boolean needInit = true;
    BGAssembly pageBarBG;
    StringWithoutBGAssembly pageBar;
    int pages = 1;


    public Window_Values_Assembly(float[] pos, Assembly fatherWindow, ModuleHeader module) {
        super(pos, fatherWindow);
        this.module = module;
    }

    public static float[] calcPosForValueName(float width, float height, WindowAssembly fatherWindow, float lMargin, boolean inLeftHalfZone) {
        return new float[]{(inLeftHalfZone ? 0 : fatherWindow.deltaX / 2f) + lMargin, fatherWindow.currentUsedHeight, lMargin + width + (inLeftHalfZone ? 0 : fatherWindow.deltaX / 2f), fatherWindow.currentUsedHeight + height};
    }

    public static float[] calcPosForValueBox(float width, float height, WindowAssembly fatherWindow, float[] lrMargin, boolean inLeftHalfZone) {
        return new float[]{fatherWindow.deltaX / 2f + (inLeftHalfZone ? (-width - lrMargin[0]) : lrMargin[1]), fatherWindow.currentUsedHeight, fatherWindow.deltaX / 2f + (inLeftHalfZone ? (-width - lrMargin[0]) : lrMargin[1]) + width, fatherWindow.currentUsedHeight + height};
    }

    public static float[] processYUsed(float yUsed, float[] pos) {
        pos[1] += yUsed;
        pos[3] += yUsed;
        return pos;
    }

    @Override
    public float draw() {
        if (needInit) {
            Init();
            return deltaY;
        }
        return super.draw();
    }

    public void Init() {
        currentUsedHeight = 0f;
        //初始化背景，标题
        BGAssembly thisBG = new BGAssembly(new float[]{0, 0, this.deltaX, this.deltaY}, this, new Color(204, 204, 204, 82), BackGroundType.RoundRect, false, 8.23f);
        this.addAssembly(thisBG);
        StringAssembly valuesTitleAssembly = new StringAssembly(new float[]{0, 0, this.deltaX, this.deltaY * 0.09516098897677025596313134458492f}, this, this.module.toString(), true, new Color(255, 255, 255, 61), Color.white, Main.fontLoader.EN22, 8.28f);
        this.addAssembly(valuesTitleAssembly);
        //计算并添加编辑区窗口
        float[] valuesEditZoneWindowPos = {this.deltaX * 0.04032185676732705007373212797333f, this.deltaY * 0.14363413672126383093562516866995f, this.deltaX * 0.95967814323267294992626787202667f, this.deltaY * 0.94615017334080670943098544767599f};
        valuesEditZoneWindow = new WindowAssembly(valuesEditZoneWindowPos, this);
        valuesEditZoneWindow.assemblyName = "valuesEditZoneWindow";
        this.addWindow(valuesEditZoneWindow);
        //添加编辑区背景
        BGAssembly valuesEditZoneBG = new BGAssembly(new float[]{0, 0, valuesEditZoneWindow.deltaX, valuesEditZoneWindow.deltaY}, valuesEditZoneWindow, new Color(161, 161, 161, 64), BackGroundType.RoundRect, false, 9.2f);
        valuesEditZoneWindow.addAssembly(valuesEditZoneBG);
        //计算，初始化并添加分割线组件
        float[] linePos = {valuesEditZoneWindow.deltaX / 2f - 0.5f, valuesEditZoneWindow.deltaY * 0.02348802317760877438046458689017f, valuesEditZoneWindow.deltaX / 2f + 0.5f, valuesEditZoneWindow.deltaY * 0.94071084898339282942728542604377f};
        BGAssembly valuesEditZoneLine = new BGAssembly(linePos, valuesEditZoneWindow, new Color(204, 204, 204), BackGroundType.Rect);
        valuesEditZoneWindow.addAssembly(valuesEditZoneLine);


        //开始排版values,到valuesEditZoneWindow
        float[] pos = {0, 0, 0, 0};
        float lMargin = 0.02883006025440749832626645837983f * valuesEditZoneWindow.deltaX;
        float rMargin = 0.01412910064717696942646730640482f * valuesEditZoneWindow.deltaX;
        float uMargin = 0.04697604635521754876092917378033f * valuesEditZoneWindow.deltaY;
        float dMargin = 0.02883006025440749832626645837983f * valuesEditZoneWindow.deltaY;

        boolean inLeftHalfZone = true;
        //一般value(name+盒)大小
        float width_default = 0.44632894443204641821022093282749f * valuesEditZoneWindow.deltaX;
        float height_default = 0.1086450411299084277510476486109f * valuesEditZoneWindow.deltaY;
        //留出上边距
        valuesEditZoneWindow.currentUsedHeight = uMargin;
        //定义ValueName显示字体
        FontDrawer valueNameFont = Main.fontLoader.EN22;
        for (ValueHeader valueHeader : module.getValueList()) {
            float yUsedValueBox = 0f;
            float yUsedValueName = 0f;
            //value调控组件
            switch (valueHeader.getValueType()) {
                case BOOLEAN:
                    float width = 0.09649073867440303503682213791564f * valuesEditZoneWindow.deltaX;
                    float height = 0.06710124683118630037767085726111f * valuesEditZoneWindow.deltaY;
                    float rSpacing = 0.04067172506137022985940638250391f * valuesEditZoneWindow.deltaX;
                    float uSpacing = 0.02077189714936106368668839567489f * valuesEditZoneWindow.deltaY;
                    pos[0] = -rSpacing - width + valuesEditZoneWindow.deltaX / (inLeftHalfZone ? 2f : 1f);
                    pos[1] = valuesEditZoneWindow.currentUsedHeight + uSpacing;
                    pos[2] = pos[0] + width;
                    pos[3] = pos[1] + height;
                    if (pos[3] > valuesEditZoneWindow.deltaY) {
                        valuesEditZoneWindow.currentUsedHeight = uMargin;
                        inLeftHalfZone = !inLeftHalfZone;
                        if (inLeftHalfZone) {
                            //翻页
                            pages++;
                        } else {

                        }
                        pos[0] = -rSpacing - width + valuesEditZoneWindow.deltaX / (inLeftHalfZone ? 2f : 1f);
                        pos[1] = valuesEditZoneWindow.currentUsedHeight + uSpacing;
                        pos[2] = pos[0] + width;
                        pos[3] = pos[1] + height;
                    }
                    Animation anim = new Animation(500, valueHeader.isOptionOpen(), Easing.CUBIC_OUT);
                    Color bgColor = new Color(65, 64, 68, 181);
                    Color trueColor = new Color(126, 183, 247);
                    Color falseColor = new Color(204, 204, 204);
                    BooleanAssembly booleanAssembly = new BooleanAssembly(pos.clone(), valuesEditZoneWindow, valueHeader.isOptionOpen(), anim, bgColor, trueColor, falseColor);
                    valuesEditZoneWindow.addAssembly(booleanAssembly);
                    yUsedValueBox = 2 * uMargin + height;
                    break;
                case ENUM_TYPE:

                    break;
                case DOUBLE:

                    break;
                case STRING:

                    break;
                case COLOR:
                    width = 0.24310979692033028341887971434948f * valuesEditZoneWindow.deltaX;
                    height = width * 1.2f;
                    rSpacing = 0.02506415978576210667261771925909f * valuesEditZoneWindow.deltaX;
                    pos[0] = -rSpacing - width + valuesEditZoneWindow.deltaX / (inLeftHalfZone ? 2f : 1f);
                    pos[1] = valuesEditZoneWindow.currentUsedHeight;
                    pos[2] = pos[0] + width;
                    pos[3] = pos[1] + height;
                    Color color = valueHeader.getColorValue();
                    float[] hsbAlpha = new float[4];
                    Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsbAlpha);
                    hsbAlpha[3] = color.getAlpha() / 255f;
                    ColorAssembly colorAssembly = new ColorAssembly(pos.clone(), valuesEditZoneWindow, hsbAlpha[0], hsbAlpha[1], hsbAlpha[2], hsbAlpha[3]);
                    valuesEditZoneWindow.addAssembly(colorAssembly);
                    break;
            }
            //valueName显示组件
            StringWithoutBGAssembly valueNameAssembly = new StringWithoutBGAssembly(calcPosForValueName(width_default, height_default, valuesEditZoneWindow, lMargin, inLeftHalfZone), valuesEditZoneWindow, valueHeader.getValueName(), Main.fontLoader.EN22, Color.red, new boolean[]{false, true});
            yUsedValueName = valueNameAssembly.draw();
            valuesEditZoneWindow.addAssembly(valueNameAssembly);
            valuesEditZoneWindow.currentUsedHeight += yUsedValueName > yUsedValueBox ? yUsedValueName : yUsedValueBox;
        }
        needInit = false;
    }

    @Override
    public void addAssembly(Assembly assembly) {
        this.currentUsedHeight += assembly.draw();
        super.addAssembly(assembly);
    }

    public void setModule(ModuleHeader module) {
        reset();
        this.module = module;
        needInit = true;
    }
}
