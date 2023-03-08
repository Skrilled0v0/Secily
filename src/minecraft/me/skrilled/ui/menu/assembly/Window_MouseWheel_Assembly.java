package me.skrilled.ui.menu.assembly;

import me.skrilled.ui.menu.ui.SecilyUserInterface;
import me.skrilled.utils.render.RenderUtil;
import me.skrilled.utils.render.ScissorPos;
import me.surge.animation.Animation;
import me.surge.animation.Easing;

import java.util.ArrayList;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;

public class Window_MouseWheel_Assembly<T> extends WindowAssembly {
    final byte mode;
    public Animation animation = new Animation(200, false, Easing.LINEAR);
    public ArrayList<T> contents;
    public float lastSkipFactor = 0f;
    public float pages;
    /**
     * 滚动速度(倍率
     */
    public float coefficient = 2.5f;
    float skipAim;
    float heightOfOnePage;
    float numOfPages2Render;
    float currentSkip = 0f;
    ScissorPos p;

    public Window_MouseWheel_Assembly(float[] pos, WindowAssembly fatherWindow, String assemblyName, ArrayList<T> contents, float numOfPages2Render, float heightOfOnePage) {
        super(pos, fatherWindow, assemblyName);
        this.contents = contents;
        this.numOfPages2Render = numOfPages2Render;
        this.heightOfOnePage = heightOfOnePage;
        mode = 0;
    }

    public Window_MouseWheel_Assembly(float[] pos, WindowAssembly fatherWindow, String assemblyName, float pages, float heightOfOnePage, float numOfPages2Render) {
        super(pos, fatherWindow, assemblyName);
        this.heightOfOnePage = heightOfOnePage;
        this.pages = pages;
        this.numOfPages2Render = numOfPages2Render;
        mode = 1;
    }

    @Override
    public void reset() {
        super.reset();
        skipAim = 0;
        currentSkip = 0;
        lastSkipFactor = 0;
        animation = new Animation(animation.length, animation.initialState, Easing.LINEAR);
    }

    @Override
    public float draw() {
        float latestSkipFactor = getSkipFactor();
        float deltaFactor = latestSkipFactor - lastSkipFactor;
        //当父窗口为ENUM组件时，如果为展开，执行滚动更新
        if (fatherWindow instanceof EnumAssembly) {
            //更新滚动坐标
            lastSkipFactor = latestSkipFactor;
            for (Assembly assembly : assemblies) {
                //如果是背景，不更新
                if (assembly instanceof BGAssembly) continue;
                //如果是展开按钮，则不更新
                if (assembly instanceof StringWithoutBGAssembly) {
                    if (((StringWithoutBGAssembly) assembly).value.equals("H") || ((StringWithoutBGAssembly) assembly).value.equals("I"))
                        continue;
                }

                //更新
                assembly.pos[1] -= deltaFactor;
                assembly.pos[3] -= deltaFactor;
            }
        } else {
            lastSkipFactor = latestSkipFactor;
            for (Assembly assembly : assemblies) {
                assembly.pos[1] -= deltaFactor;
                assembly.pos[3] -= deltaFactor;
            }
            for (WindowAssembly subWindow : subWindows) {
                subWindow.pos[1] -= deltaFactor;
                subWindow.pos[3] -= deltaFactor;
            }
            if (deltaFactor != 0) SecilyUserInterface.updateColorPoints();
        }
        double x = calcAbsX(), y = calcAbsY(), x1 = calcAbsX() + deltaX(), y1 = calcAbsY() + deltaY();
        if (RenderUtil.scissors.size() > 0) {

            p = RenderUtil.scissors.get(RenderUtil.scissors.size() - 1);
            x = max(x, p.x);
            y = max(y, p.y);
            x1 = min(x1, p.x1);
            y1 = min(y1, p.y1);
            if (x1 <= x || y1 <= y) {
                return 0f;
            }
        }
        glPushMatrix();
        RenderUtil.doScissor((int) x, (int) y, (int) x1, (int) y1);
        float result = super.draw();
        RenderUtil.deScissor();
        glPopMatrix();
        return result;
    }

    /**
     * @return 剪裁的高度
     */
    @Override
    public float getDrawHeight() {
        double y = calcAbsY(), y1 = calcAbsY() + deltaY();
        y = max(y, p.y);
        y1 = min(y1, p.y1);
        return (float) (y1 - y);
    }

    float getSkipFactor() {
        if (animation.getAnimationFactor() == 1D) {
            currentSkip = skipAim;
            animation = new Animation(animation.length, animation.initialState, Easing.LINEAR);
        }
        return (float) (currentSkip + (skipAim - currentSkip) * animation.getAnimationFactor());
    }

    public void mouseWheel(int delta) {
        delta *= coefficient;
        float restPages = 0;
        switch (mode) {
            case 0:
                restPages = contents.size() - numOfPages2Render;
                break;
            case 1:
                restPages = pages - numOfPages2Render;
                break;
        }
        //判定ENUM 框是否展开
        if (fatherWindow instanceof EnumAssembly) {
            if (!((EnumAssembly) fatherWindow).dropped()) {
                return;
            }
        }
        //滚动
        float result = skipAim - delta;
        if (result < restPages * heightOfOnePage && skipAim - delta >= 0) {
            currentSkip = getSkipFactor();
            skipAim = result;
            animation = new Animation(animation.length, animation.initialState, Easing.LINEAR);
            animation.setState(true);
        } else if (result >= restPages * heightOfOnePage && skipAim - delta >= 0) {
            if (restPages * heightOfOnePage > 0) {
                currentSkip = getSkipFactor();
                skipAim = (restPages * heightOfOnePage);
                animation = new Animation(animation.length, animation.initialState, Easing.LINEAR);
                animation.setState(true);
            }
        } else if (result < restPages * heightOfOnePage && skipAim - delta < 0) {
            currentSkip = getSkipFactor();
            skipAim = 0;
            animation = new Animation(animation.length, animation.initialState, Easing.LINEAR);
            animation.setState(true);
        }
    }

    public float getMaxPositionArg() {
        float result = 0;
        for (Assembly assembly : assemblies) {
            result = Math.max(result, assembly.positionArgs[3]);
        }
        for (WindowAssembly subWindow : subWindows) {
            result = Math.max(result, subWindow.positionArgs[3]);
        }
        return result;
    }
}
