package me.kl0udy92.apart.utils.render.gl

import me.kl0udy92.apart.utils.MinecraftInstance
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.OpenGlHelper
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.client.shader.Framebuffer
import net.minecraft.client.shader.ShaderGroup
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.GL11

object BlurUtil : MinecraftInstance() {

    var shaderGroup: ShaderGroup? = null
    var frbuffer: Framebuffer? = null
    var framebuffer: Framebuffer? = null

    private var lastFactor = 0
    private var lastWidth = 0
    private var lastHeight = 0
    private val lastWeight = 0

    private var lastX = 0f
    private var lastY = 0f
    private var lastW = 0f
    private var lastH = 0f

    private var lastStrength = 5f

    private val blurShader = ResourceLocation("shaders/post/blurArea.json")

    fun init() {
        kotlin.runCatching {
            shaderGroup = ShaderGroup(mc.textureManager, mc.resourceManager, mc.framebuffer, blurShader)
            shaderGroup!!.createBindFramebuffers(mc.displayWidth, mc.displayHeight)
            framebuffer = shaderGroup!!.mainFramebuffer
            frbuffer = shaderGroup!!.getFramebufferRaw("result")
        }.onFailure {
            it.printStackTrace()
        }
    }

    private fun setValues(strength: Float, x: Float, y: Float, w: Float, h: Float, width: Float, height: Float) {
        if (strength == lastStrength && lastX == x && lastY == y && lastW == w && lastH == h) return
        lastStrength = strength
        lastX = x
        lastY = y
        lastW = w
        lastH = h
        for (i in 0..1) {
            shaderGroup!!.listShaders[i].shaderManager.getShaderUniform("Radius").set(strength)
            shaderGroup!!.listShaders[i].shaderManager.getShaderUniform("BlurXY").set(x, height - y - h)
            shaderGroup!!.listShaders[i].shaderManager.getShaderUniform("BlurCoord").set(w, h)
        }
    }

