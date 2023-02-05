/*
 *MCP-919-main
 *Code by SkrilledSense
 *20230126
 */
package me.skrilled.ui.menu.assembly;

import me.fontloader.FontDrawer;
import me.skrilled.SenseHeader;
import me.skrilled.api.modules.ModuleHeader;
import me.skrilled.api.value.ValueHeader;
import me.skrilled.utils.render.RenderUtil;
import me.surge.animation.Animation;
import me.surge.animation.Easing;
import net.minecraft.client.main.Main;

import java.awt.*;

public class NumberAssembly extends Assembly {
    /**
     * 0：最小值；1：现在值；2：最大值；3：间隔
     */
    double[] doubles;
    Animation anim;
    Color bgColor;
    Color ugColor;
    Color buttonColor;
    double lastAnimValue;
    float barLRMargin;
    float absX;

    public NumberAssembly(float[] pos, WindowAssembly fatherWindow, double[] doubles, Animation anim, Color bgColor, Color ugColor, Color buttonColor) {
        super(pos, fatherWindow);
        this.doubles = doubles;
        this.anim = anim;
        this.bgColor = bgColor;
        this.ugColor = ugColor;
        this.buttonColor = buttonColor;
        this.lastAnimValue = doubles[1] / (doubles[2] - doubles[0]);
        this.canDrag = true;
        barLRMargin = deltaX() / 20;
        absX = calcAbsX();

    }

    @Override
    public float draw() {
        float absX = calcAbsX(), absY = calcAbsY();
        FontDrawer valueRenderFont = Main.fontLoader.EN18;
        String str = Double.toString(Math.floor(doubles[1] * 100) / 100);
        float halfStrWidth = valueRenderFont.getStringWidth(str) / 2f;
        float[] valueRenderPos = new float[]{(float) (pos[0] + barLRMargin + (deltaX() - 2 * barLRMargin) * (doubles[1] - doubles[0]) / (doubles[2] - doubles[0])), pos[1] - valueRenderFont.getHeight(), pos[2], pos[3]};
        valueRenderPos[0] += fatherWindow.calcAbsX() - halfStrWidth;
        valueRenderPos[1] += fatherWindow.calcAbsY();
        valueRenderPos[2] += fatherWindow.calcAbsX() - halfStrWidth;
        valueRenderPos[3] += fatherWindow.calcAbsY();
        RenderUtil.drawString(valueRenderPos, valueRenderFont, str, -1);
        return RenderUtil.drawNumberBar(absX, absY, deltaX(), deltaY(), getAnimValue(), bgColor.getRGB(), ugColor.getRGB(), buttonColor.getRGB());
    }

    //动起来后计算动画量值
    public double getAnimValue() {
        if (anim.getAnimationFactor() == 1D) {
            lastAnimValue = (doubles[1] - doubles[0]) / (doubles[2] - doubles[0]);
            anim = new Animation(anim.length, anim.initialState, Easing.LINEAR);
        }
        return lastAnimValue + ((doubles[1] - doubles[0]) / (doubles[2] - doubles[0]) - lastAnimValue) * anim.getAnimationFactor();
    }

    public void setDouble(double value) {
        doubles[1] = value;
        try {
            String[] valueInfo = assemblyName.split("\\.");
            ModuleHeader module = SenseHeader.getSense.getModuleManager().getModuleByName(valueInfo[0]);
            ValueHeader valueHeader = module.getValueByName(valueInfo[1]);
            valueHeader.setDoubles(doubles.clone());
        } catch (Exception e) {
            e.printStackTrace();
        }
        lastAnimValue = getAnimValue();
        anim = new Animation(anim.length, anim.initialState, Easing.LINEAR);
        anim.setState(true);
    }

    @Override
    public void mouseEventHandle(int mouseX, int mouseY, int button) {
        if (button != 0) return;
        float x = Math.max(0, mouseX - absX - barLRMargin);
        x = Math.min(x, deltaX() - 2 * barLRMargin);
        x = x / (deltaX() - 2 * barLRMargin);
        setDouble(doubles[0] + doubles[3] * Math.floor(x * (doubles[2] - doubles[0]) / doubles[3]));
    }
}
