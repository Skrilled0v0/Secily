package me.skrilled.ui;

import me.cubex2.ttfr.CFontRenderer;
import me.skrilled.utils.IMC;
import me.skrilled.utils.render.RenderUtil;
import me.surge.animation.Animation;
import me.surge.animation.ColourAnimation;
import me.surge.animation.Easing;

import java.awt.*;
import java.util.ArrayList;


public class Notification implements IMC {
    public static ArrayList<Notification> notifications = new ArrayList<>();//列表
    public int height = 0;
    boolean leave;
    String message;//信息
    Animation timerAnim = null;//计时器
    int maxHeight = RenderUtil.height() / 2;//最高到半屏幕直接抽离
    Animation motionX = new Animation(1000f, false, Easing.CIRC_OUT);//动画
    Animation motionY = new Animation(1000f, false, Easing.CIRC_OUT);//动画
    Character ICON;
    Color ICONCOLOR;
    ColourAnimation motionBGColor;
    ColourAnimation motionColor;
    int boxHeight;//Notification Y坐标目标值
    int getBoxHeightAdd = 0;
    int stayTime;//停留时间
    Type type;//类型

    public Notification(String message, int stayTime, Type type) {
        if (type.name().equals("INFO") || type.name().equals("SUCCESS")) ICON = 'G';
        if (type.name().equals("WARNING") || type.name().equals("ERROR")) ICON = 'F';
        if (type.name().equals("INFO")) ICONCOLOR = new Color(104, 169, 255);
        if (type.name().equals("WARNING")) ICONCOLOR = new Color(255, 255, 120);
        if (type.name().equals("ERROR")) ICONCOLOR = new Color(255, 104, 104);
        if (type.name().equals("SUCCESS")) ICONCOLOR = new Color(0, 167, 0);

        this.message = message;
        this.stayTime = stayTime;
        this.type = type;
        timerAnim = new Animation(stayTime, false, Easing.LINEAR);
        motionBGColor = new ColourAnimation(new Color(30, 30, 30, 20), new Color(30, 30, 30, 200), stayTime, false, Easing.LINEAR);
        motionColor = new ColourAnimation(new Color(0, 255, 169, 200), new Color(255, 59, 59, 120), stayTime * 2, false, Easing.LINEAR);
    }

    public static void sendNotification(String message, int stayTime, Type type) {
        Notification not = new Notification(message, stayTime, type);
        if (notifications.size() >= 1) {
            for (Notification notification : notifications) {
                notification.getBoxHeightAdd += notification.height * 1.1f;
                notification.motionY.setState(true);
            }
            not.motionY.setState(true);
        }
        notifications.add(not);
    }

    public static void drawNotifications() {
        if (notifications.size() == 0) return;
        for (int i = 0; i < notifications.size(); i++) {
            notifications.get(i).draw();
        }
    }

    public void draw() {
//        sense.printINFO("    public void draw() ");
        String msg = this.message;
        String info = "Time remaining:" + Math.floor(((1f - timerAnim.getAnimationFactor()) * stayTime) / 10f) / 100f + "s";
        CFontRenderer messageFont = sense.fontBuffer.EN24;
        CFontRenderer infoFont = sense.fontBuffer.EN16;
        CFontRenderer ICONFont = sense.fontBuffer.ICON64;
        //字体高度
        int mfH = messageFont.getHeight(false);
        int ifH = infoFont.getHeight(false);
     /*
     信息字体高度+nifo字体高度+上下边距+中间间距= boxheight
       信息字体getStrWidth+左右边距+图片宽度+与图片距离=boxWidth
      */
        boxHeight = messageFont.getHeight(false) + infoFont.getHeight(false) * 2 + 6;
//        boxWidth=messageFont.getStringWidth(message)+;
        setMotion(true);
        int w = RenderUtil.width();
        int h = RenderUtil.height() - 30;
        int width = messageFont.getStringWidth(msg) + 60;
        height = mfH + ifH + 10;
        int x = (int) (w - width * motionX.getAnimationFactor());
        int y = (int) (h - height - getBoxHeightAdd * motionY.getAnimationFactor());
        if (motionX.getAnimationFactor() == 1) {
            timerAnim.setState(true);
//            sense.printINFO(timerAnim.getAnimationFactor());
        }
        //背景框
        RenderUtil.drawRect(x, y, w, y + height, motionBGColor.getColour().getRGB());
        //读条
        RenderUtil.drawRect(x, y + this.height, w - width * timerAnim.getAnimationFactor(), y + height + 3, motionColor.getColour().getRGB());
        //图标
        ICONFont.drawString(String.valueOf(ICON), x + 5, y + this.height - ICONFont.getHeight(false) / 3, ICONCOLOR.getRGB());
        //message
        messageFont.drawString(msg, x + 40, y + this.height - ifH - mfH, -1);
        //infoType
        infoFont.drawString(info, x + 40, y + this.height - 5 - ifH, -1);
        if (timerAnim.getAnimationFactor() == 1) {
            setMotion(false);
            this.leave = true;
        }
        if (y < maxHeight) {
            setMotion(false);
            this.leave = true;
        }
        if (this.leave && motionX.getAnimationFactor() == 0) notifications.remove(this);
    }

    void setMotion(boolean set) {
        motionX.setState(set);
        motionBGColor.setState(set);
        motionColor.setState(set);
    }

    public enum Type {
        SUCCESS, INFO, WARNING, ERROR

    }

}