package me.kl0udy92.apart.screen.clickgui.style.astolfo.components.implementations.options

import me.kl0udy92.apart.core.Main
import me.kl0udy92.apart.option.implementations.DoubleOption
import me.kl0udy92.apart.screen.clickgui.style.astolfo.components.AbstractComponent
import me.kl0udy92.apart.screen.clickgui.style.astolfo.frames.AbstractFrame
import me.kl0udy92.apart.screen.clickgui.style.astolfo.frames.implementations.CategoryFrame
import me.kl0udy92.apart.utils.render.MouseUtil
import me.kl0udy92.apart.utils.render.RenderUtil
import me.kl0udy92.apart.utils.render.animations.easing.Animation
import me.kl0udy92.apart.utils.render.animations.easing.util.Easings
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.renderer.GlStateManager
import oh.yalan.NativeClass
import org.lwjgl.opengl.GL11
import java.awt.Color
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.roundToInt

@NativeClass
class DoubleOptionComponent(
    val doubleOption: DoubleOption,
    abstractFrame: AbstractFrame, x: Double, y: Double, width: Double, height: Double,
    private val categoryFrame: CategoryFrame = abstractFrame as CategoryFrame, var drag: Boolean = false,
    private val animation: Animation = Animation()
) : AbstractComponent(abstractFrame, x, y, width, height, 15.0) {

    private fun drawOptionDescription(mouseX: Int, mouseY: Int) {
        GlStateManager.pushMatrix()
        GL11.glDisable(GL11.GL_SCISSOR_TEST)
        val hovered = MouseUtil.isHovered(
            this.categoryFrame.x + this.x + 1.5,
            (this.categoryFrame.y + this.y),
            this.width - 1.5,
            this.height - 0.1, mouseX, mouseY
        )
        if (hovered || this.drag) {
            val sr = ScaledResolution(mc)
            val descriptionWidth =
                (sr.scaledWidth * 2) - mc.fontRendererObj.getStringWidth(this.doubleOption.description)
            val descriptionHeight = (sr.scaledHeight * 2) - mc.fontRendererObj.FONT_HEIGHT
            GlStateManager.scale(0.5, 0.5, 0.5)
            mc.fontRendererObj.drawStringWithOutline(
                this.doubleOption.description,
                descriptionWidth,
                descriptionHeight,
                -1
            )
        }
        GL11.glEnable(GL11.GL_SCISSOR_TEST)
        GlStateManager.popMatrix()
    }

    override fun init() {
        //Nothing todo.
    }

    override fun render(mouseX: Int, mouseY: Int) {
        RenderUtil.drawRect(
            this.categoryFrame.x + this.x + 1.5,
            (this.categoryFrame.y + this.y).toDouble(),
            this.categoryFrame.x + this.x + this.width - 1.5,
            this.categoryFrame.y + this.y + this.height + 0.1, Color.black.rgb
        )
        Main.fontBuffer.SF16.drawStringWithOutline(
            this.doubleOption.name,
            this.categoryFrame.x + this.x + 2,
            this.categoryFrame.y + this.y + 4,
            -1
        )

        Main.fontBuffer.tahomaBold11.drawString(
            this.clamp(this.doubleOption.value, this.doubleOption.increment).toString(),
            this.categoryFrame.x + this.x + this.width - Main.fontBuffer.tahomaBold11.getStringWidth(
                this.clamp(
                    this.doubleOption.value,
                    this.doubleOption.increment
                ).toString()
            ) - 4, this.categoryFrame.y + this.y + 4, -1
        )

        RenderUtil.drawRect(
            (this.categoryFrame.x + this.x),
            this.categoryFrame.y + this.y + this.height - 1.5,
            this.categoryFrame.x + this.x + 1.0 + this.animation.value,
            (this.categoryFrame.y + this.y + this.height - 1).toDouble(), this.categoryFrame.category.color.rgb
        )

        this.drawOptionDescription(mouseX, mouseY)
    }

    override fun update(mouseX: Int, mouseY: Int) {
        this.animation.update()
        this.animation.animate(
            this.width * (this.doubleOption.value - this.doubleOption.min) / (this.doubleOption.max - this.doubleOption.min) - 1.0,
            0.3,
            Easings().EXPO_OUT,
            true
        )

        if (this.drag) {
            this.doubleOption.value = this.clamp(
                (mouseX - (this.categoryFrame.x + this.x)).toDouble() * (this.doubleOption.max - this.doubleOption.min) / this.width + this.doubleOption.min,
                this.doubleOption.increment
            )
            if (this.doubleOption.value > this.doubleOption.max) this.doubleOption.value = this.doubleOption.max
            else if (this.doubleOption.value < this.doubleOption.min) this.doubleOption.value = this.doubleOption.min
        }
    }

    private fun clamp(value: Double, increment: Double): Double {
        return BigDecimal((value / increment).roundToInt() * increment).setScale(
            increment.toString().length - (increment.toString().indexOf(".") + 1), RoundingMode.HALF_UP
        ).toDouble()
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int, button: Int) {
        val hovered = MouseUtil.isHovered(
            this.categoryFrame.x + this.x + 1.5,
            (this.categoryFrame.y + this.y),
            this.width - 1.5,
            this.height - 0.1, mouseX, mouseY
        )
        if (hovered && button == MouseUtil.ButtonType.LEFT.identifier) this.drag = true
    }

    override fun mouseReleased(mouseX: Int, mouseY: Int, state: Int) {
        this.drag = false
    }

    override fun keyTyped(typedChar: Char, key: Int) {
        //Nothing here.
    }

    override fun closed() {
        //Nothing todo.
    }

    override fun isVisible(): Boolean {
        return this.doubleOption.dependency.invoke()
    }

}