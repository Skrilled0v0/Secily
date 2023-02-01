package me.skrilled.ui.menu.assembly;

import me.fontloader.FontDrawer;
import me.skrilled.SenseHeader;
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

    private IconAssembly(float[] pos, Assembly fatherWindow) {
        super(pos, fatherWindow);
    }

    public IconAssembly(float[] pos, Assembly fatherWindow, FontDrawer font, char[] icons, float spacing, Animation anim, Color iconColor, Color bgColor, Color currentColor, boolean isTransverse) {
        super(pos, fatherWindow);
        this.font = font;
        this.icons = icons;
        this.spacing = spacing;
        this.anim = anim;
        this.iconColor = iconColor;
        this.bgColor = bgColor;
        this.currentColor = currentColor;
        this.isTransverse = isTransverse;
        SenseHeader.getSense.printINFO("animInitialFactor:" + anim.getAnimationFactor());
        aimingPos = 1;
        currentPos = 1;
        boxWidth = font.getCharWidth(icons[0]) * 1.2f;
    }

    @Override
    public void draw() {
        float absX = calcAbsX(), absY = calcAbsY();
        RenderUtil.drawTitleIcon(font, absX, absY, icons, spacing, (float) (currentPos + (aimingPos - currentPos) * anim.getAnimationFactor()), iconColor.getRGB(), bgColor.getRGB(), currentColor.getRGB(), isTransverse);
        if (anim.getAnimationFactor() == 1D) {
            currentPos = aimingPos;
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
//        SenseHeader.getSense.printINFO("MouseClicked in IconAssembly");
        float absX = calcAbsX(), absY = calcAbsY();
        int count = 0;
        for (char icon : icons) {
            float[] pos = new float[]{absX + (spacing + boxWidth) * count, absY, absX + boxWidth + (spacing + boxWidth) * count, absY + boxWidth};
            count++;
            if (isMouseInside(mouseX, mouseY, pos[0], pos[1], pos[2], pos[3])) {
                if (anim.getAnimationFactor() < 1D) {
                    currentPos += (aimingPos - currentPos) * anim.getAnimationFactor();
                }
                aimingPos = count;
                Animation tempAnim = new Animation(anim.length, anim.initialState, Easing.LINEAR);
                anim = tempAnim;
//                SenseHeader.getSense.printINFO(anim.getAnimationFactor());
                anim.setState(true);
            }
        }
    }

    public void setAimingIndex(int i) {
        currentPos = (float) (currentPos + (aimingPos - currentPos) * anim.getAnimationFactor());
        if (i < 1 || i > icons.length) throw new IndexOutOfBoundsException("最小1，最大icons的数目！");//设置值越界
        aimingPos = i;
        anim.resetToDefault();
        anim.setState(true);
    }
}
