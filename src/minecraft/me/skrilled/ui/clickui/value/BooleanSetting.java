/*
 *MCP-919-main
 *Code by SkrilledSense
 *20230118
 */
package me.skrilled.ui.clickui.value;

import me.skrilled.utils.render.RenderUtil;
import me.surge.animation.BoundedAnimation;
import me.surge.animation.Easing;

import java.awt.*;

public class BooleanSetting extends CanDraw {
    public static BoundedAnimation motion = new BoundedAnimation(0, 10, 1000f, false, Easing.LINEAR);
    boolean value;
    int x, y;
    int valueButtonBooleanColor = new Color(0, 136, 255).getRGB();
    int valueBooleanOPColor = new Color(74, 74, 74).getRGB();
    int valueBooleanDisColor = new Color(25, 25, 25).getRGB();

    public BooleanSetting(boolean value, int x, int y) {
        this.value = value;
        this.x = x;
        this.y = y;
    }

    @Override
    public void draw() {
        int BGColor = value ? valueBooleanOPColor : valueBooleanDisColor;
        RenderUtil.drawRound(x, y, x + 35, y + 10, BGColor, BGColor);
        RenderUtil.drawRound((float) (x - 2 + motion.getAnimationValue()), (float) (y - 2), (float) (x + 12 + motion.getAnimationValue()), y + 12, valueButtonBooleanColor, valueButtonBooleanColor);
    }
}
