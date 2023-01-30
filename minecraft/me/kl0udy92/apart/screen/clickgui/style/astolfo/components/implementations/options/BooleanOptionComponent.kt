package me.kl0udy92.apart.screen.clickgui.style.astolfo.components.implementations.options

import me.kl0udy92.apart.config.implementations.ModuleConfig
import me.kl0udy92.apart.core.Main
import me.kl0udy92.apart.option.implementations.BooleanOption
import me.kl0udy92.apart.screen.clickgui.style.astolfo.components.AbstractComponent
import me.kl0udy92.apart.screen.clickgui.style.astolfo.frames.AbstractFrame
import me.kl0udy92.apart.screen.clickgui.style.astolfo.frames.implementations.CategoryFrame
import me.kl0udy92.apart.utils.render.ColorUtil
import me.kl0udy92.apart.utils.render.MouseUtil
import me.kl0udy92.apart.utils.render.RenderUtil
import me.kl0udy92.apart.utils.render.animations.easing.Animation
import me.kl0udy92.apart.utils.render.animations.easing.util.Easings
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.renderer.GlStateManager
import oh.yalan.NativeClass
import org.lwjgl.opengl.GL11
import java.awt.Color

@NativeClass
class BooleanOptionComponent(
    val booleanOption: BooleanOption,
    abstractFrame: AbstractFrame, x: Double, y: Double, width: Double, height: Double,
    private val categoryFrame: CategoryFrame = abstractFrame as CategoryFrame,
    private val alphaAnimation: Animation = Animation()
) : AbstractComponent(abstractFrame, x, y, width, height, 15.0) {

    private fun drawOptionDescription(mouseX: Int, mouseY: Int) {
        GlStateManager.pushMatrix()
        GL11.glDisable(GL11.GL_SCISSOR_TEST)
        val hovered = MouseUtil.isHovered(
            this.categoryFrame.x + this.x + 1.5,
            (this.categoryFrame.y + this.y).toDouble(),
            this.width - 1.5,
            this.height - 0.1, mouseX, mouseY
        )
        if (hovered) {
            val sr = ScaledResolution(mc)
            val descriptionWidth =
                (sr.scaledWidth * 2) - mc.fontRendererObj.getStringWidth(this.booleanOption.description)
            val descriptionHeight = (sr.scaledHeight * 2) - mc.fontRendererObj.FONT_HEIGHT
            GlStateManager.scale(0.5, 0.5, 0.5)
            mc.fontRendererObj.drawStringWithOutline(
                this.booleanOption.description,
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
            (this.categoryFrame.y + this.y),
            this.categoryFrame.x + this.x + this.width - 1.5,
            this.categoryFrame.y + this.y + this.height + 0.1, Color.black.rgb
        )
        Main.fontBuffer.SF16.drawStringWithOutline(
            this.booleanOption.name,
            this.categoryFrame.x + this.x + 2,
            this.categoryFrame.y + this.y + 5,
            -1
        )

        RenderUtil.drawRect(
            this.categoryFrame.x + this.x + this.width - 10,
            this.categoryFrame.y + this.y + this.height - 10,
            this.categoryFrame.x + this.x + this.width - 4,
            this.categoryFrame.y + this.y + this.height - 4, -1
        )

        RenderUtil.drawRect(
            this.categoryFrame.x + this.x + this.width - 10,
            this.categoryFrame.y + this.y + this.height - 10,
            this.categoryFrame.x + this.x + this.width - 4,
            this.categoryFrame.y + this.y + this.height - 4,
            ColorUtil.reAlpha(this.categoryFrame.category.color, this.alphaAnimation.value).rgb
        )

        this.drawOptionDescription(mouseX, mouseY)
    }

    override fun update(mouseX: Int, mouseY: Int) {
        this.alphaAnimation.update()
        this.alphaAnimation.animate(
            if (this.booleanOption.value) 255.0 else 0.0,
            .5,
            Easings().EXPO_BOTH,
            true
        )
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int, button: Int) {
        val hovered = MouseUtil.isHovered(
            this.categoryFrame.x + this.x + 1.5,
            (this.categoryFrame.y + this.y).toDouble(),
            this.width - 1.5,
            this.height - 0.1, mouseX, mouseY
        )
        if (hovered) {
            if (button == MouseUtil.ButtonType.LEFT.identifier) {
                Main.configManager.write(ModuleConfig())
                this.booleanOption.value = !this.booleanOption.value
            }
        }
    }

    override fun mouseReleased(mouseX: Int, mouseY: Int, state: Int) {
        //Nothing here.
    }

    override fun keyTyped(typedChar: Char, key: Int) {
        //Nothing here.
    }

    override fun isVisible(): Boolean {
        return this.booleanOption.dependency.invoke()
    }

    override fun closed() {
        //Nothing todo.
    }

}