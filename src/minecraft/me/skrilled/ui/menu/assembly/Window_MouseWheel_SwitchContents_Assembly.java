package me.skrilled.ui.menu.assembly;

import me.fontloader.FontDrawer;
import me.skrilled.api.modules.ModuleHeader;
import me.skrilled.ui.menu.assembly.bgType.BackGroundType;
import me.surge.animation.Animation;
import me.surge.animation.Easing;
import net.minecraft.client.main.Main;

import java.awt.*;
import java.util.ArrayList;

public class Window_MouseWheel_SwitchContents_Assembly<T> extends Window_MouseWheel_Assembly {
    float bgBoxHeight;
    float udMargin;
    private boolean needInit = true;

    public Window_MouseWheel_SwitchContents_Assembly(float[] pos, WindowAssembly fatherWindow, ArrayList<T> contents, int numOfContent2Render) {
        super(pos, fatherWindow, contents, numOfContent2Render, 0);
        bgBoxHeight = this.deltaY() / (numOfContent2Render + (numOfContent2Render - 1) * 0.21337989340194817129204190406175f);
        udMargin = 0.21337989340194817129204190406175f * bgBoxHeight;
        this.wheelsToNext = bgBoxHeight + udMargin;
    }


    @Override
    public void reset() {
        super.reset();
        needInit = true;
    }

    @Override
    public float draw() {
        if (needInit) init();
        return super.draw();
    }

    public void init() {
        this.assemblies = new ArrayList<>();

        for (int i = 0; i < contents.size(); i++) {
            FontDrawer font = Main.fontLoader.EN22;
            String content = contents.get(i).toString();
            float yUsed = (i - getSkipFactor() / wheelsToNext) * (bgBoxHeight + udMargin);
            float[] booleanAssemblyPos = {0.67498152904338036097526650951694f * this.deltaX(), yUsed + (bgBoxHeight - font.getHeight()) / 2f, 0.91559652394187805650353586883862f * this.deltaX(), yUsed + (bgBoxHeight + font.getHeight()) / 2f};
            Animation anim = new Animation(500, ((ModuleHeader) contents.get(i)).isEnabled(), Easing.LINEAR);
            Color bgColor = new Color(65, 64, 68, 181);
            Color trueColor = new Color(126, 183, 247);
            Color falseColor = new Color(204, 204, 204);

            BGAssembly currentBG = new BGAssembly(new float[]{0, yUsed, deltaX(), bgBoxHeight + yUsed}, this, new Color(255, 255, 255, 80), BackGroundType.RoundRect, false, font.getHeight() / 2f);
            currentBG.currentBgMotion = new Animation(1800f, false, Easing.CUBIC_OUT);
            this.addAssembly(currentBG);
            BooleanAssembly booleanAssembly = new BooleanAssembly(booleanAssemblyPos, this, ((ModuleHeader) contents.get(i)).isEnabled(), anim, bgColor, trueColor, falseColor, content);
            this.addAssembly(booleanAssembly);
            StringAssembly stringAssembly = new StringAssembly(new float[]{0, yUsed, deltaX(), bgBoxHeight + yUsed}, this, content, new boolean[]{false, true}, new Color(255, 255, 255, 25), new Color(255, 255, 255, 74), Color.white, font, font.getHeight() / 2f);
            stringAssembly.assemblyName = content;
            this.addAssembly(stringAssembly);
            needInit = false;
        }
    }
}
