/*
 *MCP-919-main
 *Code by SkrilledSense
 *20230126
 */
package me.skrilled.ui.menu.assembly;

import me.fontloader.FontDrawer;
import me.skrilled.ui.menu.assembly.bgType.BackGroundType;
import me.skrilled.utils.render.RenderUtil;
import me.surge.animation.Animation;
import net.minecraft.client.main.Main;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;

public class EnumAssembly extends WindowAssembly {


    public Animation animation;
    FontDrawer font;
    Color bgColor;
    Color fontColor;
    ArrayList<String> allChoice;
    ArrayList<String> restChoice;
    String currentValue;

    public EnumAssembly(float[] pos, WindowAssembly fatherWindow, FontDrawer font, Color bgColor, Color fontColor, ArrayList<String> allEnumValues, String currentValue, Animation animation) {
        super(pos, fatherWindow);
        this.font = font;
        this.bgColor = bgColor;
        this.fontColor = fontColor;
        this.allChoice = allEnumValues;
        this.currentValue = currentValue;
        this.animation = animation;
        restChoice = (ArrayList<String>) allEnumValues.clone();
        restChoice.removeIf(s -> s.equalsIgnoreCase(currentValue));

        this.addWindow(new WindowAssembly(new float[]{0, 0, deltaX(), deltaY()}, this));
        subWindows.get(0).assemblyName = "windowOfEnumAssembly";
        subWindows.get(0).bgAssembly = new BGAssembly(new float[]{0, 0, deltaX(), deltaY()}, subWindows.get(0), new Color(82, 82, 89), BackGroundType.RoundRect, false, 4.6f);


        float fontHeight = font.getHeight();
        float udMargin = fontHeight * 0.9f;
        if (allEnumValues.size() > 0) {
            String enumValue = allEnumValues.get(0);
            float[] enumValuePos = new float[4];
            enumValuePos[0] = 0f;
            enumValuePos[1] = udMargin / 2f;
            enumValuePos[2] = 0.8f * deltaX();
            enumValuePos[3] = udMargin / 2f + (fontHeight + udMargin);
            float[] downArrowValuePos = enumValuePos.clone();
            downArrowValuePos[0] = 0.8f * deltaX();
            downArrowValuePos[2] = deltaX();
            StringWithoutBGAssembly enumValueAssembly = new StringWithoutBGAssembly(enumValuePos, subWindows.get(0), enumValue, font, Color.white, new boolean[]{true, true});
            StringWithoutBGAssembly downArrowAssembly = new StringWithoutBGAssembly(downArrowValuePos, subWindows.get(0), "H", Main.fontLoader.ICON16, new Color(200, 200, 200), new boolean[]{true, false});
            subWindows.get(0).addAssembly(enumValueAssembly);
            subWindows.get(0).addAssembly(downArrowAssembly);
        }
        for (int i = 1; i < allEnumValues.size(); i++) {
            String enumValue = allEnumValues.get(i);
            float[] enumValuePos = new float[4];
            enumValuePos[0] = 0f;
            enumValuePos[1] = udMargin / 2f + i * (fontHeight + udMargin);
            enumValuePos[2] = 0.8f * deltaX();
            enumValuePos[3] = udMargin / 2f + (i + 1) * (fontHeight + udMargin);
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
        this.animation.setState(flag);
    }

    @Override
    public void mouseEventHandle(int mouseX, int mouseY, int button) {

    }
}
