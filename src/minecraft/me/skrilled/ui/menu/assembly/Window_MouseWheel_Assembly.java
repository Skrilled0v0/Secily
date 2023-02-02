package me.skrilled.ui.menu.assembly;

import me.fontloader.FontDrawer;
import me.skrilled.api.modules.ModuleHeader;
import me.skrilled.utils.render.RenderUtil;
import me.surge.animation.Animation;
import me.surge.animation.Easing;
import net.minecraft.client.main.Main;

import java.awt.*;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;

public class Window_MouseWheel_Assembly<T> extends WindowAssembly {
    private final float wheelsToNext = 33;
    public ArrayList<T> contents;
    Animation anim = new Animation(100, false, Easing.LINEAR);
    float[] initialPos;
    private boolean needInit = true;
    private float skipAim;
    private float currentSkip;
    private float lastSkipFactor;
    private int numOfContent2Render;

    public Window_MouseWheel_Assembly(float[] pos, WindowAssembly fatherWindow, ArrayList<T> contents, int numOfContent2Render) {
        super(pos, fatherWindow);
        this.contents = contents;
        this.numOfContent2Render = numOfContent2Render;
        initialPos = pos.clone();
    }

    float getSkipFactor() {
        if (anim.getAnimationFactor() == 1D) {
            currentSkip = skipAim;
            anim = new Animation(anim.length, anim.initialState, Easing.LINEAR);
        }
        return (float) (currentSkip + (skipAim - currentSkip) * anim.getAnimationFactor());
    }

    public void mouseWheel(int delta) {
        float result = skipAim - delta;
        if (result < (contents.size() - numOfContent2Render) * wheelsToNext && skipAim - delta >= 0) {
            currentSkip = getSkipFactor();
            skipAim = result;
            anim = new Animation(anim.length, anim.initialState, Easing.LINEAR);
            anim.setState(true);
        } else if (result >= (contents.size() - numOfContent2Render) * wheelsToNext && skipAim - delta >= 0) {
            if ((contents.size() - numOfContent2Render) * wheelsToNext > 0) {
                currentSkip = getSkipFactor();
                skipAim = ((contents.size() - numOfContent2Render) * wheelsToNext);
                anim = new Animation(anim.length, anim.initialState, Easing.LINEAR);
                anim.setState(true);
            }
        } else if (result < (contents.size() - numOfContent2Render) * wheelsToNext && skipAim - delta < 0) {
            currentSkip = getSkipFactor();
            skipAim = 0;
            anim = new Animation(anim.length, anim.initialState, Easing.LINEAR);
            anim.setState(true);
        }
    }

    @Override
    public void reset() {
        skipAim = 0;
        currentSkip = 0;
        anim = new Animation(anim.length, anim.initialState, Easing.LINEAR);
        subWindows = new ArrayList<>();
        assemblies = new ArrayList<>();
        needInit = true;
    }

    @Override
    public float draw() {
        if (needInit) init();
        glPushMatrix();
        RenderUtil.doScissor((int) calcAbsX(), (int) calcAbsY(), (int) (calcAbsX() + deltaX), (int) (calcAbsY() + deltaY));
        //更新滚动坐标
        float latestSkipFactor = getSkipFactor();
        float deltaFactor = latestSkipFactor - lastSkipFactor;
        lastSkipFactor = latestSkipFactor;
        for (Assembly assembly : assemblies) {
            assembly.pos[1] -= deltaFactor;
            assembly.pos[3] -= deltaFactor;
        }
        for (WindowAssembly subWindow : subWindows) {
            subWindow.pos[1] -= deltaFactor;
            subWindow.pos[3] -= deltaFactor;
        }
        if (bgAssembly != null) {
            currentUsedHeight += this.bgAssembly.draw();
        }
        if (windowName != null) {
            currentUsedHeight += this.windowName.draw();
        }
        for (IconAssembly icon : this.icons) {
            currentUsedHeight += icon.draw();
        }
        for (Assembly otherAssembly : assemblies) {
            currentUsedHeight += otherAssembly.draw();
        }
        for (WindowAssembly windowAssembly : subWindows) {
            currentUsedHeight += windowAssembly.draw();
        }
        glDisable(GL_SCISSOR_TEST);
        glPopMatrix();
        return deltaY;
    }

    public void init() {
        this.assemblies = new ArrayList<>();
        float bgBoxHeight = this.deltaY / (numOfContent2Render + (numOfContent2Render - 1) * 0.21337989340194817129204190406175f);
        float udMargin = 0.21337989340194817129204190406175f * bgBoxHeight;
        for (int i = 0; i < contents.size(); i++) {
            FontDrawer font = Main.fontLoader.EN22;
            String content = contents.get(i).toString();
            float yUsed = (i - getSkipFactor() / wheelsToNext) * (bgBoxHeight + udMargin);
            float[] booleanAssemblyPos = {0.67498152904338036097526650951694f * this.deltaX, yUsed + (bgBoxHeight - font.getHeight()) / 2f, 0.91559652394187805650353586883862f * this.deltaX, yUsed + (bgBoxHeight + font.getHeight()) / 2f};
            Animation anim = new Animation(500, ((ModuleHeader) contents.get(i)).isEnabled(), Easing.LINEAR);
            Color bgColor = new Color(65, 64, 68, 181);
            Color trueColor = new Color(126, 183, 247);
            Color falseColor = new Color(204, 204, 204);
            BooleanAssembly booleanAssembly = new BooleanAssembly(booleanAssemblyPos, this, ((ModuleHeader) contents.get(i)).isEnabled(), anim, bgColor, trueColor, falseColor, content);
            this.addAssembly(booleanAssembly);
            StringAssembly stringAssembly = new StringAssembly(new float[]{0, yUsed, deltaX, bgBoxHeight + yUsed}, this, content, false, new Color(255, 255, 255, 25), new Color(255, 255, 255, 74), Color.ORANGE, font, font.getHeight() / 2f);
            stringAssembly.assemblyName = content;
            this.addAssembly(stringAssembly);
            needInit = false;
        }
    }
}
