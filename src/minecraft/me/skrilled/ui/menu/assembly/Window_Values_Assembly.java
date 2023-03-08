package me.skrilled.ui.menu.assembly;

import me.fontloader.FontDrawer;
import me.skrilled.api.modules.ModuleHeader;
import me.skrilled.api.value.ValueHeader;
import me.skrilled.ui.menu.assembly.bgType.BackGroundType;
import me.surge.animation.Animation;
import me.surge.animation.Easing;
import net.minecraft.client.main.Main;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;

public class Window_Values_Assembly extends WindowAssembly {
    final static float width_default = 0.89265788886409283642044186565498f;
    final static float height_default = 0.1086450411299084277510476486109f;
    final static float lMargin = 0.05766012050881499665253291675966f;
    final static float rMargin = 0.02825820129435393885293461280964f;
    final static float uMargin = 0.04697604635521754876092917378033f;
    final static float dMargin = 0.02883006025440749832626645837983f;
    final static float rSpacing = 0.08134345012274045971881276500782f;
    final static float uSpacing = 0.02077189714936106368668839567489f;
    public ArrayList<IconAssembly> icons = new ArrayList<>();
    ModuleHeader module;
    boolean needInit = true;
    int indexOfValues = 0;
    WindowAssembly valueEditZoneL;
    WindowAssembly valueEditZoneR;

    public Window_Values_Assembly(float[] pos, WindowAssembly fatherWindow, ModuleHeader module, String assemblyName) {
        super(pos, fatherWindow, assemblyName);
        this.module = module;
    }

    public static float[] calcPosForValueName(float width, float height, WindowAssembly fatherWindow, float lMargin) {
        return new float[]{lMargin, fatherWindow.currentUsedHeight / fatherWindow.deltaY(), lMargin + width, fatherWindow.currentUsedHeight / fatherWindow.deltaY() + height};
    }

    @Override
    public float draw() {
        if (needInit) {
            init();
        }
        return super.draw();
    }

    @Override
    public void reset() {
        super.reset();
        currentUsedHeight = 0f;
        indexOfValues = 0;
    }

