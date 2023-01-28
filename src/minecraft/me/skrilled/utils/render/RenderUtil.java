package me.skrilled.utils.render;

import com.mojang.authlib.GameProfile;
import me.fontloader.FontDrawer;
import me.skrilled.ui.menu.assembly.color.ColorPoint;
import me.skrilled.utils.IMC;
import me.skrilled.utils.math.TimerUtil;
import me.skrilled.utils.render.gl.GLClientState;
import me.skrilled.utils.render.tessellate.Tessellation;
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

    public static void drawSikadi(float x, float y, boolean isRed) {
        glColor4f(1, 1, 1, 1);
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
        glBegin(GL_QUADS);

        glVertex2d(x2, y);
        glVertex2d(x, y);
        glVertex2d(x, y2);
        glVertex2d(x2, y2);

        glEnd();
    }

    /**
     * 这个方法绘制的圆角矩形可以使用透明度
     */
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
        glEnable(3042);
        glDisable(3553);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(2848);
        glPushMatrix();

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
        glPopMatrix();
        glEnable(3553);
        glDisable(3042);
        glDisable(2848);
        GlStateManager.disableBlend();
        glColor4f(1, 1, 1, 1);
    }

    /**
     * 获取圆角矩形绘制区域
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
    }


    /**
     * 绘制开关按钮
     *
     * @param posX         坐标X
     * @param posY         坐标Y
     * @param buttonWidth  按钮宽度
     * @param buttonHeight 按钮高度
     * @param motion       按钮开关动量
     * @param bgColor      背景颜色
     * @param tureColor    打开颜色
     * @param falseColor   关闭颜色
     */
    public static float drawBooleanButton(float posX, float posY, float buttonWidth, float buttonHeight, float motion, int bgColor, int tureColor, int falseColor) {
        int color;
        if (motion == 0) {
            color = falseColor;
        } else color = tureColor;
        float radius = buttonHeight / 2f;
        RenderUtil.drawRoundRect(posX, posY, posX + buttonWidth, posY + buttonHeight, radius, bgColor);
        RenderUtil.drawCircle(posX + radius + (buttonWidth - buttonHeight) * motion, posY + radius, radius, color);
        return buttonHeight;
    }

    /**
     * 枚举类型编辑框
     * list需要除去currentStr
     *
     * @param font       字体
     * @param currentStr 选择的字符
     * @param list       剩下的字符
     * @param posX       坐标X
     * @param posY       坐标Y
     * @param motion     动量
     * @param bgColor    背景颜色
     * @param fontColor  字体颜色
     * @return 所占高度
     */
    public static float drawEnumTypeBox(FontDrawer font, String currentStr, ArrayList<String> list, float posX, float posY, float motion, int bgColor, int fontColor) {
        float maxStringWidth = 0f;
        //寻找最大StringWidth
        for (String s : list) {
            float fontStringWidth = font.getStringWidth(s);
            if (fontStringWidth > maxStringWidth) maxStringWidth = fontStringWidth;
        }
        //左右边距
        float lrMargin = font.getCharWidth('A');
        //字高
        float fontHeight = font.getHeight();
        //上下边距
        float udMargin = fontHeight / 4f;
        //行距
        float lineSpace = fontHeight * 1.5f;
        //盒子宽度
        float boxWidth = maxStringWidth + lrMargin * 2;
        //总盒子高度=上下边距*2+字体高度+行距*行数-1
        drawRoundRect(posX, posY, posX + maxStringWidth + lrMargin * 2, posY + fontHeight + udMargin * 2 + (fontHeight + lineSpace) * motion * list.size(), 5, bgColor);
        //第一个字体的高度=上下边距
        font.drawString(currentStr, posX + (boxWidth - font.getStringWidth(currentStr)) / 2f, posY + udMargin, fontColor);
        if (motion > 0) {
            for (int i = 0; i < list.size(); i++) {
                String s = list.get(i);
                //其他行字体高度=字体高度+上下边距+(字高+行距)*已经画的行数*动量
                font.drawString(s, posX + boxWidth / 2 - font.getStringWidth(s) / 2f, posY + (fontHeight + lineSpace) * motion * (i + 1), fontColor);
            }
        }
        return fontHeight + udMargin * 2 + (fontHeight + lineSpace) * motion * list.size();
    }

    /**
     * 绘制数字拉条
     *
     * @param posX        坐标X
     * @param posY        坐标Y
     * @param barWidth    拉条宽度
     * @param barHeight   拉条高度
     * @param motion      动量
     * @param bgColor     背景颜色
     * @param ugColor     前景颜色
     * @param buttonColor 拉条颜色
     * @return 所占高度
     */
    public static float drawNumberBar(float posX, float posY, float barWidth, float barHeight, float motion, int bgColor, int ugColor, int buttonColor) {
        float barLRMargin = barWidth / 20;
        float barUDMargin = barHeight / 4;
        float buttonPos = posX + barLRMargin + (barWidth - barLRMargin * 2) * motion;
        drawRoundRect(posX, posY, posX + barWidth, posY + barHeight, barHeight / 2f, bgColor);
        drawRoundRect(posX + barLRMargin, posY + barUDMargin, posX + barWidth - barLRMargin, posY + barHeight - barUDMargin, barHeight / 2f - barUDMargin, ugColor);
        drawRoundRect(posX + barLRMargin, posY + barUDMargin, buttonPos, posY + barHeight - barUDMargin, barHeight / 2f - barUDMargin, buttonColor);
        drawCircle(buttonPos, posY + barHeight / 2f, barHeight / 2.5f, buttonColor);
        return barHeight;
    }

    /**
     * 字符串输入框
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
        RenderUtil.drawRoundRect(posX, posY, posX + maxStringWidth + 2 * lrMargin, posY + boxHeight, font.getHeight() / 2, bgColor);
        //画文字前row-1行
        for (int i = 0; i < row - 1; i++) {
            font.drawString(str.substring(split.get(i), split.get(i + 1)), posX + lrMargin, posY + udMargin + i * (fontHeight + lineSpacing), fontColor);
        }
        //画文字最后一行
        font.drawString(str.substring(split.get(row - 1)), posX + lrMargin, posY + udMargin + (row - 1) * (fontHeight + lineSpacing), fontColor);
        return boxHeight;
    }

    /**
     * 字符串输入框（居中）
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
        RenderUtil.drawRoundRect(posX, posY, posX + maxStringWidth + 2 * lrMargin, posY + boxHeight, font.getHeight() / 2, bgColor);
        //画文字前row-1行
        for (int i = 0; i < row - 1; i++) {
            String s1 = str.substring(split.get(i), split.get(i + 1));
            font.drawString(s1, posX + lrMargin + (maxStringWidth - font.getStringWidth(s1)) / 2f, posY + udMargin + i * (fontHeight + lineSpacing), fontColor);
        }
        //画文字最后一行
        String s1 = str.substring(split.get(row - 1));
        font.drawString(s1, posX + lrMargin + (maxStringWidth - font.getStringWidth(s1)) / 2f, posY + udMargin + (row - 1) * (fontHeight + lineSpacing), fontColor);
        return boxHeight;
    }

    /**
     * 标题枚举Box绘制
     * @param font
     * @param posX
     * @param posY
     * @param Icons
     * @param spacing
     * @param motion
     * @param iconColor
     * @param bgColor
     * @param currentColor
     * @return 高度
     */
    public static float drawTitleIcon(FontDrawer font, float posX, float posY, String[] Icons, float spacing, float motion, int iconColor, int bgColor, int currentColor,boolean isTransverse) {
        int count = 0;
        float iconWidth = 0;
        if(isTransverse) {
            for (String icon : Icons) {
                iconWidth = font.getStringWidth(icon) * 1.2f;
                drawRoundRect(posX + spacing * count, posY, posX + iconWidth + spacing * count, posY + iconWidth, iconWidth / 8f, bgColor);
                count++;
            }
            drawRoundRect(posX + (motion - 1) * spacing, posY, posX + (motion - 1) * spacing + iconWidth, posY + iconWidth, iconWidth / 8f, currentColor);
            count = 0;
            for (String icon : Icons) {
                font.drawCenteredString(icon, posX + iconWidth / 2f + spacing * count, posY + (iconWidth - font.getHeight()) / 2f, iconColor);
                count++;
            }
            return iconWidth;
        }else{
            for (String icon : Icons) {
                iconWidth = font.getStringWidth(icon) * 1.2f;
                drawRoundRect(posX , posY+ spacing * count, posX + iconWidth, posY + iconWidth + spacing * count, iconWidth / 8f, bgColor);
                count++;
            }
            drawRoundRect(posX , posY+ (motion - 1) * spacing, posX + iconWidth, posY  + (motion - 1) * spacing+ iconWidth, iconWidth / 8f, currentColor);
            count = 0;
            for (String icon : Icons) {
                font.drawCenteredString(icon, posX + iconWidth / 2f, posY + (iconWidth - font.getHeight()) / 2f + spacing * count, iconColor);
                count++;
            }
            return iconWidth;
        }
    }

    public static void drawSolidBlockESP(double x, double y, double z, float red, float green, float blue, float alpha) {
        glPushMatrix();
        glEnable(3042);
        glBlendFunc(770, 771);
        glDisable(3553);
        glEnable(2848);
        glDisable(2929);
        glDepthMask(false);
        glColor4f(red, green, blue, alpha);
        drawBoundingBox(new AxisAlignedBB(x, y, z, x + 1.0D, y + 1.0D, z + 1.0D));
        glDisable(2848);
        glEnable(3553);
        glEnable(2929);
        glDepthMask(true);
        glDisable(3042);
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

        glEnable(3042);
        glDisable(3553);
        glBlendFunc(770, 771);
        glEnable(2848);
        glShadeModel(7425);

        glPushMatrix();
        glBegin(7);
        glColor4f(f1, f2, f3, f);
        glVertex2d(left, top);
        glVertex2d(left, bottom);

        glColor4f(f5, f6, f7, f4);
        glVertex2d(right, bottom);
        glVertex2d(right, top);
        glEnd();
        glPopMatrix();

        glEnable(3553);
        glDisable(3042);
        glDisable(2848);
        glShadeModel(7424);
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
        GL11.glEnable(3042);
        GL11.glLineWidth(lineWidth);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glBlendFunc(770, 771);
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
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glDisable(3042);
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
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
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
    }

    public static void drawCircle(float x, float y, float radius, int color) {
        drawCircle(x, y, 0f, 360f, radius, color);
    }

    public static void drawIcon(float x, float y, int sizex, int sizey, ResourceLocation resourceLocation) {
        glPushMatrix();
        Minecraft.getMinecraft().getTextureManager().bindTexture(resourceLocation);
        glEnable(3042);
        glBlendFunc(770, 771);
        glEnable(2848);
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(516, 0.1f);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        glTranslatef(x, y, 0.0f);
        RenderUtil.drawScaledRect(0, 0, 0.0f, 0.0f, sizex, sizey, sizex, sizey, sizex, sizey);
        GlStateManager.disableAlpha();
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableLighting();
        GlStateManager.disableRescaleNormal();
        glDisable(2848);
        GlStateManager.disableBlend();
        glPopMatrix();
    }

    public static void drawScaledRect(int x, int y, float u, float v, int uWidth, int vHeight, int width, int height, float tileWidth, float tileHeight) {
        Gui.drawScaledCustomSizeModalRect(x, y, u, v, uWidth, vHeight, width, height, tileWidth, tileHeight);
    }

    public static void drawPlayerHead(String playerName, int x, int y, int width, int height) {
        for (Entity ent : mc.theWorld.getLoadedEntityList()) {
            if (ent != null) {
                EntityPlayer player = (EntityPlayer) ent;
                if (playerName.equalsIgnoreCase(player.getName())) {
                    GameProfile profile = new GameProfile(player.getUniqueID(), player.getName());
                    NetworkPlayerInfo networkplayerinfo = new NetworkPlayerInfo(profile);
                    glDisable(2929);
                    glEnable(3042);
                    glDepthMask(false);
                    OpenGlHelper.glBlendFunc(770, 771, 1, 0);
                    glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                    mc.getTextureManager().bindTexture(networkplayerinfo.getLocationSkin());
                    Gui.drawScaledCustomSizeModalRect(x, y, 8.0F, 8.0F, 8, 8, width, height, 64.0F, 64.0F);
                    if (player.isWearing(EnumPlayerModelParts.HAT)) {
                        Gui.drawScaledCustomSizeModalRect(x, y, 40.0F, 8.0F, 8, 8, width, height, 64.0F, 64.0F);
                    }
                    glDepthMask(true);
                    glDisable(3042);
                    glEnable(2929);
                }
            }
        }
    }

    public static void drawImageWithColor(ResourceLocation image, int x, int y, int width, int height, int color) {
        glDisable(2929);
        glEnable(3042);
        glDepthMask(false);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        float a = (color >> 24 & 0xFF) / 255.0F;
        float r = (color >> 16 & 0xFF) / 255.0F;
        float g = (color >> 8 & 0xFF) / 255.0F;
        float b = (color & 0xFF) / 255.0F;
        glColor4f(r, g, b, a);
        Minecraft.getMinecraft().getTextureManager().bindTexture(image);
        Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0f, 0.0f, width, height, (float) width, (float) height);
        glDepthMask(true);
        glDisable(3042);
        glEnable(2929);
    }

    public static void drawImage(ResourceLocation image, int x, int y, int width, int height) {
        glDisable(2929);
        glEnable(3042);
        glDepthMask(false);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        Minecraft.getMinecraft().getTextureManager().bindTexture(image);
        Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0f, 0.0f, width, height, (float) width, (float) height);
        glDepthMask(true);
        glDisable(3042);
        glEnable(2929);
    }

    public static void drawEntityOnScreen(int p_147046_0_, int p_147046_1_, int Size, float Yaw, float pitch, EntityLivingBase e) {
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

    public static void drawEntityBoxESP(Entity entity, Color boxColor, Color lineColor, boolean outline) {
        double posX = getEntityRenderPos(entity)[0];
        double posY = getEntityRenderPos(entity)[1];
        double posZ = getEntityRenderPos(entity)[2];
        AxisAlignedBB box = entity instanceof EntityLivingBase ? AxisAlignedBB.fromBounds(posX - entity.width + 0.2f, posY, posZ - entity.width + 0.2f, posX + entity.width - 0.2f, posY + entity.height + (entity.isSneaking() ? 0.02f : 0.2f), posZ + entity.width - 0.2f) : AxisAlignedBB.fromBounds(posX - entity.width, posY, posZ - entity.width, posX + entity.width, posY + entity.height + 0.2f, posZ + entity.width);
        glBlendFunc(770, 771);
        glEnable(3042);
        glEnable(2848);
        glLineWidth(2.0f);
        glDisable(3553);
        glDisable(2929);
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
        glDisable(2848);
        glEnable(3553);
        glEnable(2929);
        glDepthMask(true);
        glDisable(3042);
    }

    public static void drawBorderedRect(int x, int y, int width, int height, float borderWidth, int borderColor, int bgColor) {
        Gui.drawRect(x, y, width, height, bgColor);
        float a = (borderColor >> 24 & 0xFF) / 255.0f;
        float r = (borderColor >> 16 & 0xFF) / 255.0f;
        float g = (borderColor >> 8 & 0xFF) / 255.0f;
        float b = (borderColor & 0xFF) / 255.0f;
        glEnable(3042);
        glDisable(3553);
        glBlendFunc(770, 771);
        glEnable(2848);
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
        glEnable(3553);
        glDisable(3042);
        glDisable(2848);
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
        glEnable(3042);
        glDisable(3553);
        glBlendFunc(770, 771);
        glEnable(2848);
        glPushMatrix();
        //
        glColor4f(f22, f3, f4, f2);
        glBegin(7);
        glVertex2d(i, d);
        glVertex2d(g, d);
        glVertex2d(g, e);
        glVertex2d(i, e);
        glEnd();
        //
        glPopMatrix();
        glEnable(3553);
        glDisable(3042);
        glDisable(2848);
        GlStateManager.disableBlend();
    }


    public static void drawColorPointLists(ArrayList<ArrayList<ColorPoint>> colorPointLists) {
        glEnable(3042);
        glDisable(3553);
        glBlendFunc(770, 771);
        glEnable(2848);
        glPushMatrix();

        Color color;
        float x, y;
        for (ArrayList<ColorPoint> colorPointList : colorPointLists) {
            for (ColorPoint colorPoint : colorPointList) {
                color = colorPoint.color;
                x = colorPoint.pos[0];
                y = colorPoint.pos[1];
                glColor3f(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f);
                glBegin(GL_POINTS);
                glVertex2d(x, y);
                glEnd();
            }
        }

        glPopMatrix();
        glEnable(3553);
        glDisable(3042);
        glDisable(2848);
        GlStateManager.disableBlend();
        glColor4f(1, 1, 1, 1);
    }

    public static void drawColorPoints(ArrayList<ColorPoint> colorPoints) {
        glEnable(3042);
        glDisable(3553);
        glBlendFunc(770, 771);
        glEnable(2848);
        glPushMatrix();

        Color color;
        float x, y;
        for (ColorPoint colorPoint : colorPoints) {
            color = colorPoint.color;
            x = colorPoint.pos[0];
            y = colorPoint.pos[1];
            glColor3f(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f);
            glBegin(GL_POINTS);
            glVertex2d(x, y);
            glEnd();
        }

        glPopMatrix();
        glEnable(3553);
        glDisable(3042);
        glDisable(2848);
        GlStateManager.disableBlend();
        glColor4f(1, 1, 1, 1);
    }

    public static void drawColorPointsWithYThickness(ArrayList<ColorPoint> colorPoints, float thickness) {
        glEnable(3042);
        glDisable(3553);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(2848);
        glPushMatrix();

        Color color;
        float x, y;
        for (int t = 0; t < 2f * thickness; t++) {
            for (ColorPoint colorPoint : colorPoints) {
                color = colorPoint.color;
                x = colorPoint.pos[0];
                y = colorPoint.pos[1];
                glColor4f(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);
                glBegin(GL_POINTS);
                glVertex2d(x, y + t / 2f);
                glEnd();
            }
        }


        glPopMatrix();
        glEnable(3553);
        glDisable(3042);
        glDisable(2848);
        GlStateManager.disableBlend();
        glColor4f(1, 1, 1, 1);
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
        if (start) {
            GlStateManager.enableBlend();
            glEnable(2848);
            GlStateManager.disableDepth();
            GlStateManager.disableTexture2D();
            GlStateManager.blendFunc(770, 771);
            glHint(3154, 4354);
        } else {
            GlStateManager.disableBlend();
            GlStateManager.enableTexture2D();
            glDisable(2848);
            GlStateManager.enableDepth();
        }
        GlStateManager.depthMask(!start);
    }

    public static double[] getEntityRenderPos(Entity entity) {
        return new double[]{entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * mc.timer.renderPartialTicks - RenderManager.renderPosX, entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * mc.timer.renderPartialTicks - RenderManager.renderPosY, entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * mc.timer.renderPartialTicks - RenderManager.renderPosZ,};
    }

    public static void drawPlayerArmorList(EntityPlayer entityPlayer, int x, int y, int /*间隔*/interval, boolean /*是/否纵向绘制*/vORh) {
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

}