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
    public IconAssembly pageBar;
    public ArrayList<IconAssembly> icons = new ArrayList<>();
    public ArrayList<WindowAssembly> subWindows = new ArrayList<>();
    WindowAssembly valuesEditZoneWindow;
    ArrayList<WindowAssembly> valuesEditZoneWindows = new ArrayList<>();
    ModuleHeader module;
    boolean needInit = true;
    int page = 1;
    int lastEndIndexOfValues = 0;

    public Window_Values_Assembly(float[] pos, WindowAssembly fatherWindow, ModuleHeader module) {
        super(pos, fatherWindow);
        this.module = module;
    }

    public static float[] calcPosForValueName(float width, float height, WindowAssembly fatherWindow, float lMargin, boolean inLeftHalfZone) {
        return new float[]{(inLeftHalfZone ? 0 : fatherWindow.deltaX() / 2f) + lMargin, fatherWindow.currentUsedHeight, lMargin + width + (inLeftHalfZone ? 0 : fatherWindow.deltaX() / 2f), fatherWindow.currentUsedHeight + height};
    }

    public static float[] processYUsed(float yUsed, float[] pos) {
        pos[1] += yUsed;
        pos[3] += yUsed;
        return pos;
    }

    public WindowAssembly initWindowForNextPage(ArrayList<ValueHeader> values) {

        //计算并添加编辑区窗口
        float[] valuesEditZoneWindowPos = {this.deltaX() * 0.04032185676732705007373212797333f, this.deltaY() * 0.14363413672126383093562516866995f, this.deltaX() * 0.95967814323267294992626787202667f, this.deltaY() * 0.94615017334080670943098544767599f};
        WindowAssembly valuesEditZoneWindow = new WindowAssembly(valuesEditZoneWindowPos, this);
        valuesEditZoneWindow.assemblyName = "valuesEditZoneWindow";
//        this.addWindow(valuesEditZoneWindow);//TODO:draw时选择windows列中window draw
        //添加编辑区背景
        BGAssembly valuesEditZoneBG = new BGAssembly(new float[]{0, 0, valuesEditZoneWindow.deltaX(), valuesEditZoneWindow.deltaY()}, valuesEditZoneWindow, new Color(161, 161, 161, 64), BackGroundType.RoundRect, false, 9.2f);
        valuesEditZoneWindow.addAssembly(valuesEditZoneBG);
        //计算，初始化并添加分割线组件
        float[] linePos = {valuesEditZoneWindow.deltaX() / 2f - 0.5f, valuesEditZoneWindow.deltaY() * 0.02348802317760877438046458689017f, valuesEditZoneWindow.deltaX() / 2f + 0.5f, valuesEditZoneWindow.deltaY() * 0.94071084898339282942728542604377f};
        BGAssembly valuesEditZoneLine = new BGAssembly(linePos, valuesEditZoneWindow, new Color(204, 204, 204), BackGroundType.Rect);
        valuesEditZoneWindow.addAssembly(valuesEditZoneLine);

        //开始排版values,到valuesEditZoneWindow
        float lMargin = 0.02883006025440749832626645837983f * valuesEditZoneWindow.deltaX();
        float rMargin = 0.01412910064717696942646730640482f * valuesEditZoneWindow.deltaX();
        float uMargin = 0.04697604635521754876092917378033f * valuesEditZoneWindow.deltaY();
        float dMargin = 0.02883006025440749832626645837983f * valuesEditZoneWindow.deltaY();

        boolean inLeftHalfZone = true;
        //一般value(name+盒)大小
        float width_default = 0.44632894443204641821022093282749f * valuesEditZoneWindow.deltaX();
        float height_default = 0.1086450411299084277510476486109f * valuesEditZoneWindow.deltaY();
        //留出上边距,翻到右半边的时候重置
        valuesEditZoneWindow.currentUsedHeight = uMargin;
        //定义ValueName显示字体
        FontDrawer valueNameFont = Main.fontLoader.EN22;
        while (lastEndIndexOfValues < values.size()) {
            float[] pos = {0, 0, 0, 0};
            ValueHeader valueHeader = values.get(lastEndIndexOfValues);
            float yUsedValueBox = 0f;
            float yUsedValueName;
            //value调控组件
            float rSpacing = 0.04067172506137022985940638250391f * valuesEditZoneWindow.deltaX();
            float uSpacing = 0.02077189714936106368668839567489f * valuesEditZoneWindow.deltaY();
            switch (valueHeader.getValueType()) {
                case BOOLEAN: {
                    //以下大家一样(除了宽高计算系数)
                    float width = 0.09649073867440303503682213791564f * valuesEditZoneWindow.deltaX();
                    float height = 0.06710124683118630037767085726111f * valuesEditZoneWindow.deltaY();
                    calcPos(pos, valuesEditZoneWindow, inLeftHalfZone, width, height, rSpacing, uSpacing);
                    if (pos[3] + dMargin > valuesEditZoneWindow.deltaY()) {
                        valuesEditZoneWindow.currentUsedHeight = uMargin;
                        inLeftHalfZone = !inLeftHalfZone;
                        if (inLeftHalfZone) {
                            return valuesEditZoneWindow;
                        }
                        calcPos(pos, valuesEditZoneWindow, false, width, height, rSpacing, uSpacing);
                    }
                    //以上以下大家一样
                    Animation anim = new Animation(500, valueHeader.isOptionOpen(), Easing.CUBIC_OUT);
                    Color bgColor = new Color(65, 64, 68, 181);
                    Color trueColor = new Color(126, 183, 247);
                    Color falseColor = new Color(204, 204, 204);
                    String valueInfo = module.toString() + "." + valueHeader.getValueName();
                    BooleanAssembly booleanAssembly = new BooleanAssembly(pos.clone(), valuesEditZoneWindow, valueHeader.isOptionOpen(), anim, bgColor, trueColor, falseColor, valueInfo);
                    valuesEditZoneWindow.addAssembly(booleanAssembly);
                    yUsedValueBox = uMargin + height;
                    break;
                }
                case ENUM_TYPE: {
                    float width = 0.13659060477571970542289667484936f * valuesEditZoneWindow.deltaX();
                    float height = 0.2550313001189921879041854208702f * valuesEditZoneWindow.deltaY();
                    calcPos(pos, valuesEditZoneWindow, inLeftHalfZone, width, height, rSpacing, uSpacing);
                    if (pos[3] + dMargin > valuesEditZoneWindow.deltaY()) {
                        valuesEditZoneWindow.currentUsedHeight = uMargin;
                        inLeftHalfZone = !inLeftHalfZone;
                        if (inLeftHalfZone) {
                            return valuesEditZoneWindow;
                        }
                        calcPos(pos, valuesEditZoneWindow, false, width, height, rSpacing, uSpacing);
                    }
                    FontDrawer font = Main.fontLoader.EN16;
                    Color bgColor = new Color(82, 82, 89);
                    Color fontColor = Color.white;
                    ArrayList<String> enumValues = valueHeader.getEnumTypes();
                    String currentValue = valueHeader.getCurrentEnumType();
                    Animation animation = new Animation(500f, false, Easing.LINEAR);
                    EnumAssembly enumAssembly = new EnumAssembly(pos.clone(), valuesEditZoneWindow, font, bgColor, fontColor, enumValues, currentValue, animation);
                    enumAssembly.assemblyName = "enumAssembly." + module.toString() + "." + valueHeader.getValueName();
                    valuesEditZoneWindow.addWindow(enumAssembly);
                    yUsedValueBox = uMargin + height;
                    break;
                }
                case DOUBLE: {
                    float width = 0.20942925686230752064271367998215f * valuesEditZoneWindow.deltaX();
                    float height = 0.04027626881887319571628123544932f * valuesEditZoneWindow.deltaY();
                    calcPos(pos, valuesEditZoneWindow, inLeftHalfZone, width, height, rSpacing, (uSpacing * 1.5f));
                    if (pos[3] + dMargin > valuesEditZoneWindow.deltaY()) {
                        valuesEditZoneWindow.currentUsedHeight = uMargin;
                        inLeftHalfZone = !inLeftHalfZone;
                        if (inLeftHalfZone) {
                            return valuesEditZoneWindow;
                        }
                        calcPos(pos, valuesEditZoneWindow, false, width, height, rSpacing, (uSpacing * 2f));
                    }
                    double[] doubles = valueHeader.getDoubles();
                    Animation animation = new Animation(100, false, Easing.LINEAR);
                    Color bgColor = new Color(255, 255, 255, 61);
                    Color ugColor = new Color(204, 204, 204, 255);
                    Color buttonColor = new Color(126, 183, 247, 255);
                    NumberAssembly numberAssembly = new NumberAssembly(pos.clone(), valuesEditZoneWindow, doubles.clone(), animation, bgColor, ugColor, buttonColor);
                    numberAssembly.assemblyName = module.toString() + "." + valueHeader.getValueName();
                    valuesEditZoneWindow.addAssembly(numberAssembly);
                    yUsedValueBox = uMargin + height;
                    break;
                }
                case STRING: {
                    float width = 0;
                    break;
                }
                case COLOR: {
                    float width = 0.24310979692033028341887971434948f * valuesEditZoneWindow.deltaX();
                    float height = width * 1.2f;
                    calcPos(pos, valuesEditZoneWindow, inLeftHalfZone, width, height, rSpacing, uSpacing);
                    if (pos[3] + dMargin > valuesEditZoneWindow.deltaY()) {
                        valuesEditZoneWindow.currentUsedHeight = uMargin;
                        inLeftHalfZone = !inLeftHalfZone;
                        if (inLeftHalfZone) {
                            return valuesEditZoneWindow;
                        }
                        calcPos(pos, valuesEditZoneWindow, false, width, height, rSpacing, uSpacing);
                    }
                    Color color = valueHeader.getColorValue();
                    float[] hsbAlpha = new float[4];
                    Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsbAlpha);
                    hsbAlpha[3] = color.getAlpha() / 255f;
                    ColorAssembly colorAssembly = new ColorAssembly(pos.clone(), valuesEditZoneWindow, hsbAlpha[0], hsbAlpha[1], hsbAlpha[2], hsbAlpha[3]);
                    colorAssembly.assemblyName = module.toString() + "." + valueHeader.getValueName();
                    valuesEditZoneWindow.addAssembly(colorAssembly);
                    yUsedValueBox = colorAssembly.deltaY() + uMargin;
                    break;
                }
            }
            //valueName显示组件
            StringWithoutBGAssembly valueNameAssembly = new StringWithoutBGAssembly(calcPosForValueName(width_default, height_default, valuesEditZoneWindow, lMargin, inLeftHalfZone), valuesEditZoneWindow, valueHeader.getValueName(), valueNameFont, Color.white, new boolean[]{false, true});
            yUsedValueName = valueNameAssembly.draw();
            valuesEditZoneWindow.addAssembly(valueNameAssembly);
            valuesEditZoneWindow.currentUsedHeight += Math.max(yUsedValueName, yUsedValueBox);
            lastEndIndexOfValues++;
        }
        return valuesEditZoneWindow;
    }

    @Override
    public float draw() {
        if (needInit) {
            init();
            return deltaY();
        }
        if (page > valuesEditZoneWindows.size() + 1) page = valuesEditZoneWindows.size() + 1;
        this.valuesEditZoneWindow = valuesEditZoneWindows.get(page - 1);
        if (!subWindows.contains(valuesEditZoneWindow)) {
            subWindows.clear();
            subWindows.add(valuesEditZoneWindow);
        }
        super.draw();
        valuesEditZoneWindow.draw();
        return deltaY();
    }

    public void init() {
        currentUsedHeight = 0f;
        lastEndIndexOfValues = 0;
        //初始化背景，标题
        BGAssembly thisBG = new BGAssembly(new float[]{0, 0, this.deltaX(), this.deltaY()}, this, new Color(204, 204, 204, 82), BackGroundType.RoundRect, false, 8.23f);
        this.addAssembly(thisBG);
        StringAssembly valuesTitleAssembly = new StringAssembly(new float[]{0, 0, this.deltaX(), this.deltaY() * 0.09516098897677025596313134458492f}, this, this.module.toString(), true, new Color(255, 255, 255, 61), Color.white, Main.fontLoader.EN22, 8.28f);
        this.addAssembly(valuesTitleAssembly);
        ArrayList<ValueHeader> values = module.getValueList();
        valuesEditZoneWindows = new ArrayList<>();

        while (lastEndIndexOfValues < values.size()) {
            valuesEditZoneWindows.add(initWindowForNextPage(values));
        }
        if (valuesEditZoneWindows.size() == 0)
            valuesEditZoneWindows.add(new WindowAssembly(new float[]{0, 0, 0, 0}, this));
        FontDrawer font = Main.fontLoader.EN24;
        float halfHeight = font.getHeight() * 0.5f;
        float a = valuesEditZoneWindows.size() - 0.5f;
        float[] pageBarPos = {deltaX() / 2 - 2 * a * halfHeight, 0.99746735587801789458387826700712f * deltaY() - 2 * halfHeight, deltaX() / 2 + 2 * a * halfHeight, 0.99746735587801789458387826700712f * deltaY()};
        ArrayList<String> pageNumberList = new ArrayList<>();
        for (int i = 0; i < valuesEditZoneWindows.size(); i++) {
            pageNumberList.add(Integer.toString(i + 1));
        }
        String[] pageNumbers = new String[pageNumberList.size()];
        for (int i = 0; i < pageNumbers.length; i++) {
            pageNumbers[i] = pageNumberList.get(i);
        }
        Color bgColor = new Color(23, 23, 23, 59);
        Color selectedColor = new Color(204, 204, 204);
        Color fontColor = Color.white;
        pageBar = new IconAssembly(pageBarPos, this, font, pageNumbers, Integer.toString(page), 1.5f, new Animation(400, false, Easing.LINEAR), fontColor, bgColor, selectedColor, true);
        pageBar.assemblyName = "pageBar";
        this.addAssembly(pageBar);
        needInit = false;
    }

    private void calcPos(float[] pos, WindowAssembly fatherWindow, boolean inLeftHalfZone, float width, float height, float rSpacing, float uSpacing) {
        pos[0] = -rSpacing - width + fatherWindow.deltaX() / (inLeftHalfZone ? 2f : 1f);
        pos[1] = fatherWindow.currentUsedHeight + uSpacing;
        pos[2] = pos[0] + width;
        pos[3] = pos[1] + height;
    }

    @Override
    public void addAssembly(Assembly assembly) {
        this.currentUsedHeight += assembly.draw();
        super.addAssembly(assembly);
    }

    public void setModule(ModuleHeader module) {
        reset();
        this.module = module;
        page = 1;
        needInit = true;
    }

    @Override
    public void reset() {
        super.reset();
    }

    @Override
    public ArrayList<Assembly> getAssembliesByMousePos(int mouseX, int mouseY) {
        ArrayList<Assembly> result = (ArrayList<Assembly>) (valuesEditZoneWindow.getAssembliesByMousePos(mouseX, mouseY).clone());
        for (Assembly assemblyByMousePos : super.getAssembliesByMousePos(mouseX, mouseY)) {
            if (!result.contains(assemblyByMousePos)) {
                result.add(assemblyByMousePos);
            }
        }
        return result;
    }

    @Override
    public ArrayList<Assembly> getAssembliesCanDrag() {
        ArrayList<Assembly> result = new ArrayList<>();
        if (valuesEditZoneWindows.size() > 0) {
            for (Assembly assembly : this.valuesEditZoneWindows.get(page - 1).assemblies) {
                if (assembly.canDrag) result.add(assembly);
            }
            for (WindowAssembly windowAssembly : this.valuesEditZoneWindows.get(page - 1).subWindows) {
                for (Assembly assembly : windowAssembly.getAssembliesCanDrag()) {
                    result.add(assembly);
                }
            }
        }
        for (Assembly assembly : super.getAssembliesCanDrag()) {
            result.add(assembly);
        }
        return result;
    }
}
