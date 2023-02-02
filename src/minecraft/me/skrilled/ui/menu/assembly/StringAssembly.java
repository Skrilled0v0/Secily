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
import me.skrilled.ui.menu.ui.SecilyUserInterface;
import me.skrilled.utils.render.RenderUtil;
import me.surge.animation.Animation;
import net.minecraft.client.renderer.GlStateManager;

import java.awt.*;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;

public class StringAssembly extends Assembly {
    String value;
    boolean centered;
    Color bgColor;
    Color onSelectedBGColor;
    Color fontColor;
    boolean border;
    FontDrawer font;
    float radius;

    public StringAssembly(float[] pos, WindowAssembly fatherWindow, String value, boolean centered, Color bgColor, Color fontColor, FontDrawer font, float radius) {
        super(pos, fatherWindow);
        this.value = value;
        this.centered = centered;
        this.bgColor = bgColor;
        this.fontColor = fontColor;
        this.font = font;
        this.radius = radius;
    }

    public StringAssembly(float[] pos, WindowAssembly fatherWindow, String value, boolean centered, Color bgColor, Color onSelectedBGColor, Color fontColor, FontDrawer font, float radius) {
        super(pos, fatherWindow);
        this.value = value;
        this.centered = centered;
        this.bgColor = bgColor;
        this.onSelectedBGColor = onSelectedBGColor;
        this.fontColor = fontColor;
        this.font = font;
        this.radius = radius;
    }

    public StringAssembly(float[] pos, WindowAssembly fatherWindow, String value, boolean centered, Color bgColor, Color fontColor, boolean border, FontDrawer font) {
        super(pos, fatherWindow);
        this.value = value;
        this.centered = centered;
        this.bgColor = bgColor;
        this.fontColor = fontColor;
        this.border = border;
        this.font = font;
    }

    public static float drawCenteredStringBox_P(float[] pos, FontDrawer font, String str, int bgColor, int fontColor, float radius) {
        float fontHeight = font.getHeight();
        float udMargin = fontHeight / 4f;
        float lrMargin = 1.5f * font.getCharWidth('A');
        float lineSpacing = fontHeight * 0.6f;
        float boxHeight;
        float maxStringWidth = pos[2] - pos[0] - 2f * lrMargin;
        int row = 1;
        ArrayList<Integer> split = new ArrayList<>();
        int strHead = 0;
        split.add(strHead);
        for (int i = 0; i < str.length(); i++) {
            if (font.getStringWidth(str.substring(strHead, i)) > maxStringWidth) {
                strHead = i;
                split.add(i);
                row++;
            }
        }
        boxHeight = row * (fontHeight + lineSpacing) - lineSpacing + 2 * udMargin;
        drawRoundRect(pos[0], pos[1], pos[2], pos[3], radius, bgColor);
        for (int i = 0; i < row - 1; i++) {
            String s1 = str.substring(split.get(i), split.get(i + 1));
            font.drawString(s1, pos[0] + lrMargin + (maxStringWidth - font.getStringWidth(s1)) / 2f, pos[1] + udMargin + i * (fontHeight + lineSpacing), fontColor);
        }
        String s1 = str.substring(split.get(row - 1));
        font.drawString(s1, pos[0] + lrMargin + (maxStringWidth - font.getStringWidth(s1)) / 2f, pos[1] + udMargin + (row - 1) * (fontHeight + lineSpacing), fontColor);
        return boxHeight;
    }

