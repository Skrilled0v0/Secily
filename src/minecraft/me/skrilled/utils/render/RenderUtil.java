package me.skrilled.utils.render;

import com.mojang.authlib.GameProfile;
import me.fontloader.FontDrawer;
import me.skrilled.ui.menu.assembly.color.ColorPoint;
import me.skrilled.utils.IMC;
import me.skrilled.utils.math.TimerUtil;
import me.skrilled.utils.render.gl.GLClientState;
import me.skrilled.utils.render.tessellate.Tessellation;
import me.surge.animation.Animation;
import me.surge.animation.Easing;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.main.Main;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static org.lwjgl.opengl.GL11.*;

public class RenderUtil implements IMC {
    private static final List<Integer> csBuffer;
    private static final Consumer<Integer> ENABLE_CLIENT_STATE;
    private static final Consumer<Integer> DISABLE_CLIENT_STATE;
    private static final Map<Integer, Boolean> glCapMap = new HashMap<>();
    public static Tessellation tessellator;
    public volatile static ArrayList<ScissorPos> scissors = new ArrayList<>();
    static TimerUtil redT = new TimerUtil();
    static TimerUtil blueT = new TimerUtil();
    static int blueCount = 1;
    static int redCount = 1;

    static {
        tessellator = Tessellation.createExpanding(4, 1.0f, 2.0f);
        csBuffer = new ArrayList<>();
        ENABLE_CLIENT_STATE = GL11::glEnableClientState;
        DISABLE_CLIENT_STATE = GL11::glEnableClientState;
    }

    public static void reColor() {
        GL11.glColor4f(1, 1, 1, 1);
    }

    public static void drawSikadi(float x, float y, boolean isRed) {
        if (isRed) {

            drawIcon(x, y, 280 / 2, 320 / 2, Main.redlimgs.get(redCount - 1));
            if (redT.hasReached(50)) {
                redCount++;
                redT.reset();
            }
            if (redCount >= Main.REDINDEX) redCount = 1;
        } else {
            drawIcon(x, y, 452 / 2, 480 / 2, Main.bluelimgs.get(blueCount - 1));
            if (blueT.hasReached(50)) {
                blueCount++;
                blueT.reset();
            }
            if (blueCount >= Main.BLUEINDEX) blueCount = 1;
        }
    }

    public static Color rainbow(long time, float count, float fade) {
        float hue = ((float) time + (1.0F + count) * 2.0E8F) / 1.0E10F % 1.0F;
        long color = Long.parseLong(Integer.toHexString(Color.HSBtoRGB(hue, 1.0005F, 1.0F)), 16);
        Color c = new Color((int) color);
        return new Color((float) c.getRed() / 255.0F * fade, (float) c.getGreen() / 255.0F * fade, (float) c.getBlue() / 255.0F * fade, (float) c.getAlpha() / 255.0F);
    }

    public static void quickDrawRect(final float x, final float y, final float x2, final float y2) {
        glPushMatrix();
        glBegin(GL_QUADS);
        glVertex2d(x2, y);
        glVertex2d(x, y);
        glVertex2d(x, y2);
        glVertex2d(x2, y2);
        glEnd();
        glPopMatrix();
    }

    /**
     * ??????????????????????????????????????????????????????
     * <p>
     * ???????????????gl??????????????????????????????????????????????????????, ????????????????????????????????????????????????
     */
    public static void drawRoundRect(float startX, float startY, float endX, float endY, float radius, int rgba, boolean filled) {
        if ((endX - startX) < 1.999f * radius) {
            return;
        }
        if ((endY - startY) < 1.999f * radius) {
            return;
        }
        float z;
        if (startX > endX) {
            z = startX;
            startX = endX;
            endX = z;
        }

        if (startY > endY) {
            z = startY;
            startY = endY;
            endY = z;
        }
        double x1 = startX + radius;
        double y1 = startY + radius;
        double x2 = endX - radius;
        double y2 = endY - radius;
        Color color = new Color(rgba, true);
        float r = color.getRed() / 255f;
        float g = color.getGreen() / 255f;
        float b = color.getBlue() / 255f;
        float a = color.getAlpha() / 255f;
        glPushMatrix();
        glEnable(GL_BLEND);
        glDisable(GL_TEXTURE_2D);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_LINE_SMOOTH);

        if (filled) {
            glBegin(GL_POLYGON);
        } else {
            glLineWidth(5f);
            glBegin(GL_LINE_STRIP);
        }

        glColor4f(r, g, b, a);
        double degree = Math.PI / 180;
        for (double i = 0; i <= 90; i += 1) {
            glVertex2d(x2 + Math.sin(i * degree) * radius, y2 + Math.cos(i * degree) * radius);
        }

        for (double i = 90; i <= 180; i += 1) {
            glVertex2d(x2 + Math.sin(i * degree) * radius, y1 + Math.cos(i * degree) * radius);
        }

        for (double i = 180; i <= 270; i += 1) {
            glVertex2d(x1 + Math.sin(i * degree) * radius, y1 + Math.cos(i * degree) * radius);
        }

        for (double i = 270; i <= 360; i += 1) {
            glVertex2d(x1 + Math.sin(i * degree) * radius, y2 + Math.cos(i * degree) * radius);
        }
        glVertex2d(x2, y2 + radius);

