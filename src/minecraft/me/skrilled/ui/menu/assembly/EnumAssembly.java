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
import me.surge.animation.Animation;
import me.surge.animation.Easing;
import net.minecraft.client.main.Main;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;

public class EnumAssembly extends WindowAssembly {


    FontDrawer font;
    Color bgColor;
    Color fontColor;
    ArrayList<String> allChoice;
    ArrayList<String> restChoice;
    String currentValue;
    Animation animation;
    float udMargin;


    public EnumAssembly(float[] pos, WindowAssembly fatherWindow, FontDrawer font, Color bgColor, Color fontColor, ArrayList<String> contents, String currentValue, Animation animation) {
        super(pos, fatherWindow);
        this.font = font;
        this.bgColor = bgColor;
        this.fontColor = fontColor;
        this.currentValue = currentValue;
        this.allChoice = contents;
        this.animation = animation;
        restChoice = getRestContents(contents, currentValue);
        initSubWindow(font);
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
        this.addWindow(new Window_MouseWheel_Assembly<>(new float[]{0, 0, deltaX(), deltaY()}, this, contents, 3));
        subWindows.get(0).assemblyName = "windowOfEnumAssembly";
        subWindows.get(0).bgAssembly = new BGAssembly(new float[]{0, 0, deltaX(), deltaY()}, subWindows.get(0), new Color(82, 82, 89), BackGroundType.RoundRect, false, 4.6f);
        float fontHeight = font.getHeight();
        udMargin = fontHeight * 0.9f;
        float[] enumValuePos = new float[4];
        enumValuePos[0] = 0f;
        enumValuePos[1] = udMargin / 2f;
        enumValuePos[2] = 0.8f * deltaX();
        enumValuePos[3] = udMargin / 2f + (fontHeight + udMargin);

        if (contents.size() > 0) {
            StringWithoutBGAssembly enumValueAssembly = new StringWithoutBGAssembly(enumValuePos, subWindows.get(0), currentValue, font, Color.white, new boolean[]{true, true});
            subWindows.get(0).addAssembly(enumValueAssembly);
        }

        float[] downArrowValuePos = enumValuePos.clone();
        downArrowValuePos[0] = 0.8f * deltaX();
        downArrowValuePos[2] = deltaX();
        StringWithoutBGAssembly downArrowAssembly = new StringWithoutBGAssembly(downArrowValuePos, subWindows.get(0), "H", Main.fontLoader.ICON16, new Color(200, 200, 200), new boolean[]{true, false});
        subWindows.get(0).addAssembly(downArrowAssembly);

        for (int i = 0; i < restChoice.size(); i++) {
            String enumValue = restChoice.get(i);
            enumValuePos = new float[4];
            enumValuePos[0] = 0f;
            enumValuePos[1] = udMargin / 2f + (i + 1) * (fontHeight + udMargin);
            enumValuePos[2] = 0.8f * deltaX();
            enumValuePos[3] = udMargin / 2f + (i + 2) * (fontHeight + udMargin);
            StringWithoutBGAssembly enumValueAssembly = new StringWithoutBGAssembly(enumValuePos, subWindows.get(0), enumValue, font, Color.white, new boolean[]{true, true});
            subWindows.get(0).addAssembly(enumValueAssembly);
        }
    }

    @Override
    public float draw() {
        float absX = calcAbsX(), absY = calcAbsY();
        GL11.glPushMatrix();
        RenderUtil.doScissor((int) absX, (int) absY, (int) (absX + deltaX()), (int) (absY + (deltaY() * (1 + 2 * animation.getAnimationFactor()) / 3f)));
        subWindows.get(0).pos[3] = (float) (deltaY() * (1 + 2 * animation.getAnimationFactor()) / 3f);


        float result = 0f;
        result += super.draw();


        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        GL11.glPopMatrix();
        return result;
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
        if (!flag) {
            Window_MouseWheel_Assembly window = ((Window_MouseWheel_Assembly) (subWindows.get(0)));
            float skipAimOnFolding = getSkipAimOnFolding();
            window.currentSkip = window.getSkipFactor();
            window.skipAim = skipAimOnFolding;
            window.animation = new Animation(window.animation.length, window.animation.initialState, Easing.LINEAR);
            window.animation.setState(true);
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
                    return stringWithoutBGAssembly.pos[1] - window.assemblies.get(0).pos[1];
                }
            }
        }
        return 0f;
    }
}
