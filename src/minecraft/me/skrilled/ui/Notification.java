package me.skrilled.ui;

import me.fontloader.FontDrawer;
import me.skrilled.utils.IMC;
import me.skrilled.utils.render.BlurUtil;
import me.skrilled.utils.render.RenderUtil;
import me.surge.animation.Animation;
import me.surge.animation.ColourAnimation;
import me.surge.animation.Easing;
import net.minecraft.client.main.Main;

import java.awt.*;
import java.util.ArrayList;

public class Notification implements IMC {
    public volatile static ArrayList<Notification> notifications = new ArrayList<>();//列表
    public int height = 0;
    boolean leave;
    String message;//信息
    Animation timerAnim;//计时器
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
    infoType informationType;//类型
    posType positionType;

    public Notification(String message, int stayTime, infoType informationType, posType positionType) {
        if (informationType.name().equals("INFO") || informationType.name().equals("SUCCESS")) ICON = 'G';
        if (informationType.name().equals("WARNING") || informationType.name().equals("ERROR")) ICON = 'F';
        if (informationType.name().equals("INFO")) ICONCOLOR = new Color(104, 169, 255);
        if (informationType.name().equals("WARNING")) ICONCOLOR = new Color(255, 255, 120);
        if (informationType.name().equals("ERROR")) ICONCOLOR = new Color(255, 104, 104);
        if (informationType.name().equals("SUCCESS")) ICONCOLOR = new Color(0, 167, 0);
        this.positionType = positionType;
        this.message = message;
        this.stayTime = stayTime;
        this.informationType = informationType;
        timerAnim = new Animation(stayTime, false, Easing.LINEAR);
        motionBGColor = new ColourAnimation(new Color(30, 30, 30, 20), new Color(30, 30, 30, 125), stayTime, false, Easing.LINEAR);
        motionColor = new ColourAnimation(new Color(0, 255, 169, 255), new Color(255, 59, 59, 175), stayTime * 2, false, Easing.LINEAR);
    }

    public static void sendNotification(String message, int stayTime, infoType infoType, posType positionType) {
        Notification not = new Notification(message, stayTime, infoType, positionType);
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
//        SenseHeader.getSense.printINFO("    public void draw() ");
        String msg = this.message;
        String info = "Time remaining:" + Math.floor(((1f - timerAnim.getAnimationFactor()) * stayTime) / 10f) / 100f + "s";
        FontDrawer messageFont = Main.fontLoader.EN24;
        FontDrawer infoFont = Main.fontLoader.EN16;
        FontDrawer ICONFont = Main.fontLoader.ICON64;
        //字体高度
        int mfH = messageFont.getHeight();
        int ifH = infoFont.getHeight();
     /*
     信息字体高度+nifo字体高度+上下边距+中间间距= boxheight
       信息字体getStrWidth+左右边距+图片宽度+与图片距离=boxWidth
      */
        boxHeight = messageFont.getHeight() + infoFont.getHeight() * 2 + 6;
//        boxWidth=messageFont.getStringWidth(message)+;
        setMotion(true);
        int w = RenderUtil.width();
        int h = RenderUtil.height() - 30;
        int width = messageFont.getStringWidth(msg) + 60;
        height = mfH + ifH + 20;
        int upMargin = 10;
        int x = (int) (w - width * motionX.getAnimationFactor());
        int y = (int) (h - height - getBoxHeightAdd * motionY.getAnimationFactor());
        if (motionX.getAnimationFactor() == 1) {
            timerAnim.setState(true);
//            SenseHeader.getSense.printINFO(timerAnim.getAnimationFactor());
        }
        if (positionType == posType.LEFT) {        //背景框
            RenderUtil.drawRect(x, y, w, y + height, motionBGColor.getColour().getRGB());
            BlurUtil.blurArea(x, y, w, y + height, 10);
            //读条
            RenderUtil.drawRect(x, y + this.height, w - width * timerAnim.getAnimationFactor(), y + height + 3, motionColor.getColour().getRGB());
            //图标
            ICONFont.drawChar(ICON, x + 5, y + this.height - ICONFont.getHeight() - 7, ICONCOLOR.getRGB());
            messageFont.drawString(msg, x + 40, y + this.height - ifH - mfH - upMargin, -1);
            //informationType
            infoFont.drawString(info, x + 40, y + this.height - ifH - upMargin, -1);
        } else if (positionType == posType.UP) {
            RenderUtil.drawUpNotification(new String[]{message, informationType.name(), ICON.toString()}, new FontDrawer[]{infoFont, messageFont, ICONFont}, motionX, ICONCOLOR);

        }
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

    public enum infoType {
        SUCCESS, INFO, WARNING, ERROR

    }

    public enum posType {
        UP, LEFT, NONE

    }

}