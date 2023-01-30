package me.kl0udy92.apart.utils.render

import com.mojang.authlib.GameProfile
import com.mojang.authlib.minecraft.MinecraftProfileTexture
import me.kl0udy92.apart.utils.MinecraftInstance
import net.minecraft.client.entity.AbstractClientPlayer
import net.minecraft.client.gui.Gui
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.renderer.GLAllocation
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.OpenGlHelper
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.culling.Frustum
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.client.resources.DefaultPlayerSkin
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.AxisAlignedBB
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.Display
import org.lwjgl.opengl.GL11
import org.lwjgl.util.glu.GLU
import java.awt.Color
import javax.vecmath.Vector3d
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

object RenderUtil : MinecraftInstance() {

    private val frustrum = Frustum()

    private val viewport = GLAllocation.createDirectIntBuffer(16)
    private val modelview = GLAllocation.createDirectFloatBuffer(16)
    private val projection = GLAllocation.createDirectFloatBuffer(16)
    private val vector = GLAllocation.createDirectFloatBuffer(4)

    fun drawRect(left: Int, top: Int, right: Int, bottom: Int, color: Int) {
        var left = left
        var top = top
        var right = right
        var bottom = bottom
        if (left < right) {
            val i = left
            left = right
            right = i
        }
        if (top < bottom) {
            val j = top
            top = bottom
            bottom = j
        }
        val f3 = (color shr 24 and 255).toFloat() / 255.0f
        val f = (color shr 16 and 255).toFloat() / 255.0f
        val f1 = (color shr 8 and 255).toFloat() / 255.0f
        val f2 = (color and 255).toFloat() / 255.0f
        val tessellator = Tessellator.getInstance()
        val worldrenderer = tessellator.worldRenderer
        GlStateManager.enableBlend()
        GlStateManager.disableTexture2D()
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)
        GlStateManager.color(f, f1, f2, f3)
        worldrenderer.begin(7, DefaultVertexFormats.POSITION)
        worldrenderer.pos(left.toDouble(), bottom.toDouble(), 0.0).endVertex()
        worldrenderer.pos(right.toDouble(), bottom.toDouble(), 0.0).endVertex()
        worldrenderer.pos(right.toDouble(), top.toDouble(), 0.0).endVertex()
        worldrenderer.pos(left.toDouble(), top.toDouble(), 0.0).endVertex()
        tessellator.draw()
        GlStateManager.enableTexture2D()
        GlStateManager.disableBlend()
    }

    fun drawRect(left: Double, top: Double, right: Double, bottom: Double, color: Int) {
        var left = left
        var top = top
        var right = right
        var bottom = bottom
        if (left < right) {
            val i = left
            left = right
            right = i
        }
        if (top < bottom) {
            val j = top
            top = bottom
            bottom = j
        }
        val f3 = (color shr 24 and 255).toFloat() / 255.0f
        val f = (color shr 16 and 255).toFloat() / 255.0f
        val f1 = (color shr 8 and 255).toFloat() / 255.0f
        val f2 = (color and 255).toFloat() / 255.0f
        val tessellator = Tessellator.getInstance()
        val worldrenderer = tessellator.worldRenderer
        GlStateManager.enableBlend()
        GlStateManager.disableTexture2D()
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)
        GlStateManager.color(f, f1, f2, f3)
        worldrenderer.begin(7, DefaultVertexFormats.POSITION)
        worldrenderer.pos(left, bottom, 0.0).endVertex()
        worldrenderer.pos(right, bottom, 0.0).endVertex()
        worldrenderer.pos(right, top, 0.0).endVertex()
        worldrenderer.pos(left, top, 0.0).endVertex()
        tessellator.draw()
        GlStateManager.enableTexture2D()
        GlStateManager.disableBlend()
    }

    fun drawRect(left: Float, top: Float, right: Float, bottom: Float, color: Int) {
        var left = left
        var top = top
        var right = right
        var bottom = bottom
        if (left < right) {
            val i = left
            left = right
            right = i
        }
        if (top < bottom) {
            val j = top
            top = bottom
            bottom = j
        }
        val f3 = (color shr 24 and 255).toFloat() / 255.0f
        val f = (color shr 16 and 255).toFloat() / 255.0f
        val f1 = (color shr 8 and 255).toFloat() / 255.0f
        val f2 = (color and 255).toFloat() / 255.0f
        val tessellator = Tessellator.getInstance()
        val worldrenderer = tessellator.worldRenderer
        GlStateManager.enableBlend()
        GlStateManager.disableTexture2D()
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)
        GlStateManager.color(f, f1, f2, f3)
        worldrenderer.begin(7, DefaultVertexFormats.POSITION)
        worldrenderer.pos(left.toDouble(), bottom.toDouble(), 0.0).endVertex()
        worldrenderer.pos(right.toDouble(), bottom.toDouble(), 0.0).endVertex()
        worldrenderer.pos(right.toDouble(), top.toDouble(), 0.0).endVertex()
        worldrenderer.pos(left.toDouble(), top.toDouble(), 0.0).endVertex()
        tessellator.draw()
        GlStateManager.enableTexture2D()
        GlStateManager.disableBlend()
    }

    fun doScissor(x: Int, y: Int, width: Int, height: Int) {
        val scale = ScaledResolution(mc)
        val factor = scale.scaleFactor
        GL11.glScissor(
            x * factor,
            (scale.scaledHeight - height) * factor,
            (width + x) * factor,
            (height - y) * factor
        )
    }

    fun drawImage(image: ResourceLocation, x: Float, y: Float, width: Float, height: Float, color: Int) {
        GL11.glPushMatrix()
        GL11.glDisable(GL11.GL_DEPTH_TEST)
        GL11.glEnable(GL11.GL_BLEND)
        GL11.glDepthMask(false)
        val alpha = (color shr 24 and 255).toFloat() / 255.0f
        val red = (color shr 16 and 255).toFloat() / 255.0f
        val green = (color shr 8 and 255).toFloat() / 255.0f
        val blue = (color and 255).toFloat() / 255.0f
        OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO)
        GL11.glColor4f(red, green, blue, alpha)
        mc.textureManager.bindTexture(image)
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR)
        Gui.drawModalRectWithCustomSizedTexture(
            x.toInt(),
            y.toInt(),
            0.0f,
            0.0f,
            width.toInt(),
            height.toInt(),
            width,
            height
        )
        GlStateManager.color(1f, 1f, 1f, 1f)
        GL11.glDepthMask(true)
        GL11.glDisable(GL11.GL_BLEND)
        GL11.glEnable(GL11.GL_DEPTH_TEST)
        GL11.glPopMatrix()
    }

    fun drawFace(target: EntityPlayer, x: Int, y: Int) {
        val gameProfile = target.gameProfile
        val cache = mc.skinManager.loadSkinFromCache(gameProfile)
        val skin = if (cache.containsKey(MinecraftProfileTexture.Type.SKIN)) {
            mc.skinManager.loadSkin(
                cache[MinecraftProfileTexture.Type.SKIN],
                MinecraftProfileTexture.Type.SKIN
            )
        } else {
            val skin = EntityPlayer.getUUID(gameProfile)
            DefaultPlayerSkin.getDefaultSkin(skin)
        }
        if (skin != null) {
            mc.textureManager.bindTexture(skin)
            GL11.glEnable(3042)
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f)
            Gui.drawModalRectWithCustomSizedTexture(x, y, 24.0f, 24.0f, 24, 24, 192.0f, 192.0f)
            Gui.drawModalRectWithCustomSizedTexture(x, y, 120.0f, 24.0f, 24, 24, 192.0f, 192.0f)
            GL11.glDisable(3042)
        }
    }

    fun drawFace(name: String, x: Int, y: Int) {
        AbstractClientPlayer.getDownloadImageSkin(AbstractClientPlayer.getLocationSkin(name), name)
            .loadTexture(mc.resourceManager);
        if (AbstractClientPlayer.getLocationSkin(name) != null) {
            mc.textureManager.bindTexture(AbstractClientPlayer.getLocationSkin(name))
            GL11.glEnable(3042)
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f)
            Gui.drawModalRectWithCustomSizedTexture(x, y, 24.0f, 24.0f, 24, 24, 192.0f, 192.0f)
            Gui.drawModalRectWithCustomSizedTexture(x, y, 120.0f, 24.0f, 24, 24, 192.0f, 192.0f)
            GL11.glDisable(3042)
        }
    }

    fun drawFace(profile: GameProfile, x: Int, y: Int) {
        val gameProfile = profile
        val cache = mc.skinManager.loadSkinFromCache(gameProfile)
        val skin = if (cache.containsKey(MinecraftProfileTexture.Type.SKIN)) {
            mc.skinManager.loadSkin(
                cache[MinecraftProfileTexture.Type.SKIN],
                MinecraftProfileTexture.Type.SKIN
            )
        } else {
            val skin = EntityPlayer.getUUID(gameProfile)
            DefaultPlayerSkin.getDefaultSkin(skin)
        }
        if (skin != null) {
            mc.textureManager.bindTexture(skin)
            GL11.glEnable(3042)
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f)
            Gui.drawModalRectWithCustomSizedTexture(x, y, 24.0f, 24.0f, 24, 24, 192.0f, 192.0f)
            Gui.drawModalRectWithCustomSizedTexture(x, y, 120.0f, 24.0f, 24, 24, 192.0f, 192.0f)
            GL11.glDisable(3042)
        }
    }

    fun isInViewFrustrum(entity: Entity): Boolean {
        return this.isInViewFrustrum(entity.entityBoundingBox) || entity.ignoreFrustumCheck
    }

    private fun isInViewFrustrum(bb: AxisAlignedBB): Boolean {
        val current = mc.renderViewEntity
        this.frustrum.setPosition(current.posX, current.posY, current.posZ)
        return this.frustrum.isBoundingBoxInFrustum(bb)
    }

    fun project2D(scaleFactor: Int, x: Double, y: Double, z: Double): Vector3d? {
        GL11.glGetFloat(2982, this.modelview)
        GL11.glGetFloat(2983, this.projection)
        GL11.glGetInteger(2978, this.viewport)
        return if (GLU.gluProject(
                x.toFloat(),
                y.toFloat(),
                z.toFloat(),
                this.modelview,
                this.projection,
                this.viewport,
                this.vector
            )
        ) {
            Vector3d(
                (this.vector.get(0) / scaleFactor.toFloat()).toDouble(),
                ((Display.getHeight().toFloat() - this.vector.get(1)) / scaleFactor.toFloat()).toDouble(),
                this.vector.get(2).toDouble()
            )
        } else null
    }

    fun drawRoundedRect(x: Float, y: Float, width: Float, height: Float, round: Float, color: Int) {
        var x = x
        var y = y
        var width = width
        var height = height
        x = (x.toDouble() + ((round / 2.0f).toDouble() + 0.5)).toFloat()
        y = (y.toDouble() + ((round / 2.0f).toDouble() + 0.5)).toFloat()
        width = (width.toDouble() - ((round / 2.0f).toDouble() + 0.5)).toFloat()
        height = (height.toDouble() - ((round / 2.0f).toDouble() + 0.5)).toFloat()
        this.drawRect(x, y, width, height, color)
        this.drawCircle((width - round / 2.0f), (y + round / 2.0f), 0f, 360f, round, color)
        this.drawCircle((x + round / 2.0f), (height - round / 2.0f), 0f, 360f, round, color)
        this.drawCircle((x + round / 2.0f), (y + round / 2.0f), 0f, 360f, round, color)
        this.drawCircle((width - round / 2.0f), (height - round / 2.0f), 0f, 360f, round, color)
        this.drawRect(
            (x - round / 2.0f - 0.5f),
            (y + round / 2.0f), width, (height - round / 2.0f), color
        )
        this.drawRect(
            x,
            (y + round / 2.0f), (width + round / 2.0f + 0.5f), (height - round / 2.0f), color
        )
        this.drawRect(
            (x + round / 2.0f),
            (y - round / 2.0f - 0.5f), (width - round / 2.0f), (height - round / 2.0f), color
        )
        this.drawRect(
            (x + round / 2.0f),
            y, (width - round / 2.0f), (height + round / 2.0f + 0.5f), color
        )
    }

    fun drawRoundedRect(x: Double, y: Double, width: Double, height: Double, round: Double, color: Int) {
        this.drawRoundedRect(x.toFloat(), y.toFloat(), width.toFloat(), height.toFloat(), round.toFloat(), color)
    }

    fun drawRoundedRect(x: Int, y: Int, width: Int, height: Int, round: Double, color: Int) {
        this.drawRoundedRect(x.toFloat(), y.toFloat(), width.toFloat(), height.toFloat(), round.toFloat(), color)
    }

    fun drawRectWithin2Circles(x: Float, y: Float, width: Float, height: Float, color: Int) {
        val radius = (height - y) / 2f
        this.drawCircle(x + radius, y + radius, radius - 0.5f, color)
        this.drawCircle(width - radius, y + radius, radius - 0.5f, color)
        this.drawRect(x + radius, y, width - radius, height, color)
    }

    fun drawRectWithin2Circles(x: Double, y: Double, width: Double, height: Double, color: Int) {
        this.drawRectWithin2Circles(x.toFloat(), y.toFloat(), width.toFloat(), height.toFloat(), color)
    }

    fun drawRectWithin2Circles(x: Int, y: Int, width: Int, height: Int, color: Int) {
        this.drawRectWithin2Circles(x.toFloat(), y.toFloat(), width.toFloat(), height.toFloat(), color)
    }

    fun drawCircle(x: Float, y: Float, start: Float, end: Float, radius: Float, color: Int) {
        var start = start
        var end = end
        var ldy: Float
        var ldx: Float
        var i: Float
        var temp = 0.0f
        if (start > end) {
            temp = end
            end = start
            start = temp
        }
        val alpha = (color shr 24 and 255).toFloat() / 255.0f
        val red = (color shr 16 and 255).toFloat() / 255.0f
        val green = (color shr 8 and 255).toFloat() / 255.0f
        val blue = (color and 255).toFloat() / 255.0f
        GlStateManager.enableBlend()
        GlStateManager.disableTexture2D()
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)
        GlStateManager.color(red, green, blue, alpha)
        if (alpha > 0.5f) {
            GL11.glEnable(GL11.GL_LINE_SMOOTH)
            GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST)
            GL11.glLineWidth(Float.MIN_VALUE)
            GL11.glBegin(GL11.GL_LINE_STRIP)
            i = end
            while (i >= start) {
                ldx = cos((i.toDouble() * PI / 180.0)).toFloat() * (radius * 1)
                ldy = sin((i.toDouble() * PI / 180.0)).toFloat() * (radius * 1)
                GL11.glVertex2f((x + ldx), (y + ldy))
                i -= 4.0f
            }
            GL11.glEnd()
            GL11.glDisable(GL11.GL_LINE_SMOOTH)
        }
        GL11.glEnable(GL11.GL_LINE_SMOOTH)
        GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
        GL11.glLineWidth(Float.MIN_VALUE)
        GL11.glBegin(GL11.GL_TRIANGLE_FAN)
        i = end
        while (i >= start) {
            ldx = cos((i.toDouble() * PI / 180.0)).toFloat() * radius
            ldy = sin((i.toDouble() * PI / 180.0)).toFloat() * radius
            GL11.glVertex2f((x + ldx), (y + ldy))
            i -= 4.0f
        }
        GL11.glEnd()
        GL11.glDisable(GL11.GL_LINE_SMOOTH)
        GlStateManager.enableTexture2D()
        GlStateManager.disableBlend()
    }

    fun drawCircle(x: Float, y: Float, radius: Float, color: Int) {
        this.drawCircle(x, y, 0f, 360f, radius, color)
    }

    fun drawGradientRect(
        x: Float,
        y: Float,
        width: Float,
        height: Float,
        firstColor: Int,
        secondColor: Int,
        perpendicular: Boolean
    ) {
        GL11.glPushMatrix()
        GL11.glEnable(GL11.GL_BLEND)
        GL11.glDisable(GL11.GL_TEXTURE_2D)
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
        GL11.glEnable(GL11.GL_LINE_SMOOTH)
        GL11.glPushMatrix()
        GL11.glShadeModel(GL11.GL_SMOOTH)
        GL11.glBegin(GL11.GL_QUADS)
        this.resetColour4d(firstColor)
        GL11.glVertex2d(width.toDouble(), y.toDouble())
        if (perpendicular) this.resetColour4d(secondColor)
        GL11.glVertex2d(x.toDouble(), y.toDouble())
        this.resetColour4d(secondColor)
        GL11.glVertex2d(x.toDouble(), height.toDouble())
        if (perpendicular) this.resetColour4d(firstColor)
        GL11.glVertex2d(width.toDouble(), height.toDouble())
        GL11.glEnd()
        GL11.glShadeModel(GL11.GL_FLAT)
        GL11.glPopMatrix()
        GL11.glEnable(GL11.GL_TEXTURE_2D)
        GL11.glDisable(GL11.GL_BLEND)
        GL11.glDisable(GL11.GL_LINE_SMOOTH)
        GL11.glPopMatrix()
    }

    fun drawGradientRect(
        x: Double,
        y: Double,
        width: Double,
        height: Double,
        firstColor: Int,
        secondColor: Int,
        perpendicular: Boolean
    ) {
        this.drawGradientRect(x.toFloat(), y.toFloat(), width.toFloat(), height.toFloat(), firstColor, secondColor, perpendicular)
    }

    fun drawGradientRect(
        x: Int,
        y: Int,
        width: Int,
        height: Int,
        firstColor: Int,
        secondColor: Int,
        perpendicular: Boolean
    ) {
        this.drawGradientRect(x.toDouble(), y.toDouble(), width.toDouble(), height.toDouble(), firstColor, secondColor, perpendicular)
    }

    fun resetColour4d(colour: Color) {
        GL11.glColor4d(colour.red / 255.0, colour.green / 255.0, colour.blue / 255.0, colour.alpha / 255.0)
    }

    fun resetColour4f(colour: Color) {
        GL11.glColor4f(colour.red / 255f, colour.green / 255f, colour.blue / 255f, colour.alpha / 255f)
    }

    fun resetColour4d(hex: Int) {
        val colour = Color(hex)
        GL11.glColor4d(colour.red / 255.0, colour.green / 255.0, colour.blue / 255.0, colour.alpha / 255.0)
    }

    fun resetColour4f(hex: Int) {
        val colour = Color(hex)
        GL11.glColor4f(colour.red / 255f, colour.green / 255f, colour.blue / 255f, colour.alpha / 255f)
    }

}