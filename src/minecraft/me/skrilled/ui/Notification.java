package me.skrilled.ui;

import me.cubex2.ttfr.CFontRenderer;
import me.skrilled.utils.IMC;
import me.skrilled.utils.math.TimerUtil;
import me.skrilled.utils.render.RenderUtil;
import me.surge.animation.Animation;
import me.surge.animation.Easing;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;


public class Notification implements IMC {
    public static ArrayList<Notification> notifications = new ArrayList<>();//列表
    boolean start, up, leave;
    ResourceLocation image;//图片
    String message;//信息
    TimerUtil timerUtil;//计时器
    int maxHeight = RenderUtil.height() / 2;//最高到半屏幕直接抽离
    Animation motionX = new Animation(1000f, false, Easing.LINEAR);//动画
    Animation motionY = new Animation(1000f, false, Easing.LINEAR);//动画
    Animation motionLastTimer;
    int boxHeight;//Notification Y坐标目标值
    int boxWidth;//Notification X坐标目标值
    int getBoxHeightAdd;
    int currentHeight;//当前Notification Y值
    int stayTime;//停留时间
    Type type;//类型

    public Notification(String message, int stayTime, Type type) {
        image = new ResourceLocation("skrilled/NotificationICON/" + type.name() + ".png");
        motionLastTimer = new Animation(stayTime, false, Easing.LINEAR);
        this.message = message;
        this.stayTime = stayTime;
        this.type = type;
    }

    public static void sendNotification(String message, int stayTime, Type type) {
        Notification not = new Notification(message, stayTime, type);
        if (notifications.size() >= 1) {
            for (Notification notification : notifications) {
                notification.motionY.setState(true);
            }
            not.motionY.setState(true);
        }
        notifications.add(not);
    }

    public static void drawNotifications() {
        for (Notification notification : notifications) {
            notification.draw();
        }
    }

    public void draw() {
        CFontRenderer messageFont = sense.fontBuffer.EN24;
        CFontRenderer infoFont = sense.fontBuffer.EN12;
        int mfH = messageFont.getHeight(false);
        int ifH = infoFont.getHeight(false);
     /*
     信息字体高度+nifo字体高度+上下边距+中间间距= boxheight
       信息字体getStrWidth+左右边距+图片宽度+与图片距离=boxWidth
      */
        boxHeight = messageFont.getHeight(false) + infoFont.getHeight(false) * 2 + 6;
//        boxWidth=messageFont.getStringWidth(message)+;
        motionX.setState(true);
        int w = RenderUtil.width();
        int h = RenderUtil.height() - 30;
        int x = (int) (w - boxWidth * motionX.getAnimationFactor());
        int y = (int) (h - boxHeight - getBoxHeightAdd * motionY.getAnimationFactor());
        String msg = "KillAura is Opened";
        String info = "Type:INFO  LastTimer:1000";
        int width = messageFont.getStringWidth(msg) + 5 + 5 + 30 + 5;
        int height = mfH + ifH + 10;
        RenderUtil.drawRect(w - width, h - height, w, h, new Color(48, 48, 48, 100).getRGB());
        RenderUtil.drawRect(w - width, h, w, h + 3, new Color(50, 200, 150, 175).getRGB());
        GL11.glColor4f(1, 1, 1, 0.7f);
        RenderUtil.drawIcon(w - width + 5, h - height + 2.5f, 30, 30, image);
        messageFont.drawString(message, w - width + 40, h - ifH - mfH, -1);
        infoFont.drawString(info, w - width + 40, h - 5 - ifH, -1);
    }

    enum Type {
        SUCCESS, INFO, WARNING, ERROR

    }

}