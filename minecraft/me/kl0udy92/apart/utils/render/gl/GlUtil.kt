package me.kl0udy92.apart.utils.render.gl

import org.lwjgl.opengl.GL11

object GlUtil {

    fun draw(mode: Int, render: Runnable) {
        GL11.glBegin(mode)
        render.run()
        GL11.glEnd()
    }

    fun scale(x: Float, y: Float, scale: Float, runnable: Runnable) {
        GL11.glPushMatrix()
        GL11.glTranslatef(x, y, 0f)
        GL11.glScalef(scale, scale, 1f)
        GL11.glTranslatef(-x, -y, 0f)
        runnable.run()
        GL11.glPopMatrix()
    }

}