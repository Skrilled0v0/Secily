package me.kl0udy92.apart.screen.clickgui.style.imgui.components.options

import com.google.common.util.concurrent.AtomicDouble
import me.kl0udy92.apart.config.implementations.ModuleConfig
import me.kl0udy92.apart.core.Main
import me.kl0udy92.apart.option.implementations.ArrayOption
import me.kl0udy92.apart.screen.clickgui.style.astolfo.components.AbstractComponent
import me.kl0udy92.apart.screen.clickgui.style.imgui.components.ModuleComponent
import me.kl0udy92.apart.screen.clickgui.style.imgui.frames.CategoryFrame
import me.kl0udy92.apart.screen.clickgui.style.imgui.utils.Palette
import me.kl0udy92.apart.utils.render.MouseUtil
import me.kl0udy92.apart.utils.render.RenderUtil
import oh.yalan.NativeClass
import java.awt.Color
import kotlin.math.roundToInt

@NativeClass
class ComboBoxComponenet(
    val arrayOption: ArrayOption,
    categoryFrame: CategoryFrame,
    val daddy: ModuleComponent,
    x: Double, y: Double, width: Double, height: Double, offset: Double = 30.0,
    var extended: Boolean = false
): AbstractComponent(categoryFrame, x, y, width, height, offset) {

    override fun init() {
        //Nothing todo.
    }

    override fun render(mouseX: Int, mouseY: Int) {
        Main.fontBuffer.SF17.drawString(
            this.arrayOption.name,
            this.x + 3.0,
            this.daddy.daddy.gui.y + this.daddy.y + this.y - this.daddy.daddy.scrollTranslate.y,
            -1
        )

        RenderUtil.drawRect(
            this.x + 7.5,
            this.daddy.daddy.gui.y + this.daddy.y + this.y - this.daddy.daddy.scrollTranslate.y + 11.5,
            this.x + 3.0 + this.width - 7 - 4.5,
            this.daddy.daddy.gui.y + this.daddy.y + this.y - this.daddy.daddy.scrollTranslate.y + 25.5,
            Palette.LINE_GRAY.color.rgb
        )
        RenderUtil.drawRect(
            this.x + 8.0,
            this.daddy.daddy.gui.y + this.daddy.y + this.y - this.daddy.daddy.scrollTranslate.y + 12.0,
            this.x + 3.0 + this.width - 7 - 5,
            this.daddy.daddy.gui.y + this.daddy.y + this.y - this.daddy.daddy.scrollTranslate.y + 25.0,
            Palette.BACKGROUND.color.rgb
        )
        Main.fontBuffer.SF16.drawString(
            this.arrayOption.value,
            this.x + 12.0,
            this.daddy.daddy.gui.y + this.daddy.y + this.y - this.daddy.daddy.scrollTranslate.y + 14.0,
            -1
        )
        Main.fontBuffer.SF16.drawString(
            if (this.extended) "-" else "+",
            this.x + this.width - 7 - 10,
            this.daddy.daddy.gui.y + this.daddy.y + this.y - this.daddy.daddy.scrollTranslate.y + 14.0,
            -1
        )

        val yAxis = AtomicDouble(this.daddy.daddy.gui.y + this.daddy.y + this.y - this.daddy.daddy.scrollTranslate.y + 30.0)
        if (this.extended) {
            RenderUtil.drawRect(
                this.x + 7.5,
                this.daddy.daddy.gui.y + this.daddy.y + this.y - this.daddy.daddy.scrollTranslate.y + 28.0,
                this.x + 3.0 + this.width - 7 - 4.5,
                this.daddy.daddy.gui.y + this.daddy.y + this.y - this.daddy.daddy.scrollTranslate.y + 28.0 + (this.arrayOption.values.size * 14.0),
                Palette.LINE_GRAY.color.rgb
            )
            RenderUtil.drawRect(
                this.x + 8.0,
                this.daddy.daddy.gui.y + this.daddy.y + this.y - this.daddy.daddy.scrollTranslate.y + 28.5,
                this.x + 3.0 + this.width - 7 - 5.0,
                this.daddy.daddy.gui.y + this.daddy.y + this.y - this.daddy.daddy.scrollTranslate.y + 27.5 + (this.arrayOption.values.size * 14.0),
                Palette.BACKGROUND.color.rgb
            )
            this.arrayOption.values.forEach {
                val hovering = MouseUtil.isHovered(
                    this.x + 12.0,
                    yAxis.get(),
                    Main.fontBuffer.SF16.getStringWidth(it).toDouble(),
                    Main.fontBuffer.SF16.getHeight(false).toDouble(), mouseX, mouseY
                )
                Main.fontBuffer.SF16.drawString(
                    it,
                    this.x + 12.0,
                    yAxis.get(),
                    if (this.arrayOption.value == it) -1
                    else if (hovering) Color(155, 155, 155).rgb else Color(95, 95, 95).rgb
                )
                yAxis.addAndGet(14.0)
            }
        }
    }

    override fun update(mouseX: Int, mouseY: Int) {
        if (this.extended) {
            this.offset = 30.0 + (this.arrayOption.values.size * 14.0) + 5.0
        }else this.offset = 30.0
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int, button: Int) {
        when (button) {
            MouseUtil.ButtonType.LEFT.identifier -> {
                if (this.extended) {
                    val yAxis = AtomicDouble(this.daddy.daddy.gui.y + this.daddy.y + this.y - this.daddy.daddy.scrollTranslate.y + 30.0)
                    this.arrayOption.values.forEach {
                        val hovering = MouseUtil.isHovered(
                            this.x + 12.0,
                            yAxis.get(),
                            Main.fontBuffer.SF16.getStringWidth(it).toDouble(),
                            Main.fontBuffer.SF16.getHeight(false).toDouble(), mouseX, mouseY
                        )
                        if (hovering) {
                            Main.configManager.write(ModuleConfig())
                            this.arrayOption.value = it
                        }
                        yAxis.addAndGet(14.0)
                    }
                }
            }
            MouseUtil.ButtonType.RIGHT.identifier -> {
                if (this.isHovering(mouseX, mouseY)) this.extended = !this.extended
            }
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
            this.x + 7.5,
            this.daddy.daddy.gui.y + this.daddy.y + this.y - this.daddy.daddy.scrollTranslate.y + 11.5,
            this.x + 3.0 + this.width - 7 - 4.5,
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

    override fun isVisible(): Boolean {
        return this.arrayOption.dependency.invoke()
    }
    
}