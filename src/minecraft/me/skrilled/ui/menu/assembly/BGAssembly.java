package me.skrilled.ui.menu.assembly;

import me.skrilled.ui.menu.assembly.bgType.BackGroundType;
import me.skrilled.ui.menu.ui.SecilyUserInterface;
import me.skrilled.utils.render.BlurUtil;
import me.skrilled.utils.render.RenderUtil;
import me.surge.animation.Animation;

import java.awt.*;

public class BGAssembly extends Assembly {
    Color color;
    BackGroundType bgType;
    boolean canBlur = false;
    Animation currentBgMotion;
    float radius = 2.5f;

    /**
     * 默认圆角矩形
     */
    public BGAssembly(float[] pos, WindowAssembly fatherWindow, Color color) {
        super(pos, fatherWindow);
        this.color = color;
        bgType = BackGroundType.RoundRect;
    }

    public BGAssembly(float[] pos, WindowAssembly fatherWindow, Color color, BackGroundType bgType) {
        super(pos, fatherWindow);
        this.color = color;
        this.bgType = bgType;
    }

    public BGAssembly(float[] pos, WindowAssembly fatherWindow, Color color, BackGroundType bgType, boolean canBlur) {
        super(pos, fatherWindow);
        this.color = color;
        this.bgType = bgType;
        this.canBlur = canBlur;
    }

    public BGAssembly(float[] pos, WindowAssembly fatherWindow, Color color, BackGroundType bgType, boolean canBlur, float radius) {
        super(pos, fatherWindow);
        this.color = color;
        this.bgType = bgType;
        this.canBlur = canBlur;
        this.radius = radius;
    }

    @Override
    public float draw() {
        float absX = calcAbsX();
        float absY = calcAbsY();
        float motion = 1;
        if (currentBgMotion != null) {
            motion = (float) currentBgMotion.getAnimationFactor();
        }
        switch (bgType) {
            case Rect:
                if (canBlur) BlurUtil.blurArea(absX, absY, absX + deltaX() * motion, absY + deltaY(), 20);
                RenderUtil.drawRect(absX, absY, absX + deltaX() * motion, absY + deltaY(), color.getRGB());
                return 0f;
            case RoundRect:
                if (canBlur)
                    BlurUtil.blurAreaRounded(absX, absY, absX + deltaX() * motion, absY + deltaY(), radius, 20);
                RenderUtil.drawRoundRect(absX, absY, absX + deltaX() * motion, absY + deltaY(), radius, color.getRGB());
                return 0f;
        }
        return 0f;
    }

    @Override
    public void mouseEventHandle(int mouseX, int mouseY, int button) {
        if (currentBgMotion != null) {
            currentBgMotion.setState(true);
        }
        if (!this.fatherWindow.assemblyName.equalsIgnoreCase("modulesWindow")) return;
        for (Assembly modulesWindow : ((Window_MouseWheel_SwitchContents_Assembly) SecilyUserInterface.mainGui.getAssemblyByName("modulesWindow")).assemblies) {
            if (modulesWindow instanceof BGAssembly && modulesWindow != this) {
                ((BGAssembly) modulesWindow).currentBgMotion.setState(false);
            }
        }
    }
}
