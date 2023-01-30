package me.kl0udy92.apart.utils.render.shaders

import me.kl0udy92.apart.core.Main
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL20
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.nio.FloatBuffer
import java.nio.IntBuffer

class ShaderProgram {

    private var programID = 0
    private val vertexName: String
    private val fragmentName: String
    private var vertexShaderID = 0
    private var fragmentShaderID = 0

    constructor(vertexName: String, fragmentName: String) {
        this.vertexName = vertexName
        this.fragmentName = fragmentName
        val vertexSource = this.readShader(vertexName)
        vertexShaderID = GL20.glCreateShader(35633)
        GL20.glShaderSource(vertexShaderID, vertexSource)
        GL20.glCompileShader(vertexShaderID)
        val fragmentSource = this.readShader(fragmentName)
        fragmentShaderID = GL20.glCreateShader(35632)
        GL20.glShaderSource(fragmentShaderID, fragmentSource)
        GL20.glCompileShader(fragmentShaderID)
        val compiled = GL20.glGetShaderi(fragmentShaderID, 35713) == 1
        programID = if (compiled) GL20.glCreateProgram() else 0
        val n = programID
        if (!compiled) {
            val shaderLog = GL20.glGetShaderInfoLog(fragmentShaderID, 2048)
            System.err.printf("%s: Failed to compile shader source. Message\n%s%n", this, shaderLog)
            return
        }
        GL20.glAttachShader(programID, vertexShaderID)
        GL20.glAttachShader(programID, fragmentShaderID)
        GL20.glLinkProgram(programID)
    }

    constructor(fragmentName: String) : this("sandbox/passthrough.vsh", fragmentName)

    fun renderCanvas() {
        GL11.glBegin(GL11.GL_QUADS)
        GL11.glVertex2f(-1f, -1f)
        GL11.glVertex2f(-1f, 1f)
        GL11.glVertex2f(1f, 1f)
        GL11.glVertex2f(1f, -1f)
        GL11.glEnd()
    }

    private fun readShader(fileName: String): String {
        val stringBuilder = StringBuilder()
        try {
            var line: String?
            val inputStreamReader = InputStreamReader(
                ShaderProgram::class.java.classLoader.getResourceAsStream(
                    String.format(
                        "assets/minecraft/${Main.name.lowercase()}/shaders/%s", fileName
                    )
                )
            )
            val bufferedReader = BufferedReader(inputStreamReader)
            while (bufferedReader.readLine().also { line = it } != null) {
                stringBuilder.append(line).append('\n')
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return stringBuilder.toString()
    }

    fun deleteShaderProgram() {
        GL20.glDeleteShader(vertexShaderID)
        GL20.glDeleteShader(fragmentShaderID)
        GL20.glDeleteProgram(programID)
    }

    fun init() {
        GL20.glUseProgram(programID)
    }

    fun uninit() {
        GL20.glUseProgram(0)
    }

    fun getUniform(name: String?): Int {
        return GL20.glGetUniformLocation(programID, name)
    }

    fun getCurrentUniformValue(name: String?, buffer: FloatBuffer): Float {
        GL20.glGetUniform(programID, getUniform(name), buffer)
        return buffer[0]
    }

    fun getCurrentUniformValue(name: String?, buffer: IntBuffer): Int {
        init()
        GL20.glGetUniform(programID, getUniform(name), buffer)
        uninit()
        return buffer[0]
    }

    fun getProgramID(): Int {
        return programID
    }

    override fun toString(): String {
        return "ShaderProgram{programID=$programID, vertexName='$vertexName', fragmentName='$fragmentName'}"
    }

}