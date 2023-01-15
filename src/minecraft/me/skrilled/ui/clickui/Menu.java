/*
 *eclipse
 *Code by SkrilledSense
 *20221230
 */
package me.skrilled.ui.clickui;

import me.cubex2.ttfr.CFontRenderer;
import me.skrilled.api.modules.ModuleHeader;
import me.skrilled.api.modules.ModuleHeader.ModuleType;
import me.skrilled.utils.IMC;
import me.skrilled.utils.render.RenderUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.io.IOException;
import java.util.Collection;

public class Menu extends GuiScreen implements IMC {
    public static ModuleType currentModuleType = ModuleType.COMBAT;
    int moduleTypeInterval = 40;         //初始化ModuleType的间隔
    int typeSideSize = 40;               //初始化ModuleType的盒子宽度
    int typeICONSize = 30;               //初始化ModuleType的ICON宽度
    int typeSideHeight = 40;             //初始化ModuleType的盒子高度
    float moduleBoxLRInterval = 37.5f;   //初始化Module左右间距
    int moduleBoxUDInterval = 20;        //初始化Module上下间距
    int moduleBoxWidth = 175;           //初始化ModuleBox宽度
    int moduleBoxHeight = 150;          //初始化ModuleBox高度
    int moduleUDMargin = 15;            //初始化Module上下窗口边距
    int moduleLRMargin = 25;            //初始化Module左右窗口边距
    int posX = 0;                       //初始化窗口定位x轴
    int posY = 0;                       //初始化窗口定位y轴
    int posInClickX;                    //初始化点击拖动定位x轴
    int posInClickY;                    //初始化点击拖动定位y轴
    int windowAlpha = 125;              //初始化透明值
    int windowWidth = 650;              //初始化窗口宽度
    int windowHeight = 425;             //初始化窗口高度
    int upSide = 50;                    //初始化上标题高度
    int downSide = 25;                  //初始化下标题高度
    boolean clickDag = false;           //初始化点击判定布尔
    int modulePageIndex = 1;              //初始化页码
    int maxWCount = 3;                    //每行最多编辑区
    int maxHCount = 2;                    //最多行数
    float[] modulePosInfo = {posX + moduleLRMargin, posY + moduleUDMargin + upSide, posX + moduleLRMargin + moduleBoxWidth, posY + moduleUDMargin + moduleBoxHeight + upSide};

