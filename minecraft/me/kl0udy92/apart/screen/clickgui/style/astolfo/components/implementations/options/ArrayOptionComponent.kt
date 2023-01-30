package me.kl0udy92.apart.screen.clickgui.style.astolfo.components.implementations.options

import me.kl0udy92.apart.config.implementations.ModuleConfig
import me.kl0udy92.apart.core.Main
import me.kl0udy92.apart.option.implementations.ArrayOption
import me.kl0udy92.apart.screen.clickgui.style.astolfo.components.AbstractComponent
import me.kl0udy92.apart.screen.clickgui.style.astolfo.frames.AbstractFrame
import me.kl0udy92.apart.screen.clickgui.style.astolfo.frames.implementations.CategoryFrame
import me.kl0udy92.apart.utils.render.MouseUtil
import me.kl0udy92.apart.utils.render.RenderUtil
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.renderer.GlStateManager
import oh.yalan.NativeClass
import org.lwjgl.opengl.GL11
import java.awt.Color

@NativeClass
class ArrayOptionComponent(
    val arrayOption: ArrayOption,
    abstractFrame: AbstractFrame, x: Double, y: Double, width: Double, height: Double,
    private val categoryFrame: CategoryFrame = abstractFrame as CategoryFrame
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
                (sr.scaledWidth * 2) - mc.fontRendererObj.getStringWidth(this.arrayOption.description)
            val descriptionHeight = (sr.scaledHeight * 2) - mc.fontRendererObj.FONT_HEIGHT
            GlStateManager.scale(0.5, 0.5, 0.5)
            mc.fontRendererObj.drawStringWithOutline(
                this.arrayOption.description,
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
            "${this.arrayOption.name}: ${this.arrayOption.value}",
            this.categoryFrame.x + this.x + 2,
            this.categoryFrame.y + this.y + 5,
            -1
        )

        this.drawOptionDescription(mouseX, mouseY)
    }

    override fun update(mouseX: Int, mouseY: Int) {
        //Nothing here.
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int, button: Int) {
        val hovered = MouseUtil.isHovered(
            this.categoryFrame.x + this.x + 1.5,
            (this.categoryFrame.y + this.y).toDouble(),
            this.width - 1.5,
            this.height - 0.1, mouseX, mouseY
        )
        if (hovered) {
            var index = 0
            for (value in this.arrayOption.values) {
                if (value == this.arrayOption.value) break
                ++index
            }
            if (button == MouseUtil.ButtonType.LEFT.identifier) {
                Main.configManager.write(ModuleConfig())
                if (index + 1 < this.arrayOption.values.size) {
                    this.arrayOption.value = this.arrayOption.values[index + 1]
                } else {
                    this.arrayOption.value = this.arrayOption.values[0]
                }
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
        return this.arrayOption.dependency.invoke()
    }

    override fun closed() {
        //Nothing todo.
    }

}