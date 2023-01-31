/*
 *MCP-919-main
 *Code by SkrilledSense
 *20230128
 */
package me.skrilled.ui.menu.ui;

import me.skrilled.SenseHeader;
import me.skrilled.api.modules.ModuleHeader;
import me.skrilled.api.modules.ModuleType;
import me.skrilled.api.modules.module.render.SettingMenu;
import me.skrilled.ui.menu.assembly.*;
import me.skrilled.ui.menu.assembly.bgType.BackGroundType;
import me.surge.animation.Animation;
import me.surge.animation.Easing;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.main.Main;

import java.awt.*;
import java.io.IOException;
import java.util.List;

public class SecilyUserInterface extends GuiScreen {
    private final ModuleType currentModuleType = ModuleType.COMBAT;
    private final ModuleHeader currentModule = SenseHeader.getSense.getModuleManager().getModuleListByModuleType(currentModuleType).get(0);
    /**
     * 点击拖动定位x轴
     */
    float posInClickX;
    /**
     * 点击拖动定位y轴
     */
    float posInClickY;
    /**
     * Window背景传参
     */
    float[] windowsPos = SettingMenu.getGuiPos();
    /**
     * 初始化 底层窗口
     */
    private final WindowAssembly mainGui = new WindowAssembly(windowsPos, null);
    /**
     * 拖动布尔
     */
    private boolean clickDag = false;
    /**
     * 初始化 大背景
     */
    private BGAssembly bigBg;
    private StringWithoutBGAssembly currentModuleTypeAssembly;
    /**
     * 初始化 编辑区
     */
    private BGAssembly areaEdit;
    /**
     * 初始化 左边栏 背景
     */
    private BGAssembly leftSideBarBG;
    /**
     * 初始化 左边栏 窗口
     */
    private WindowAssembly leftSideBar;
    /**
     * 初始化 ModuleTypeICON背景
     */
    private BGAssembly leftModuleTypeICONBarBg;
    /**
     * 初始化 ModuleTypeICON
     */
    private IconAssembly ModuleTypeICONBar;


