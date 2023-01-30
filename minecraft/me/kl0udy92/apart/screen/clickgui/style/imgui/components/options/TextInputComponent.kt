package me.kl0udy92.apart.screen.clickgui.style.imgui.components.options

import me.kl0udy92.apart.config.implementations.ModuleConfig
import me.kl0udy92.apart.core.Main
import me.kl0udy92.apart.option.implementations.StringOption
import me.kl0udy92.apart.screen.clickgui.style.astolfo.components.AbstractComponent
import me.kl0udy92.apart.screen.clickgui.style.imgui.components.ModuleComponent
import me.kl0udy92.apart.screen.clickgui.style.imgui.frames.CategoryFrame
import me.kl0udy92.apart.screen.clickgui.style.imgui.utils.Direction
import me.kl0udy92.apart.screen.clickgui.style.imgui.utils.Palette
import me.kl0udy92.apart.utils.render.ColorUtil
import me.kl0udy92.apart.utils.render.MouseUtil
import me.kl0udy92.apart.utils.render.RenderUtil
import me.kl0udy92.apart.utils.render.animations.easing.Animation
import me.kl0udy92.apart.utils.render.animations.easing.util.Easings
import oh.yalan.NativeClass
import org.lwjgl.input.Keyboard
import java.awt.Color
import java.nio.charset.Charset
import kotlin.math.roundToInt

@NativeClass
class TextInputComponent(
    val stringOption: StringOption,
    categoryFrame: CategoryFrame,
    val daddy: ModuleComponent,
    x: Double, y: Double, width: Double, height: Double, offset: Double = 30.0,
    var selected: Boolean = false, var textOpacity: Int = 115,
    var selectedBarOpacity: Int = 0, var selectedBarAnimatedX: Animation = Animation(), var direction: Direction = Direction.OUT
): AbstractComponent(categoryFrame, x, y, width, height, offset) {

    override fun init() {
        //Nothing todo.
    }

    override fun render(mouseX: Int, mouseY: Int) {
        Main.fontBuffer.SF16.drawString(
            this.stringOption.name,
            this.x + 3.0,
            this.daddy.daddy.gui.y + this.daddy.y + this.y - this.daddy.daddy.scrollTranslate.y,
            Color(this.textOpacity, this.textOpacity, this.textOpacity).rgb
        )

        RenderUtil.drawRect(
            this.x + 2.5,
            this.daddy.daddy.gui.y + this.daddy.y + this.y - this.daddy.daddy.scrollTranslate.y + 11.5,
            this.x + 3.0 + this.width - 7 + 0.5,
            this.daddy.daddy.gui.y + this.daddy.y + this.y - this.daddy.daddy.scrollTranslate.y + 25.5,
            Palette.LINE_GRAY.color.rgb
        )
        RenderUtil.drawRect(
            this.x + 3.0,
            this.daddy.daddy.gui.y + this.daddy.y + this.y - this.daddy.daddy.scrollTranslate.y + 12.0,
            this.x + 3.0 + this.width - 7,
            this.daddy.daddy.gui.y + this.daddy.y + this.y - this.daddy.daddy.scrollTranslate.y + 25.0,
            Palette.BACKGROUND.color.rgb
        )
        Main.fontBuffer.SF16.drawString(
            this.stringOption.value,
            this.x + 7.0,
            this.daddy.daddy.gui.y + this.daddy.y + this.y - this.daddy.daddy.scrollTranslate.y + 14.0,
            -1
        )

        RenderUtil.drawRect(this.x + this.selectedBarAnimatedX.value, this.daddy.daddy.gui.y + this.daddy.y + this.y - this.daddy.daddy.scrollTranslate.y + 15.0, this.x + this.selectedBarAnimatedX.value + 0.8, this.daddy.daddy.gui.y + this.daddy.y + this.y - this.daddy.daddy.scrollTranslate.y + 15.0 + 7.0, ColorUtil.reAlpha(Color.WHITE, this.selectedBarOpacity.toDouble()).rgb)
    }

    override fun update(mouseX: Int, mouseY: Int) {
        if (this.selected) {
            Keyboard.enableRepeatEvents(true)
            this.selectedBarAnimatedX.update()
            this.selectedBarAnimatedX.animate(7.8 + Main.fontBuffer.SF16.getStringWidth(this.stringOption.value), 0.2, Easings().EXPO_OUT, true)
            if (this.textOpacity != 255) this.textOpacity += 5
            this.updateSelectedOpacity()
        }else {
            Keyboard.enableRepeatEvents(false)
            if (this.textOpacity > 115) this.textOpacity -= 5
            if (this.selectedBarOpacity > 0) this.selectedBarOpacity -= 5
        }
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int, button: Int) {
        if (button == MouseUtil.ButtonType.LEFT.identifier)
            if (this.isHovering(mouseX, mouseY)) this.selected = !this.selected
    }

    override fun mouseReleased(mouseX: Int, mouseY: Int, state: Int) {
        //Nothing todo.
    }

    override fun keyTyped(typedChar: Char, key: Int) {
        Main.configManager.write(ModuleConfig())
        if (key == Keyboard.KEY_RSHIFT || key == Keyboard.KEY_LSHIFT || key == Keyboard.KEY_CAPITAL || !this.selected) return

        //enter
        if (key == Keyboard.KEY_RETURN) {
            this.selected = false
            return
        }

        //backspace
        if (key == Keyboard.KEY_BACK) {
            if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
                this.stringOption.value = ""
                return
            }
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

        if (this.stringOption.value.toByteArray(Charset.defaultCharset()).size < 23) {
            whitelistedChars.filter { typedChar == it }.forEach { _ ->
                this.stringOption.value = this.stringOption.value + typedChar
            }

            if (!Character.isLetterOrDigit(typedChar)) {
                return
            }

            this.stringOption.value = this.stringOption.value + typedChar
        }
    }

    fun isHovering(mouseX: Int, mouseY: Int): Boolean {
        return MouseUtil.isHovering(
            this.x + 2.5,
            this.daddy.daddy.gui.y + this.daddy.y + this.y - this.daddy.daddy.scrollTranslate.y + 11.5,
            this.x + 3.0 + this.width - 7 + 0.5,
            this.daddy.daddy.gui.y + this.daddy.y + this.y - this.daddy.daddy.scrollTranslate.y + 25.5, mouseX, mouseY
        ) && MouseUtil.isHovering(
            (this.daddy.daddy.gui.x + 41).roundToInt(),
            (this.daddy.daddy.gui.y + 25 + 25.5).roundToInt(),
            (this.daddy.daddy.gui.x + 45.0 + 190.0 + 5.0 + 191.0).roundToInt(),
            (this.daddy.daddy.gui.y + 250.0).roundToInt(),
            mouseX, mouseY
        )
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