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
    }

    @Override
    public void draw() {
        float absX=calcAbsX(),absY = calcAbsY();
        RenderUtil.drawTitleIcon(font,absX,absY,icons,spacing,(float) anim.getAnimationFactor(),iconColor.getRGB(),bgColor.getRGB(),currentColor.getRGB(),isTransverse);
    }
}