    @Override
    public void initGui() {
        //实例化 大背景
        bigBg = new BGAssembly(new float[]{0, 0, mainGui.deltaX, mainGui.deltaY}, mainGui, new Color(0, 0, 0, 125), BackGroundType.RoundRect, true, 10f);
        //添加 大背景 至 底层窗口
        mainGui.bgAssembly = bigBg;

        //实例化 currentModuleType string
        currentModuleTypeAssembly = new StringWithoutBGAssembly(new float[]{0, 0, mainGui.deltaX, ((mainGui.deltaY * 0.13255813953488372093023255813953f) - Main.fontLoader.EN36.getHeight()) / 2f}, mainGui, currentModuleType.name().toLowerCase(), Main.fontLoader.EN36, Color.white);
        mainGui.windowName = currentModuleTypeAssembly;

        //计算 编辑区 背景 Pos
        float[] areaEditPos = {0.01704958975262581302525836774406f * bigBg.deltaX, 0.14244186046511627906976744186047f * bigBg.deltaY, 0.98295041024737418697474163225594f * bigBg.deltaX, 0.97520930232558139534883720930233f * bigBg.deltaY};

        //初始化 编辑区背景 组件
        areaEdit = new BGAssembly(areaEditPos, mainGui, new Color(204, 204, 204, 60), BackGroundType.RoundRect, false, 10f);

        //添加 编辑区背景 组件 至 底层窗口
        mainGui.addAssembly(areaEdit);

        //计算 左边栏 窗口 Pos
        float[] leftSideBarPos = {0.02911877394636015325670498084291f * bigBg.deltaX, 0.16424418604651162790697674418605f * bigBg.deltaY, 0.28588888888888888888888888888888f * bigBg.deltaX, 0.9273255813953488372093023255814f * bigBg.deltaY};

        //初始化 左边栏 窗口
        leftSideBar = new WindowAssembly(leftSideBarPos, mainGui);

        //添加 左边栏 窗口 至 底层窗口
        mainGui.addWindow(leftSideBar);

        //初始化 左边栏 背景
        leftSideBarBG = new BGAssembly(new float[]{0, 0, leftSideBar.deltaX, leftSideBar.deltaY}, leftSideBar, new Color(121, 121, 121, 114), BackGroundType.RoundRect, false, 9);

        //添加 左边栏 背景 至 左边栏窗口
        leftSideBar.addAssembly(leftSideBarBG);

        //计算 左边栏顶部TypeBar 背景 Pos
        float[] leftSideTopBarPos = {leftSideBarBG.pos[0] + 0.04665161775771256583897667419112f * leftSideBarBG.deltaX, leftSideBarBG.pos[1] + 0.02888446215139442231075697211155f * leftSideBarBG.deltaY, leftSideBarBG.pos[2] - 0.04665161775771256583897667419112f * leftSideBarBG.deltaX, leftSideBarBG.pos[1] + 0.14940239043824701195219123505976f * leftSideBarBG.deltaY};

        //初始化 左边栏顶部TypeBar背景 组件
        leftModuleTypeICONBarBg = new BGAssembly(leftSideTopBarPos, leftSideBar, new Color(121, 121, 121, 114), BackGroundType.RoundRect, false, 9);

        //添加 左边栏顶部TypeBar背景 组件 至 左边栏窗口
        leftSideBar.addAssembly(leftModuleTypeICONBarBg);

        //计算 ModuleTypeICON 组件 Pos
        float[] moduleTypeICONPos = {0.08714631268436578171091445427729f * leftSideBar.deltaX, 0.04280152671755725190839694656489f * leftSideBar.deltaY, 0.96166134185303514376996805111821f * leftSideBar.deltaX, 0.14031180400890868596881959910913f * leftSideBar.deltaY};

        //计算 ModuleTypeICON 组件 Spacing
        float spacing = 0.03421828908554572271386430678466f * leftSideBar.deltaX;

        //初始化 ModuleTypeICON 组件
        ModuleTypeICONBar = new IconAssembly(moduleTypeICONPos, leftSideBar, Main.fontLoader.ICON47, new char[]{'A', 'B', 'C', 'D', 'E'}, spacing, new Animation(200, false, Easing.LINEAR), new Color(196, 196, 196), new Color(144, 144, 144), new Color(126, 183, 247), true);

        //添加 ModuleTypeICON 组件 至 左边栏窗口
        leftSideBar.addAssembly(ModuleTypeICONBar);

        //计算 Modules Pos
        float[] modulesBGsStartPos = {0.04277286135693215339233038348083f * leftSideBar.deltaX, 0.17175572519083969465648854961832f * leftSideBar.deltaY, 0.95427728613569321533923303834808f * leftSideBar.deltaX, 0.28475190839694656488549618320611f * leftSideBar.deltaY};

        //计算 边距/高度
        float modulesUDMargin = 0.02579365079365079365079365079365f * leftSideBar.deltaY;
        float modulesBGHeight = modulesBGsStartPos[3] - modulesBGsStartPos[1];

        //遍历计算Modules Pos并且自动New 组件 Add
        List<ModuleHeader> tempList = SenseHeader.getSense.getModuleManager().getModuleListByModuleType(currentModuleType);
        for (int i = 0; i < tempList.size(); i++) {
            String mName = tempList.get(i).getModuleName();
            leftSideBar.addAssembly(new StringAssembly(new float[]{modulesBGsStartPos[0], modulesBGsStartPos[1] + i * (modulesBGHeight + modulesUDMargin), modulesBGsStartPos[2], modulesBGsStartPos[3] + i * (modulesBGHeight + modulesUDMargin)}, leftSideBar, mName, false, new Color(255, 255, 255, 25), new Color(-1), Main.fontLoader.EN24));
        }
        super.initGui();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        //拖动
        clickDag = mouseButton == 0 && mouseX > mainGui.calcAbsX() && mouseX < (mainGui.calcAbsX() + mainGui.deltaX) && mouseY > mainGui.calcAbsY() && mouseY < mainGui.calcAbsY() + mainGui.deltaY * 0.13255813953488372093023255813953f;
        if (clickDag) {
            posInClickX = mouseX - mainGui.calcAbsX();
            posInClickY = mouseY - mainGui.calcAbsY();
        }
        //组件点击判定
        for (Assembly assembly : mainGui.getAssembliesClicked(mouseX, mouseY)) {
            //组件点击处理
            assembly.mouseClicked(mouseX, mouseY, mouseButton);
//            SenseHeader.getSense.printINFO("组件点击处理");
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        //拖动
        if (clickDag) {
            mainGui.pos[0] = mouseX - posInClickX;
            mainGui.pos[1] = mouseY - posInClickY;
            mainGui.pos[2] = mainGui.deltaX + mouseX - posInClickX;
            mainGui.pos[3] = mainGui.deltaY + mouseY - posInClickY;
        }
        //绘制
        mainGui.draw();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        clickDag = false;
        super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    public void onGuiClosed() {

        super.onGuiClosed();
    }
}
