/*
 *eclipse
 *Code by SkrilledSense
 *20221230
 */
package me.skrilled.ui.clickui;

import me.cubex2.ttfr.CFontRenderer;
import me.skrilled.SenseHeader;
import me.skrilled.api.modules.ModuleHeader;
import me.skrilled.api.modules.ModuleHeader.ModuleType;
import me.skrilled.api.modules.module.render.SettingMenu;
import me.skrilled.api.value.SubEnumValueHeader;
import me.skrilled.api.value.ValueHeader;
import me.skrilled.utils.IMC;
import me.skrilled.utils.render.RenderUtil;
import me.surge.animation.BoundedAnimation;
import me.surge.animation.Easing;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SecilyMenu extends GuiScreen implements IMC {
    public static ModuleType currentModuleType = ModuleType.COMBAT;
    /**
     * ModuleType的间隔
     */
    public static int moduleTypeInterval = 40;
    /**
     * 是否在拖动gui页面
     */
    public static boolean clickDag = false;
    public static boolean clickDag1 = true;
    public ModuleHeader currentModule = sense.moduleManager.getModuleByIndex(0);
    /**
     * ModuleType的盒子宽度
     */
    int typeSideSize = 40;
    /**
     * ModuleType的ICON宽度
     */
    int typeICONSize = 30;
    /**
     * ModuleType的盒子高度
     */
    int typeSideHeight = 40;
    /**
     * Module左右间距
     */
    float moduleBoxLRInterval = 37.5f;
    /**
     * Module上下间距
     */
    int moduleBoxUDInterval = 20;
    /**
     * ModuleBox宽度
     */
    int moduleBoxWidth = 175;
    /**
     * ModuleBox高度
     */
    int moduleBoxHeight = 150;
    /**
     * Module上下窗口边距
     */
    int moduleUDMargin = 15;
    /**
     * Module左右窗口边距
     */
    int moduleLRMargin = 25;
    /**
     * Modules标题区域高度
     */
    int moduleBoxTitleHeight = 20;
    /**
     * GUI窗口定位x轴
     */
    int posX = 0;
    /**
     * GUI窗口定位y轴
     */
    int posY = 0;
    /**
     * 点击拖动定位x轴
     */
    int posInClickX;
    /**
     * 点击拖动定位y轴
     */
    int posInClickY;
    /**
     * 窗口宽度
     */
    int windowWidth = 650;
    /**
     * 窗口高度
     */
    int windowHeight = 425;
    /**
     * 上标题高度
     */
    int upSide = 50;
    /**
     * 下标题高度
     */
    int downSide = 25;
    /**
     * 每行最多编辑数
     */
    int maxWCount = 3;
    /**
     * 页码标签宽度
     */
    float pageNumBarX = 32.0f;
    /**
     * 页码标签高度
     */
    float pageNumBarY = 18;
    /**
     * 页码标签间距
     */
    float pageNumBarInterval = 16.0f;
    /**
     * 页码
     */
    int currentPage = 1;

    /**
     * 该moduleType下module总和
     */
    ArrayList<PageNumBar> PageNumBars = new ArrayList<>();
    BoundedAnimation windowAlpha = new BoundedAnimation(125, 220, 888f, false, Easing.LINEAR);
    BoundedAnimation moduleAlpha = new BoundedAnimation(165, 250, 550f, false, Easing.LINEAR);

    /**
     * 该moduleType下moduleHeader总和
     */
    List<ModuleHeader> moduleHeaders = new ArrayList<>();//初始化加载ModuleType类型的ModuleList
    /**
     * 窗口背景化透明值
     */
    int windows_Alpha = 0;
    /**
     * Module背景透明质
     */
    int module_Alpha = 0;
    /**
     * Value Y坐标增值存储
     */
    boolean closed;

    /*
    绘制开始
     */
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        float scale = (float) MenuMotion.getMenuMotion().getAnimationFactor();
        int bcColor = new Color(175, 175, 175, windows_Alpha).getRGB();         //定义背景颜色局部变量
        int titleColor = new Color(52, 81, 129, windows_Alpha).getRGB();        //定义标题背景颜色局部变量
        int moduleBGColor = new Color(240, 240, 240, windows_Alpha).getRGB();   //定义Module背景颜色局部变量
        int typeBoxColor = new Color(94, 164, 255, windows_Alpha).getRGB();     //定义ModuleType被选中颜色局部变量
        int valueFontColor = new Color(39, 37, 42, 130).getRGB();            //定义Value字体颜色局部变量

//        int moduleColor = new Color(205, 174, 161, 175).getRGB();                     //Module颜色-背景暗色
//        int moduleCurrentColor = new Color(171, 157, 242, 175).getRGB();
        int moduleCurrentColor = new Color(255, 68, 0, 130).getRGB();           //Module颜色-背景亮色
        int moduleColor = new Color(74, 38, 255, 130).getRGB();
        int width = posX + windowWidth;                                 //更新窗口宽度
        int height = posY + windowHeight;                               //更新窗口高度
        CFontRenderer minFont = sense.fontBuffer.EN16;
        CFontRenderer midFont = sense.fontBuffer.EN24;
        CFontRenderer valueFont = sense.fontBuffer.EN18;
        CFontRenderer bigFont = sense.fontBuffer.EN36;
        /*
        判定鼠标是否按下来对窗口透明值进行自增自减
         */

        windows_Alpha = (int) windowAlpha.getAnimationValue();
        if (Mouse.isButtonDown(0)) {
            windowAlpha.setState(true);
            windows_Alpha = (int) windowAlpha.getAnimationValue();
        } else {
            windowAlpha.setState(false);
        }

        /*
        在mouseClicked方法中判断鼠标点击事件的触发
        修正鼠标位置与窗口的坐标关系
         */
        if (clickDag) {
            posX = mouseX - posInClickX;
            posY = mouseY - posInClickY;
            //enumSelected动画更新
            for (ModuleHeader moduleHeader : SenseHeader.sense.getModuleManager().getModuleListByModuleType(SecilyMenu.currentModuleType)) {
                for (ValueHeader valueHeader : moduleHeader.getValueListByValueType(ValueHeader.ValueType.ENUM_TYPE)) {
                    SubEnumValueHeader subEnumValueHeader = valueHeader.getCurrentSubEnumHeader();
                    valueHeader.selectedEnumBGAnim = new BoundedAnimation(subEnumValueHeader.x1, subEnumValueHeader.x1, 0, false, Easing.LINEAR);
                }
            }
        }
        if (clickDag1) {
            //enumSelected动画更新
            for (ModuleHeader moduleHeader : SenseHeader.sense.getModuleManager().getModuleListByModuleType(SecilyMenu.currentModuleType)) {
                for (ValueHeader valueHeader : moduleHeader.getValueListByValueType(ValueHeader.ValueType.ENUM_TYPE)) {
                    SubEnumValueHeader subEnumValueHeader = valueHeader.getCurrentSubEnumHeader();
                    valueHeader.selectedEnumBGAnim = new BoundedAnimation(subEnumValueHeader.x1, subEnumValueHeader.x1, 0, false, Easing.LINEAR);
                }
            }
            clickDag1 = false;
        }
        //double类型拖动
        for (ModuleHeader moduleHeader : SenseHeader.sense.getModuleManager().getModuleListByModuleType(SecilyMenu.currentModuleType)) {
            for (ValueHeader valueHeader : moduleHeader.getValueListByValueType(ValueHeader.ValueType.DOUBLE)) {
                if (valueHeader.onClicking) {
                    double[] ds = valueHeader.getDoubles();
                    int finalX;
                    int barLength;
                    finalX = Math.max(mouseX, valueHeader.x1);
                    finalX = Math.min(finalX, valueHeader.x2);
                    barLength = valueHeader.x2 - valueHeader.x1;
                    double factor = (double) (finalX - valueHeader.x1) / (double) barLength;
                    factor = Math.floor((factor * (ds[2] - ds[0])) / ds[3]);
                    valueHeader.setDoubles(new double[]{ds[0], ds[0] + factor * ds[3], ds[2], ds[3]});
                }
            }
        }

        GlStateManager.pushMatrix();
        //背景
        GL11.glScaled(scale, scale, 1);
        RenderUtil.drawRound(posX, posY, width, height, bcColor, bcColor);
        //上标题背景
        RenderUtil.drawRound(posX, posY, width, posY + upSide, titleColor, titleColor);
        //下标题背景
        RenderUtil.drawRound(posX, posY + windowHeight - downSide, width, posY + windowHeight, titleColor, titleColor);
        GlStateManager.popMatrix();
        if (MenuMotion.getMenuMotion().getAnimationFactor() == 1) {
            float xAxis = 0;
            for (ModuleType moduleType : ModuleType.values()) {
                //通过x坐标+(窗口宽度-绘制ModuleType选择区的宽度)/2的算法进行遍历，计算ModuleType图标绘制区域坐标
                float[] moduleTypePosInfo = new float[]{posX + xAxis + (windowWidth - typeSideSize * 5 - moduleTypeInterval * 4) / 2f, posY + 5, posX + xAxis + (windowWidth - typeSideSize * 5 - moduleTypeInterval * 4) / 2f + typeSideSize, posY + 5 + typeSideHeight};
                if (moduleType == currentModuleType) {
                    RenderUtil.drawRound(moduleTypePosInfo[0], moduleTypePosInfo[1], moduleTypePosInfo[2], moduleTypePosInfo[3], typeBoxColor, typeBoxColor);
                    minFont.drawCenteredString(currentModuleType.name(), moduleTypePosInfo[0] + typeSideSize / 2f, moduleTypePosInfo[1] + typeSideHeight - minFont.getHeight(false), -1);
                 /*
                 每绘制一个Module编辑区计数器+1，在计数器不大于ModuleType分支下的Module数量之前一直执行，（也就是在绘制了6个Module编辑区之前每绘制一个width坐标就会增加）
                 */
                    moduleHeaders = sense.getModuleManager().getModuleListByModuleType(moduleType);
                /*
                防止从已经翻到第n页后切换module类型导致而页码未重置导致的显示bug
                 */
                    if (6 * (currentPage - 1) >= (moduleHeaders.size() - 1) && currentPage > 1) {
                        currentPage = 1;
                    }

                    /*
                    两if计算每个module的绘制位置
                     */
                    for (int i = 0; i < moduleHeaders.size(); i++) {
                        ModuleHeader moduleHeader = moduleHeaders.get(i);
                        int nDrawCount = (i % 6);
                        if (nDrawCount < sense.getModuleManager().getModuleListByModuleType(moduleType).size()) {

                            moduleHeader.modulePosInfo = new float[]{0f, 0f, 0f, posY + moduleUDMargin + moduleBoxHeight + upSide};
                            moduleHeader.modulePosInfo[0] = posX + moduleLRMargin + (moduleBoxLRInterval + moduleBoxWidth) * nDrawCount;
                            moduleHeader.modulePosInfo[1] = posY + moduleUDMargin + upSide;
                            moduleHeader.modulePosInfo[2] = posX + moduleLRMargin + moduleBoxWidth + (moduleBoxLRInterval + moduleBoxWidth) * nDrawCount;
                        }
                        if (nDrawCount >= maxWCount) {
                            moduleHeader.modulePosInfo = new float[4];
                            moduleHeader.modulePosInfo[0] = posX + moduleLRMargin + (moduleBoxLRInterval + moduleBoxWidth) * (nDrawCount - maxWCount);
                            moduleHeader.modulePosInfo[1] = posY + moduleUDMargin + upSide + moduleBoxHeight + moduleBoxUDInterval;
                            moduleHeader.modulePosInfo[2] = posX + moduleLRMargin + moduleBoxWidth + (moduleBoxLRInterval + moduleBoxWidth) * (nDrawCount - maxWCount);
                            moduleHeader.modulePosInfo[3] = posY + moduleUDMargin + moduleBoxHeight + upSide + moduleBoxHeight + moduleBoxUDInterval;
                        }


                    }
                /*
                绘制module层
                 */
                    for (int i = 6 * (currentPage - 1); i < (Math.min((currentPage * 6), moduleHeaders.size())); i++) {
                        int moduleCurrentBGColor;
                        int moduleCurrentTitleOPorDis;
                        ModuleHeader module = moduleHeaders.get(i);
                        moduleCurrentTitleOPorDis = module.moduleMotionColor.getColour().getRGB();
                        if (Mouse.isButtonDown(0) && module.menuFlag) {
                            moduleAlpha.setState(true);
                            module_Alpha = (int) moduleAlpha.getAnimationValue();
                        } else moduleAlpha.setState(false);
                        moduleCurrentBGColor = new Color(200, 200, 240, module_Alpha).getRGB();


                        /*
                        背景
                         */
                        RenderUtil.drawRound(module.modulePosInfo[0], module.modulePosInfo[1], module.modulePosInfo[2], module.modulePosInfo[3], module.menuFlag ? moduleCurrentBGColor : moduleBGColor, module.menuFlag ? moduleCurrentBGColor : moduleBGColor);
                        /*
                        上标题背景
                         */
                        RenderUtil.drawRound(module.modulePosInfo[0], module.modulePosInfo[1], module.modulePosInfo[2], module.modulePosInfo[1] + moduleBoxTitleHeight, moduleCurrentTitleOPorDis, moduleCurrentTitleOPorDis);
                        /*
                        下标题背景
                         */
                        String str = module.isIsOpen() ? "Enabled" : "Disabled";
                        RenderUtil.drawRound(module.modulePosInfo[0], module.modulePosInfo[1] + moduleBoxHeight - moduleBoxTitleHeight, module.modulePosInfo[2], module.modulePosInfo[1] + moduleBoxHeight, moduleCurrentTitleOPorDis, moduleCurrentTitleOPorDis);
                        //E/D
                        if (module != sense.getModuleManager().getModuleByClass(SettingMenu.class)) {
                            RenderUtil.drawRound(module.modulePosInfo[0] + 7, module.modulePosInfo[1] + moduleBoxHeight - moduleBoxTitleHeight + 1.5f, module.modulePosInfo[0] + midFont.getStringWidth(str) + 13, module.modulePosInfo[1] + moduleBoxHeight - 1.5f, moduleBGColor, moduleBGColor);
                            midFont.drawString(str, module.modulePosInfo[0] + 10, module.modulePosInfo[1] + moduleBoxHeight - moduleBoxTitleHeight / 1.5f, 1);
                        }
                        bigFont.drawCenteredString(module.getModuleName(), module.modulePosInfo[0] + moduleBoxWidth / 2f, module.modulePosInfo[1] + bigFont.getHeight(false) / 3f, module.menuFlag ? moduleCurrentColor : moduleColor);

                        //滚轮处理
                        if (isHovering(mouseX, mouseY, module.modulePosInfo[0], module.modulePosInfo[1], module.modulePosInfo[2], module.modulePosInfo[3])) {
                            currentModule = module;
                            int mouseWheel = Mouse.getDWheel();
                            if (mouseWheel > 0 && module.valueWheelY > 0) module.valueWheelY -= 1;
                            if (mouseWheel < 0 && module.valueWheelY < module.getValueList().size() - 1)
                                module.valueWheelY += 1;
                        }
                /*
                Values绘制
                 */
                        int yValue = 0;
                        int skipValue = 0;
                        /*
                        boolean类型value绘制
                         */
                        for (ValueHeader valueHeader : module.getValueListByValueType(ValueHeader.ValueType.BOOLEAN)) {
                            if (module.valueWheelY > 0 && skipValue < module.valueWheelY) {
                                valueHeader.posDel();
                                valueHeader.visible = false;
                                skipValue++;
                                continue;
                            }
                            float valueX = module.modulePosInfo[0] + 20;
                            float valueY = module.modulePosInfo[1] + yValue + bigFont.getHeight(false) + 5;
                            if (valueY + valueFont.getHeight(false) > (module.modulePosInfo[1] + moduleBoxHeight - moduleBoxTitleHeight))
                                break;
                            String valueStr = valueHeader.getValueName() + ": ";
                            valueFont.drawString(valueStr, valueX, valueY, valueFontColor);
                            valueHeader.visible = true;
                            yValue += (valueFont.getHeight(false) * 1.5f);

                            valueHeader.x1 = (int) (module.modulePosInfo[2] - 55);
                            valueHeader.y1 = (int) valueY;
                            valueHeader.draw();
                        }
                        /*
                        enum类型value绘制
                         */
                        for (ValueHeader valueHeader : module.getValueListByValueType(ValueHeader.ValueType.ENUM_TYPE)) {
                            if (module.valueWheelY > 0 && skipValue < module.valueWheelY) {
                                valueHeader.posDel();
                                valueHeader.visible = false;
                                skipValue++;
                                continue;
                            }
                            float valueX = module.modulePosInfo[0] + 20;
                            float valueY = module.modulePosInfo[1] + yValue + bigFont.getHeight(false) + 5;
                            if (valueY + valueFont.getHeight(false) > (module.modulePosInfo[1] + moduleBoxHeight - moduleBoxTitleHeight))
                                break;
                            String valueStr = valueHeader.getValueName() + ": ";
                            valueFont.drawString(valueStr, valueX, valueY, valueFontColor);
                            valueHeader.visible = true;
                            yValue += (valueFont.getHeight(false) * 1.5f);

                            valueHeader.x1 = (int) module.modulePosInfo[2];
                            valueHeader.y1 = (int) valueY;
                            valueHeader.x2 = (int) (module.modulePosInfo[2] - 16);
                            valueHeader.draw();
                            if (!valueHeader.visible) {
                                SubEnumValueHeader subEnumValueHeader = valueHeader.getCurrentSubEnumHeader();
                                valueHeader.selectedEnumBGAnim = new BoundedAnimation(subEnumValueHeader.x1, subEnumValueHeader.x1, 0, false, Easing.LINEAR);
                            }
                        }
                        /*
                        double类型value绘制
                         */
                        for (ValueHeader valueHeader : module.getValueListByValueType(ValueHeader.ValueType.DOUBLE)) {
                            if (module.valueWheelY > 0 && skipValue < module.valueWheelY) {
                                valueHeader.posDel();
                                valueHeader.visible = false;
                                skipValue++;
                                continue;
                            }
                            float valueX = module.modulePosInfo[0] + 20;
                            float valueY = module.modulePosInfo[1] + yValue + bigFont.getHeight(false) + 5;
                            if (valueY + valueFont.getHeight(false) > (module.modulePosInfo[1] + moduleBoxHeight - moduleBoxTitleHeight))
                                break;
                            String valueStr = valueHeader.getValueName() + ": ";
                            valueFont.drawString(valueStr, valueX, valueY, valueFontColor);
                            valueHeader.visible = true;
                            yValue += (valueFont.getHeight(false) * 1.5f);

                            valueHeader.x1 = (int) (valueX + valueFont.getStringWidth(valueStr));
                            valueHeader.y1 = (int) valueY;
                            valueHeader.x2 = (int) (module.modulePosInfo[2] - 16);
                            valueHeader.draw();

                        }
                        /*
                        String类型value绘制
                         */
                        for (ValueHeader valueHeader : module.getValueListByValueType(ValueHeader.ValueType.STRING)) {
                            if (module.valueWheelY > 0 && skipValue < module.valueWheelY) {
                                valueHeader.posDel();
                                valueHeader.visible = false;
                                skipValue++;
                                continue;
                            }
                            float valueX = module.modulePosInfo[0] + 20;
                            float valueY = module.modulePosInfo[1] + yValue + bigFont.getHeight(false) + 5;
                            if (valueY + valueFont.getHeight(false) > (module.modulePosInfo[1] + moduleBoxHeight - moduleBoxTitleHeight))
                                break;
                            String valueStr = valueHeader.getValueName() + ": ";
                            valueFont.drawString(valueStr, valueX, valueY, valueFontColor);
                            valueHeader.visible = true;
                            yValue += (valueFont.getHeight(false) * 1.5f);

                            valueHeader.x1 = (int) (valueX + valueFont.getStringWidth(valueStr));
                            valueHeader.y1 = (int) valueY;
                            valueHeader.x2 = (int) (module.modulePosInfo[2] - 16);
                            valueHeader.draw();
                        }

                    }

                /*
                计算当前Module类型一共多少页
                 */
                    int modulePageMAXIndex = (moduleHeaders.size() / 6) + 1;
                /*
                计算页码标签坐标
                 */
                    float[] fstPageBarPos = new float[4];
                    fstPageBarPos[0] = posX + (windowWidth / 2f) - ((modulePageMAXIndex / 2f) * pageNumBarX) - (((modulePageMAXIndex / 2f) - 0.5f) * pageNumBarInterval);
                    fstPageBarPos[1] = posY + windowHeight - (downSide / 2f) - (pageNumBarY / 2);
                    fstPageBarPos[2] = fstPageBarPos[0] + pageNumBarX;
                    fstPageBarPos[3] = fstPageBarPos[1] + pageNumBarY;
                    PageNumBars = new ArrayList<>();
                    PageNumBars.add(new PageNumBar(fstPageBarPos[0], fstPageBarPos[1], fstPageBarPos[2], fstPageBarPos[3], 1));
                    for (int i = 0; i < modulePageMAXIndex; i++) {
                        if (i != 0) {
                            float[] P = new float[4];
                            P[0] = fstPageBarPos[0] + (i * (pageNumBarX + pageNumBarInterval));
                            P[1] = fstPageBarPos[1];
                            P[2] = fstPageBarPos[2] + (i * (pageNumBarX + pageNumBarInterval));
                            P[3] = fstPageBarPos[3];
                            PageNumBars.add(new PageNumBar(P[0], P[1], P[2], P[3], i + 1));
                        }
                    }
                /*
                绘制页码标签
                 */
                    for (int i = 0; i < modulePageMAXIndex; i++) {
                        RenderUtil.drawRound(PageNumBars.get(i).x1, PageNumBars.get(i).y1, PageNumBars.get(i).x2, PageNumBars.get(i).y2, typeBoxColor, typeBoxColor);
                        bigFont.drawCenteredString(String.valueOf(i + 1), PageNumBars.get(i).x1 + pageNumBarX / 2f, PageNumBars.get(i).y1 + bigFont.getHeight(false) / 2f - 5, currentPage == PageNumBars.get(i).num ? moduleBGColor : bcColor);
                        fstPageBarPos[0] = fstPageBarPos[0] + pageNumBarX + pageNumBarInterval;
                        fstPageBarPos[2] = fstPageBarPos[2] + pageNumBarX + pageNumBarInterval;
                    }
                }
                Character icon = 'A';
                switch (moduleType) {
                    case COMBAT:
                        icon = 'A';
                        break;
                    case MISC:
                        icon = 'B';
                        break;
                    case MOVE:
                        icon = 'C';
                        break;
                    case PLAYER:
                        icon = 'D';
                        break;
                    case RENDER:
                        icon = 'E';
                        break;
                }
                sense.fontBuffer.ICON64.drawString(String.valueOf(icon), moduleTypePosInfo[0] + (typeSideSize - typeICONSize) / 2f + 3, moduleTypePosInfo[3] - sense.fontBuffer.ICON64.getHeight(false) / 3f - 3, -1);
                xAxis += moduleTypeInterval + typeSideSize;

            }
        }
        if (closed) {
            if (MenuMotion.getMenuMotion().getAnimationFactor() == 0) IMC.mc.displayGuiScreen(null);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        clickDag = mouseButton == 0 && mouseX > posX && mouseX < (posX + windowWidth) && mouseY > posY && mouseY < posY + upSide;
        int moduleTypeInterval = SecilyMenu.moduleTypeInterval;//ModuleType间隔
        int typeSideSize = this.typeSideSize;//ModuleTypeIcon大小
        CFontRenderer midFont = sense.fontBuffer.EN24;
        float xAxis = 0;
        /*
        ModuleType切换判定
         */
        for (ModuleType value : ModuleType.values()) {
            if (mouseButton == 0 && isMouseClickedInside(mouseX, mouseY, posX + xAxis + (windowWidth - typeSideSize * 5 - moduleTypeInterval * 4) / 2f, posY + 5, posX + xAxis + (windowWidth - typeSideSize * 5 - moduleTypeInterval * 4) / 2f + typeSideSize, posY + 5 + typeSideSize)) {
                currentModuleType = value;
            }
            xAxis += moduleTypeInterval + typeSideSize;
            for (ModuleHeader moduleHeader : sense.getModuleManager().getModuleListByModuleType(currentModuleType)) {
                if (moduleHeader.isIsOpen()) moduleHeader.moduleMotionColor.setState(true);
            }
        }
        for (ModuleHeader modules : moduleHeaders) {
            String str = modules.isIsOpen() ? "Enabled" : "Disabled";
            //切换选中Module
            modules.menuFlag = isMouseClickedInside(mouseX, mouseY, modules.modulePosInfo[0], modules.modulePosInfo[1], modules.modulePosInfo[2], modules.modulePosInfo[3]);
            //开关Module
            float p1, p2, p3, p4 = 0;
            p1 = modules.modulePosInfo[0] + 7;
            p2 = modules.modulePosInfo[1] + moduleBoxHeight - moduleBoxTitleHeight + 1.5f;
            p3 = modules.modulePosInfo[0] + midFont.getStringWidth(str) + 13;
            p4 = modules.modulePosInfo[1] + moduleBoxHeight - 1.5f;
            boolean p5 = modules != sense.getModuleManager().getModuleByClass(SettingMenu.class);
            if (isMouseClickedInside(mouseX, mouseY, p1, p2, p3, p4) && p5) {
                modules.moduleMotionColor.setState(!modules.isIsOpen());
                modules.toggle();
            }
            /*
            调value的判定
             */
            for (ValueHeader value : modules.getValueList()) {
                //boolean切换
                if (isMouseClickedInside(mouseX, mouseY, value.x1, value.y1, value.x2, value.y2)) {
                    if (value.getValueType().equals(ValueHeader.ValueType.BOOLEAN))
                        value.setOptionOpen(!value.isOptionOpen());
                }
                //enum切换
                if (value.getValueType().equals(ValueHeader.ValueType.ENUM_TYPE)) {
                    for (SubEnumValueHeader subEnumValueHeader : value.subEnumValueHeaders) {
                        if (isMouseClickedInside(mouseX, mouseY, subEnumValueHeader.x1, subEnumValueHeader.y1, subEnumValueHeader.x2, subEnumValueHeader.y2)) {
                            if (!value.getCurrentEnumType().equalsIgnoreCase(subEnumValueHeader.name)) {
                                value.selectedEnumBGAnim = new BoundedAnimation(value.getCurrentSubEnumHeader().x1, subEnumValueHeader.x1, 660f, false, Easing.LINEAR);
                                value.selectedEnumBGAnim.setState(true);
                                value.setCurrentEnumType(subEnumValueHeader.name);
                            }
                        }
                    }
                }
                //double拖动调整
                if (value.getValueType().equals(ValueHeader.ValueType.DOUBLE)) {
                    if (isMouseClickedInside(mouseX, mouseY, value.x1, value.y1, value.x2, value.y2))
                        value.onClicking = true;
                }
            }
        }
        for (PageNumBar page : PageNumBars) {
            if (isMouseClickedInside(mouseX, mouseY, page.x1, page.y1, page.x2, page.y2)) {
                currentPage = page.num;
            }
        }
        if (clickDag) {
            posInClickX = mouseX - posX;
            posInClickY = mouseY - posY;
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    boolean isMouseClickedInside(int Mx, int My, float x1, float y1, float x2, float y2) {
        return Mx > x1 && My > y1 && Mx < x2 && My < y2;
    }

    private boolean isHovering(int mouseX, int mouseY, float xLeft, float yUp, float xRight, float yBottom) {
        return mouseX > xLeft && mouseX < xRight && mouseY > yUp && mouseY < yBottom;
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        if (clickDag) {
            clickDag1 = true;
        }
        clickDag = false;
        for (ModuleHeader moduleHeader : sense.moduleManager.getModuleListByModuleType(currentModuleType)) {
            for (ValueHeader valueHeader : moduleHeader.getValueListByValueType(ValueHeader.ValueType.DOUBLE)) {
                valueHeader.onClicking = false;
            }
        }
        super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == 1) {
            closed = true;
            sense.getModuleManager().getModuleByClass(SettingMenu.class).toggle();
        }
        super.keyTyped(typedChar, keyCode);
    }
}
