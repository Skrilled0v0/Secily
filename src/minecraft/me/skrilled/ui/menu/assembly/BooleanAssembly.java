/*
 *MCP-919-main
 *Code by SkrilledSense
 *20230126
 */
package me.skrilled.ui.menu.assembly;

import me.skrilled.SenseHeader;
import me.skrilled.api.manager.ModuleManager;
import me.skrilled.api.modules.ModuleHeader;
import me.skrilled.api.value.ValueHeader;
import me.skrilled.utils.render.RenderUtil;
import me.surge.animation.Animation;

import java.awt.*;

public class BooleanAssembly extends Assembly {
    boolean value;
    Animation animation;
    Color bgColor;
    Color tureColor;
    Color falseColor;

    public BooleanAssembly(float[] pos, WindowAssembly fatherWindow, boolean value, Animation animation, Color bgColor, Color tureColor, Color falseColor, String valueInfo) {
        super(pos, fatherWindow);
        this.value = value;
        this.animation = animation;
        this.bgColor = bgColor;
        this.tureColor = tureColor;
        this.falseColor = falseColor;
        this.assemblyName = valueInfo;

        animation.lastMillis = animation.initialState ? -(long) (double) animation.length.get() : 0;
    }

    @Override
    public float draw() {
        float absX = calcAbsX(), absY = calcAbsY();
        return RenderUtil.drawBooleanButton(absX, absY, deltaX(), deltaY(), (float) animation.getAnimationFactor(), bgColor.getRGB(), tureColor.getRGB(), falseColor.getRGB());
    }

    @Override
    public float getDrawHeight() {
        return deltaY();
    }

    @Override
    public void mouseEventHandle(int mouseX, int mouseY, int button) {
        SenseHeader.getSense.getModuleManager();
        String[] valueInfo = this.assemblyName.split("\\.");
        switch (valueInfo.length) {
            case 0:
                break;
            case 1:
                ModuleHeader moduleHeader = ModuleManager.getModuleByName(valueInfo[0]);
                moduleHeader.setEnabled(!moduleHeader.isEnabled());
                animation.setState(moduleHeader.isEnabled());
                break;
            case 2:
                moduleHeader = ModuleManager.getModuleByName(valueInfo[0]);
                ValueHeader valueHeader = moduleHeader.getValueByName(valueInfo[1]);
                valueHeader.setOptionOpen(!valueHeader.isOptionOpen());
                animation.setState(valueHeader.isOptionOpen());
                break;
        }
    }

    public void SetAnimState(boolean in) {
        animation.setState(in);
    }
}
