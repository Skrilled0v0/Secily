/*
 *MCP-919-main
 *Code by SkrilledSense
 *20230126
 */
package me.skrilled.ui.menu.assembly;

import me.fontloader.FontDrawer;
import me.skrilled.SenseHeader;
import me.skrilled.api.modules.ModuleHeader;
import me.skrilled.api.value.ValueHeader;
import me.skrilled.ui.menu.assembly.bgType.BackGroundType;
import me.skrilled.utils.render.RenderUtil;
import me.skrilled.utils.render.ScissorPos;
import me.surge.animation.Animation;
import me.surge.animation.Easing;
import net.minecraft.client.main.Main;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class EnumAssembly extends WindowAssembly {


    FontDrawer font;
    Color bgColor;
    Color fontColor;
    ArrayList<String> allChoice;
    ArrayList<String> restChoice;
    String currentValue;
    Animation animation;
    float udMargin;
    boolean animationedValueCalc = false;


    public EnumAssembly(float[] pos, WindowAssembly fatherWindow, FontDrawer font, Color bgColor, Color fontColor, ArrayList<String> contents, String currentValue, Animation animation) {
        super(pos, fatherWindow, "enumAssembly");
        this.font = font;
        this.bgColor = bgColor;
        this.fontColor = fontColor;
        this.currentValue = currentValue;
        this.allChoice = contents;
        this.animation = animation;
        restChoice = getRestContents(contents, currentValue);

        udMargin = (0.3333f - font.getHeight() * 0.3333f / deltaY()) / 2;
        initSubWindow(font);
    }

    private static void setWindow_Wheel_AssemblySkipAim(float skipAimOnFolding, Window_MouseWheel_Assembly window) {
        window.currentSkip = window.getSkipFactor();
        window.skipAim = skipAimOnFolding;
        window.animation = new Animation(window.animation.length, window.animation.initialState, Easing.LINEAR);
        window.animation.setState(true);
    }

    @Override
    public void updateRenderPos() {
        super.updateRenderPos();
        Window_MouseWheel_Assembly enumWindow = (Window_MouseWheel_Assembly) subWindows.get(0);
        enumWindow.heightOfOnePage = font.getHeight() + udMargin * deltaY();
    }

    private ArrayList<String> getRestContents(ArrayList<String> contents, String currentValue) {
        ArrayList<String> restChoice = (ArrayList<String>) contents.clone();
        restChoice.removeIf(s -> s.equalsIgnoreCase(currentValue));
        return restChoice;
    }

    private void initSubWindow(FontDrawer font) {
        this.subWindows = new ArrayList<>();
        ArrayList<String> contents = new ArrayList<>();
        contents.add(currentValue);
        for (String s : restChoice) {
            contents.add(s);
        }
        this.addWindow(new Window_MouseWheel_Assembly<>(new float[]{0, 0, 1, 1}, this, "enumWindow", contents, 3, font.getHeight() + udMargin * deltaY()));
        Window_MouseWheel_Assembly enumWindow = (Window_MouseWheel_Assembly) subWindows.get(0);
        enumWindow.addAssembly(new BGAssembly(new float[]{0, 0, 1, 1}, enumWindow, new Color(82, 82, 89), BackGroundType.RoundRect, false, 4.6f));
        float fontHeight = font.getHeight() / enumWindow.deltaY();
        float[] enumValuePos = new float[4];
        enumValuePos[0] = 0;
        enumValuePos[1] = udMargin / 2f;
        enumValuePos[2] = 0.8f;
        enumValuePos[3] = udMargin / 2f + (fontHeight + udMargin);

        if (contents.size() > 0) {
            StringWithoutBGAssembly enumValueAssembly = new StringWithoutBGAssembly(enumValuePos, enumWindow, currentValue, font, Color.white, new boolean[]{true, false});
            enumWindow.addAssembly(enumValueAssembly);
        }

        float[] downArrowValuePos = enumValuePos.clone();
        downArrowValuePos[0] = 0.8f;
        downArrowValuePos[2] = 1;
        StringWithoutBGAssembly downArrowAssembly = new StringWithoutBGAssembly(downArrowValuePos, enumWindow, "H", Main.fontLoader.ICON16, new Color(200, 200, 200), new boolean[]{true, false});
        enumWindow.addAssembly(downArrowAssembly);

        for (int i = 0; i < restChoice.size(); i++) {
            String enumValue = restChoice.get(i);
            enumValuePos = new float[4];
            enumValuePos[0] = 0f;
            enumValuePos[1] = udMargin / 2f + (i + 1) * (fontHeight + udMargin);
            enumValuePos[2] = 0.8f;
            enumValuePos[3] = udMargin / 2f + (i + 2) * (fontHeight + udMargin);
            StringWithoutBGAssembly enumValueAssembly = new StringWithoutBGAssembly(enumValuePos, enumWindow, enumValue, font, Color.white, new boolean[]{true, false});
            enumWindow.addAssembly(enumValueAssembly);
        }
    }

    @Override
    public float draw() {
        float absX = calcAbsX(), absY = calcAbsY(), deltaX = deltaX(), deltaY = deltaY();
        ScissorPos p = RenderUtil.scissors.get(RenderUtil.scissors.size() - 1);
        double x = max(absX, p.x), y = max(absY, p.y), x1 = min(absX + deltaX, p.x1), y1 = min(absY + (deltaY * (1 + 2 * animation.getAnimationFactor()) / 3f), p.y1);
        if (x1 <= x || y1 <= y) {
            return deltaY;
        }
        GL11.glPushMatrix();
        RenderUtil.doScissor((int) x, (int) y, (int) x1 + 1, (int) y1 + 1);
        subWindows.get(0).pos[3] = (float) (deltaY * (1 + 2 * animation.getAnimationFactor()) / 3f);
        subWindows.get(0).getAssembliesByClass(BGAssembly.class).get(0).pos[3] = (float) (deltaY * (1 + 2 * animation.getAnimationFactor()) / 3f);

        float result = 0f;
        result += super.draw();

        RenderUtil.deScissor();
        GL11.glPopMatrix();
        return result;
    }

    @Override
    public float getDrawHeight() {
        return (float) (deltaY() * (1 + 2 * animation.getAnimationFactor()) / 3f);
    }

    public void setDropDown(boolean flag) {
        for (Assembly assembly : subWindows.get(0).assemblies) {
            if (assembly instanceof StringWithoutBGAssembly) {
                StringWithoutBGAssembly stringWithoutBGAssembly = (StringWithoutBGAssembly) assembly;
                if (stringWithoutBGAssembly.value.equals("H") || stringWithoutBGAssembly.value.equals("I")) {
                    stringWithoutBGAssembly.value = flag ? "I" : "H";
                }
            }
        }
        Window_MouseWheel_Assembly window = ((Window_MouseWheel_Assembly) (subWindows.get(0)));
        if (!flag) {
            float skipAimOnFolding = getSkipAimOnFolding();
            setWindow_Wheel_AssemblySkipAim(skipAimOnFolding, window);
        } else {
            setWindow_Wheel_AssemblySkipAim(0f, window);
        }
        this.animation.setState(flag);
    }

    @Override
    public void mouseEventHandle(int mouseX, int mouseY, int button) {

    }

    public boolean dropped() {
        Window_MouseWheel_Assembly window_mouseWheel_assembly = (Window_MouseWheel_Assembly) (subWindows.get(0));
        for (Assembly assembly : window_mouseWheel_assembly.assemblies) {
            if (assembly instanceof StringWithoutBGAssembly) {
                if (((StringWithoutBGAssembly) assembly).value.equals("I")) {
                    return true;
                }
            }
        }
        return false;
    }

    public void setCurrentValue(String currentValue) {
        this.currentValue = currentValue;
        this.restChoice = getRestContents(allChoice, currentValue);
        String[] valueInfo = assemblyName.split("\\.");
        ModuleHeader module = SenseHeader.getSense.getModuleManager().getModuleByName(valueInfo[1]);
        ValueHeader value = module.getValueByName(valueInfo[2]);
        value.setCurrentEnumType(this.currentValue);
    }

    public float getSkipAimOnFolding() {
        Window_MouseWheel_Assembly window = (Window_MouseWheel_Assembly) (subWindows.get(0));
        for (Assembly assembly : window.assemblies) {
            if (assembly instanceof StringWithoutBGAssembly) {
                StringWithoutBGAssembly stringWithoutBGAssembly = (StringWithoutBGAssembly) assembly;
                if (stringWithoutBGAssembly.value.equals(currentValue)) {
                    return stringWithoutBGAssembly.pos[1] - (udMargin * deltaY() / 2f) - window.assemblies.get(0).pos[1];
                }
            }
        }
        return 0f;
    }
}