    public static float drawStringBox_P(float[] pos, FontDrawer font, String str, int bgColor, int fontColor) {
        float fontHeight = font.getHeight();
        float udMargin = fontHeight / 4f;
        float lrMargin = 1.5f * font.getCharWidth('A');
        float lineSpacing = fontHeight * 0.6f;
        float boxHeight;
        float maxStringWidth = pos[2] - pos[0] - 2f * lrMargin;
        //计算所需行数
        int row = 1;
        ArrayList<Integer> split = new ArrayList<>();
        int strHead = 0;
        split.add(strHead);
        for (int i = 0; i < str.length(); i++) {
            if (font.getStringWidth(str.substring(strHead, i)) > maxStringWidth) {
                strHead = i;
                split.add(i);
                row++;
            }
        }
        boxHeight = row * (fontHeight + lineSpacing) - lineSpacing + 2 * udMargin;
        //画背景
        drawRoundRect(pos[0], pos[1], pos[2], pos[3], font.getHeight() / 2f, bgColor);
        //画文字前row-1行
        for (int i = 0; i < row - 1; i++) {
            font.drawString(str.substring(split.get(i), split.get(i + 1)), pos[0] + lrMargin, pos[1] + udMargin + i * (fontHeight + lineSpacing), fontColor);
        }
        //画文字最后一行
        font.drawString(str.substring(split.get(row - 1)), pos[0] + lrMargin, (pos[1] + pos[3] - fontHeight) / 2 + (row - 1) * (fontHeight + lineSpacing), fontColor);
        return boxHeight;
    }

    public static void drawRoundRect(float startX, float startY, float width, float height, float radius, int rgba) {
        float z;
        if (startX > width) {
            z = startX;
            startX = width;
            width = z;
        }

        if (startY > height) {
            z = startY;
            startY = height;
            height = z;
        }
        double x1 = startX + radius;
        double y1 = startY + radius;
        double x2 = width - radius;
        double y2 = height - radius;
        Color color = new Color(rgba, true);
        float r = color.getRed() / 255f;
        float g = color.getGreen() / 255f;
        float b = color.getBlue() / 255f;
        float a = color.getAlpha() / 255f;
        glPushMatrix();
        glEnable(3042);
        glDisable(3553);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(2848);

        glBegin(GL_POLYGON);
        glColor4f(r, g, b, a);
        double degree = Math.PI / 180;
        for (double i = 0; i <= 90; i += 1) {
            glColor4f(r, g, b, a);
            glVertex2d(x2 + Math.sin(i * degree) * radius, y2 + Math.cos(i * degree) * radius);
        }

        for (double i = 90; i <= 180; i += 1) {
            glColor4f(r, g, b, a);
            glVertex2d(x2 + Math.sin(i * degree) * radius, y1 + Math.cos(i * degree) * radius);
        }

        for (double i = 180; i <= 270; i += 1) {
            glColor4f(r, g, b, a);
            glVertex2d(x1 + Math.sin(i * degree) * radius, y1 + Math.cos(i * degree) * radius);
        }

        for (double i = 270; i <= 360; i += 1) {
            glColor4f(r, g, b, a);
            glVertex2d(x1 + Math.sin(i * degree) * radius, y2 + Math.cos(i * degree) * radius);
        }

        glEnd();
        glEnable(3553);
        glDisable(3042);
        glDisable(2848);
        GlStateManager.disableBlend();
        glPopMatrix();
    }

    @Override
    public float draw() {
        if (this.assemblyName.equalsIgnoreCase(SecilyUserInterface.currentModule.toString())) {

        }
        if (autoPushPopMatrix) {
            if (centered)
                return RenderUtil.drawCenteredStringBox_P(calcAbsPos(), font, value, bgColor.getRGB(), fontColor.getRGB(), radius);
            else return RenderUtil.drawStringBox_P(calcAbsPos(), font, value, bgColor.getRGB(), fontColor.getRGB());
        } else {
            if (centered)
                return drawCenteredStringBox_P(calcAbsPos(), font, value, bgColor.getRGB(), fontColor.getRGB(), radius);
            else return drawStringBox_P(calcAbsPos(), font, value, bgColor.getRGB(), fontColor.getRGB());
        }
    }

    @Override
    public void mouseEventHandle(int mouseX, int mouseY, int button) {
    }
}
