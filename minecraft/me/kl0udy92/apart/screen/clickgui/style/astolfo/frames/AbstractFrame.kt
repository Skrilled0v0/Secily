package me.kl0udy92.apart.screen.clickgui.style.astolfo.frames

import me.kl0udy92.apart.screen.clickgui.style.astolfo.components.AbstractComponent
import me.kl0udy92.apart.utils.MinecraftInstance
import me.kl0udy92.apart.utils.render.MouseUtil
import oh.yalan.NativeClass
import org.lwjgl.input.Keyboard

@NativeClass
abstract class AbstractFrame(
    var name: String,
    var x: Double,
    var y: Double,
    var width: Double,
    var height: Double,
    var open: Boolean,
    var drag: Boolean,
    var dragX: Double,
    var dragY: Double
) : IFrame, MinecraftInstance() {

    val components = mutableListOf<AbstractComponent>()

    override fun update(mouseX: Int, mouseY: Int) {
        if (this.drag) {
            this.x = mouseX - this.dragX
            this.y = mouseY - this.dragY
        }
        if (this.open && this.components.isNotEmpty()) this.components.forEach { it.update(mouseX, mouseY) }
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int, button: Int) {
        val hovered = MouseUtil.isHovered(
            this.x.toFloat(),
            this.y.toFloat(),
            this.width.toFloat(),
            this.height.toFloat(),
            mouseX,
            mouseY
        )
        if (hovered) {
            if (button == MouseUtil.ButtonType.LEFT.identifier && !this.drag) {
                this.drag = true
                this.dragX = mouseX - this.x
                this.dragY = mouseY - this.y
            }
            if (button == MouseUtil.ButtonType.RIGHT.identifier) {
                this.open = !this.open
            }
        }
        if (this.open && this.components.isNotEmpty()) this.components.forEach {
            it.mouseClicked(
                mouseX,
                mouseY,
                button
            )
        }
    }

    override fun mouseReleased(mouseX: Int, mouseY: Int, state: Int) {
        this.drag = false
        if (this.open && this.components.isNotEmpty()) this.components.forEach {
            it.mouseReleased(
                mouseX,
                mouseY,
                state
            )
        }
    }

    override fun keyTyped(typedChar: Char, key: Int) {
        if (this.open && key != Keyboard.KEY_ESCAPE && this.components.isNotEmpty()) {
            this.components.forEach { it.keyTyped(typedChar, key) }
        }
    }

}