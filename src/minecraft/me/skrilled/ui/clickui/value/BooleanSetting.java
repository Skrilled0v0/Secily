/*
 *MCP-919-main
 *Code by SkrilledSense
 *20230118
 */
package me.skrilled.ui.clickui.value;

import me.skrilled.utils.render.RenderUtil;
import me.surge.animation.Animation;
import me.surge.animation.Easing;

public class BooleanSetting {
    boolean value;
    int x, y;
    Animation motion = new Animation(1000f, false, Easing.BACK_IN_OUT);

    public BooleanSetting(boolean value, int x, int y) {
        this.value = value;
        this.x = x;
        this.y = y;
    }

    public void draw(int buttonColor, int opBGColor, int disBGColor) {
        int BGColor = value ? opBGColor : disBGColor;
        motion.setState(value);
        RenderUtil.drawRound(x, y, x + 40, y + 10, BGColor, BGColor);
        RenderUtil.drawRound((float) ((x - 2) * motion.getAnimationFactor()), (float) (y - 2), (float) ((x + 12) * motion.getAnimationFactor()), y - 12, buttonColor, buttonColor);


    }
}
