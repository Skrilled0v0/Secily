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
import me.skrilled.ui.menu.MenuMotion;
import me.skrilled.ui.menu.assembly.*;
import me.skrilled.ui.menu.assembly.bgType.BackGroundType;
import me.skrilled.ui.menu.assembly.color.Color_alpha_Assembly;
import me.skrilled.ui.menu.assembly.color.Color_h_Assembly;
import me.skrilled.ui.menu.assembly.color.Color_sb_Assembly;
import me.skrilled.ui.menu.assembly.enums.FillingMode;
import me.skrilled.ui.menu.assembly.enums.SideOfBoundedWindow;
import me.surge.animation.Animation;
import me.surge.animation.Easing;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.main.Main;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

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
    public static boolean mainGuiDrag = false;
    public static boolean mainGUIScale = false;
    public static KeyTypeStringAssembly keyTypeStringAssembly;
    public static Animation uiAnimation = new Animation(800f, false, Easing.BACK_OUT);
    /**
     * Window背景传参
     */
    static float[] windowsPos = SettingMenu.getGuiPos();
    /**
     * 初始化 底层窗口
     */
    public static WindowAssembly mainGui = new WindowAssembly(windowsPos, null, "mainGui");
    private static SecilyUserInterface secilyUserInterface;
    boolean closed;
    /**
     * 点击拖动定位x轴
     */
    float posInClickX;
    /**
     * 点击拖动定位y轴
     */
    float posInClickY;
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
        BGAssembly bigBg = new BGAssembly(new float[]{0, 0, 1, 1}, mainGui, new Color(0, 0, 0, 125), BackGroundType.RoundRect, true, 0.018f * mainGui.deltaY());

        //添加 大背景 至 底层窗口
        mainGui.addAssembly(bigBg);

        //实例化边线(用于拖动缩放mainGui
        Color edgeLineColorForMainGui = new Color(255, 198, 128, 255);
        LineAssembly lineDownForMainGui = new LineAssembly(new float[]{0.018f, 1, 0.982f, 1}, mainGui, 2.5f, edgeLineColorForMainGui, FillingMode.ROUNDED_SIDE, SideOfBoundedWindow.DOWN);
        lineDownForMainGui.canDrag = true;
        mainGui.addAssembly(lineDownForMainGui);
        LineAssembly lineRightForMainGui = new LineAssembly(new float[]{1, 0.018f, 1, 0.982f}, mainGui, 2.5f, edgeLineColorForMainGui, FillingMode.ROUNDED_SIDE, SideOfBoundedWindow.RIGHT);
        lineRightForMainGui.canDrag = true;
        mainGui.addAssembly(lineRightForMainGui);

        //实例化 currentModuleType string
        StringWithoutBGAssembly stringWithoutBGAssembly = new StringWithoutBGAssembly(new float[]{0, 0, 1, 0.12455626829420190571090490128916f}, mainGui, upperHeadLowerOther(currentModuleType.name()), Main.fontLoader.EN36, Color.white, new boolean[]{true, true});
        stringWithoutBGAssembly.assemblyName = "currentModuleType";
        mainGui.addAssembly(stringWithoutBGAssembly);

        //计算 编辑区 背景 Pos
        float[] areaEditPos = {0.01704958975262581302525836774406f, 0.14244186046511627906976744186047f, 0.98295041024737418697474163225594f, 0.97520930232558139534883720930233f};

        //初始化 编辑区背景 组件
        BGAssembly areaEdit = new BGAssembly(areaEditPos, mainGui, new Color(204, 204, 204, 60), BackGroundType.RoundRect, false, 0.018f);

        //添加 编辑区背景 组件 至 底层窗口
        mainGui.addAssembly(areaEdit);

        //计算 左边栏 窗口 Pos
        float[] leftSideBarPos = {0.02911877394636015325670498084291f, 0.16424418604651162790697674418605f, 0.28588888888888888888888888888888f, 0.9273255813953488372093023255814f};

        //初始化 左边栏 窗口
        WindowAssembly leftSideBar = new WindowAssembly(leftSideBarPos, mainGui, "leftSideBar");

        //添加 左边栏 窗口 至 底层窗口
        mainGui.addWindow(leftSideBar);

        //初始化 左边栏 背景
        BGAssembly leftSideBarBG = new BGAssembly(new float[]{0, 0, 1, 1}, leftSideBar, new Color(121, 121, 121, 114), BackGroundType.RoundRect, false, 9);

        //添加 左边栏 背景 至 左边栏窗口
        leftSideBar.addAssembly(leftSideBarBG);

        //计算 左边栏顶部TypeBar 背景 Pos
        float[] leftSideTopBarPos = {0.04665161775771256583897667419112f, 0.02888446215139442231075697211155f, 0.95334838224228743416102332580888f, 0.14940239043824701195219123505976f};

        //初始化 左边栏顶部TypeBar背景 组件
        BGAssembly leftModuleTypeICONBarBg = new BGAssembly(leftSideTopBarPos, leftSideBar, new Color(121, 121, 121, 114), BackGroundType.RoundRect, false, 9);

        //添加 左边栏顶部TypeBar背景 组件 至 左边栏窗口
        leftSideBar.addAssembly(leftModuleTypeICONBarBg);

        //计算 ModuleTypeICON 组件 Pos
        float[] moduleTypeICONPos = {0.09914631268436578171091445427729f, 0.04280152671755725190839694656489f, 0.96166134185303514376996805111821f, 0.14031180400890868596881959910913f};

        //初始化 ModuleTypeICON 组件
        IconAssembly moduleTypeICONBar = new IconAssembly(moduleTypeICONPos, leftSideBar, Main.fontLoader.ICON42, new String[]{"A", "B", "C", "D", "E"}, getIconStringByModuleType(currentModuleType), 0.03421828908554572271386430678466f, new Animation(100, false, Easing.CUBIC_OUT), new Color(196, 196, 196), new Color(144, 144, 144), new Color(126, 183, 247, 166), true);
        moduleTypeICONBar.assemblyName = "moduleTypeICONBar";

        //添加 ModuleTypeICON 组件 至 左边栏窗口
        leftSideBar.addAssembly(moduleTypeICONBar);

        //计算 ModulesWindow Pos
        float[] modulesWindowPos = {0.04277286135693215339233038348083f, 0.17175572519083969465648854961832f, 0.95427728613569321533923303834808f, 0.96651785714285714285714285714286f};
        //获取 当前moduleType 的 modules
        ArrayList<ModuleHeader> tempList = SenseHeader.getSense.getModuleManager().getModuleListByModuleType(currentModuleType);
        //初始化 ModulesWindow
        WindowAssembly modulesWindow = new Window_MouseWheel_SwitchContents_Assembly<>(modulesWindowPos, leftSideBar, tempList, 6, "modulesWindow");
        //添加 modulesWindow 至 leftSideBar
        leftSideBar.addWindow(modulesWindow);
        //计算 valuesWindow pos
        float[] valuesWindowPos = {0.31461039502426172650114221873906f, 0.16415950900836773754725636280232f, 0.96479965316569675343082489870104f, 0.92612980274917350796437779781395f};
        //初始化valuesWindow
        valuesWindow = new Window_Values_Assembly(valuesWindowPos, mainGui, currentModule, "valuesWindow");
        mainGui.addWindow(valuesWindow);
        super.initGui();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == 1) {
            closed = true;
            SenseHeader.getSense.getModuleManager().getModuleByClass(SettingMenu.class).toggle();
        }
        if (keyTypeStringAssembly != null) keyTypeStringAssembly.keyTyped(typedChar, keyCode);
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        //mainGUI拖动
        mainGuiDrag = mouseButton == 0 && mouseX > mainGui.calcAbsX() && mouseX < (mainGui.calcAbsX() + mainGui.deltaX()) && mouseY > mainGui.calcAbsY() && mouseY < mainGui.calcAbsY() + mainGui.deltaY() * 0.13255813953488372093023255813953f;
        if (mainGuiDrag) {
            posInClickX = mouseX;
            posInClickY = mouseY;
        }
        //组件点击判定
        ArrayList<Assembly> assemblies = mainGui.getAssembliesByMousePos(mouseX, mouseY);
        keyTypeStringAssembly = null;//重置可输入类型组件的值
        for (Assembly assembly : assemblies) {

            //通用的处理
            assembly.mouseEventHandle(mouseX, mouseY, mouseButton);
            if (assembly.canDrag) assembly.onDrag = true;
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
    public void handleMouseInput() throws IOException {
        //父类中内容
        int i = Mouse.getEventX() * this.width / this.mc.displayWidth;
        int j = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
        int k = Mouse.getEventButton();

        if (Mouse.getEventButtonState()) {
            if (this.mc.gameSettings.touchscreen && this.touchValue++ > 0) {
                return;
            }

            this.eventButton = k;
            this.lastMouseEvent = Minecraft.getSystemTime();
            this.mouseClicked(i, j, this.eventButton);
        } else if (k != -1) {
            if (this.mc.gameSettings.touchscreen && --this.touchValue > 0) {
                return;
            }

            this.eventButton = -1;
            this.mouseReleased(i, j, k);
        } else if (this.eventButton != -1 && this.lastMouseEvent > 0L) {
            long l = Minecraft.getSystemTime() - this.lastMouseEvent;
            this.mouseClickMove(i, j, this.eventButton, l);
        }

        //TODO:230223
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        //mainGui拖动
        if (mainGuiDrag) {
            mainGui.onDrag(mouseX - posInClickX, mouseY - posInClickY);
            ArrayList<Assembly> assemblies1 = mainGui.getAssembliesByClass(ColorAssembly.class);
            for (Object assembliesByClass : mainGui.getAssembliesByClass(Color_h_Assembly.class)) {
                ((Color_h_Assembly) assembliesByClass).init = false;
            }
            for (Object assembliesByClass : mainGui.getAssembliesByClass(Color_sb_Assembly.class)) {
                ((Color_sb_Assembly) assembliesByClass).init = false;
            }
            for (Object assembliesByClass : mainGui.getAssembliesByClass(Color_alpha_Assembly.class)) {
                ((Color_alpha_Assembly) assembliesByClass).init = false;
            }
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
            int c = 0;
            for (Assembly assembly : targetAssemblies) {
                if (assembly instanceof Window_MouseWheel_Assembly) {
                    int[] val = new int[]{0};
                    assembly.getOldestFatherWindow(val);
                    c = Math.max(val[0], c);
                }
            }
            for (Assembly assembly : targetAssemblies) {
                int[] val = new int[]{0};
                assembly.getOldestFatherWindow(val);
                if (val[0] == c) {
                    if (assembly instanceof Window_MouseWheel_Assembly) {
                        ((Window_MouseWheel_Assembly) assembly).mouseWheel(mouseWheelFactor);
                        break;
                    }
                }
            }
        }
        //切换ModuleType
        if (onModuleTypeSwitching) {
            ((StringWithoutBGAssembly) (mainGui.getAssemblyByName("currentModuleType"))).value = upperHeadLowerOther(currentModuleType.name());
            Assembly assembly = mainGui.getAssemblyByName("modulesWindow");
            if (assembly instanceof Window_MouseWheel_SwitchContents_Assembly) {
                Window_MouseWheel_SwitchContents_Assembly<ModuleHeader> window = (Window_MouseWheel_SwitchContents_Assembly<ModuleHeader>) assembly;
                window.reset();
                window.contents = SenseHeader.getSense.getModuleManager().getModuleListByModuleType(currentModuleType);
                onModuleTypeSwitching = false;
            }
        }
        //绘制
        GL11.glPushMatrix();
        float motion = (float) MenuMotion.getMenuMotion().getAnimationFactor();
        GlStateManager.scale(motion, motion, motion);
        mainGui.draw();
        GL11.glPopMatrix();
        super.drawScreen(mouseX, mouseY, partialTicks);
        if (closed) {
            if (MenuMotion.getMenuMotion().getAnimationFactor() == 0) mc.displayGuiScreen(null);
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        mainGuiDrag = false;
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
