package me.kl0udy92.apart.utils.render.gl

import me.kl0udy92.apart.utils.MinecraftInstance
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.shader.Framebuffer
import org.lwjgl.opengl.EXTFramebufferObject
import org.lwjgl.opengl.EXTPackedDepthStencil
import org.lwjgl.opengl.GL11

object StencilUtil : MinecraftInstance() {

    fun dispose() {
        GL11.glDisable(GL11.GL_STENCIL_TEST)
        GlStateManager.disableAlpha()
        GlStateManager.disableBlend()
    }

    fun erase(invert: Boolean) {
        GL11.glStencilFunc(if (invert) GL11.GL_EQUAL else GL11.GL_NOTEQUAL, 1, 65535)
        GL11.glStencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_REPLACE)
        GlStateManager.colorMask(true, true, true, true)
        GlStateManager.enableAlpha()
        GlStateManager.enableBlend()
        GL11.glAlphaFunc(GL11.GL_GREATER, 0.0f)
    }

    fun write(renderClipLayer: Boolean) {
        StencilUtil.checkSetupFBO(mc.framebuffer)
        GL11.glClearStencil(0)
        GL11.glClear(GL11.GL_STENCIL_BUFFER_BIT)
        GL11.glEnable(GL11.GL_STENCIL_TEST)
        GL11.glStencilFunc(GL11.GL_ALWAYS, 1, 65535)
        GL11.glStencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_REPLACE)
        if (!renderClipLayer) {
            GlStateManager.colorMask(false, false, false, false)
        }
    }

    fun checkSetupFBO(framebuffer: Framebuffer?) {
        if (framebuffer != null) {
            if (framebuffer.depthBuffer > -1) {
                setupFBO(framebuffer)
                framebuffer.depthBuffer = -1
            }
        }
    }

    fun setupFBO(framebuffer: Framebuffer) {
        EXTFramebufferObject.glDeleteRenderbuffersEXT(framebuffer.depthBuffer)
        val stencilDepthBufferID = EXTFramebufferObject.glGenRenderbuffersEXT()
        EXTFramebufferObject.glBindRenderbufferEXT(EXTFramebufferObject.GL_RENDERBUFFER_EXT, stencilDepthBufferID)
        EXTFramebufferObject.glRenderbufferStorageEXT(
            EXTFramebufferObject.GL_RENDERBUFFER_EXT,
            EXTPackedDepthStencil.GL_DEPTH_STENCIL_EXT,
            mc.displayWidth,
            mc.displayHeight
        )
        EXTFramebufferObject.glFramebufferRenderbufferEXT(
            EXTFramebufferObject.GL_FRAMEBUFFER_EXT,
            EXTFramebufferObject.GL_STENCIL_ATTACHMENT_EXT,
            EXTFramebufferObject.GL_RENDERBUFFER_EXT,
            stencilDepthBufferID
        )
        EXTFramebufferObject.glFramebufferRenderbufferEXT(
            EXTFramebufferObject.GL_FRAMEBUFFER_EXT,
            EXTFramebufferObject.GL_DEPTH_ATTACHMENT_EXT,
            EXTFramebufferObject.GL_RENDERBUFFER_EXT,
            stencilDepthBufferID
        )
    }

    fun initStencil() {
        initStencil(mc.framebuffer)
    }

    fun initStencil(framebuffer: Framebuffer) {
        framebuffer.bindFramebuffer(false)
        checkSetupFBO(framebuffer)
        GL11.glClear(GL11.GL_STENCIL_BUFFER_BIT)
        GL11.glEnable(GL11.GL_STENCIL_TEST)
    }

    fun bindWriteStencilBuffer() {
        GL11.glStencilFunc(GL11.GL_ALWAYS, 1, 1)
        GL11.glStencilOp(GL11.GL_REPLACE, GL11.GL_REPLACE, GL11.GL_REPLACE)
        GL11.glColorMask(false, false, false, false)
    }

    fun bindReadStencilBuffer(ref: Int) {
        GL11.glColorMask(true, true, true, true)
        GL11.glStencilFunc(GL11.GL_EQUAL, ref, 1)
        GL11.glStencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_KEEP)
    }

    fun uninitStencilBuffer() {
        GL11.glDisable(GL11.GL_STENCIL_TEST)
    }

}