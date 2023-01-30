package me.kl0udy92.apart.utils.render.shaders.implementations

import me.kl0udy92.apart.utils.render.shaders.Shader
import me.kl0udy92.apart.utils.render.shaders.ShaderProgram
import org.lwjgl.opengl.GL20

class MainMenuShader(override var pass: Int) : Shader(ShaderProgram("sandbox/background.fsh")) {

    override fun setUniforms() {
        GL20.glUniform1f(this.shaderProgram.getUniform("time"), pass.toFloat() / 100.0f)
        GL20.glUniform2f(
            this.shaderProgram.getUniform("resolution"), mc.displayWidth.toFloat(),
            mc.displayHeight.toFloat()
        )
    }

}