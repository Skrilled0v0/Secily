/*
 *eclipse
 *Code by SkrilledSense
 *20221230
 */
package me.skrilled.ui.clickui;

import me.cubex2.ttfr.CFontRenderer;
import me.skrilled.api.modules.ModuleHeader;
import me.skrilled.api.modules.ModuleHeader.ModuleType;
import me.skrilled.api.modules.module.render.SettingMenu;
import me.skrilled.api.value.ValueHeader;
import me.skrilled.utils.IMC;
import me.skrilled.utils.render.RenderUtil;
import me.surge.animation.BoundedAnimation;
import me.surge.animation.Easing;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Menu extends GuiScreen implements IMC {
    public static ModuleType currentModuleType = ModuleType.COMBAT;
    /**
     * ModuleType的间隔
     */
    public static int moduleTypeInterval = 40;
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
     * 是否在拖动gui页面
     */
    boolean clickDag = false;
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
     * ValueList的上下间隔
     */
    int valueListUDInterval = 10;
    /**
     * 该moduleType下module总和
     */
    ArrayList<PageNumBar> PageNumBars = new ArrayList<>();
    BoundedAnimation windowAlpha = new BoundedAnimation(75, 220, 1800f, false, Easing.LINEAR);
    BoundedAnimation moduleAlpha = new BoundedAnimation(120, 250, 800f, false, Easing.LINEAR);
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
    boolean closeed;

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
        CFontRenderer minFont = sense.fontBuffer.font16;
        CFontRenderer midFont = sense.fontBuffer.font24;
        CFontRenderer valueFont = sense.fontBuffer.font18;
        CFontRenderer bigFont = sense.fontBuffer.font36;
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
                float[] moduleTypePosInfo = {posX + xAxis + (windowWidth - typeSideSize * 5 - moduleTypeInterval * 4) / 2f, posY + 5, posX + xAxis + (windowWidth - typeSideSize * 5 - moduleTypeInterval * 4) / 2f + typeSideSize, posY + 5 + typeSideHeight};
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

                            moduleHeader.modulePosInfo = new float[]{posX + moduleLRMargin + (moduleBoxLRInterval + moduleBoxWidth) * nDrawCount, posY + moduleUDMargin + upSide, posX + moduleLRMargin + moduleBoxWidth + (moduleBoxLRInterval + moduleBoxWidth) * nDrawCount, posY + moduleUDMargin + moduleBoxHeight + upSide};
                        }
                        if (nDrawCount >= maxWCount) {
                            moduleHeader.modulePosInfo = new float[]{posX + moduleLRMargin + (moduleBoxLRInterval + moduleBoxWidth) * (nDrawCount - maxWCount), posY + moduleUDMargin + upSide + moduleBoxHeight + moduleBoxUDInterval, posX + moduleLRMargin + moduleBoxWidth + (moduleBoxLRInterval + moduleBoxWidth) * (nDrawCount - maxWCount), posY + moduleUDMargin + moduleBoxHeight + upSide + moduleBoxHeight + moduleBoxUDInterval};
                        }


                    }
                /*
                绘制module层
                 */

                    for (int i = 6 * (currentPage - 1); i < (Math.min((currentPage * 6), moduleHeaders.size())); i++) {
                        int moduleCurrentBGColor;
                        ModuleHeader modules = moduleHeaders.get(i);
                        if (Mouse.isButtonDown(0) && modules.menuFlag) {
                            moduleAlpha.setState(true);
                            module_Alpha = (int) moduleAlpha.getAnimationValue();
                        } else moduleAlpha.setState(false);
                        moduleCurrentBGColor = new Color(200, 200, 240, module_Alpha).getRGB();
                        RenderUtil.drawRound(modules.modulePosInfo[0], modules.modulePosInfo[1], modules.modulePosInfo[2], modules.modulePosInfo[3], modules.menuFlag ? moduleCurrentBGColor : moduleBGColor, modules.menuFlag ? moduleCurrentBGColor : moduleBGColor);
                        bigFont.drawCenteredString(modules.getModuleName(), modules.modulePosInfo[0] + moduleBoxWidth / 2f, modules.modulePosInfo[1] + bigFont.getHeight(false) / 2f, modules.menuFlag ? moduleCurrentColor : moduleColor);
                /*
                Values绘制
                 */
                        int yAxsi = 0;
                        for (ValueHeader valueHeader : modules.getValueListByValueType(ValueHeader.ValueType.BOOLEAN)) {
                            String valueStr = valueHeader.getValueName() + ":";
                            midFont.drawString(valueStr, modules.modulePosInfo[0] + 20, modules.modulePosInfo[1] + yAxsi + bigFont.getHeight(false), valueFontColor);
                            yAxsi += (valueListUDInterval + midFont.getHeight(false));
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
                        midFont.drawCenteredString(String.valueOf(i + 1), PageNumBars.get(i).x1 + pageNumBarX / 2f, PageNumBars.get(i).y1 + 2.0f, currentPage == PageNumBars.get(i).num ? moduleBGColor : bcColor);
                        fstPageBarPos[0] = fstPageBarPos[0] + pageNumBarX + pageNumBarInterval;
                        fstPageBarPos[2] = fstPageBarPos[2] + pageNumBarX + pageNumBarInterval;
                    }
                }
                GlStateManager.color(1, 1, 1, 0.5f);
                RenderUtil.drawIcon(moduleTypePosInfo[0] + (typeSideSize - typeICONSize) / 2f, moduleTypePosInfo[1], typeICONSize, typeICONSize, new ResourceLocation("skrilled/MenuICON/" + moduleType + ".png"));
                xAxis += moduleTypeInterval + typeSideSize;
            }
        }
        if (closeed) {
            if (MenuMotion.getMenuMotion().getAnimationFactor() == 0) IMC.mc.displayGuiScreen(null);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        clickDag = mouseButton == 0 && mouseX > posX && mouseX < (posX + windowWidth) && mouseY > posY && mouseY < posY + upSide;
        int moduleTypeInterval = Menu.moduleTypeInterval;//ModuleType间隔
        int typeSideSize = this.typeSideSize;//ModuleTypeIcon大小
        float xAxis = 0;
        for (ModuleType value : ModuleType.values()) {
            if (mouseButton == 0 && mouseX > posX + xAxis + (windowWidth - typeSideSize * 5 - moduleTypeInterval * 4) / 2f && mouseY > posY + 5 && mouseX < posX + xAxis + (windowWidth - typeSideSize * 5 - moduleTypeInterval * 4) / 2f + typeSideSize && mouseY < posY + 5 + typeSideSize) {
                currentModuleType = value;

            }
            xAxis += moduleTypeInterval + typeSideSize;
        }
        for (ModuleHeader moduleHeader : moduleHeaders) {
            moduleHeader.menuFlag = mouseX > moduleHeader.modulePosInfo[0] && mouseY > moduleHeader.modulePosInfo[1] && mouseX < moduleHeader.modulePosInfo[2] && mouseY < moduleHeader.modulePosInfo[3];
        }
        for (PageNumBar page : PageNumBars) {
            if (mouseX > page.x1 && mouseY > page.y1 && mouseX < page.x2 && mouseY < page.y2) {
                currentPage = page.num;
            }
        }
        if (clickDag) {
            posInClickX = mouseX - posX;
            posInClickY = mouseY - posY;
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        clickDag = false;
        super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == 1) {
            closeed = true;
            sense.getModuleManager().getModuleByClass(SettingMenu.class).toggle();
        }
        super.keyTyped(typedChar, keyCode);
    }
}
