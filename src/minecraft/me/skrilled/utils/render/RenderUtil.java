package me.skrilled.utils.render;

import me.skrilled.ui.menu.assembly.color.ColorPoint;
import me.skrilled.utils.IMC;
import me.skrilled.utils.render.gl.GLClientState;
import me.skrilled.utils.render.tessellate.Tessellation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
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

    static {
        tessellator = Tessellation.createExpanding(4, 1.0f, 2.0f);
        csBuffer = new ArrayList<>();
        ENABLE_CLIENT_STATE = GL11::glEnableClientState;
        DISABLE_CLIENT_STATE = GL11::glEnableClientState;
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
     * 使用drawRect和drawCircle的方法绘制圆角矩形
     * 不可使用透明度:(
     */
    public static void drawRoundedRect(float x, float y, float width, float height, float round, int color) {
        x = (float) (x + ((round / 2.0f) + 0.5));
        y = (float) (y + ((round / 2.0f) + 0.5));
        width = (float) (width - ((round / 2.0f) + 0.5));
        height = (float) (height - ((round / 2.0f) + 0.5));
        drawRect(x, y, width, height, color);
        drawCircle((width - round / 2.0f), (y + round / 2.0f), 0f, 360f, round, color);
        drawCircle((x + round / 2.0f), (height - round / 2.0f), 0f, 360f, round, color);
        drawCircle((x + round / 2.0f), (y + round / 2.0f), 0f, 360f, round, color);
        drawCircle((width - round / 2.0f), (height - round / 2.0f), 0f, 360f, round, color);
        drawRect((x - round / 2.0f - 0.5f), (y + round / 2.0f), width, (height - round / 2.0f), color);
        drawRect(x, (y + round / 2.0f), (width + round / 2.0f + 0.5f), (height - round / 2.0f), color);
        drawRect((x + round / 2.0f), (y - round / 2.0f - 0.5f), (width - round / 2.0f), (height - round / 2.0f), color);
        drawRect((x + round / 2.0f), y, (width - round / 2.0f), (height + round / 2.0f + 0.5f), color);
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
    public static void drawBooleanButtton(float posX, float posY, float buttonWidth, float buttonHeight, float motion, int bgColor, int tureColor, int falseColor) {
        int color;
        if (motion == 0) {
            color = falseColor;
        } else color = tureColor;
        float radius = buttonHeight / 2f;
        RenderUtil.drawRoundRect(posX, posY, posX + buttonWidth, posY + buttonHeight, radius, bgColor);
        RenderUtil.drawCircle(posX + radius + (buttonWidth - buttonHeight) * motion, posY + radius, radius, color);
    }

    /**
     * 枚举类型编辑框
     *
     * @param posX      坐标X
     * @param posY      坐标Y
     * @param boxWidth  盒子宽度
     * @param boxHeight 盒子高度
     * @param motion    动量
     * @param bgColor   背景颜色
     * @param fontColor 字体颜色
     */
    public static void drawEnumTypeBox(float posX, float posY, float boxWidth, float boxHeight, float motion, int bgColor, int fontColor) {

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

    public static int rainbow(int delay) {
        double rainbow = Math.ceil((double) (System.currentTimeMillis() + (long) delay) / 5.0);
        return Color.getHSBColor((float) (rainbow % 720.0 / 720.0), 0.5f, 0.7f).brighter().getRGB();
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

    public static void drawCirque(float x, float y, float radius, int start, int end) {
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        glColor4f(109, 115, 213, 255);
        glEnable(3042);
        glLineWidth(2F);
        glBegin(3);
        for (float i = end; i >= start; i -= (360 / 90f))
            glVertex2f((float) (x + (Math.cos(i * Math.PI / 180) * (radius * 1.001F))), (float) (y + (Math.sin(i * Math.PI / 180) * (radius * 1.001F))));
        glEnd();
        glEnable(2848);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
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

    public static void drawImageWithColor(ResourceLocation image, int x, int y, int width, int height, int color) {
        glDisable(2929);
        glEnable(3042);
        glDepthMask(false);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        float var11 = (color >> 24 & 0xFF) / 255.0F;
        float var6 = (color >> 16 & 0xFF) / 255.0F;
        float var7 = (color >> 8 & 0xFF) / 255.0F;
        float var8 = (color & 0xFF) / 255.0F;
        glColor4f(var6, var7, var8, var11);
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

    public static void drawImage(ResourceLocation image, int x, int y, int width, int height, int color) {
        glDisable(2929);
        glEnable(3042);
        glDepthMask(false);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        float var11 = (color >> 24 & 0xFF) / 255.0F;
        float var6 = (color >> 16 & 0xFF) / 255.0F;
        float var7 = (color >> 8 & 0xFF) / 255.0F;
        float var8 = (color & 0xFF) / 255.0F;
        glColor4f(var6, var7, var8, var11);
        Minecraft.getMinecraft().getTextureManager().bindTexture(image);
        Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0f, 0.0f, width, height, (float) width, (float) height);
        glDepthMask(true);
        glDisable(3042);
        glEnable(2929);
    }

    public static void drawblock(double a, double a2, double a3, int a4, int a5, float a6) {
        float a7 = (float) (a4 >> 24 & 255) / 255.0f;
        float a8 = (float) (a4 >> 16 & 255) / 255.0f;
        float a9 = (float) (a4 >> 8 & 255) / 255.0f;
        float a10 = (float) (a4 & 255) / 255.0f;
        float a11 = (float) (a5 >> 24 & 255) / 255.0f;
        float a12 = (float) (a5 >> 16 & 255) / 255.0f;
        float a13 = (float) (a5 >> 8 & 255) / 255.0f;
        float a14 = (float) (a5 & 255) / 255.0f;
        glPushMatrix();
        glEnable(3042);
        glBlendFunc(770, 771);
        glDisable(3553);
        glEnable(2848);
        glDisable(2929);
        glDepthMask(false);
        glColor4f(a8, a9, a10, a7);
        drawOutlinedBoundingBox(new AxisAlignedBB(a, a2, a3, a + 1.0, a2 + 1.0, a3 + 1.0));
        glLineWidth(a6);
        glColor4f(a12, a13, a14, a11);
        drawOutlinedBoundingBox(new AxisAlignedBB(a, a2, a3, a + 1.0, a2 + 1.0, a3 + 1.0));
        glDisable(2848);
        glEnable(3553);
        glEnable(2929);
        glDepthMask(true);
        glDisable(3042);
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

    public static void drawBorderedRect(double g, double d, double h, double e, float l1, int col1, int col2) {
        Gui.drawRect((int) g, (int) d, (int) h, (int) e, col2);
        float f = (col1 >> 24 & 0xFF) / 255.0f;
        float f2 = (col1 >> 16 & 0xFF) / 255.0f;
        float f3 = (col1 >> 8 & 0xFF) / 255.0f;
        float f4 = (col1 & 0xFF) / 255.0f;
        glEnable(3042);
        glDisable(3553);
        glBlendFunc(770, 771);
        glEnable(2848);
        glPushMatrix();
        glColor4f(f2, f3, f4, f);
        glLineWidth(l1);
        glBegin(1);
        glVertex2d(g, d);
        glVertex2d(g, e);
        glVertex2d(h, e);
        glVertex2d(h, d);
        glVertex2d(g, d);
        glVertex2d(h, d);
        glVertex2d(g, e);
        glVertex2d(h, e);
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

    public static void drawVLine(float x, float y, float x1, int y1) {
        if (x1 < y) {
            float var5 = y;
            y = x1;
            x1 = var5;
        }
        drawRect(x, y + 1.0f, x + 1.0f, x1, y1);
    }

    public static void drawRound(float x, float y, float x1, float y1, int borderC, int insideC) {
        enableGL2D();
        glScalef(0.5f, 0.5f, 0.5f);
        drawVLine(x *= 2.0f, (y *= 2.0f) + 1.0f, (y1 *= 2.0f) - 2.0f, borderC);
        drawVLine((x1 *= 2.0f) - 1.0f, y + 1.0f, y1 - 2.0f, borderC);
        drawHLine(x + 2.0f, x1 - 3.0f, y, borderC);
        drawHLine(x + 2.0f, x1 - 3.0f, y1 - 1.0f, borderC);
        drawHLine(x + 1.0f, x + 1.0f, y + 1.0f, borderC);
        drawHLine(x1 - 2.0f, x1 - 2.0f, y + 1.0f, borderC);
        drawHLine(x1 - 2.0f, x1 - 2.0f, y1 - 2.0f, borderC);
        drawHLine(x + 1.0f, x + 1.0f, y1 - 2.0f, borderC);
        Gui.drawRect((int) (x + 1.0f), (int) (y + 1.0f), (int) (x1 - 1.0f), (int) (y1 - 1.0f), insideC);
        glScalef(2.0f, 2.0f, 2.0f);
        disableGL2D();
        Gui.drawRect(0, 0, 0, 0, 0);
        GlStateManager.disableBlend();
    }

    public static void drawHLine(float par1, float par2, float par3, int par4) {
        if (par2 < par1) {
            float var5 = par1;
            par1 = par2;
            par2 = var5;
        }
        RenderUtil.drawRect(par1, par3, par2 + 1.0f, par3 + 1.0f, par4);
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

    public static void drawPoint(double x, double y, int col1) {
        float a = (float) (col1 >> 24 & 255) / 255.0f;
        float r = (float) (col1 >> 16 & 255) / 255.0f;
        float g = (float) (col1 >> 8 & 255) / 255.0f;
        float b = (float) (col1 & 255) / 255.0f;
        //
        glEnable(3042);
        glDisable(3553);
        glBlendFunc(770, 771);
        glEnable(2848);
        glPushMatrix();
        //
        glColor4f(r, g, b, a);
        glBegin(GL_POINTS);
        glVertex2d(x, y);
        glEnd();
        //
        glPopMatrix();
        glEnable(3553);
        glDisable(3042);
        glDisable(2848);
        GlStateManager.disableBlend();
        glColor4f(1, 1, 1, 1);
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
        glBlendFunc(770, 771);
        glEnable(2848);
        glPushMatrix();

        Color color;
        float x, y;
        for (int t = 0; t < 2f * thickness; t++) {
            for (ColorPoint colorPoint : colorPoints) {
                color = colorPoint.color;
                x = colorPoint.pos[0];
                y = colorPoint.pos[1];
                glColor3f(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f);
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

    public static void disableGL2D() {
        glEnable(3553);
        glDisable(3042);
        glEnable(2929);
        glDisable(2848);
        glHint(3154, 4352);
        glHint(3155, 4352);
    }

    public static void enableGL2D() {
        glDisable(2929);
        glEnable(3042);
        glDisable(3553);
        glBlendFunc(770, 771);
        glDepthMask(true);
        glEnable(2848);
        glHint(3154, 4354);
        glHint(3155, 4354);
    }

    public static void drawCustomImage(int x, int y, int width, int height, ResourceLocation image) {
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        glDisable(2929);
        glEnable(3042);
        glDepthMask(false);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        Minecraft.getMinecraft().getTextureManager().bindTexture(image);
        Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0f, 0.0f, width, height, (float) width, (float) height);
        glDepthMask(true);
        glDisable(3042);
        glEnable(2929);
    }

    public static void pre() {
        glDisable(2929);
        glDisable(3553);
        glEnable(3042);
        glBlendFunc(770, 771);
    }

    public static void post() {
        glDisable(3042);
        glEnable(3553);
        glEnable(2929);
        glColor3d(1.0, 1.0, 1.0);
    }

    public static void startDrawing() {
        glEnable(3042);
        glEnable(3042);
        glBlendFunc(770, 771);
        glEnable(2848);
        glDisable(3553);
        glDisable(2929);
        mc.entityRenderer.setupCameraTransform(mc.timer.renderPartialTicks, 0);
    }

    public static void stopDrawing() {
        glDisable(3042);
        glEnable(3553);
        glDisable(2848);
        glDisable(3042);
        glEnable(2929);
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

    public static void drawBorderedRectNameTag(final float x, final float y, final float x2, final float y2, final float l1, final int col1) {
        RenderUtil.drawRect(x, y, x2, y2, col1);
        final float f = (col1 >> 24 & 0xFF) / 255.0f;
        final float f2 = (col1 >> 16 & 0xFF) / 255.0f;
        final float f3 = (col1 >> 8 & 0xFF) / 255.0f;
        final float f4 = (col1 & 0xFF) / 255.0f;
        glPushMatrix();
        glEnable(3042);
        glDisable(3553);
        glBlendFunc(770, 771);
        glEnable(2848);
        glColor4f(f2, f3, f4, f);
        glLineWidth(l1);
        glBegin(1);
        glVertex2d(x, y);
        glVertex2d(x, y2);
        glVertex2d(x2, y2);
        glVertex2d(x2, y);
        glVertex2d(x, y);
        glVertex2d(x2, y);
        glVertex2d(x, y2);
        glVertex2d(x2, y2);
        glEnd();
        glEnable(3553);
        glDisable(3042);
        glDisable(2848);
        glPopMatrix();
    }

}