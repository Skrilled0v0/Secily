/*
 *MCP-919-main
 *Code by SkrilledSense
 *20230118
 */
package me.skrilled.ui.clickui.value;

import me.skrilled.utils.render.RenderUtil;
import me.surge.animation.BoundedAnimation;
import me.surge.animation.Easing;

public class BooleanSetting {
    public BoundedAnimation motion = new BoundedAnimation(0, 10, 1000f, false, Easing.LINEAR);
    boolean value;
    int x, y;

    public BooleanSetting(boolean value, int x, int y) {
        this.value = value;
        this.x = x;
        this.y = y;
    }

    public void draw(int buttonColor, int opBGColor, int disBGColor) {
        int BGColor = value ? opBGColor : disBGColor;
        RenderUtil.drawRound(x, y, x + 35, y + 10, BGColor, BGColor);
        RenderUtil.drawRound((float) (x - 2 + motion.getAnimationValue()), (float) (y - 2), (float) (x + 12 + motion.getAnimationValue()), y + 12, buttonColor, buttonColor);
    }
}
