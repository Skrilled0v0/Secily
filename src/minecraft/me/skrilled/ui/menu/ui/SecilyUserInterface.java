/*
 *MCP-919-main
 *Code by SkrilledSense
 *20230128
 */
package me.skrilled.ui.menu.ui;

import me.skrilled.SenseHeader;
import me.skrilled.api.modules.module.render.SettingMenu;
import me.skrilled.ui.menu.assembly.BGAssembly;
import me.skrilled.ui.menu.assembly.IconAssembly;
import me.skrilled.ui.menu.assembly.WindowAssembly;
import me.skrilled.ui.menu.assembly.bgType.BackGroundType;
import me.surge.animation.Animation;
import me.surge.animation.Easing;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.main.Main;

import java.awt.*;
import java.io.IOException;

public class SecilyUserInterface extends GuiScreen {
    /**
     * Window背景传参
     */
    float[] windowsPos = SettingMenu.getGuiPos();
    /**
     * 初始化底层窗口
     */

    WindowAssembly mainGui = new WindowAssembly(windowsPos, null);

    @Override
    public void initGui() {
        BGAssembly bigBg = new BGAssembly(new float[]{0,0,mainGui.deltaX,mainGui.deltaY}, mainGui, new Color(0, 0, 0, 125), BackGroundType.RoundRect, true, 10f);
        //添加 背景 至 底层窗口
        mainGui.addAssembly(bigBg);

        //pos1+0.14301191765980498374864572047671*dY
        //pos1+0.95991332611050920910075839653304*dY
        //pos0+0.01887871853546910755148741418764*dX
        //pos0+0.98112128146453089244851258581236*dX

        //计算 编辑区 背景 Pos
        float[] areaEditPos = {
                0.01704958975262581302525836774406f * bigBg.deltaX,
                0.14244186046511627906976744186047f * bigBg.deltaY,
                0.98295041024737418697474163225594f * bigBg.deltaX,
                0.96220930232558139534883720930233f * bigBg.deltaY};

        //初始化 编辑区背景 组件
        BGAssembly areaEdit = new BGAssembly(areaEditPos, mainGui, new Color(204, 204, 204, 60), BackGroundType.RoundRect, false, 10f);

        //添加 编辑区背景 组件 至 底层窗口
        mainGui.addAssembly(areaEdit);

        //计算 左边栏 窗口 Pos
        float[] leftSideBarPos = {
                0.02911877394636015325670498084291f * bigBg.deltaX,
                0.16424418604651162790697674418605f* bigBg.deltaY,
                0.28888888888888888888888888888889f * bigBg.deltaX,
                0.9273255813953488372093023255814f * bigBg.deltaY};

        //初始化 左边栏 窗口
        WindowAssembly leftSideBar = new WindowAssembly(leftSideBarPos, mainGui);

        //添加 左边栏 窗口 至 底层窗口
        mainGui.addWindow(leftSideBar);

        //初始化 左边栏 背景
        BGAssembly leftSideBarBG = new BGAssembly(new float[]{0,0,leftSideBar.deltaX,leftSideBar.deltaY}, leftSideBar, new Color(121, 121, 121, 114), BackGroundType.RoundRect, false, 9);

        //添加 左边栏 背景 至 左边栏窗口
        leftSideBar.addAssembly(leftSideBarBG);

        //计算 左边栏顶部TypeBar 背景 Pos
        float[] leftSideTopBarPos = {leftSideBarBG.pos[0] + 0.04665161775771256583897667419112f * leftSideBarBG.deltaX, leftSideBarBG.pos[1] + 0.02888446215139442231075697211155f * leftSideBarBG.deltaY, leftSideBarBG.pos[2] - 0.04665161775771256583897667419112f * leftSideBarBG.deltaX, leftSideBarBG.pos[1] + 0.14940239043824701195219123505976f * leftSideBarBG.deltaY};

        //初始化 左边栏顶部TypeBar背景 组件
        BGAssembly leftModuleTypeICONBarBg = new BGAssembly(leftSideTopBarPos, leftSideBar, new Color(121, 121, 121, 114), BackGroundType.RoundRect, false, 9);

        //添加 左边栏顶部TypeBar背景 组件 至 左边栏窗口
        leftSideBar.addAssembly(leftModuleTypeICONBarBg);

        //计算 ModuleTypeICON 组件 Pos
        float[] moduleTypeICONPos = {0.07374631268436578171091445427729f * leftSideBar.deltaX,
                0.04580152671755725190839694656489f * leftSideBar.deltaY,
                0, 0};

        //计算 ModuleTypeICON 组件 Spacing
        float spacing = 0.03421828908554572271386430678466f * leftSideBar.deltaX;

        //初始化 ModuleTypeICON 组件
        IconAssembly ModuleTypeICONBar = new IconAssembly(moduleTypeICONPos, leftSideBar, Main.fontLoader.ICON64, new String[]{"A", "B", "C", "D", "E"}, spacing, new Animation(1000, false, Easing.LINEAR), new Color(196, 196, 196), new Color(144, 144, 144), new Color(126, 183, 247), true);

        //添加 ModuleTypeICON 组件 至 左边栏窗口
        leftSideBar.addAssembly(ModuleTypeICONBar);

        //计算 Modules Pos
        float[] modulesBGsStartPos = {
                0.04277286135693215339233038348083f * leftSideBar.deltaX,
                0.17175572519083969465648854961832f * leftSideBar.deltaY,
                0.95427728613569321533923303834808f * leftSideBar.deltaX,
                0.28475190839694656488549618320611f * leftSideBar.deltaY};

        //计算 边距/高度
        float modulesUDMargin = 0.02579365079365079365079365079365f * leftSideBar.deltaY;
        float modulesBGHeight = modulesBGsStartPos[3] - modulesBGsStartPos[1];

        //遍历计算Modules Pos并且自动New 组件 Add
        for (int i = 0; i < 6; i++) {
            leftSideBar.addAssembly(new BGAssembly(new float[]{modulesBGsStartPos[0], modulesBGsStartPos[1] + i * (modulesBGHeight + modulesUDMargin), modulesBGsStartPos[2], modulesBGsStartPos[3] + i * (modulesBGHeight + modulesUDMargin)}, leftSideBar, new Color(255, 255, 255, 30), BackGroundType.RoundRect, false, 5f));
        }
        super.initGui();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        mainGui.draw();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
