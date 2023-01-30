package me.kl0udy92.apart.screen.clickgui.style.imgui.components.options

import me.kl0udy92.apart.config.implementations.ModuleConfig
import me.kl0udy92.apart.core.Main
import me.kl0udy92.apart.features.module.implementations.visual.ClickGUIModule
import me.kl0udy92.apart.option.implementations.DoubleOption
import me.kl0udy92.apart.screen.clickgui.style.astolfo.components.AbstractComponent
import me.kl0udy92.apart.screen.clickgui.style.imgui.components.ModuleComponent
import me.kl0udy92.apart.screen.clickgui.style.imgui.frames.CategoryFrame
import me.kl0udy92.apart.screen.clickgui.style.imgui.utils.Palette
import me.kl0udy92.apart.utils.render.MouseUtil
import me.kl0udy92.apart.utils.render.RenderUtil
import me.kl0udy92.apart.utils.render.animations.easing.Animation
import me.kl0udy92.apart.utils.render.animations.easing.util.Easings
import oh.yalan.NativeClass
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.roundToInt

@NativeClass
class SliderComponent(
    val doubleOption: DoubleOption,
    categoryFrame: CategoryFrame,
    val daddy: ModuleComponent,
    x: Double, y: Double, width: Double, height: Double, offset: Double = 25.0,
    var drag: Boolean = false, val slideAnimaion: Animation = Animation()
): AbstractComponent(categoryFrame, x, y, width, height, offset) {

    override fun init() {
        //Nothing todo.
    }

    override fun render(mouseX: Int, mouseY: Int) {
        Main.fontBuffer.SF16.drawString(
            this.doubleOption.name,
            this.x + 3.0,
            this.daddy.daddy.gui.y + this.daddy.y + this.y - this.daddy.daddy.scrollTranslate.y,
            -1
        )

        val slideWidth = this.x + 3.0 + this.width - 7

        RenderUtil.drawRect(
            this.x + 2.5,
            this.daddy.daddy.gui.y + this.daddy.y + this.y - this.daddy.daddy.scrollTranslate.y + 11.5,
            slideWidth + 0.5,
            this.daddy.daddy.gui.y + this.daddy.y + this.y - this.daddy.daddy.scrollTranslate.y + 20.5,
            Palette.LINE_GRAY.color.rgb
        )
        RenderUtil.drawRect(
            this.x + 3.0,
            this.daddy.daddy.gui.y + this.daddy.y + this.y - this.daddy.daddy.scrollTranslate.y + 12.0,
            slideWidth,
            this.daddy.daddy.gui.y + this.daddy.y + this.y - this.daddy.daddy.scrollTranslate.y + 20.0,
            Palette.BACKGROUND.color.rgb
        )
        RenderUtil.drawRect(
            this.x + 3.5,
            this.daddy.daddy.gui.y + this.daddy.y + this.y - this.daddy.daddy.scrollTranslate.y + 12.5,
            this.x + if (this.doubleOption.value == this.doubleOption.min) 3.5 else 2.5 + this.slideAnimaion.value,
            this.daddy.daddy.gui.y + this.daddy.y + this.y - this.daddy.daddy.scrollTranslate.y + 19.5,
            (Main.moduleManager.getModuleByClass(ClickGUIModule().javaClass) as ClickGUIModule).colourOption.value.rgb
        )

        Main.fontBuffer.SF16.drawString(
            this.clamp(this.doubleOption.value, this.doubleOption.increment).toString(),
            this.x + this.width - 3 - Main.fontBuffer.SF16.getStringWidth(
                this.clamp(
                    this.doubleOption.value,
                    this.doubleOption.increment
                ).toString()),
            this.daddy.daddy.gui.y + this.daddy.y + this.y - this.daddy.daddy.scrollTranslate.y, -1
        )
    }

    override fun update(mouseX: Int, mouseY: Int) {
        this.slideAnimaion.update()
        this.slideAnimaion.animate(
            (this.width - 7) * (this.doubleOption.value - this.doubleOption.min) / (this.doubleOption.max - this.doubleOption.min),
            0.3,
            Easings().EXPO_OUT,
            true
        )

        if (this.drag) {
            Main.configManager.write(ModuleConfig())
            this.doubleOption.value = this.clamp(
                (mouseX - (this.x + if (this.doubleOption.value == this.doubleOption.min) 3.5 else 2.5)) * (this.doubleOption.max - this.doubleOption.min) / (this.width - 7) + this.doubleOption.min,
                this.doubleOption.increment
            )
            if (this.doubleOption.value > this.doubleOption.max) this.doubleOption.value = this.doubleOption.max
            else if (this.doubleOption.value < this.doubleOption.min) this.doubleOption.value = this.doubleOption.min
        }
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int, button: Int) {
        if (button == MouseUtil.ButtonType.LEFT.identifier)
            if (this.isHovering(mouseX, mouseY)) this.drag = true
    }

    override fun mouseReleased(mouseX: Int, mouseY: Int, state: Int) {
        this.drag = false
    }

    override fun keyTyped(typedChar: Char, key: Int) {
        //Nothing todo.
    }

    override fun closed() {
        this.drag = false
    }

    private fun clamp(value: Double, increment: Double): Double {
        return BigDecimal((value / increment).roundToInt() * increment).setScale(
            increment.toString().length - (increment.toString().indexOf(".") + 1), RoundingMode.HALF_UP
        ).toDouble()
    }

    fun isHovering(mouseX: Int, mouseY: Int): Boolean {
        return MouseUtil.isHovering(
            this.x + 2.5,
            this.daddy.daddy.gui.y + this.daddy.y + this.y - this.daddy.daddy.scrollTranslate.y + 11.5,
            this.x + 3.0 + this.width - 7.0 + 0.5,
            this.daddy.daddy.gui.y + this.daddy.y + this.y - this.daddy.daddy.scrollTranslate.y + 20.5, mouseX, mouseY
        ) && MouseUtil.isHovering(
            (this.daddy.daddy.gui.x + 41).roundToInt(),
            (this.daddy.daddy.gui.y + 25 + 25.5).roundToInt(),
            (this.daddy.daddy.gui.x + 45.0 + 190.0 + 5.0 + 191.0).roundToInt(),
            (this.daddy.daddy.gui.y + 250.0).roundToInt(),
            mouseX, mouseY
        )
    }

    override fun isVisible(): Boolean {
        return this.doubleOption.dependency.invoke()
    }

}