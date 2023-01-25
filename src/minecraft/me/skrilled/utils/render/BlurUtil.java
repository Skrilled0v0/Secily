package me.skrilled.utils.render;

import com.google.gson.JsonSyntaxException;
import me.skrilled.utils.IMC;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

/**
 * @author DeadAngels.
 * @since 2022-07-02.
 **/
public class BlurUtil implements IMC {

    private static final ResourceLocation blurShader = new ResourceLocation("shaders/post/blurArea.json");
    private static ShaderGroup shaderGroup;
    private static Framebuffer frbuffer, framebuffer;
    private static int lastFactor;
    private static int lastWidth;
    private static int lastHeight;
    private static int lastWeight;
    private static float lastX;
    private static float lastY;
    private static float lastW;
    private static float lastH;
    private static float lastStrength = 5F;

    public static void init() {
        try {
            shaderGroup = new ShaderGroup(mc.getTextureManager(), mc.getResourceManager(), mc.getFramebuffer(), blurShader);
            shaderGroup.createBindFramebuffers(mc.displayWidth, mc.displayHeight);
            framebuffer = shaderGroup.getMainFramebuffer();
            frbuffer = shaderGroup.getFramebufferRaw("result");
        } catch (JsonSyntaxException | IOException e) {
            e.printStackTrace();
        }
    }

    private static void setValues(float strength, float x, float y, float w, float h, float width, float height) {
        if (strength == lastStrength && lastX == x && lastY == y && lastW == w && lastH == h) return;
        lastStrength = strength;
        lastX = x;
        lastY = y;
        lastW = w;
        lastH = h;

        for (int i = 0; i < 2; i++) {
            shaderGroup.getListShaders().get(i).getShaderManager().getShaderUniform("Radius").set(strength);
            shaderGroup.getListShaders().get(i).getShaderManager().getShaderUniform("BlurXY").set(x, height - y - h);
            shaderGroup.getListShaders().get(i).getShaderManager().getShaderUniform("BlurCoord").set(w, h);
        }
    }

    public static void blurArea(float x, float y, float x2, float y2, float blurStrength) {
        if (!OpenGlHelper.isFramebufferEnabled()) return;

        if (x > x2) {
            float z = x;
            x = x2;
            x2 = z;
        }

        if (y > y2) {
            float z = y;
            y = y2;
            y2 = y;
        }

        ScaledResolution scaledResolution = new ScaledResolution(mc);
        final int scaleFactor = scaledResolution.getScaleFactor();
        final int width = scaledResolution.getScaledWidth();
        final int height = scaledResolution.getScaledHeight();

        if (sizeHasChanged(scaleFactor, width, height) || framebuffer == null || frbuffer == null || shaderGroup == null) {
            init();
        }

        lastFactor = scaleFactor;
        lastWidth = width;
        lastHeight = height;

        float _w = x2 - x;
        float _h = y2 - y;

        //setValues(blurStrength);
        setValues(blurStrength, x, y, _w, _h, width, height);

        framebuffer.bindFramebuffer(true);
        shaderGroup.loadShaderGroup(mc.timer.renderPartialTicks);

        mc.getFramebuffer().bindFramebuffer(true);

        StencilUtil.write(false);
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        RenderUtil.quickDrawRect(x, y, x2, y2);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        StencilUtil.erase(true);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        GlStateManager.pushMatrix();
        GlStateManager.colorMask(true, true, true, false);
        GlStateManager.disableDepth();
        GlStateManager.depthMask(false);
        GlStateManager.enableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.disableAlpha();
        frbuffer.bindFramebufferTexture();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        float f = (float) width;
        float f1 = (float) height;
        float f2 = (float) frbuffer.framebufferWidth / (float) frbuffer.framebufferTextureWidth;
        float f3 = (float) frbuffer.framebufferHeight / (float) frbuffer.framebufferTextureHeight;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        worldrenderer.pos(0.0D, f1, 0.0D).tex(0.0D, 0.0D).color(255, 255, 255, 255).endVertex();
        worldrenderer.pos(f, f1, 0.0D).tex(f2, 0.0D).color(255, 255, 255, 255).endVertex();
        worldrenderer.pos(f, 0.0D, 0.0D).tex(f2, f3).color(255, 255, 255, 255).endVertex();
        worldrenderer.pos(0.0D, 0.0D, 0.0D).tex(0.0D, f3).color(255, 255, 255, 255).endVertex();
        tessellator.draw();
        frbuffer.unbindFramebufferTexture();
        GlStateManager.enableDepth();
        GlStateManager.depthMask(true);
        GlStateManager.colorMask(true, true, true, true);
        GlStateManager.popMatrix();
        GlStateManager.disableBlend();
        StencilUtil.dispose();

        GlStateManager.enableAlpha();
    }