    public Assembly initValueBox(ValueHeader valueHeader, WindowAssembly valuesEditZoneWindow) {
        float[] pos = new float[4];
        Assembly result = null;

        switch (valueHeader.getValueType()) {
            case BOOLEAN: {
                //以下大家一样(除了宽高计算系数)
                float width = 2 * 0.09649073867440303503682213791564f;
                float height = 0.06710124683118630037767085726111f;
                calcPos(pos, valuesEditZoneWindow, width, height, rSpacing, uSpacing);

                Animation anim = new Animation(500, valueHeader.isOptionOpen(), Easing.CUBIC_OUT);
                Color bgColor = new Color(65, 64, 68, 181);
                Color trueColor = new Color(126, 183, 247);
                Color falseColor = new Color(204, 204, 204);
                String valueInfo = module.toString() + "." + valueHeader.getValueName();
                BooleanAssembly booleanAssembly = new BooleanAssembly(pos.clone(), valuesEditZoneWindow, valueHeader.isOptionOpen(), anim, bgColor, trueColor, falseColor, valueInfo);
                result = booleanAssembly;
                break;
            }
            case ENUM_TYPE: {
                float width = 2 * 0.13659060477571970542289667484936f;
                float height = 0.2550313001189921879041854208702f;
                calcPos(pos, valuesEditZoneWindow, width, height, rSpacing, uSpacing);
                FontDrawer font = Main.fontLoader.EN16;
                Color bgColor = new Color(82, 82, 89);
                Color fontColor = Color.white;
                ArrayList<String> enumValues = valueHeader.getEnumTypes();
                String currentValue = valueHeader.getCurrentEnumType();
                Animation animation = new Animation(500f, false, Easing.LINEAR);
                EnumAssembly enumAssembly = new EnumAssembly(pos.clone(), valuesEditZoneWindow, font, bgColor, fontColor, enumValues, currentValue, animation);
                enumAssembly.assemblyName = "enumAssembly." + module.toString() + "." + valueHeader.getValueName();
                result = enumAssembly;
                break;
            }
            case DOUBLE: {
                float width = 2 * 0.20942925686230752064271367998215f;
                float height = 0.04027626881887319571628123544932f;
                calcPos(pos, valuesEditZoneWindow, width, height, rSpacing, (uSpacing * 1.5f));
                double[] doubles = valueHeader.getDoubles();
                Animation animation = new Animation(100, false, Easing.LINEAR);
                Color bgColor = new Color(255, 255, 255, 61);
                Color ugColor = new Color(204, 204, 204, 255);
                Color buttonColor = new Color(126, 183, 247, 255);
                NumberAssembly numberAssembly = new NumberAssembly(pos.clone(), valuesEditZoneWindow, doubles.clone(), animation, bgColor, ugColor, buttonColor);
                numberAssembly.assemblyName = module.toString() + "." + valueHeader.getValueName();
                result = numberAssembly;
                break;
            }
            case STRING: {
                float width = 2 * 0.24942925686230752064271367998215f;
                float height = 0.0569093072585234621553106730819f;
                calcPos(pos, valuesEditZoneWindow, width, height, rSpacing, uSpacing);
                Color bgColorOut = new Color(0, 0, 0, 64);
                Color bgColorIn = new Color(0, 0, 0, 128);
                Color fontColor = new Color(255, 189, 189, 254);
                FontDrawer font = Main.fontLoader.EN16;
                KeyTypeStringAssembly keyTypeStringAssembly = new KeyTypeStringAssembly(pos.clone(), valuesEditZoneWindow, valueHeader.getStrValue(), new boolean[]{true, true}, bgColorOut, bgColorIn, fontColor, false, font);
                keyTypeStringAssembly.assemblyName = module.toString() + "." + valueHeader.getValueName();
                result = keyTypeStringAssembly;
                break;
            }
            case COLOR: {
                float width = 2 * 0.24310979692033028341887971434948f;
                float height = width * valuesEditZoneWindow.deltaX() * 1.2f / valuesEditZoneWindow.deltaY();
                calcPos(pos, valuesEditZoneWindow, width, height, rSpacing, uSpacing);
                Color color = valueHeader.getColorValue();
                float[] hsbAlpha = new float[4];
                Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsbAlpha);
                hsbAlpha[3] = color.getAlpha() / 255f;
                ColorAssembly colorAssembly = new ColorAssembly(pos.clone(), valuesEditZoneWindow, hsbAlpha[0], hsbAlpha[1], hsbAlpha[2], hsbAlpha[3]);
                colorAssembly.assemblyName = module.toString() + "." + valueHeader.getValueName();
                result = colorAssembly;
                break;
            }
        }
        return result;
    }

    public void init() {
        //valueName将不计入高度计算
        //高度计算的变化将是max(valueName,valueBox)
        //将分左右边进行绘制
        //左右的区别：组件名末尾


        this.reset();
        //初始化背景，标题
        BGAssembly thisBG = new BGAssembly(new float[]{0, 0, 1, 1}, this, new Color(204, 204, 204, 82), BackGroundType.RoundRect, false, 8.23f);
        this.addAssembly(thisBG);
        StringAssembly valuesTitleAssembly = new StringAssembly(new float[]{0, 0, 1, 0.1125573477818604554607543957983f}, this, this.module.toString(), new boolean[]{true, false}, new Color(255, 255, 255, 61), Color.white, Main.fontLoader.EN24, 8.28f);
        this.addAssembly(valuesTitleAssembly);
        //初始化绑定键位组件
        StringAssembly bindAssembly = new StringAssembly(new float[]{0.43473680836058216323651984355966f, 0.06273484046417969317639243528264f, 0.56526319163941783676348015644034f, 0.1125573477818604554607543957983f}, this, "Bind:" + (module.getKey() == 0 ? "N/A" : Keyboard.getKeyName(module.getKey())), new boolean[]{true, true}, new Color(200, 200, 200, 160), Color.black, Main.fontLoader.EN12, 5f);
        bindAssembly.assemblyName = "bindAssembly" + "." + module.toString();
        this.addAssembly(bindAssembly);
        //初始化ArrayList显示与否
        StringAssembly renderedInArrayListAssembly = new StringAssembly(new float[]{0.93947554016798102199140860421876f, 0, 0.9809450535359363980252612681926f, 0.07414377945236760706649228789105f}, this, module.isCanView() ? "J" : "K", new boolean[]{true, true}, new Color(255, 255, 255, 0), Color.BLACK, Main.fontLoader.ICON64, 5f);
        renderedInArrayListAssembly.assemblyName = "renderedInArrayListAssembly" + "." + module.toString();
        this.addAssembly(renderedInArrayListAssembly);
        ArrayList<ValueHeader> values = module.getValueList();

        //初始化module的values组件

        float[] valuesEditZoneWindowPos = {0.04032185676732705007373212797333f, 0.14363413672126383093562516866995f, 0.95967814323267294992626787202667f, 0.94615017334080670943098544767599f};
        Window_MouseWheel_Assembly valueEditZone = new Window_MouseWheel_Assembly<>(valuesEditZoneWindowPos, this, "valueEditZone_MouseWheelWindow", 1, (valuesEditZoneWindowPos[3] - valuesEditZoneWindowPos[1]) * this.deltaY(), 1);
        this.addWindow(valueEditZone);

        valueEditZoneL = new WindowAssembly(new float[]{0f, 0f, 0.5f, 1f}, valueEditZone, "valueEditZoneL");
        valueEditZoneR = new WindowAssembly(new float[]{0.5f, 0f, 1f, 1f}, valueEditZone, "valueEditZoneR");

        valueEditZone.addWindow(valueEditZoneL);
        valueEditZone.addWindow(valueEditZoneR);

        FontDrawer valueNameFont = Main.fontLoader.EN22;
        while (indexOfValues < values.size()) {
            ValueHeader value = values.get(indexOfValues);

            WindowAssembly targetWindow = valueEditZone.subWindows.stream().min(Comparator.comparing(WindowAssembly::getCurrentUsedHeight)).get();

            //valueName
            StringWithoutBGAssembly valueName = new StringWithoutBGAssembly(calcPosForValueName(width_default, height_default, targetWindow, lMargin), targetWindow, value.getValueName(), valueNameFont, Color.white, new boolean[]{false, true});
            targetWindow.addAssembly(valueName);

            //valueBox
            Assembly valueBox = initValueBox(value, targetWindow);
            if (valueBox instanceof WindowAssembly) targetWindow.addWindow((WindowAssembly) valueBox);
            else targetWindow.addAssembly(valueBox);


            //targetWindow的currentUsedHeight处理
            targetWindow.currentUsedHeight += Math.max(valueName.deltaY(), valueBox.deltaY()) + uMargin * targetWindow.deltaY();

            indexOfValues++;
        }
        valueEditZoneL.currentUsedHeight += dMargin;
        valueEditZoneR.currentUsedHeight += dMargin;

        valueEditZone.pages = valueEditZone.subWindows.stream().max(Comparator.comparing(WindowAssembly::getCurrentUsedHeight)).get().getCurrentUsedHeight() / valueEditZone.deltaY();


        //添加背景
        this.addAssembly(new BGAssembly(valuesEditZoneWindowPos, this, new Color(161, 161, 161, 64), BackGroundType.RoundRect, false, 9.2f));
        //添加分割线组件
        float[] linePos = {0.49958211077846149900621914470732f, 0.16248365198978638599987544373171f, 0.50041788922153850099378085529268f, 0.89856967885242158144941977538353f};
        BGAssembly valuesEditZoneLine = new BGAssembly(linePos, this, new Color(204, 204, 204), BackGroundType.Rect);
        this.addAssembly(valuesEditZoneLine);

        needInit = false;
    }

    @Override
    public void updateRenderPos() {
        super.updateRenderPos();
        init();
    }

    private void calcPos(float[] pos, WindowAssembly fatherWindow, float width, float height, float rSpacing, float uSpacing) {
        pos[0] = 1 - rSpacing - width;
        pos[1] = fatherWindow.currentUsedHeight / fatherWindow.deltaY() + uSpacing;
        pos[2] = pos[0] + width;
        pos[3] = pos[1] + height;
    }

    public void setModule(ModuleHeader module) {
        reset();
        this.module = module;
        needInit = true;
    }
}
