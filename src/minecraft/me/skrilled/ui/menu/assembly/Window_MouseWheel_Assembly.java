package me.skrilled.ui.menu.assembly;

import me.skrilled.utils.render.RenderUtil;
import me.surge.animation.Animation;
import me.surge.animation.Easing;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;

public class Window_MouseWheel_Assembly<T> extends WindowAssembly {
    public Animation animation = new Animation(200, false, Easing.LINEAR);
    public ArrayList<T> contents;
    float skipAim = 0f;
    float wheelsToNext = 33f;
    float numOfContent2Render;
    float currentSkip = 0f;
    private float lastSkipFactor = 0f;

    public Window_MouseWheel_Assembly(float[] pos, WindowAssembly fatherWindow, ArrayList<T> contents, float numOfContent2Render) {
        super(pos, fatherWindow);
        this.contents = contents;
        this.numOfContent2Render = numOfContent2Render;
    }

    @Override
    public void reset() {
        super.reset();
        skipAim = 0;
        currentSkip = 0;
        animation = new Animation(animation.length, animation.initialState, Easing.LINEAR);
    }

    @Override
    public float draw() {
        glPushMatrix();
        RenderUtil.doScissor((int) calcAbsX(), (int) calcAbsY(), (int) (calcAbsX() + deltaX()), (int) (calcAbsY() + deltaY()));
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
        float result = super.draw();
        glDisable(GL_SCISSOR_TEST);
        glPopMatrix();
        return result;
    }

    float getSkipFactor() {
        if (animation.getAnimationFactor() == 1D) {
            currentSkip = skipAim;
            animation = new Animation(animation.length, animation.initialState, Easing.LINEAR);
        }
        return (float) (currentSkip + (skipAim - currentSkip) * animation.getAnimationFactor());
    }

    public void mouseWheel(int delta) {
        float result = skipAim - delta;
        if (result < (contents.size() - numOfContent2Render) * wheelsToNext && skipAim - delta >= 0) {
            currentSkip = getSkipFactor();
            skipAim = result;
            animation = new Animation(animation.length, animation.initialState, Easing.LINEAR);
            animation.setState(true);
        } else if (result >= (contents.size() - numOfContent2Render) * wheelsToNext && skipAim - delta >= 0) {
            if ((contents.size() - numOfContent2Render) * wheelsToNext > 0) {
                currentSkip = getSkipFactor();
                skipAim = ((contents.size() - numOfContent2Render) * wheelsToNext);
                animation = new Animation(animation.length, animation.initialState, Easing.LINEAR);
                animation.setState(true);
            }
        } else if (result < (contents.size() - numOfContent2Render) * wheelsToNext && skipAim - delta < 0) {
            currentSkip = getSkipFactor();
            skipAim = 0;
            animation = new Animation(animation.length, animation.initialState, Easing.LINEAR);
            animation.setState(true);
        }
    }
}