    public static void preCustomBlur(float blurStrength, float x, float y, float x2, float y2) {
        if (!OpenGlHelper.isFramebufferEnabled()) return;

        if (x > x2) {
            float z = x;
            x = x2;
            x2 = z;
        }

        if (y > y2) {
            float z = y;
            y = y2;
            y2 = y;
        }

        ScaledResolution scaledResolution = new ScaledResolution(mc);
        final int scaleFactor = scaledResolution.getScaleFactor();
        final int width = scaledResolution.getScaledWidth();
        final int height = scaledResolution.getScaledHeight();


        if (sizeHasChanged(scaleFactor, width, height) || framebuffer == null || frbuffer == null || shaderGroup == null) {
            init();
        }

        lastFactor = scaleFactor;
        lastWidth = width;
        lastHeight = height;

        float _w = x2 - x;
        float _h = y2 - y;

        setValues(blurStrength, x, y, _w, _h, width, height);

        framebuffer.bindFramebuffer(true);
        shaderGroup.loadShaderGroup(mc.timer.renderPartialTicks);

        mc.getFramebuffer().bindFramebuffer(true);

        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
    }

    public static void blurAreaRounded(float x, float y, float x2, float y2, float rad, float blurStrength) {
        if (!OpenGlHelper.isFramebufferEnabled()) return;

        if (x > x2) {
            float z = x;
            x = x2;
            x2 = z;
        }

        if (y > y2) {
            float z = y;
            y = y2;
            y2 = y;
        }

        ScaledResolution scaledResolution = new ScaledResolution(mc);
        final int scaleFactor = scaledResolution.getScaleFactor();
        final int width = scaledResolution.getScaledWidth();
        final int height = scaledResolution.getScaledHeight();

        if (sizeHasChanged(scaleFactor, width, height) || framebuffer == null || frbuffer == null || shaderGroup == null) {
            init();
        }

        lastFactor = scaleFactor;
        lastWidth = width;
        lastHeight = height;

        //setValues(blurStrength);
        float _w = x2 - x;
        float _h = y2 - y;

        setValues(blurStrength, x, y, _w, _h, width, height);

        framebuffer.bindFramebuffer(true);
        shaderGroup.loadShaderGroup(mc.timer.renderPartialTicks);

        mc.getFramebuffer().bindFramebuffer(true);

        StencilUtil.write(false);
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        RenderUtil.quickRoundedRect(x, y, x2, y2, rad);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        StencilUtil.erase(true);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        GlStateManager.pushMatrix();
        GlStateManager.colorMask(true, true, true, false);
        GlStateManager.disableDepth();
        GlStateManager.depthMask(false);
        GlStateManager.enableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.disableAlpha();
        frbuffer.bindFramebufferTexture();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        float f = (float) width;
        float f1 = (float) height;
        float f2 = (float) frbuffer.framebufferWidth / (float) frbuffer.framebufferTextureWidth;
        float f3 = (float) frbuffer.framebufferHeight / (float) frbuffer.framebufferTextureHeight;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        worldrenderer.pos(0.0D, f1, 0.0D).tex(0.0D, 0.0D).color(255, 255, 255, 255).endVertex();
        worldrenderer.pos(f, f1, 0.0D).tex(f2, 0.0D).color(255, 255, 255, 255).endVertex();
        worldrenderer.pos(f, 0.0D, 0.0D).tex(f2, f3).color(255, 255, 255, 255).endVertex();
        worldrenderer.pos(0.0D, 0.0D, 0.0D).tex(0.0D, f3).color(255, 255, 255, 255).endVertex();
        tessellator.draw();
        frbuffer.unbindFramebufferTexture();
        GlStateManager.enableDepth();
        GlStateManager.depthMask(true);
        GlStateManager.colorMask(true, true, true, true);
        GlStateManager.popMatrix();
        GlStateManager.disableBlend();
        StencilUtil.dispose();

        GlStateManager.enableAlpha();
    }

    private static boolean sizeHasChanged(int scaleFactor, int width, int height) {
        return lastFactor != scaleFactor || lastWidth != width || lastHeight != height;
    }

}