    fun blurArea(x: Float, y: Float, x2: Float, y2: Float, blurStrength: Float) {
        var x = x
        var y = y
        var x2 = x2
        var y2 = y2
        if (!OpenGlHelper.isFramebufferEnabled()) return
        if (x > x2) {
            val z = x
            x = x2
            x2 = z
        }
        if (y > y2) {
            val z = y
            y = y2
            y2 = y
        }
        val scaledResolution = ScaledResolution(mc)
        val scaleFactor = scaledResolution.scaleFactor
        val width = scaledResolution.scaledWidth
        val height = scaledResolution.scaledHeight
        if (sizeHasChanged(
                scaleFactor,
                width,
                height
            ) || framebuffer == null || frbuffer == null || shaderGroup == null
        ) {
            init()
        }
        lastFactor = scaleFactor
        lastWidth = width
        lastHeight = height
        val _w = x2 - x
        val _h = y2 - y

        //setValues(blurStrength);
        setValues(blurStrength, x, y, _w, _h, width.toFloat(), height.toFloat())
        framebuffer!!.bindFramebuffer(true)
        shaderGroup!!.loadShaderGroup(mc.timer.renderPartialTicks)
        mc.framebuffer.bindFramebuffer(true)
        StencilUtil.write(false)
        GlStateManager.enableBlend()
        GlStateManager.disableTexture2D()
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)
        GlUtil.draw(GL11.GL_QUADS) {
            GL11.glVertex2d(x2.toDouble(), y.toDouble())
            GL11.glVertex2d(x.toDouble(), y.toDouble())
            GL11.glVertex2d(x.toDouble(), y2.toDouble())
            GL11.glVertex2d(x2.toDouble(), y2.toDouble())
        }
        GlStateManager.enableTexture2D()
        GlStateManager.disableBlend()
        StencilUtil.erase(true)
        GlStateManager.enableBlend()
        GlStateManager.blendFunc(770, 771)
        GlStateManager.pushMatrix()
        GlStateManager.colorMask(true, true, true, false)
        GlStateManager.disableDepth()
        GlStateManager.depthMask(false)
        GlStateManager.enableTexture2D()
        GlStateManager.disableLighting()
        GlStateManager.disableAlpha()
        frbuffer!!.bindFramebufferTexture()
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f)
        val f = width.toFloat()
        val f1 = height.toFloat()
        val f2 = frbuffer!!.framebufferWidth.toFloat() / frbuffer!!.framebufferTextureWidth.toFloat()
        val f3 = frbuffer!!.framebufferHeight.toFloat() / frbuffer!!.framebufferTextureHeight.toFloat()
        val tessellator = Tessellator.getInstance()
        val worldrenderer = tessellator.worldRenderer
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR)
        worldrenderer.pos(0.0, f1.toDouble(), 0.0).tex(0.0, 0.0).color(255, 255, 255, 255).endVertex()
        worldrenderer.pos(f.toDouble(), f1.toDouble(), 0.0).tex(f2.toDouble(), 0.0).color(255, 255, 255, 255)
            .endVertex()
        worldrenderer.pos(f.toDouble(), 0.0, 0.0).tex(f2.toDouble(), f3.toDouble()).color(255, 255, 255, 255)
            .endVertex()
        worldrenderer.pos(0.0, 0.0, 0.0).tex(0.0, f3.toDouble()).color(255, 255, 255, 255).endVertex()
        tessellator.draw()
        frbuffer!!.unbindFramebufferTexture()
        GlStateManager.enableDepth()
        GlStateManager.depthMask(true)
        GlStateManager.colorMask(true, true, true, true)
        GlStateManager.popMatrix()
        GlStateManager.disableBlend()
        StencilUtil.dispose()
        GlStateManager.enableAlpha()
    }

    fun preCustomBlur(blurStrength: Float, x: Float, y: Float, x2: Float, y2: Float) {
        var x = x
        var y = y
        var x2 = x2
        var y2 = y2
        if (!OpenGlHelper.isFramebufferEnabled()) return
        if (x > x2) {
            val z = x
            x = x2
            x2 = z
        }
        if (y > y2) {
            val z = y
            y = y2
            y2 = y
        }
        val scaledResolution = ScaledResolution(mc)
        val scaleFactor = scaledResolution.scaleFactor
        val width = scaledResolution.scaledWidth
        val height = scaledResolution.scaledHeight
        if (sizeHasChanged(
                scaleFactor,
                width,
                height
            ) || framebuffer == null || frbuffer == null || shaderGroup == null
        ) {
            init()
        }
        lastFactor = scaleFactor
        lastWidth = width
        lastHeight = height
        val _w = x2 - x
        val _h = y2 - y
        setValues(blurStrength, x, y, _w, _h, width.toFloat(), height.toFloat())
        framebuffer!!.bindFramebuffer(true)
        shaderGroup!!.loadShaderGroup(mc.timer.renderPartialTicks)
        mc.framebuffer.bindFramebuffer(true)
        GlStateManager.enableBlend()
        GlStateManager.disableTexture2D()
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)
    }

    fun postCustomBlur() {
        val scaledResolution = ScaledResolution(mc)
        val width = scaledResolution.scaledWidth
        val height = scaledResolution.scaledHeight
        GlStateManager.enableTexture2D()
        GlStateManager.disableBlend()
        StencilUtil.erase(true)
        GlStateManager.enableBlend()
        GlStateManager.blendFunc(770, 771)
        GlStateManager.pushMatrix()
        GlStateManager.colorMask(true, true, true, false)
        GlStateManager.disableDepth()
        GlStateManager.depthMask(false)
        GlStateManager.enableTexture2D()
        GlStateManager.disableLighting()
        GlStateManager.disableAlpha()
        frbuffer!!.bindFramebufferTexture()
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f)
        val f = width.toFloat()
        val f1 = height.toFloat()
        val f2 = frbuffer!!.framebufferWidth.toFloat() / frbuffer!!.framebufferTextureWidth.toFloat()
        val f3 = frbuffer!!.framebufferHeight.toFloat() / frbuffer!!.framebufferTextureHeight.toFloat()
        val tessellator = Tessellator.getInstance()
        val worldrenderer = tessellator.worldRenderer
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR)
        worldrenderer.pos(0.0, f1.toDouble(), 0.0).tex(0.0, 0.0).color(255, 255, 255, 255).endVertex()
        worldrenderer.pos(f.toDouble(), f1.toDouble(), 0.0).tex(f2.toDouble(), 0.0).color(255, 255, 255, 255)
            .endVertex()
        worldrenderer.pos(f.toDouble(), 0.0, 0.0).tex(f2.toDouble(), f3.toDouble()).color(255, 255, 255, 255)
            .endVertex()
        worldrenderer.pos(0.0, 0.0, 0.0).tex(0.0, f3.toDouble()).color(255, 255, 255, 255).endVertex()
        tessellator.draw()
        frbuffer!!.unbindFramebufferTexture()
        GlStateManager.enableDepth()
        GlStateManager.depthMask(true)
        GlStateManager.colorMask(true, true, true, true)
        GlStateManager.popMatrix()
        OpenGlHelper()
        StencilUtil.dispose()
        GlStateManager.enableAlpha()
    }

    private fun sizeHasChanged(scaleFactor: Int, width: Int, height: Int): Boolean {
        return lastFactor != scaleFactor || lastWidth != width || lastHeight != height
    }

}