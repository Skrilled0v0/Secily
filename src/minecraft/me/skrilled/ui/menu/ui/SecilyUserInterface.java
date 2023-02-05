/*
 *MCP-919-main
 *Code by SkrilledSense
 *20230128
 */
package me.skrilled.ui.menu.ui;

import me.skrilled.SenseHeader;
import me.skrilled.api.manager.ModuleManager;
import me.skrilled.api.modules.ModuleHeader;
import me.skrilled.api.modules.ModuleType;
import me.skrilled.api.modules.module.render.SettingMenu;
import me.skrilled.ui.menu.assembly.*;
import me.skrilled.ui.menu.assembly.bgType.BackGroundType;
import me.surge.animation.Animation;
import me.surge.animation.Easing;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.main.Main;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class SecilyUserInterface extends GuiScreen {
    public static ModuleType currentModuleType = ModuleType.COMBAT;
    public static boolean onModuleTypeSwitching = false;
    public static ModuleHeader currentModule = SenseHeader.getSense.getModuleManager().getModuleListByModuleType(currentModuleType).get(0);
    /**
     * 拖动布尔
     */
    public static boolean mainGUIClickDrag = false;
    /**
     * Window背景传参
     */
    static float[] windowsPos = SettingMenu.getGuiPos();
    /**
     * 初始化 底层窗口
     */
    public static WindowAssembly mainGui = new WindowAssembly(windowsPos, null);
    private static SecilyUserInterface secilyUserInterface;
    /**
     * 点击拖动定位x轴
     */
    float posInClickX;
    /**
     * 点击拖动定位y轴
     */
    float posInClickY;
    ArrayList<Assembly> assembliesClicked;
    private Window_Values_Assembly valuesWindow;

    public SecilyUserInterface() {
        secilyUserInterface = this;
    }

    public static String getIconStringByModuleType(ModuleType moduleType) {
        switch (moduleType) {
            case COMBAT:
                return "A";
            case MISC:
                return "B";
            case MOVE:
                return "C";
            case PLAYER:
                return "D";
            case RENDER:
                return "E";
        }
        return "0";
    }

    public static SecilyUserInterface getSecilyUserInterface() {
        return secilyUserInterface;
    }

    public static String upperHeadLowerOther(String s) {
        String s1 = s.substring(0, 1);
        String s2 = s.substring(1);
        return s1.toUpperCase() + s2.toLowerCase();
    }

    @Override
    public void initGui() {
        mainGui.reset();
        //实例化 大背景
        BGAssembly bigBg = new BGAssembly(new float[]{0, 0, mainGui.deltaX(), mainGui.deltaY()}, mainGui, new Color(0, 0, 0, 125), BackGroundType.RoundRect, true, 10f);

        //添加 大背景 至 底层窗口
        mainGui.bgAssembly = bigBg;

        //实例化 currentModuleType string
        mainGui.windowName = new StringWithoutBGAssembly(new float[]{0, Main.fontLoader.EN36.getHeight() / 2f, mainGui.deltaX(), ((mainGui.deltaY() * 0.13255813953488372093023255813953f) - Main.fontLoader.EN36.getHeight()) / 2f}, mainGui, upperHeadLowerOther(currentModuleType.name()), Main.fontLoader.EN36, Color.white, new boolean[]{true, true});

        //计算 编辑区 背景 Pos
        float[] areaEditPos = {0.01704958975262581302525836774406f * bigBg.deltaX(), 0.14244186046511627906976744186047f * bigBg.deltaY(), 0.98295041024737418697474163225594f * bigBg.deltaX(), 0.97520930232558139534883720930233f * bigBg.deltaY()};

        //初始化 编辑区背景 组件
        BGAssembly areaEdit = new BGAssembly(areaEditPos, mainGui, new Color(204, 204, 204, 60), BackGroundType.RoundRect, false, 10f);

        //添加 编辑区背景 组件 至 底层窗口
        mainGui.addAssembly(areaEdit);

        //计算 左边栏 窗口 Pos
        float[] leftSideBarPos = {0.02911877394636015325670498084291f * bigBg.deltaX(), 0.16424418604651162790697674418605f * bigBg.deltaY(), 0.28588888888888888888888888888888f * bigBg.deltaX(), 0.9273255813953488372093023255814f * bigBg.deltaY()};

        //初始化 左边栏 窗口
        WindowAssembly leftSideBar = new WindowAssembly(leftSideBarPos, mainGui);

        //添加 左边栏 窗口 至 底层窗口
        mainGui.addWindow(leftSideBar);

        //初始化 左边栏 背景
        BGAssembly leftSideBarBG = new BGAssembly(new float[]{0, 0, leftSideBar.deltaX(), leftSideBar.deltaY()}, leftSideBar, new Color(121, 121, 121, 114), BackGroundType.RoundRect, false, 9);

        //添加 左边栏 背景 至 左边栏窗口
        leftSideBar.addAssembly(leftSideBarBG);

        //计算 左边栏顶部TypeBar 背景 Pos
        float[] leftSideTopBarPos = {leftSideBarBG.pos[0] + 0.04665161775771256583897667419112f * leftSideBarBG.deltaX(), leftSideBarBG.pos[1] + 0.02888446215139442231075697211155f * leftSideBarBG.deltaY(), leftSideBarBG.pos[2] - 0.04665161775771256583897667419112f * leftSideBarBG.deltaX(), leftSideBarBG.pos[1] + 0.14940239043824701195219123505976f * leftSideBarBG.deltaY()};

        //初始化 左边栏顶部TypeBar背景 组件
        BGAssembly leftModuleTypeICONBarBg = new BGAssembly(leftSideTopBarPos, leftSideBar, new Color(121, 121, 121, 114), BackGroundType.RoundRect, false, 9);

        //添加 左边栏顶部TypeBar背景 组件 至 左边栏窗口
        leftSideBar.addAssembly(leftModuleTypeICONBarBg);

        //计算 ModuleTypeICON 组件 Pos
        float[] moduleTypeICONPos = {0.09914631268436578171091445427729f * leftSideBar.deltaX(), 0.04280152671755725190839694656489f * leftSideBar.deltaY(), 0.96166134185303514376996805111821f * leftSideBar.deltaX(), 0.14031180400890868596881959910913f * leftSideBar.deltaY()};

        //计算 ModuleTypeICON 组件 Spacing
        float spacing = 0.03421828908554572271386430678466f * leftSideBar.deltaX();

        //初始化 ModuleTypeICON 组件
        IconAssembly moduleTypeICONBar = new IconAssembly(moduleTypeICONPos, leftSideBar, Main.fontLoader.ICON42, new String[]{"A", "B", "C", "D", "E"}, getIconStringByModuleType(currentModuleType), spacing, new Animation(100, false, Easing.CUBIC_OUT), new Color(196, 196, 196), new Color(144, 144, 144), new Color(126, 183, 247, 166), true);
        moduleTypeICONBar.assemblyName = "moduleTypeICONBar";

        //添加 ModuleTypeICON 组件 至 左边栏窗口
        leftSideBar.addAssembly(moduleTypeICONBar);

        //计算 ModulesWindow Pos
        float[] modulesWindowPos = {0.04277286135693215339233038348083f * leftSideBar.deltaX(), 0.17175572519083969465648854961832f * leftSideBar.deltaY(), 0.95427728613569321533923303834808f * leftSideBar.deltaX(), 0.96651785714285714285714285714286f * leftSideBar.deltaY()};
        //获取 当前moduleType 的 modules
        ArrayList<ModuleHeader> tempList = SenseHeader.getSense.getModuleManager().getModuleListByModuleType(currentModuleType);
        //初始化 ModulesWindow
        WindowAssembly modulesWindow = new Window_MouseWheel_SwitchContents_Assembly<>(modulesWindowPos, leftSideBar, tempList, 6);
        modulesWindow.assemblyName = "modulesWindow";
        //添加 modulesWindow 至 leftSideBar
        leftSideBar.addWindow(modulesWindow);
        //计算 valuesWindow pos
        float[] valuesWindowPos = {0.31461039502426172650114221873906f * mainGui.deltaX(), 0.16415950900836773754725636280232f * mainGui.deltaY(), 0.96479965316569675343082489870104f * mainGui.deltaX(), 0.92612980274917350796437779781395f * mainGui.deltaY()};
        //初始化valuesWindow
        valuesWindow = new Window_Values_Assembly(valuesWindowPos, mainGui, currentModule);
        valuesWindow.assemblyName = "valuesWindow";
        mainGui.addWindow(valuesWindow);
        super.initGui();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        //mainGUI拖动
        mainGUIClickDrag = mouseButton == 0 && mouseX > mainGui.calcAbsX() && mouseX < (mainGui.calcAbsX() + mainGui.deltaX()) && mouseY > mainGui.calcAbsY() && mouseY < mainGui.calcAbsY() + mainGui.deltaY() * 0.13255813953488372093023255813953f;
        if (mainGUIClickDrag) {
            posInClickX = mouseX;
            posInClickY = mouseY;
        }
        //组件点击判定
        ArrayList<Assembly> assemblies = mainGui.getAssembliesByMousePos(mouseX, mouseY);
        for (Assembly assembly : assemblies) {
        ArrayList<Assembly> assemblyList = mainGui.getAssembliesCanDrag();

            if (assembly.canDrag) assembly.onDrag = true;
            //通用的处理
            try {
                assembly.mouseEventHandle(mouseX, mouseY, mouseButton);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //组件点击特殊处理
            if (!assembly.assemblyName.equalsIgnoreCase("defaultName")) {
                //点到module的处理
                ModuleHeader module = ModuleManager.getModuleByName(assembly.assemblyName);
                if (module != null) {
                    if (!module.toString().equalsIgnoreCase(currentModule.toString())) {
                        currentModule = module;
                        valuesWindow.setModule(module);
                    }
                }
            }


        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        //mainGui拖动
        if (mainGUIClickDrag) {
            mainGui.onDrag(mouseX - posInClickX, mouseY - posInClickY);
            posInClickX = mouseX;
            posInClickY = mouseY;
        }
        //其他组件拖动
        ArrayList<Assembly> assembliesCanDrag = mainGui.getAssembliesCanDrag();
        for (Assembly assembly : assembliesCanDrag) {
            if (assembly.onDrag) {
                assembly.mouseEventHandle(mouseX, mouseY, 0);
            }
        }
        //滚轮处理
        int mouseWheel = Mouse.getDWheel();
        if (mouseWheel != 0) {
            int mouseWheelFactor = mouseWheel / 16;
            ArrayList<Assembly> targetAssemblies = mainGui.getAssembliesByMousePos(mouseX, mouseY);
            for (Assembly assembly : targetAssemblies) {
                //Module列的滚轮处理
                if (assembly.assemblyName.equalsIgnoreCase("modulesWindow")) {
                    ((Window_MouseWheel_SwitchContents_Assembly<ModuleHeader>) assembly).mouseWheel(mouseWheelFactor);
                }
                //枚举类型的滚轮处理
                if (assembly.assemblyName.startsWith("enumAssembly")) {
                    ((Window_MouseWheel_Assembly) ((EnumAssembly) assembly).subWindows.get(0)).mouseWheel(mouseWheelFactor);
                }
            }
        }
        //切换ModuleType
        if (onModuleTypeSwitching) {
            mainGui.windowName.value = upperHeadLowerOther(currentModuleType.name());
            Assembly assembly = mainGui.getAssemblyByName("modulesWindow");
            if (assembly instanceof Window_MouseWheel_SwitchContents_Assembly) {
                Window_MouseWheel_SwitchContents_Assembly<ModuleHeader> window = (Window_MouseWheel_SwitchContents_Assembly<ModuleHeader>) assembly;
                window.reset();
                window.contents = SenseHeader.getSense.getModuleManager().getModuleListByModuleType(currentModuleType);
                onModuleTypeSwitching = false;
            }
        }
        //绘制
        mainGui.draw();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        mainGUIClickDrag = false;
        for (Assembly assembly : mainGui.getAssembliesCanDrag()) {
            assembly.onDrag = false;
        }
        super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
    }
}