    /*
    绘制开始
     */
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        int bcColor = new Color(175, 175, 175, windowAlpha).getRGB();       //定义背景颜色局部变量
        int titleColor = new Color(52, 81, 129, windowAlpha).getRGB();      //定义标题背景颜色局部变量
        int moduleBGColor = new Color(240, 240, 240, windowAlpha).getRGB(); //定义Module背景颜色局部变量
        int typeBoxColor = new Color(94, 164, 255, windowAlpha).getRGB();
        int width = posX + windowWidth;                                             //更新窗口宽度
        int height = posY + windowHeight;                                           //更新窗口高度
        CFontRenderer minFont = sense.getFontBuffer().font16;
        CFontRenderer midFont = sense.getFontBuffer().font24;
        /*
        判定鼠标是否按下来对窗口透明值进行自增自减
         */
        if (Mouse.isButtonDown(0)) {
            if (windowAlpha < 200) {
                windowAlpha++;
            }
        } else if (windowAlpha > 125) {
            windowAlpha--;
        }
        /*
        在mouseClicked方法中判断鼠标点击事件的触发
        修正鼠标位置与窗口的坐标关系
         */
        if (clickDag) {
            posX = mouseX - posInClickX;
            posY = mouseY - posInClickY;
        }
        //背景
        RenderUtil.drawRound(posX, posY, width, height, bcColor, bcColor);
        //上标题背景
        RenderUtil.drawRound(posX, posY, width, posY + upSide, titleColor, titleColor);
        //下标题背景
        RenderUtil.drawRound(posX, posY + windowHeight - downSide, width, posY + windowHeight, titleColor, titleColor);
        float xAxis = 0;
        for (ModuleType moduleType : ModuleType.values()) {
            //通过x坐标+(窗口宽度-绘制ModuleType选择区的宽度)/2的算法进行遍历，计算ModuleType图标绘制区域坐标
            float[] moduleTypePosInfo = {posX + xAxis + (windowWidth - typeSideSize * 5 - moduleTypeInterval * 4) / 2f, posY + 5, posX + xAxis + (windowWidth - typeSideSize * 5 - moduleTypeInterval * 4) / 2f + typeSideSize, posY + 5 + typeSideHeight};
            if (moduleType == currentModuleType) {
                RenderUtil.drawRound(moduleTypePosInfo[0], moduleTypePosInfo[1], moduleTypePosInfo[2], moduleTypePosInfo[3], typeBoxColor, typeBoxColor);
                minFont.drawCenteredString(currentModuleType.name(), moduleTypePosInfo[0] + typeSideSize / 2f, moduleTypePosInfo[1] + typeSideHeight - minFont.getHeight(false), -1);
                int drawCount = 0;
                 /*
                 每绘制一个Module编辑区计数器+1，在计数器不大于ModuleType分支下的Module数量之前一直执行，（也就是在绘制了6个Module编辑区之前每绘制一个width坐标就会增加）
                 */
                for (ModuleHeader moduleHeader : sense.getModuleManager().getModuleListByModuleType(moduleType)) {

                    if (drawCount < sense.getModuleManager().getModuleListByModuleType(moduleType).size())
                        modulePosInfo = new float[]{posX + moduleLRMargin + (moduleBoxLRInterval + moduleBoxWidth) * drawCount, posY + moduleUDMargin + upSide, posX + moduleLRMargin + moduleBoxWidth + (moduleBoxLRInterval + moduleBoxWidth) * drawCount, posY + moduleUDMargin + moduleBoxHeight + upSide};
                    if (drawCount>=maxWCount)
                        modulePosInfo = new float[]{posX + moduleLRMargin + (moduleBoxLRInterval + moduleBoxWidth) * (drawCount - maxWCount), posY + moduleUDMargin + upSide + moduleBoxHeight + moduleBoxUDInterval, posX + moduleLRMargin + moduleBoxWidth + (moduleBoxLRInterval + moduleBoxWidth) * (drawCount - maxWCount), posY + moduleUDMargin + moduleBoxHeight + upSide + moduleBoxHeight + moduleBoxUDInterval};
                    RenderUtil.drawRound(modulePosInfo[0], modulePosInfo[1], modulePosInfo[2], modulePosInfo[3], moduleBGColor, moduleBGColor);
                    midFont.drawCenteredString(moduleHeader.getModuleName(), modulePosInfo[0] + moduleBoxWidth / 2f, modulePosInfo[1], -1);
                    drawCount++;
                    if (drawCount>=(maxWCount*maxWCount)) {
                        modulePageIndex += 1;//绘制到第七个编辑区时重置计数器并将页码+1
                    }

                }

            }
            GlStateManager.color(1, 1, 1, 0.5f);
            RenderUtil.drawIcon(moduleTypePosInfo[0] + (typeSideSize - typeICONSize) / 2f, moduleTypePosInfo[1], typeICONSize, typeICONSize, new ResourceLocation("skrilled/MenuICON/" + moduleType + ".png"));
            xAxis += moduleTypeInterval + typeSideSize;
        }

    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        clickDag = mouseButton == 0 && mouseX > posX && mouseX < width && mouseY > posY && mouseY < posY + 50;
        int moduleTypeInterval = this.moduleTypeInterval;//ModuleType间隔
        int typeSideSize = this.typeSideSize;//ModuleTypeIcon大小
        float xAxis = 0;
        for (ModuleType value : ModuleType.values()) {
            if (mouseButton == 0 && mouseX > posX + xAxis + (windowWidth - typeSideSize * 5 - moduleTypeInterval * 4) / 2f && mouseY > posY + 5 && mouseX < posX + xAxis + (windowWidth - typeSideSize * 5 - moduleTypeInterval * 4) / 2f + typeSideSize && mouseY < posY + 5 + typeSideSize) {
                currentModuleType = value;

            }
            xAxis += moduleTypeInterval + typeSideSize;
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

    enum ModulePage {
        P1(1), P2(2), P3(3);

        ModulePage(int i) {

        }
    }
}
