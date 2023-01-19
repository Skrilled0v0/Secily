package me.skrilled.ui;

import me.cubex2.ttfr.CFontRenderer;
import me.skrilled.utils.IMC;
import me.skrilled.utils.render.RenderUtil;
import me.surge.animation.Animation;
import me.surge.animation.Easing;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;


public class Notification implements IMC {
    public static ArrayList<Notification> notifications = new ArrayList<>();//列表
    public int height = 0;
    boolean up, leave;
    ResourceLocation image;//图片
    String message;//信息
    Animation timerAnim = null;//计时器
    int maxHeight = RenderUtil.height() / 2;//最高到半屏幕直接抽离
    Animation motionX = new Animation(1000f, false, Easing.LINEAR);//动画
    Animation motionY = new Animation(1000f, false, Easing.LINEAR);//动画
    Animation motionLastTimer;
    int boxHeight;//Notification Y坐标目标值
    int boxWidth;//Notification X坐标目标值
    int getBoxHeightAdd = 0;
    int currentHeight;//当前Notification Y值
    int stayTime;//停留时间
    Type type;//类型

    public Notification(String message, int stayTime, Type type) {
        image = new ResourceLocation("skrilled/NotificationICON/" + type.name() + ".png");
        motionLastTimer = new Animation(stayTime, false, Easing.LINEAR);
        this.message = message;
        this.stayTime = stayTime;
        this.type = type;
        timerAnim = new Animation(stayTime, false, Easing.LINEAR);
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
        String info = "Type:" + this.type + "  LastTimer:" + this.stayTime + "ms";
        CFontRenderer messageFont = sense.fontBuffer.EN24;
        CFontRenderer infoFont = sense.fontBuffer.EN12;
        //字体高度
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
        int width = messageFont.getStringWidth(msg) + 5 + 5 + 30 + 5;
        height = mfH + ifH + 10;
        int x = (int) (w - width * motionX.getAnimationFactor());
        int y = (int) (h - height - getBoxHeightAdd * motionY.getAnimationFactor());
        if (motionX.getAnimationFactor() == 1) {
            timerAnim.setState(true);
//            sense.printINFO(timerAnim.getAnimationFactor());
        }
        //背景框
        RenderUtil.drawRect(x, y, w, y+height, new Color(48, 48, 48, 100).getRGB());
        //读条
        RenderUtil.drawRect(x, y + this.height, w - width * timerAnim.getAnimationFactor(), y + height + 3, new Color(50, 200, 150, 175).getRGB());
        //图标
        GL11.glColor4f(1, 1, 1, 0.7f);
        RenderUtil.drawIcon(x + 5, y + 2.5f, 30, 30, image);
        //message
        messageFont.drawString(msg, x + 40, y + this.height - ifH - mfH, -1);
        //infoType
        infoFont.drawString(info, x + 40, y + this.height - 5 - ifH, -1);
        if (timerAnim.getAnimationFactor() == 1) {
            motionX.setState(false);
            this.leave = true;
        }
        if (y<maxHeight){
            motionX.setState(false);
            this.leave = true;
        }
        if (this.leave && motionX.getAnimationFactor() == 0) notifications.remove(this);
    }

    public enum Type {
        SUCCESS, INFO, WARNING, ERROR

    }

}