        glEnd();
        glEnable(GL_TEXTURE_2D);
        glDisable(GL_BLEND);
        glDisable(GL_LINE_SMOOTH);
        GlStateManager.disableBlend();
        glPopMatrix();
    }

    /**
     * ??????????????????????????????
     */
    public static void quickRoundedRect(float paramXStart, float paramYStart, float paramXEnd, float paramYEnd, float radius) {
        float z = 0;
        if (paramXStart > paramXEnd) {
            z = paramXStart;
            paramXStart = paramXEnd;
            paramXEnd = z;
        }

        if (paramYStart > paramYEnd) {
            z = paramYStart;
            paramYStart = paramYEnd;
            paramYEnd = z;
        }

        double x1 = paramXStart + radius;
        double y1 = paramYStart + radius;
        double x2 = paramXEnd - radius;
        double y2 = paramYEnd - radius;

        glPushMatrix();
        glEnable(GL_LINE_SMOOTH);
        glLineWidth(1);

        glBegin(GL_POLYGON);

        double degree = Math.PI / 180;
        for (double i = 0; i <= 90; i += 1)
            glVertex2d(x2 + Math.sin(i * degree) * radius, y2 + Math.cos(i * degree) * radius);
        for (double i = 90; i <= 180; i += 1)
            glVertex2d(x2 + Math.sin(i * degree) * radius, y1 + Math.cos(i * degree) * radius);
        for (double i = 180; i <= 270; i += 1)
            glVertex2d(x1 + Math.sin(i * degree) * radius, y1 + Math.cos(i * degree) * radius);
        for (double i = 270; i <= 360; i += 1)
            glVertex2d(x1 + Math.sin(i * degree) * radius, y2 + Math.cos(i * degree) * radius);
        glEnd();
        glDisable(GL_LINE_SMOOTH);
        glPopMatrix();
    }

    /**
     * ??????????????????
     *
     * @param posX         ??????X
     * @param posY         ??????Y
     * @param buttonWidth  ????????????
     * @param buttonHeight ????????????
     * @param motion       ??????????????????
     * @param bgColor      ????????????
     * @param tureColor    ????????????
     * @param falseColor   ????????????
     */
    public static float drawBooleanButton(float posX, float posY, float buttonWidth, float buttonHeight, float motion, int bgColor, int tureColor, int falseColor) {
        int color;
        if (motion == 0) {
            color = falseColor;
        } else color = tureColor;
        float radius = buttonHeight / 2f;
        RenderUtil.drawRoundRect(posX, posY, posX + buttonWidth, posY + buttonHeight, radius, bgColor, true);
        RenderUtil.drawCircle(posX + radius + (buttonWidth - buttonHeight) * motion, posY + radius, radius, color);
        return buttonHeight;
    }

    /**
     * ?????????????????????
     * list????????????currentStr
     *
     * @param font       ??????
     * @param currentStr ???????????????
     * @param list       ???????????????
     * @param posX       ??????X
     * @param posY       ??????Y
     * @param motion     ??????
     * @param bgColor    ????????????
     * @param fontColor  ????????????
     * @return ????????????
     */
    public static float drawEnumTypeBox(FontDrawer font, String currentStr, ArrayList<String> list, float posX, float posY, float motion, int bgColor, int fontColor) {
        float maxStringWidth = 0f;
        //????????????StringWidth
        for (String s : list) {
            float fontStringWidth = font.getStringWidth(s);
            if (fontStringWidth > maxStringWidth) maxStringWidth = fontStringWidth;
        }
        //????????????
        float lrMargin = font.getCharWidth('A');
        //??????
        float fontHeight = font.getHeight();
        //????????????
        float udMargin = fontHeight / 4f;
        //??????
        float lineSpace = fontHeight * 1.5f;
        //????????????
        float boxWidth = maxStringWidth + lrMargin * 2;
        //???????????????=????????????*2+????????????+??????*??????-1
        drawRoundRect(posX, posY, posX + maxStringWidth + lrMargin * 2, posY + fontHeight + udMargin * 2 + (fontHeight + lineSpace) * motion * list.size(), 5, bgColor, true);
        //????????????????????????=????????????
        font.drawString(currentStr, posX + (boxWidth - font.getStringWidth(currentStr)) / 2f, posY + udMargin, fontColor);
        if (motion > 0) {
            for (int i = 0; i < list.size(); i++) {
                String s = list.get(i);
                //?????????????????????=????????????+????????????+(??????+??????)*??????????????????*??????
                font.drawString(s, posX + boxWidth / 2 - font.getStringWidth(s) / 2f, posY + (fontHeight + lineSpace) * motion * (i + 1), fontColor);
            }
        }
        return fontHeight + udMargin * 2 + (fontHeight + lineSpace) * motion * list.size();
    }

    /**
     * ??????????????????
     *
     * @param posX        ??????X
     * @param posY        ??????Y
     * @param barWidth    ????????????
     * @param barHeight   ????????????
     * @param motion      ??????
     * @param bgColor     ????????????
     * @param ugColor     ????????????
     * @param buttonColor ????????????
     * @return ????????????
     */
    public static float drawNumberBar(float posX, float posY, float barWidth, float barHeight, double motion, int bgColor, int ugColor, int buttonColor) {
        float barLRMargin = barWidth / 20;
        float barUDMargin = barHeight / 4;
        float buttonPos = (float) (posX + barLRMargin + (barWidth - barLRMargin * 2) * motion);
        drawRoundRect(posX, posY, posX + barWidth, posY + barHeight, barHeight / 2f, bgColor, true);
        drawRoundRect(posX + barLRMargin, posY + barUDMargin, posX + barWidth - barLRMargin, posY + barHeight - barUDMargin, barHeight / 2f - barUDMargin, ugColor, true);
        drawRoundRect(posX + barLRMargin, posY + barUDMargin, buttonPos, posY + barHeight - barUDMargin, barHeight / 2f - barUDMargin, buttonColor, true);
        drawCircle(buttonPos, posY + barHeight / 2f, barHeight / 2.5f, buttonColor);
        return barHeight;
    }

    /**
     * ??????????????????
     *
     * @param font
     * @param str
     * @param posX
     * @param posY
     * @param bgColor
     * @param fontColor
     * @return
     */
    public static float drawStringBox(FontDrawer font, String str, float posX, float posY, int bgColor, int fontColor) {
        float fontHeight = font.getHeight();
        float udMargin = fontHeight / 4f;
        float lrMargin = 1.5f * font.getCharWidth('A');
        float lineSpacing = fontHeight * 0.6f;
        float boxHeight;
        float maxStringWidth = font.getCharWidth('A') * 13;
        //??????????????????
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
        //?????????
        RenderUtil.drawRoundRect(posX, posY, posX + maxStringWidth + 2 * lrMargin, posY + boxHeight, font.getHeight() / 2f, bgColor, true);
        //????????????row-1???
        for (int i = 0; i < row - 1; i++) {
            font.drawString(str.substring(split.get(i), split.get(i + 1)), posX + lrMargin, posY + udMargin + i * (fontHeight + lineSpacing), fontColor);
        }
        //?????????????????????
        font.drawString(str.substring(split.get(row - 1)), posX + lrMargin, posY + udMargin + (row - 1) * (fontHeight + lineSpacing), fontColor);
        return boxHeight;
    }

    /**
     * ??????????????????????????????
     *
     * @param font
     * @param str
     * @param posX
     * @param posY
     * @param bgColor
     * @param fontColor
     * @return
     */
    public static float drawCenteredStringBox(FontDrawer font, String str, float posX, float posY, int bgColor, int fontColor) {
        float fontHeight = font.getHeight();
        float udMargin = fontHeight / 4f;
        float lrMargin = 1.5f * font.getCharWidth('A');
        float lineSpacing = fontHeight * 0.6f;
        float boxHeight;
        float maxStringWidth = font.getCharWidth('A') * 13;
        //??????????????????
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
        //?????????
        RenderUtil.drawRoundRect(posX, posY, posX + maxStringWidth + 2 * lrMargin, posY + boxHeight, font.getHeight() / 2, bgColor, true);
        //????????????row-1???
        for (int i = 0; i < row - 1; i++) {
            String s1 = str.substring(split.get(i), split.get(i + 1));
            font.drawString(s1, posX + lrMargin + (maxStringWidth - font.getStringWidth(s1)) / 2f, posY + udMargin + i * (fontHeight + lineSpacing), fontColor);
        }
        //?????????????????????
        String s1 = str.substring(split.get(row - 1));
        font.drawString(s1, posX + lrMargin + (maxStringWidth - font.getStringWidth(s1)) / 2f, posY + udMargin + (row - 1) * (fontHeight + lineSpacing), fontColor);
        return boxHeight;
    }

    public static float drawCenteredString_P(float[] pos, FontDrawer font, String str, int fontColor, boolean[] centered) {
        float fontHeight = font.getHeight();
        float lineSpacing = fontHeight * 0.6f;
        float udMargin = fontHeight * 0.01f;
        float lrMargin = 1.5f * font.getCharWidth('A');
        float boxHeight;
        float maxStringWidth = pos[2] - pos[0];
        boxHeight = (fontHeight + lineSpacing) - lineSpacing;
        font.drawString(str, pos[0] + ((centered[0]) ? ((maxStringWidth - font.getStringWidth(str)) / 2f) : lrMargin), pos[1] + (centered[1] ? ((pos[3] - pos[1] - fontHeight) / 2f) : udMargin), fontColor);
        return boxHeight;
    }

    public static float drawCenteredStringBox_P(float[] pos, FontDrawer font, String str, int bgColor, int fontColor, float radius, boolean[] centered) {
        float fontHeight = font.getHeight();
        float lineSpacing = fontHeight * 0.6f;
        float udMargin = 0f;
        float lrMargin = 1.5f * font.getCharWidth('A');
        float boxHeight;
        float maxStringWidth = pos[2] - pos[0];
        boxHeight = (fontHeight + lineSpacing) - lineSpacing;
        drawRoundRect(pos[0], pos[1], pos[2], pos[3], radius, bgColor, true);
        font.drawString(str, pos[0] + ((centered[0]) ? ((maxStringWidth - font.getStringWidth(str)) / 2f) : lrMargin), pos[1] + (centered[1] ? ((pos[3] - pos[1] - fontHeight) / 2f) : udMargin), fontColor);
        return boxHeight;
    }

    public static float drawCenteredString(float[] pos, FontDrawer font, String str, int fontColor) {
        float fontHeight = font.getHeight();
        font.drawString(str, (pos[2] + pos[0] - font.getStringWidth(str)) / 2f, pos[1], fontColor);
        return fontHeight;
    }

    public static float drawString(float[] pos, FontDrawer font, String str, int fontColor) {
        float fontHeight = font.getHeight();
        font.drawString(str, pos[0], pos[1], fontColor);
        return fontHeight;
    }

    public static void drawBlockESP(double x, double y, double z, int bgrgba, int linergba, boolean perspective) {
        glPushMatrix();
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glDisable(GL_TEXTURE_2D);
        glEnable(GL_LINE_SMOOTH);
        glDepthMask(false);
        if (perspective) {
            glDisable(GL_DEPTH_TEST);
        }
        Color bg = new Color(bgrgba, true);
        float bgr = bg.getRed() / 255f;
        float bgg = bg.getGreen() / 255f;
        float bgb = bg.getBlue() / 255f;
        float bga = bg.getAlpha() / 255f;
        Color line = new Color(linergba, true);
        float liner = line.getRed() / 255f;
        float lineg = line.getGreen() / 255f;
        float lineb = line.getBlue() / 255f;
        float linea = line.getAlpha() / 255f;
        glColor4f(bgr, bgg, bgb, bga);
        drawBoundingBox(new AxisAlignedBB(x - 0.01, y - 0.01, z - 0.01, x + 1.01D, y + 1.01D, z + 1.01D));
        glColor4f(liner, lineg, lineb, linea);
        drawOutlinedBoundingBox(new AxisAlignedBB(x - 0.01, y - 0.01, z - 0.01, x + 1.01D, y + 1.01D, z + 1.01D));
        glDisable(GL_LINE_SMOOTH);
        glEnable(GL_TEXTURE_2D);
        glDisable(GL_BLEND);
        if (perspective) {
            glEnable(GL_DEPTH_TEST);
        }
        glDepthMask(true);

        glPopMatrix();

    }

    public static void drawGradientSideways(double left, double top, double right, double bottom, int col1, int col2) {
        float f = (col1 >> 24 & 0xFF) / 255.0F;
        float f1 = (col1 >> 16 & 0xFF) / 255.0F;
        float f2 = (col1 >> 8 & 0xFF) / 255.0F;
        float f3 = (col1 & 0xFF) / 255.0F;

        float f4 = (col2 >> 24 & 0xFF) / 255.0F;
        float f5 = (col2 >> 16 & 0xFF) / 255.0F;
        float f6 = (col2 >> 8 & 0xFF) / 255.0F;
        float f7 = (col2 & 0xFF) / 255.0F;

        glPushMatrix();
        glEnable(GL_BLEND);
        glDisable(GL_TEXTURE_2D);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_LINE_SMOOTH);
        glShadeModel(7425);

        glBegin(7);
        glColor4f(f1, f2, f3, f);
        glVertex2d(left, top);
        glVertex2d(left, bottom);

        glColor4f(f5, f6, f7, f4);
        glVertex2d(right, bottom);
        glVertex2d(right, top);
        glEnd();

        glEnable(GL_TEXTURE_2D);
        glDisable(GL_BLEND);
        glDisable(GL_LINE_SMOOTH);
        glShadeModel(7424);
        glPopMatrix();
    }

    public static void drawMyTexturedModalRect(float x, float y, int textureX, int textureY, float width, float height, float factor) {
        float f2;
        float f = f2 = 1.0f / factor;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(x, y + height, 0.0D).tex(textureX * f, (textureY + height) * f).endVertex();
        worldrenderer.pos(x + width, y + height, 0.0D).tex((textureX + width) * f, (textureY + height) * f).endVertex();
        worldrenderer.pos(x + width, y, 0.0D).tex((textureX + width) * f, textureY * f).endVertex();
        worldrenderer.pos(x, y, 0.0D).tex(textureX * f, textureY * f).endVertex();
        tessellator.draw();
    }

    public static void drawAngleCirque(float posX, float posY, float radius, float startAngle, float angle, float lineWidth, int color) {
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        radius *= 2.0;
        posX *= 2;
        posY *= 2;
        float a = (float) (color >> 24 & 255) / 255.0f;
        float r = (float) (color >> 16 & 255) / 255.0f;
        float g = (float) (color >> 8 & 255) / 255.0f;
        float b = (float) (color & 255) / 255.0f;
        GL11.glEnable(GL_BLEND);
        GL11.glLineWidth(lineWidth);
        GL11.glDisable(GL_TEXTURE_2D);
        GL11.glEnable(GL_LINE_SMOOTH);
        GL11.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glPushMatrix();

        GL11.glColor4f(r, g, b, a);
        GL11.glBegin(3);
        float i = startAngle - angle;
        while (i <= startAngle) {
            double x = Math.sin((double) i * Math.PI / 180.0) * radius;
            double y = Math.cos((double) i * Math.PI / 180.0) * radius;
            GL11.glVertex2d((double) posX + x, (double) posY + y);
            ++i;
        }
        GL11.glEnd();

        glPopMatrix();
        GL11.glDisable(GL_LINE_SMOOTH);
        GL11.glEnable(GL_TEXTURE_2D);
        GL11.glDisable(GL_BLEND);
        GL11.glScalef(2.0f, 2.0f, 2.0f);
    }

    public static void drawCircle(float x, float y, float start, float end, float radius, int color) {
        float ldy;
        float ldx;
        float i;
        float temp;
        if (start > end) {
            temp = end;
            end = start;
            start = temp;
        }
        float alpha = (color >> 24 & 255) / 255.0f;
        float red = (color >> 16 & 255) / 255.0f;
        float green = (color >> 8 & 255) / 255.0f;
        float blue = (color & 255) / 255.0f;
        glPushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, 1, 0);
        GlStateManager.color(red, green, blue, alpha);
        if (alpha > 0.5f) {
            glEnable(GL_LINE_SMOOTH);
            glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
            glLineWidth(Float.MIN_VALUE);
            glBegin(GL_LINE_STRIP);
            i = end;
            while (i >= start) {
                ldx = (float) (Math.cos((i * Math.PI / 180.0)) * (radius * 1));
                ldy = (float) (Math.sin((i * Math.PI / 180.0)) * (radius * 1));
                glVertex2f((x + ldx), (y + ldy));
                i -= 4.0f;
            }
            glEnd();
            glDisable(GL_LINE_SMOOTH);
        }
        glEnable(GL_LINE_SMOOTH);
        glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
        glLineWidth(Float.MIN_VALUE);
        glBegin(GL_TRIANGLE_FAN);
        i = end;
        while (i >= start) {
            ldx = (float) (Math.cos((i * Math.PI / 180.0)) * radius);
            ldy = (float) (Math.sin((i * Math.PI / 180.0)) * radius);
            glVertex2f((x + ldx), (y + ldy));
            i -= 4.0f;
        }
        glEnd();
        glDisable(GL_LINE_SMOOTH);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        glPopMatrix();
    }

    public static void drawCircle(float x, float y, float radius, int color) {
        drawCircle(x, y, 0f, 360f, radius, color);
    }

    public static void drawIcon(float x, float y, int sizex, int sizey, ResourceLocation resourceLocation) {
        glPushMatrix();
        Minecraft.getMinecraft().getTextureManager().bindTexture(resourceLocation);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_LINE_SMOOTH);
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(516, 0.1f);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glTranslatef(x, y, 0.0f);
        RenderUtil.drawScaledRect(0, 0, 0.0f, 0.0f, sizex, sizey, sizex, sizey, sizex, sizey);
        GlStateManager.disableAlpha();
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableLighting();
        GlStateManager.disableRescaleNormal();
        glDisable(GL_LINE_SMOOTH);
        GlStateManager.disableBlend();
        glPopMatrix();
    }

    public static void drawScaledRect(int x, int y, float u, float v, int uWidth, int vHeight, int width, int height, float tileWidth, float tileHeight) {
        Gui.drawScaledCustomSizeModalRect(x, y, u, v, uWidth, vHeight, width, height, tileWidth, tileHeight);
    }

    public static void drawPlayerHead(String playerName, int x, int y, int width, int height) {
        glPushMatrix();
        for (Entity ent : mc.theWorld.getLoadedEntityList()) {
            if (ent != null) {
                EntityPlayer player = (EntityPlayer) ent;
                if (playerName.equalsIgnoreCase(player.getName())) {
                    GameProfile profile = new GameProfile(player.getUniqueID(), player.getName());
                    NetworkPlayerInfo networkplayerinfo = new NetworkPlayerInfo(profile);
                    glDisable(GL_DEPTH_TEST);
                    glEnable(GL_BLEND);
                    glDepthMask(false);
                    OpenGlHelper.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, 1, 0);
                    glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                    mc.getTextureManager().bindTexture(networkplayerinfo.getLocationSkin());
                    Gui.drawScaledCustomSizeModalRect(x, y, 8.0F, 8.0F, 8, 8, width, height, 64.0F, 64.0F);
                    if (player.isWearing(EnumPlayerModelParts.HAT)) {
                        Gui.drawScaledCustomSizeModalRect(x, y, 40.0F, 8.0F, 8, 8, width, height, 64.0F, 64.0F);
                    }
                    glDepthMask(true);
                    glDisable(GL_BLEND);
                    glEnable(GL_DEPTH_TEST);
                }
            }
        }
        glPopMatrix();
    }

    public static void drawImageWithColor(ResourceLocation image, int x, int y, int width, int height, int color) {
        glPushMatrix();
        glDisable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);
        glDepthMask(false);
        OpenGlHelper.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, 1, 0);
        float a = (color >> 24 & 0xFF) / 255.0F;
        float r = (color >> 16 & 0xFF) / 255.0F;
        float g = (color >> 8 & 0xFF) / 255.0F;
        float b = (color & 0xFF) / 255.0F;
        glColor4f(r, g, b, a);
        Minecraft.getMinecraft().getTextureManager().bindTexture(image);
        Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0f, 0.0f, width, height, (float) width, (float) height);
        glDepthMask(true);
        glDisable(GL_BLEND);
        glEnable(GL_DEPTH_TEST);
        glPopMatrix();
    }

    public static void drawImage(ResourceLocation image, int x, int y, int width, int height) {
        glPushMatrix();
        glDisable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);
        glDepthMask(false);
        OpenGlHelper.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, 1, 0);
        Minecraft.getMinecraft().getTextureManager().bindTexture(image);
        Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0f, 0.0f, width, height, (float) width, (float) height);
        glDepthMask(true);
        glDisable(GL_BLEND);
        glEnable(GL_DEPTH_TEST);
        glPopMatrix();
    }

    public static void drawEntityOnScreen(int p_147046_0_, int p_147046_1_, int Size, float Yaw, float pitch, EntityLivingBase e) {
        glPushMatrix();
        GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) p_147046_0_, (float) p_147046_1_, 40.0f);
        GlStateManager.scale((float) (-Size), (float) Size, (float) Size);
        GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f);
        float var6 = e.renderYawOffset;
        float var7 = e.rotationYaw;
        float var8 = e.rotationPitch;
        float var9 = e.prevRotationYawHead;
        float var10 = e.rotationYawHead;
        GlStateManager.rotate(135.0f, 0.0f, 1.0f, 0.0f);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.rotate(-135.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(-(float) Math.atan(pitch / 40.0f) * 20.0f, 1.0f, 0.0f, 0.0f);
        e.renderYawOffset = (float) Math.atan(Yaw / 40.0f) * -14.0f;
        e.rotationYaw = (float) Math.atan(Yaw / 40.0f) * -14.0f;
        e.rotationPitch = -(float) Math.atan(pitch / 40.0f) * 15.0f;
        e.rotationYawHead = e.rotationYaw;
        e.prevRotationYawHead = e.rotationYaw;
        GlStateManager.translate(0.0f, 0.0f, 0.0f);
        RenderManager var11 = Minecraft.getMinecraft().getRenderManager();
        var11.setPlayerViewY(180.0f);
        var11.setRenderShadow(false);
        var11.renderEntityWithPosYaw(e, 0.0, 0.0, 0.0, 0.0f, 1.0f);
        var11.setRenderShadow(true);
        e.renderYawOffset = var6;
        e.rotationYaw = var7;
        e.rotationPitch = var8;
        e.prevRotationYawHead = var9;
        e.rotationYawHead = var10;
        GlStateManager.color(1.0f, 1.0f, 1.0f);
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        glPopMatrix();
    }

    public static void drawOutlinedBoundingBox(AxisAlignedBB aa) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.begin(3, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(3, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(1, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        tessellator.draw();
    }

    public static void drawBoundingBox(AxisAlignedBB aa) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        tessellator.draw();
    }

    public static void drawEntityBoxESP(Entity entity, Color boxColor, Color lineColor, boolean hp, boolean outline) {
        double posX = getEntityRenderPos(entity)[0];
        double posY = getEntityRenderPos(entity)[1];
        double posZ = getEntityRenderPos(entity)[2];


        AxisAlignedBB box = entity instanceof EntityLivingBase ? AxisAlignedBB.fromBounds(posX - entity.width + 0.2f, posY, posZ - entity.width + 0.2f, posX + entity.width - 0.2f, posY + entity.height + (entity.isSneaking() ? 0.02f : 0.2f), posZ + entity.width - 0.2f) : AxisAlignedBB.fromBounds(posX - entity.width, posY, posZ - entity.width, posX + entity.width, posY + entity.height + 0.2f, posZ + entity.width);
        EntityLivingBase entityLivingBase = (EntityLivingBase) entity;
        float health = entityLivingBase.getHealth() + entityLivingBase.getAbsorptionAmount();
        float animHealth = health;
        float maxHealth = entityLivingBase.getMaxHealth() + entityLivingBase.getAbsorptionAmount();
        if (entityLivingBase.lastHealth == -1f) {
            //?????????
            entityLivingBase.lastHealth = health;
            entityLivingBase.aimHealth = health;
            entityLivingBase.healthESPAnim.setState(true);
        } else {
            //?????????
            if (health != entityLivingBase.aimHealth) {
                //?????????????????????
                entityLivingBase.lastHealth = (float) (entityLivingBase.lastHealth + (entityLivingBase.aimHealth - entityLivingBase.lastHealth) * entityLivingBase.healthESPAnim.getAnimationFactor());
                entityLivingBase.aimHealth = health;
                entityLivingBase.healthESPAnim = new Animation(entityLivingBase.healthESPAnim.length, entityLivingBase.healthESPAnim.initialState, Easing.LINEAR);
                entityLivingBase.healthESPAnim.setState(true);
            }
            //?????????????????????
            animHealth = (float) (entityLivingBase.lastHealth + (entityLivingBase.aimHealth - entityLivingBase.lastHealth) * entityLivingBase.healthESPAnim.getAnimationFactor());
        }
        float hpFloat = animHealth / maxHealth;
        Color hpColor = Colors.getHealthColor(animHealth, maxHealth);
        glPushMatrix();
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_BLEND);
        glEnable(GL_LINE_SMOOTH);
        glLineWidth(2.0f);
        glDisable(GL_TEXTURE_2D);
        glDisable(GL_DEPTH_TEST);
        glDepthMask(false);
        glColor4f(boxColor.getRed() / 255.0f, boxColor.getGreen() / 255.0f, boxColor.getBlue() / 255.0f, boxColor.getAlpha() / 255.0f);
        drawBoundingBox(box);
        if (outline) {
            glLineWidth(4.0f);
            glColor4f(0f, 0f, 0f, 1f);
            drawOutlinedBoundingBox(box);
        }
        glLineWidth(1.5f);
        glColor4f(lineColor.getRed() / 255.0f, lineColor.getGreen() / 255.0f, lineColor.getBlue() / 255.0f, 1f);
        drawOutlinedBoundingBox(box);
        if (hp) {
            glColor4f(hpColor.getRed() / 255.0f, hpColor.getGreen() / 255.0f, hpColor.getBlue() / 255.0f, 1f);
            drawOutlinedBoundingBox(new AxisAlignedBB(posX - entity.width, posY, posZ - entity.width, posX + entity.width, posY + (entity.height * 1.12f) * hpFloat, posZ + entity.width));
        }
        glDisable(GL_LINE_SMOOTH);
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_DEPTH_TEST);
        glDepthMask(true);
        glDisable(GL_BLEND);
        glPopMatrix();
    }

    public static void drawEntityCircularESP(EntityLivingBase entity, Color bgLineColor, boolean hpLine, boolean bkbg) {
        double posX = getEntityRenderPos(entity)[0];
        double posY = getEntityRenderPos(entity)[1];
        double posZ = getEntityRenderPos(entity)[2];

        float health = entity.getHealth();
        float animHealth = health;
        float maxHealth = entity.getMaxHealth();
        if (entity.lastHealth == -1f) {
            //?????????
            entity.lastHealth = health;
            entity.aimHealth = health;
            entity.healthESPAnim.setState(true);
        } else {
            //?????????
            if (health != entity.aimHealth) {
                //?????????????????????
                entity.lastHealth = (float) (entity.lastHealth + (entity.aimHealth - entity.lastHealth) * entity.healthESPAnim.getAnimationFactor());
                entity.aimHealth = health;
                entity.healthESPAnim = new Animation(entity.healthESPAnim.length, entity.healthESPAnim.initialState, Easing.LINEAR);
                entity.healthESPAnim.setState(true);
            }
            //?????????????????????
            animHealth = (float) (entity.lastHealth + (entity.aimHealth - entity.lastHealth) * entity.healthESPAnim.getAnimationFactor());
        }


        float hpFloat = animHealth / maxHealth;
        Color hpColor = Colors.getHealthColor(animHealth, maxHealth);

        GL11.glPushMatrix();
        GlStateManager.enableBlend();
        GL11.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        float scale = 0.05f;
        GlStateManager.translate(posX, posY + entity.height * 0.5f - (entity.isChild() ? entity.height * 0.1f : 0.0f), posZ);
        GL11.glRotatef(-RenderManager.playerViewY, 0.0f, 1.0f, 0.0f);
        GL11.glRotatef(RenderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        GL11.glScalef(-scale, -scale, -scale);
        glDisable(GL_DEPTH_TEST);
        glDepthMask(false);
        if (bkbg) {
            drawAngleCirque(0, 0, 10 * entity.height, 0, 360, 5f, new Color(0, 0, 0).getRGB());
        }
        drawAngleCirque(0, 0, 10 * entity.height, 0, 360, 2f, bgLineColor.getRGB());
        if (hpLine) drawAngleCirque(0, 0, 10 * entity.height, 0, 360 * hpFloat, 1.8f, hpColor.getRGB());
        glEnable(GL_DEPTH_TEST);
        glDepthMask(true);
        GlStateManager.disableBlend();
        GL11.glPopMatrix();
    }

    public static void drawEntity2DESP(EntityLivingBase entity, Color bgLineColor, boolean hpLine, boolean bkbg) {
        double posX = getEntityRenderPos(entity)[0];
        double posY = getEntityRenderPos(entity)[1];
        double posZ = getEntityRenderPos(entity)[2];

        float health = entity.getHealth();
        float animHealth = health;
        float maxHealth = entity.getMaxHealth();
        if (entity.lastHealth == -1f) {
            //?????????
            entity.lastHealth = health;
            entity.aimHealth = health;
            entity.healthESPAnim.setState(true);
        } else {
            //?????????
            if (health != entity.aimHealth) {
                //?????????????????????
                entity.lastHealth = (float) (entity.lastHealth + (entity.aimHealth - entity.lastHealth) * entity.healthESPAnim.getAnimationFactor());
                entity.aimHealth = health;
                entity.healthESPAnim = new Animation(entity.healthESPAnim.length, entity.healthESPAnim.initialState, Easing.LINEAR);
                entity.healthESPAnim.setState(true);
            }
            //?????????????????????
            animHealth = (float) (entity.lastHealth + (entity.aimHealth - entity.lastHealth) * entity.healthESPAnim.getAnimationFactor());
        }


        float hpFloat = animHealth / maxHealth;
        Color hpColor = Colors.getHealthColor(animHealth, maxHealth);

        GL11.glPushMatrix();
        GlStateManager.enableBlend();
        GL11.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        float scale = 0.05f;
        GlStateManager.translate(posX, posY + entity.height * 0.5f - (entity.isChild() ? entity.height * 0.1f : 0.0f), posZ);
        GL11.glRotatef(-RenderManager.playerViewY, 0.0f, 1.0f, 0.0f);
        GL11.glRotatef(RenderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        GL11.glScalef(-scale, -scale, -scale);
        glDisable(GL_DEPTH_TEST);
        glDepthMask(false);
        if (bkbg) {
            drawBorder(- (entity.width * 20), - entity.height * 13.5f, (entity.width * 20),  entity.height * 13.5f, 6, Colors.BLACK.c);
        }
        drawBorder(-(entity.width * 20), - entity.height * 13.5f,(entity.width * 20), entity.height * 13.5f, 2, bgLineColor.getRGB());
        if (hpLine) {
            drawRect((entity.width * 21) + 2.7f, - entity.height * 13.5f - 0.3f,  (entity.width * 21) + 4.3f,  entity.height * 13.5f + 0.3f, Colors.BLACK.c);
            drawRect((entity.width * 21) + 3f,  entity.height * 13.5f - (entity.height * 13.5f +  entity.height * 13.5f) * hpFloat, (entity.width * 21) + 4f, entity.height * 13.5f, hpColor.getRGB());
        }
        glEnable(GL_DEPTH_TEST);
        glDepthMask(true);
        GlStateManager.disableBlend();
        GL11.glPopMatrix();
    }

    public static void drawEntityNameTag(EntityLivingBase entity, Color bgColor, boolean hp) {
        double posX = getEntityRenderPos(entity)[0];
        double posY = getEntityRenderPos(entity)[1];
        double posZ = getEntityRenderPos(entity)[2];
        FontDrawer font = Main.fontLoader.EN48;
        String name = entity.getDisplayName().getUnformattedText();
        float[] rectPos = {-font.getStringWidth(name) * 0.7f, (float) (posY + (entity.height) - font.getHeight() * 0.7f), font.getStringWidth(name) * 0.7f, (float) (posY + entity.height + font.getHeight() * 0.7f)};
        float health = entity.getHealth();
        float animHealth = health;
        float maxHealth = entity.getMaxHealth();
        float scale = mc.thePlayer.getDistanceToEntity(entity) / 100f;
        if (scale <= 0.06) scale = 0.06f;
        if (scale >= 0.3) scale = 0.3f;
//        SenseHeader.getSense.printINFO(dis);
        if (entity.lastHealth == -1f) {
            //?????????
            entity.lastHealth = health;
            entity.aimHealth = health;
            entity.healthESPAnim.setState(true);
        } else {
            //?????????
            if (health != entity.aimHealth) {
                //?????????????????????
                entity.lastHealth = (float) (entity.lastHealth + (entity.aimHealth - entity.lastHealth) * entity.healthESPAnim.getAnimationFactor());
                entity.aimHealth = health;
                entity.healthESPAnim = new Animation(entity.healthESPAnim.length, entity.healthESPAnim.initialState, Easing.LINEAR);
                entity.healthESPAnim.setState(true);
            }
            //?????????????????????
            animHealth = (float) (entity.lastHealth + (entity.aimHealth - entity.lastHealth) * entity.healthESPAnim.getAnimationFactor());
        }


        float hpFloat = animHealth / maxHealth;
        Color hpColor = Colors.getHealthColor(animHealth, maxHealth);

        GL11.glPushMatrix();
        GlStateManager.enableBlend();
        GL11.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.translate(posX, posY + entity.height * 1.35f - (entity.isChild() ? entity.height * 0.1f : 0.0f), posZ);
        GL11.glRotatef(-RenderManager.playerViewY, 0.0f, 1.0f, 0.0f);
        GL11.glRotatef(RenderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        GL11.glScalef(-scale * 0.2f, -scale * 0.2f, -scale * 0.2f);
        glDisable(GL_DEPTH_TEST);
        drawRect(rectPos[0], rectPos[1], rectPos[2], rectPos[3], bgColor.getRGB());
        if (hp) {
            drawRect(rectPos[0], rectPos[3] - 2, rectPos[0] + ((rectPos[2] - rectPos[0])), rectPos[3], Colors.BLACK.c);
            drawRect(rectPos[0], rectPos[3] - 2, rectPos[0] + ((rectPos[2] - rectPos[0]) * hpFloat), rectPos[3], hpColor.getRGB());
            font.drawStringWithShadow(name, -(rectPos[2] - rectPos[0]) * 0.5f + font.getStringWidth(name) * 0.2f, rectPos[1], 0.5f, -1);
        }
        glEnable(GL_DEPTH_TEST);
        glDepthMask(true);
        GlStateManager.disableBlend();
        GL11.glPopMatrix();
    }

    /**
     * ????????????Notification
     *
     * @param infos  [0]:?????? [1]:?????? [2]:??????
     * @param fonts  [0]:?????? [1]:?????? [2]:??????
     * @param motion ??????
     */
    public static void drawUpNotification(String[] infos, FontDrawer[] fonts, Animation motion, Color iconColor) {

        FontDrawer icon = fonts[2];
        FontDrawer title = fonts[1];
        FontDrawer msg = fonts[0];
        float margin = title.getHeight() / 2f;
        float msgWidth = msg.getStringWidth(infos[0]);
        float titleTextWidth = (title.getStringWidth(infos[1]) + icon.getStringWidth(infos[2])) / 2f;
        float scale = (float) motion.getAnimationFactor();
        float[] pos = {width() / 2f - margin - msgWidth / 2f, height() * 0.08f, width() / 2f + margin + msgWidth / 2f, (height() * 0.08f + margin * 3 + icon.getHeight() + msg.getHeight()) * scale};
        if (motion.getAnimationFactor() > 0.6f) {
            drawRoundRect(pos[0], pos[1], pos[2], pos[3], 10, new Color(0, 0, 0, 40).getRGB(), true);
            BlurUtil.blurAreaRounded(pos[0], pos[1], pos[2], pos[3], 10, 20);
            glPushMatrix();

            glScalef(1, scale, scale);
            icon.drawString(infos[2], width() / 2f - titleTextWidth, height() * 0.08 + margin * scale, iconColor.getRGB());
            title.drawString(infos[1], width() / 2f - titleTextWidth + icon.getStringWidth(infos[2]) + margin / 4f, height() * 0.08 + margin + icon.getHeight() / 2f - margin / 2f * scale, iconColor.getRGB());

            msg.drawCenteredString(infos[0], width() / 2f, height() * 0.08 + margin * 2 + icon.getHeight() * scale, -1);
            glPopMatrix();
        }
    }

    public static void drawBorderedRect(int x, int y, int width, int height, float borderWidth, int borderColor, int bgColor) {
        Gui.drawRect(x, y, width, height, bgColor);
        float a = (borderColor >> 24 & 0xFF) / 255.0f;
        float r = (borderColor >> 16 & 0xFF) / 255.0f;
        float g = (borderColor >> 8 & 0xFF) / 255.0f;
        float b = (borderColor & 0xFF) / 255.0f;
        glPushMatrix();
        glEnable(GL_BLEND);
        glDisable(GL_TEXTURE_2D);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_LINE_SMOOTH);
        glPushMatrix();
        glColor4f(r, g, b, a);
        glLineWidth(borderWidth);
        glBegin(1);
        glVertex2d(x, y);
        glVertex2d(x, height);
        glVertex2d(width, height);
        glVertex2d(width, y);
        glVertex2d(x, y);
        glVertex2d(width, y);
        glVertex2d(x, height);
        glVertex2d(width, height);
        glEnd();
        glPopMatrix();
        glEnable(GL_TEXTURE_2D);
        glDisable(GL_BLEND);
        glDisable(GL_LINE_SMOOTH);
        glPopMatrix();
    }

    public static void drawBorder(float x, float y, float width, float height, float borderWidth, int borderColor) {
        float a = (borderColor >> 24 & 0xFF) / 255.0f;
        float r = (borderColor >> 16 & 0xFF) / 255.0f;
        float g = (borderColor >> 8 & 0xFF) / 255.0f;
        float b = (borderColor & 0xFF) / 255.0f;
        glPushMatrix();
        glEnable(GL_BLEND);
        glDisable(GL_TEXTURE_2D);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_LINE_SMOOTH);
        glPushMatrix();
        glColor4f(r, g, b, a);
        glLineWidth(borderWidth);
        glBegin(1);
        glVertex2d(x, y);
        glVertex2d(x, height);
        glVertex2d(width, height);
        glVertex2d(width, y);
        glVertex2d(x, y);
        glVertex2d(width, y);
        glVertex2d(x, height);
        glVertex2d(width, height);
        glEnd();
        glPopMatrix();
        glEnable(GL_TEXTURE_2D);
        glDisable(GL_BLEND);
        glDisable(GL_LINE_SMOOTH);
        glPopMatrix();
    }

    public static int width() {
        return new ScaledResolution(Minecraft.getMinecraft()).getScaledWidth();
    }

    public static int height() {
        return new ScaledResolution(Minecraft.getMinecraft()).getScaledHeight();
    }

    public static void drawRect(double g, double d, double i, double e, int col1) {
        float f2 = (float) (col1 >> 24 & 255) / 255.0f;
        float f22 = (float) (col1 >> 16 & 255) / 255.0f;
        float f3 = (float) (col1 >> 8 & 255) / 255.0f;
        float f4 = (float) (col1 & 255) / 255.0f;
        //
        glPushMatrix();
        glEnable(GL_BLEND);
        glDisable(GL_TEXTURE_2D);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_LINE_SMOOTH);
        //
        glColor4f(f22, f3, f4, f2);
        glBegin(7);
        glVertex2d(i, d);
        glVertex2d(g, d);
        glVertex2d(g, e);
        glVertex2d(i, e);
        glEnd();
        //
        glEnable(GL_TEXTURE_2D);
        glDisable(GL_BLEND);
        glDisable(GL_LINE_SMOOTH);
        GlStateManager.disableBlend();
        glPopMatrix();
    }

    /**
     * ????????????Box??????
     *
     * @param font
     * @param posX
     * @param posY
     * @param icons
     * @param spacing
     * @param motion
     * @param iconColor
     * @param bgColor
     * @param currentColor
     * @return ??????
     */
    public static float drawTitleIcon(FontDrawer font, float posX, float posY, String[] icons, float boxWidth, float spacing, float motion, int iconColor, int bgColor, int currentColor, boolean isTransverse) {
        int count = 0;
        if (isTransverse) {
            for (String icon : icons) {
                drawRoundRect(posX + (spacing + boxWidth) * count, posY, posX + boxWidth + (spacing + boxWidth) * count, posY + boxWidth, boxWidth / 8f, bgColor, true);
                count++;
            }
            drawRoundRect(posX + (motion - 1) * (spacing + boxWidth), posY, posX + (motion - 1) * (spacing + boxWidth) + boxWidth, posY + boxWidth, boxWidth / 8f, currentColor, true);
            count = 0;
            for (String icon : icons) {
                font.drawCenteredString(icon, posX + boxWidth / 2f + (spacing + boxWidth) * count, posY - (font.getHeight()) / 12f, iconColor);
                count++;
            }
        } else {
            for (String icon : icons) {
                drawRoundRect(posX, posY + spacing * count, posX + boxWidth, posY + boxWidth + spacing * count, boxWidth / 8f, bgColor, true);
                count++;
            }
            drawRoundRect(posX, posY + (motion - 1) * spacing, posX + boxWidth, posY + (motion - 1) * spacing + boxWidth, boxWidth / 8f, currentColor, true);
            count = 0;
            for (String icon : icons) {
                font.drawCenteredString(icon, posX + boxWidth / 2f, posY + (boxWidth - font.getHeight()) / 2f + spacing * count, iconColor);
                count++;
            }
        }
        return boxWidth;
    }

    public static void drawColorPointLists(ArrayList<ArrayList<ColorPoint>> colorPointLists) {
        glPushMatrix();
        glEnable(GL_BLEND);
        glDisable(GL_TEXTURE_2D);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_LINE_SMOOTH);

        Color color;
        float x, y;
        glBegin(GL_POINTS);
        for (ArrayList<ColorPoint> colorPointList : colorPointLists) {
            for (ColorPoint colorPoint : colorPointList) {
                color = colorPoint.color;
                x = colorPoint.pos[0];
                y = colorPoint.pos[1];
                glColor3f(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f);
                glVertex2d(x, y);
            }
        }
        glEnd();

        glEnable(GL_TEXTURE_2D);
        glDisable(GL_BLEND);
        glDisable(GL_LINE_SMOOTH);
        GlStateManager.disableBlend();
        glPopMatrix();
    }

    public static void drawColorPoints(ArrayList<ColorPoint> colorPoints) {

        glPushMatrix();
        glEnable(GL_BLEND);
        glDisable(GL_TEXTURE_2D);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_LINE_SMOOTH);

        Color color;
        float x, y;
        glBegin(GL_POINTS);
        for (ColorPoint colorPoint : colorPoints) {
            color = colorPoint.color;
            x = colorPoint.pos[0];
            y = colorPoint.pos[1];
            glColor3f(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f);
            glVertex2d(x, y);
        }
        glEnd();
        glEnable(GL_TEXTURE_2D);
        glDisable(GL_BLEND);
        glDisable(GL_LINE_SMOOTH);
        GlStateManager.disableBlend();
        glPopMatrix();
    }

    public static void drawColorPointsWithYThickness(ArrayList<ColorPoint> colorPoints, float thickness) {
        glPushMatrix();
        glEnable(GL_BLEND);
        glDisable(GL_TEXTURE_2D);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_LINE_SMOOTH);

        Color color;
        float x, y;
        glBegin(GL_POINTS);
        for (int t = 0; t < 2f * thickness; t++) {
            for (ColorPoint colorPoint : colorPoints) {
                color = colorPoint.color;
                x = colorPoint.pos[0];
                y = colorPoint.pos[1];
                glColor4f(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);
                glVertex2d(x, y + t / 2f);
            }
        }
        glEnd();

        glEnable(GL_TEXTURE_2D);
        glDisable(GL_BLEND);
        glDisable(GL_LINE_SMOOTH);
        GlStateManager.disableBlend();
        glPopMatrix();
    }

    public static void drawLine(float x, float y, float x1, float y1, float width) {
        drawLine(x, y, 0.0f, x1, y1, 0.0f, width);
    }

    public static void drawLine(float x, float y, float z, float x1, float y1, float z1, float width) {
        glLineWidth(width);
        setupRender(true);
        setupClientState(GLClientState.VERTEX, true);
        RenderUtil.tessellator.addVertex(x, y, z).addVertex(x1, y1, z1).draw(3);
        setupClientState(GLClientState.VERTEX, false);
        setupRender(false);
    }

    public static void setupClientState(GLClientState state, boolean enabled) {
        RenderUtil.csBuffer.clear();
        if (state.ordinal() > 0) {
            RenderUtil.csBuffer.add(state.getCap());
        }
        RenderUtil.csBuffer.add(32884);
        RenderUtil.csBuffer.forEach(enabled ? RenderUtil.ENABLE_CLIENT_STATE : RenderUtil.DISABLE_CLIENT_STATE);
    }

    public static void setupRender(boolean start) {
        glPushMatrix();
        if (start) {
            GlStateManager.enableBlend();
            glEnable(GL_LINE_SMOOTH);
            GlStateManager.disableDepth();
            GlStateManager.disableTexture2D();
            GlStateManager.blendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            glHint(3154, 4354);
        } else {
            GlStateManager.disableBlend();
            GlStateManager.enableTexture2D();
            glDisable(GL_LINE_SMOOTH);
            GlStateManager.enableDepth();
        }
        GlStateManager.depthMask(!start);
        glPopMatrix();
    }

    public static double[] getEntityRenderPos(Entity entity) {
        return new double[]{entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * mc.timer.renderPartialTicks - RenderManager.renderPosX, entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * mc.timer.renderPartialTicks - RenderManager.renderPosY, entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * mc.timer.renderPartialTicks - RenderManager.renderPosZ,};
    }

    public static void drawPlayerArmorList(EntityPlayer entityPlayer, int x, int y, int /*??????*/interval, boolean /*???/???????????????*/vORh) {
        glPushMatrix();
        ArrayList<ItemStack> stuff = new ArrayList<>();
        for (int index = 3; index >= 0; --index) {
            ItemStack armor = (entityPlayer).inventory.armorInventory[index];
            if (armor == null) continue;
            stuff.add(armor);
        }
        if (entityPlayer.getCurrentEquippedItem() != null) {
            stuff.add(entityPlayer.getCurrentEquippedItem());
        }
        int Axis = 0;
        for (ItemStack everything : stuff) {
            if (mc.theWorld != null) {
                RenderHelper.enableGUIStandardItemLighting();
            }
            GlStateManager.pushMatrix();
            GlStateManager.clear(256);
            mc.getRenderItem().zLevel = -150.0f;
            mc.getRenderItem().renderItemAndEffectIntoGUI(everything, x + (vORh ? 0 : Axis), y + (vORh ? Axis : 0));
            mc.getRenderItem().zLevel = 0.0f;
            GlStateManager.disableBlend();
            GlStateManager.scale(0.5, 0.5, 0.5);
            GlStateManager.disableDepth();
            GlStateManager.disableLighting();
            GlStateManager.enableDepth();
            GlStateManager.scale(2.0f, 2.0f, 2.0f);
            GlStateManager.enableAlpha();
            GlStateManager.popMatrix();
            Axis += interval;
        }
        glPopMatrix();

    }

    public static void drawPlayerEquippedItem(EntityPlayer entityPlayer, int x, int y) {
        if (entityPlayer.getCurrentEquippedItem() != null) {
            GlStateManager.pushMatrix();
            GlStateManager.clear(256);
            mc.getRenderItem().zLevel = -150.0f;
            mc.getRenderItem().renderItemAndEffectIntoGUI(entityPlayer.getCurrentEquippedItem(), x, y);
            mc.getRenderItem().zLevel = 0.0f;
            GlStateManager.disableBlend();
            GlStateManager.scale(0.5, 0.5, 0.5);
            GlStateManager.disableDepth();
            GlStateManager.disableLighting();
            GlStateManager.enableDepth();
            GlStateManager.scale(2.0f, 2.0f, 2.0f);
            GlStateManager.enableAlpha();
            GlStateManager.popMatrix();
        }

    }

    public static void doScissor(int x, int y, int x1, int y1) {
        ScaledResolution scale = new ScaledResolution(mc);
        int factor = scale.getScaleFactor();
        GL11.glEnable(GL_SCISSOR_TEST);
        GL11.glScissor(x * factor, (scale.getScaledHeight() - y1) * factor, (x1 - x) * factor, (y1 - y) * factor);
        scissors.add(new ScissorPos(x, y, x1, y1));
    }

    public static void reScissor(int x, int y, int x1, int y1) {
        ScaledResolution scale = new ScaledResolution(mc);
        int factor = scale.getScaleFactor();
        GL11.glEnable(GL_SCISSOR_TEST);
        GL11.glScissor(x * factor, (scale.getScaledHeight() - y1) * factor, (x1 - x) * factor, (y1 - y) * factor);
    }

    public static void deScissor() {
        glDisable(GL_SCISSOR_TEST);
        scissors.remove(scissors.size() - 1);
        if (scissors.size() > 0) {
            int i = scissors.size() - 1;
            ScissorPos p = scissors.get(i);
            reScissor(p.x, p.y, p.x1, p.y1);
        }
    }

}
