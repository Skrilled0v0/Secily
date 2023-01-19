/*
 *MCP-919-main
 *Code by SkrilledSense
 *20230119
 */
package me.skrilled.ui;

import me.cubex2.ttfr.CFontRenderer;
import me.skrilled.utils.IMC;
import me.skrilled.utils.math.TimerUtil;
import me.skrilled.utils.render.RenderUtil;
import me.surge.animation.Animation;
import me.surge.animation.Easing;

import java.awt.*;

public class Notification implements IMC {
    String message;
    TimerUtil timer = new TimerUtil();
    long stayTime;
    Type type;
    Animation motionX = new Animation(1000f, false, Easing.CUBIC_OUT);
    Animation motionY = new Animation(1000f, false, Easing.CUBIC_OUT);

    public Notification(String message, long stayTime, Type type) {
        this.message = message;
        this.stayTime = stayTime;
        this.type = type;
        motionX.setState(true);
        motionY.setState(true);
    }

    public Notification() {
    }

    public void drawNotification() {
        CFontRenderer messageFont = sense.getFontBuffer().EN24;
        CFontRenderer infoFont = sense.getFontBuffer().EN16;
        int w = RenderUtil.width();
        int h = RenderUtil.height();
        double boxWidth = (messageFont.getStringWidth(this.message) + 20) * motionX.getAnimationFactor();
        double boxHeight = messageFont.getHeight(false) + infoFont.getHeight(false) + 10;
        RenderUtil.drawRound((float) (w - boxWidth), (float) (h - boxHeight), w, h, new Color(36, 36, 36).getRGB(), new Color(36, 36, 36).getRGB());


    }

    public enum Type {
        SUCCESS("SUCCESS"), INFO("INFO"), WARNING("WARNING"), ERROR("ERROR");

        Type(String s) {
        }
    }
}
