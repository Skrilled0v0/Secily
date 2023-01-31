package me.skrilled.ui.menu.assembly;

import me.fontloader.FontDrawer;
import me.skrilled.utils.render.RenderUtil;
import me.surge.animation.Animation;

import java.awt.*;

public class IconAssembly extends Assembly {
    FontDrawer font;
    String[] icons;
    float spacing;
    Animation anim;
    float aimingPos, currentPos;
    Color iconColor;
    Color bgColor;
    Color currentColor;
    boolean isTransverse;

    private IconAssembly(float[] pos, Assembly fatherWindow) {
        super(pos, fatherWindow);
    }

    public IconAssembly(float[] pos, Assembly fatherWindow, FontDrawer font, String[] icons, float spacing, Animation anim, Color iconColor, Color bgColor, Color currentColor, boolean isTransverse) {
        super(pos, fatherWindow);
        this.font = font;
        this.icons = icons;
        this.spacing = spacing;
        this.anim = anim;
        this.iconColor = iconColor;
        this.bgColor = bgColor;
        this.currentColor = currentColor;
        this.isTransverse = isTransverse;
        aimingPos = 1;
        currentPos = 1;
    }

    @Override
    public void draw() {
        float absX = calcAbsX(), absY = calcAbsY();
        RenderUtil.drawTitleIcon(font, absX, absY, icons, spacing, (float) (currentPos + (aimingPos - currentPos) * anim.getAnimationFactor()), iconColor.getRGB(), bgColor.getRGB(), currentColor.getRGB(), isTransverse);
    }

    @Override
    public void MouseClicked(int mouseX,int mouseY,int button) {

    }

    public void SetAimingIndex(int i) {
        currentPos = (float) (currentPos + (aimingPos - currentPos) * anim.getAnimationFactor());
        if (i < 1 || i > icons.length) throw new IndexOutOfBoundsException("最小1，最大icons的数目！");//设置值越界
        aimingPos = i;
        anim.resetToDefault();
        anim.setState(true);
    }
}
