package me.kl0udy92.apart.screen.clickgui.style.astolfo.components.implementations.options

import me.kl0udy92.apart.config.implementations.ModuleConfig
import me.kl0udy92.apart.core.Main
import me.kl0udy92.apart.option.implementations.StringOption
import me.kl0udy92.apart.screen.clickgui.style.astolfo.components.AbstractComponent
import me.kl0udy92.apart.screen.clickgui.style.astolfo.frames.AbstractFrame
import me.kl0udy92.apart.screen.clickgui.style.astolfo.frames.implementations.CategoryFrame
import me.kl0udy92.apart.screen.clickgui.style.imgui.utils.Direction
import me.kl0udy92.apart.utils.render.ColorUtil
import me.kl0udy92.apart.utils.render.MouseUtil
import me.kl0udy92.apart.utils.render.RenderUtil
import me.kl0udy92.apart.utils.render.animations.easing.Animation
import me.kl0udy92.apart.utils.render.animations.easing.util.Easings
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.renderer.GlStateManager
import oh.yalan.NativeClass
import org.lwjgl.input.Keyboard
import org.lwjgl.opengl.GL11
import java.awt.Color

@NativeClass
class StringOptionComponent(
    val stringOption: StringOption,
    abstractFrame: AbstractFrame, x: Double, y: Double, width: Double, height: Double,
    private val categoryFrame: CategoryFrame = abstractFrame as CategoryFrame,
    private var selected: Boolean = false, var selectedBarOpacity: Int = 0,
    var selectedBarAnimatedX: Animation = Animation(), var direction: Direction = Direction.OUT
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
                (sr.scaledWidth * 2) - mc.fontRendererObj.getStringWidth(this.stringOption.description)
            val descriptionHeight = (sr.scaledHeight * 2) - mc.fontRendererObj.FONT_HEIGHT
            GlStateManager.scale(0.5, 0.5, 0.5)
            mc.fontRendererObj.drawStringWithOutline(
                this.stringOption.description,
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
            this.categoryFrame.x + this.width - 1.5,
            this.categoryFrame.y + this.y + this.height + 0.1, -16777216
        )

        Main.fontBuffer.SF17.drawString(
            this.stringOption.value,
            this.categoryFrame.x + this.x + 1 + 4,
            this.categoryFrame.y + this.y + 4,
            -1
        )

        RenderUtil.drawRect(this.categoryFrame.x + this.x + this.selectedBarAnimatedX.value, this.categoryFrame.y + this.y + 1 + 3, this.categoryFrame.x + this.x + this.selectedBarAnimatedX.value + 0.8, this.categoryFrame.y + this.y + 1 + 3 + 7.0, ColorUtil.reAlpha(Color.WHITE, this.selectedBarOpacity.toDouble()).rgb)

        this.drawOptionDescription(mouseX, mouseY)
    }

    override fun update(mouseX: Int, mouseY: Int) {
        if (this.selected) {
            Keyboard.enableRepeatEvents(true)
            this.selectedBarAnimatedX.update()
            this.selectedBarAnimatedX.animate(
                5.5 + Main.fontBuffer.SF17.getStringWidth(this.stringOption.value),
                0.2,
                Easings().EXPO_OUT,
                true
            )
            this.updateSelectedOpacity()
        }else {
            Keyboard.enableRepeatEvents(false)
            if (this.selectedBarOpacity > 0) this.selectedBarOpacity -= 5
        }
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int, button: Int) {
        val hovered = MouseUtil.isHovered(
            this.categoryFrame.x + this.x + 1.5,
            (this.categoryFrame.y + this.y).toDouble(),
            this.width - 1.5,
            this.height - 0.1, mouseX, mouseY
        )
        if (button == MouseUtil.ButtonType.LEFT.identifier) {
            this.selected = hovered
        }
    }

    override fun mouseReleased(mouseX: Int, mouseY: Int, state: Int) {
        //Nothing here.
    }

    override fun keyTyped(typedChar: Char, key: Int) {
        Main.configManager.write(ModuleConfig())
        if (key == Keyboard.KEY_RSHIFT || key == Keyboard.KEY_LSHIFT || key == Keyboard.KEY_CAPITAL) return

        //enter
        if (key == Keyboard.KEY_RETURN) {
            this.selected = false
            return
        }

        //backspace
        if (key == Keyboard.KEY_BACK && this.selected) {
            if (this.stringOption.value.toCharArray().isEmpty()) {
                return
            }
            this.stringOption.value = this.removeLastChar(this.stringOption.value)
            return
        }

        val whitelistedChars = listOf(
            '&', ' ', '#', '[', ']', '(', ')',
            '.', ',', '<', '>', '-', '$',
            '!', '"', '\'', '\\', '/', '=',
            '+', ',', '|', '^', '?', '`', ';', ':',
            '@', '£', '%', '{', '}', '_', '*', '»'
        )

        if (this.selected) {
            whitelistedChars.filter { typedChar == it }.forEach { _ ->
                this.stringOption.value = this.stringOption.value + typedChar
            }
        }

        if (!Character.isLetterOrDigit(typedChar)) {
            return
        }

        if (this.selected) {
            this.stringOption.value = this.stringOption.value + typedChar
        }
    }

    override fun closed() {
        //Nothing todo.
    }

    private fun removeLastChar(s: String): String {
        return s.substring(0, s.length - 1)
    }

    override fun isVisible(): Boolean {
        return this.stringOption.dependency.invoke()
    }

    fun updateSelectedOpacity() {
        if (this.selectedBarOpacity != 255 && this.direction != Direction.IN) {
            this.selectedBarOpacity += 5
        }else if (this.selectedBarOpacity != 0 && this.direction == Direction.IN) {
            this.selectedBarOpacity -= 5
        }
        if (this.selectedBarOpacity == 0 && this.direction == Direction.IN) {
            this.direction = Direction.OUT
        }else if (this.selectedBarOpacity == 255) {
            this.direction = Direction.IN
        }
    }

}