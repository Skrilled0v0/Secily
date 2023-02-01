package me.skrilled.ui.menu.assembly;

import me.fontloader.FontDrawer;
import me.skrilled.SenseHeader;
import me.skrilled.api.modules.ModuleType;
import me.skrilled.ui.menu.ui.SecilyUserInterface;
import me.skrilled.utils.render.RenderUtil;
import me.surge.animation.Animation;
import me.surge.animation.Easing;

import java.awt.*;

public class IconAssembly extends Assembly {
    FontDrawer font;
    char[] icons;
    float spacing;
    volatile Animation anim;
    volatile float aimingPos, currentPos;
    Color iconColor;
    Color bgColor;
    Color currentColor;
    boolean isTransverse;
    float boxWidth;

    public IconAssembly(float[] pos, WindowAssembly fatherWindow, FontDrawer font, char[] icons, char currentIcon, float spacing, Animation anim, Color iconColor, Color bgColor, Color currentColor, boolean isTransverse) {
        super(pos, fatherWindow);
        this.font = font;
        this.icons = icons;
        this.spacing = spacing;
        this.anim = anim;
        this.iconColor = iconColor;
        this.bgColor = bgColor;
        this.currentColor = currentColor;
        this.isTransverse = isTransverse;
        for (int i = 0; i < icons.length; i++) {
            if (currentIcon == icons[i]) {
                currentPos = i + 1;
                aimingPos = i + 1;
                break;
            }
        }
        boxWidth = font.getCharWidth(icons[0]) * 1.2f;
    }

    public static ModuleType getModuleTypeByChar(char c) {
        switch (c) {
            case 'A':
                return ModuleType.COMBAT;
            case 'B':
                return ModuleType.MISC;
            case 'C':
                return ModuleType.MOVE;
            case 'D':
                return ModuleType.PLAYER;
            case 'E':
                return ModuleType.RENDER;
        }
        return null;
    }

    @Override
    public float draw() {
        float absX = calcAbsX(), absY = calcAbsY();
        if (anim.getAnimationFactor() == 1D) {
            currentPos = aimingPos;
        }
        return RenderUtil.drawTitleIcon(font, absX, absY, icons, boxWidth, spacing, (float) (currentPos + (aimingPos - currentPos) * anim.getAnimationFactor()), iconColor.getRGB(), bgColor.getRGB(), currentColor.getRGB(), isTransverse);
    }

    @Override
    public void mouseEventHandle(int mouseX, int mouseY, int button) {
        float absX = calcAbsX(), absY = calcAbsY();
        int count = 0;
        for (char icon : icons) {
            float[] pos = new float[]{absX + (spacing + boxWidth) * count, absY, absX + boxWidth + (spacing + boxWidth) * count, absY + boxWidth};
            count++;
            if (isMouseInside(mouseX, mouseY, pos[0], pos[1], pos[2], pos[3])) {
                SecilyUserInterface.currentModuleType = getModuleTypeByChar(icon);
                SecilyUserInterface.onModuleTypeSwitching = true;
                try {
                    SecilyUserInterface.currentModule = SenseHeader.getSense.getModuleManager().getModuleListByModuleType(SecilyUserInterface.currentModuleType).get(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Window_Values_Assembly valuesWindow = (Window_Values_Assembly) SecilyUserInterface.getSecilyUserInterface().mainGui.getAssemblyByName("valuesWindow");
                valuesWindow.setModule(SecilyUserInterface.currentModule);
                if (anim.getAnimationFactor() < 1D) {
                    float deltaPos = (aimingPos - currentPos);
                    currentPos += deltaPos * anim.getAnimationFactor();
                }
                aimingPos = count;
                anim = new Animation(anim.length, anim.initialState, Easing.LINEAR);
                anim.setState(true);
            }
        }
    }
}
