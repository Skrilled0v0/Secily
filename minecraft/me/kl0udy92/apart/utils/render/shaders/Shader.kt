package me.kl0udy92.apart.utils.render.shaders

import me.kl0udy92.apart.utils.MinecraftInstance
import net.minecraft.client.shader.Framebuffer
import org.lwjgl.opengl.GL11

abstract class Shader(protected var shaderProgram: ShaderProgram) : MinecraftInstance() {

    protected open var pass = 0
    protected var frameBuffer: Framebuffer
    protected var width = 0f
    protected var height = 0f

    init {
        this.frameBuffer = Framebuffer(mc.displayWidth, mc.displayHeight, false)
    }

    abstract fun setUniforms()

    fun render(width: Float, height: Float) {
        this.width = width
        this.height = height
        if (this.frameBuffer.framebufferWidth != mc.displayWidth || this.frameBuffer.framebufferHeight != mc.displayHeight) {
            this.frameBuffer.deleteFramebuffer()
            this.frameBuffer = Framebuffer(mc.displayWidth, mc.displayHeight, false)
        }
        this.frameBuffer.framebufferClear()
        GL11.glEnable(3042)
        GL11.glBlendFunc(770, 771)
        mc.framebuffer.bindFramebuffer(false)
        this.doShaderPass(pass, mc.framebuffer)
        ++pass
    }

    private fun doShaderPass(pass: Int, framebufferIn: Framebuffer) {
        this.shaderProgram.init()
        this.setUniforms()
        GL11.glBindTexture(3553, framebufferIn.framebufferTexture)
        this.shaderProgram.renderCanvas()
        this.shaderProgram.uninit()
    }

}