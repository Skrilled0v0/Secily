package me.kl0udy92.apart.screen.clickgui.style.imgui.components.options

import me.kl0udy92.apart.config.implementations.ModuleConfig
import me.kl0udy92.apart.core.Main
import me.kl0udy92.apart.features.module.implementations.visual.ClickGUIModule
import me.kl0udy92.apart.option.implementations.BooleanOption
import me.kl0udy92.apart.screen.clickgui.style.astolfo.components.AbstractComponent
import me.kl0udy92.apart.screen.clickgui.style.imgui.components.ModuleComponent
import me.kl0udy92.apart.screen.clickgui.style.imgui.frames.CategoryFrame
import me.kl0udy92.apart.screen.clickgui.style.imgui.utils.Palette
import me.kl0udy92.apart.utils.render.ColorUtil
import me.kl0udy92.apart.utils.render.MouseUtil
import me.kl0udy92.apart.utils.render.RenderUtil
import oh.yalan.NativeClass
import java.awt.Color
import kotlin.math.roundToInt

@NativeClass
class CheckBoxComponent(
    val booleanOption: BooleanOption,
    categoryFrame: CategoryFrame,
    val daddy: ModuleComponent,
    x: Double, y: Double, width: Double, height: Double, offset: Double = 15.0,
    var textOpacity: Int = 115, var opacity: Int = 115
): AbstractComponent(categoryFrame, x, y, width, height, offset) {

    override fun init() {
        //Nothing todo.
    }

    override fun render(mouseX: Int, mouseY: Int) {
        Main.fontBuffer.SF17.drawString(
            this.booleanOption.name,
            this.x + 3.0,
            this.daddy.daddy.gui.y + this.daddy.y + this.y - this.daddy.daddy.scrollTranslate.y,
            Color(this.textOpacity, this.textOpacity, this.textOpacity).rgb
        )
        RenderUtil.drawRect(
            this.x + this.width - 12.5,
            this.daddy.daddy.gui.y + this.daddy.y + this.y - this.daddy.daddy.scrollTranslate.y - 1.5,
            this.x + this.width - 3.5,
            this.daddy.daddy.gui.y + this.daddy.y + this.y - this.daddy.daddy.scrollTranslate.y + 7.5,
            Palette.LINE_GRAY.color.rgb
        )
        RenderUtil.drawRect(
            this.x + this.width - 12,
            this.daddy.daddy.gui.y + this.daddy.y + this.y - this.daddy.daddy.scrollTranslate.y - 1,
            this.x + this.width - 4,
            this.daddy.daddy.gui.y + this.daddy.y + this.y - this.daddy.daddy.scrollTranslate.y + 7,
            ColorUtil.reAlpha((Main.moduleManager.getModuleByClass(ClickGUIModule().javaClass) as ClickGUIModule).colourOption.value, this.opacity.toDouble()).rgb
        )
    }

    override fun update(mouseX: Int, mouseY: Int) {
        if (this.booleanOption.value) {
            if (this.textOpacity != 255) this.textOpacity += 5
        }else if (this.textOpacity > 115) this.textOpacity -= 5

        if (this.booleanOption.value) {
            if (this.opacity != 255) this.opacity += 5
        }else if (this.opacity > 0) this.opacity -= 5
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int, button: Int) {
        if (button == MouseUtil.ButtonType.LEFT.identifier)
            if (this.isHovering(mouseX, mouseY)) {
                Main.configManager.write(ModuleConfig())
                this.booleanOption.value = !this.booleanOption.value
            }
    }

    override fun mouseReleased(mouseX: Int, mouseY: Int, state: Int) {
        //Nothing todo.
    }

    override fun keyTyped(typedChar: Char, key: Int) {
        //Nothing todo.
    }

    fun isHovering(mouseX: Int, mouseY: Int): Boolean {
        return MouseUtil.isHovering(
            this.x + this.width - 12.5,
            this.daddy.daddy.gui.y + this.daddy.y + this.y - this.daddy.daddy.scrollTranslate.y - 1.5,
            this.x + this.width - 3.5,
            this.daddy.daddy.gui.y + this.daddy.y + this.y - this.daddy.daddy.scrollTranslate.y + 7.5, mouseX, mouseY
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

    override fun isVisible(): Boolean {
        return this.booleanOption.dependency.invoke()
    }

}