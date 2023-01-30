package me.kl0udy92.apart.screen.clickgui.style.imgui.components.options

import me.kl0udy92.apart.config.implementations.ModuleConfig
import me.kl0udy92.apart.core.Main
import me.kl0udy92.apart.option.implementations.ColourOption
import me.kl0udy92.apart.screen.clickgui.style.astolfo.components.AbstractComponent
import me.kl0udy92.apart.screen.clickgui.style.imgui.components.ModuleComponent
import me.kl0udy92.apart.screen.clickgui.style.imgui.frames.CategoryFrame
import me.kl0udy92.apart.utils.render.ColorUtil
import me.kl0udy92.apart.utils.render.MouseUtil
import me.kl0udy92.apart.utils.render.RenderUtil
import net.minecraft.client.gui.Gui
import net.minecraft.util.ResourceLocation
import oh.yalan.NativeClass
import java.awt.Color
import kotlin.math.roundToInt

@NativeClass
class ColourPickerComponent(
    val colourOption: ColourOption,
    categoryFrame: CategoryFrame,
    val daddy: ModuleComponent,
    x: Double, y: Double, width: Double, height: Double, offset: Double = 15.0,
    var extended: Boolean = false,
    var hue: Float = 0f, var saturation: Float = 0f, var brightness: Float = 0f, var alpha: Int = colourOption.alpha,
    var hsbPickerPointX: Float = 0f, var hsbPickerPointY: Float = 0f,
    var hueSliderRectangleY: Float = 0f, var alphaSliderRectangleX: Float = 0f,
    var hsbPickerDragging: Boolean = false, var hueSliderDragging: Boolean = false, var alphaSliderDragging: Boolean = false
) : AbstractComponent(categoryFrame, x, y, width, height, offset) {

    override fun init() {
        this.updateHSB(this.colourOption.value.rgb)
        this.alpha = this.colourOption.alpha
    }

    override fun render(mouseX: Int, mouseY: Int) {
        Main.fontBuffer.SF17.drawString(
            this.colourOption.name,
            this.x + 3.0,
            this.daddy.daddy.gui.y + this.daddy.y + this.y - this.daddy.daddy.scrollTranslate.y,
            -1
        )

        RenderUtil.drawRect(
            this.x + this.width - 12.5,
            this.daddy.daddy.gui.y + this.daddy.y + this.y - this.daddy.daddy.scrollTranslate.y - 1.5,
            this.x + this.width - 3.5,
            this.daddy.daddy.gui.y + this.daddy.y + this.y - this.daddy.daddy.scrollTranslate.y + 7.5,
            this.colourOption.value.rgb
        )

        if (this.extended) {
            this.drawHSBRectangle(
                this.x + 3,
                this.daddy.daddy.gui.y + this.daddy.y + this.y - this.daddy.daddy.scrollTranslate.y + 9.5 + 3.0,
                this.x + this.width - 18,
                this.daddy.daddy.gui.y + this.daddy.y + this.y - this.daddy.daddy.scrollTranslate.y + 39.5 + 3.0 + 30
            )

            this.drawPoint()

            this.drawHueSlider(
                (this.x + this.width - 14).roundToInt(),
                (this.daddy.daddy.gui.y + this.daddy.y + this.y - this.daddy.daddy.scrollTranslate.y + 9.5 + 3.0).toInt(),
                (this.x + this.width - 3.5).roundToInt(),
                (this.daddy.daddy.gui.y + this.daddy.y + this.y - this.daddy.daddy.scrollTranslate.y + 39.5 + 3.0 + 30).toInt(),
            )

            this.drawAlphaSlider(
                this.x + 3,
                this.daddy.daddy.gui.y + this.daddy.y + this.y - this.daddy.daddy.scrollTranslate.y + 39.5 + 3.0 + 30 + 4,
                this.x + this.width - 18,
                this.daddy.daddy.gui.y + this.daddy.y + this.y - this.daddy.daddy.scrollTranslate.y + 39.5 + 3.0 + 30 + 4 + 11.5
            )
        }
    }

    override fun update(mouseX: Int, mouseY: Int) {
        if (this.extended) {
            this.offset = 39.5 + 3.0 + 30 + 4 + 11.5 + 7.5

            if (this.hsbPickerDragging) {
                val xDelta = (this.x + this.width - 18) - (this.x + 3)
                var xDif = (mouseX - (this.x + 3))
                if (xDif < 0) xDif = 0.0 else if (xDif > 169) xDif = 169.0
                this.hsbPickerPointX = xDif.toFloat()
                this.saturation = (xDif / xDelta).toFloat()
                val yDelta = (this.daddy.daddy.gui.y + this.daddy.y + this.y - this.daddy.daddy.scrollTranslate.y + 39.5 + 3.0 + 30) - (this.daddy.daddy.gui.y + this.daddy.y + this.y - this.daddy.daddy.scrollTranslate.y + 9.5 + 3.0)
                var yDif = (mouseY - (this.daddy.daddy.gui.y + this.daddy.y + this.y - this.daddy.daddy.scrollTranslate.y + 9.5 + 3.0))
                if (yDif < 0) yDif = 0.0 else if (yDif > 60) yDif = 60.0
                this.hsbPickerPointY = yDif.toFloat()
                this.brightness = 1 - (yDif / yDelta).toFloat()
                this.colourOption.value = ColorUtil.toColorRGB(Color.HSBtoRGB(this.hue, this.saturation, this.brightness), this.alpha.toFloat())
            }

            if (this.hueSliderDragging) {
                val yDelta = (this.daddy.daddy.gui.y + this.daddy.y + this.y - this.daddy.daddy.scrollTranslate.y + 39.5 + 3.0 + 30) - (this.daddy.daddy.gui.y + this.daddy.y + this.y - this.daddy.daddy.scrollTranslate.y + 9.5 + 3.0)
                var yDif = (mouseY - (this.daddy.daddy.gui.y + this.daddy.y + this.y - this.daddy.daddy.scrollTranslate.y + 9.5 + 3.0))
                if (yDif < 0) yDif = 0.0 else if (yDif > 60) yDif = 60.0
                this.hue = (1 - (yDif / yDelta)).toFloat()
                this.colourOption.value = ColorUtil.toColorRGB(Color.HSBtoRGB(this.hue, this.saturation, this.brightness), this.alpha.toFloat())
            }

            if (this.alphaSliderDragging) {
                val xDelta = (this.x + this.width - 18) - (this.x + 3)
                var xDif = (mouseX - (this.x + 3))
                if (xDif < 0) xDif = 0.0 else if (xDif > 169) xDif = 169.0
                this.alpha = (255 * (xDif / xDelta)).toInt()
                this.colourOption.alpha = this.alpha
                this.colourOption.value = ColorUtil.toColorRGB(Color.HSBtoRGB(this.hue, this.saturation, this.brightness), this.alpha.toFloat())
            }

        } else {
            this.offset = 15.0
        }
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int, button: Int) {
        val hoveringPureColour = MouseUtil.isHovering(
            this.x + this.width - 12.5,
            this.daddy.daddy.gui.y + this.daddy.y + this.y - this.daddy.daddy.scrollTranslate.y - 1.5,
            this.x + this.width - 3.5,
            this.daddy.daddy.gui.y + this.daddy.y + this.y - this.daddy.daddy.scrollTranslate.y + 7.5,
            mouseX, mouseY
        ) && MouseUtil.isHovering(
            (this.daddy.daddy.gui.x + 41).roundToInt(),
            (this.daddy.daddy.gui.y + 25 + 25.5).roundToInt(),
            (this.daddy.daddy.gui.x + 45.0 + 190.0 + 5.0 + 191.0).roundToInt(),
            (this.daddy.daddy.gui.y + 250.0).roundToInt(),
            mouseX, mouseY
        )
        val hoveringHSBPicker = MouseUtil.isHovering(
            this.x + 3,
            this.daddy.daddy.gui.y + this.daddy.y + this.y - this.daddy.daddy.scrollTranslate.y + 9.5 + 3.0,
            this.x + this.width - 18,
            this.daddy.daddy.gui.y + this.daddy.y + this.y - this.daddy.daddy.scrollTranslate.y + 39.5 + 3.0 + 30,
            mouseX, mouseY
        ) && MouseUtil.isHovering(
            (this.daddy.daddy.gui.x + 41).roundToInt(),
            (this.daddy.daddy.gui.y + 25 + 25.5).roundToInt(),
            (this.daddy.daddy.gui.x + 45.0 + 190.0 + 5.0 + 191.0).roundToInt(),
            (this.daddy.daddy.gui.y + 250.0).roundToInt(),
            mouseX, mouseY
        )
        val hoveringHueBar = MouseUtil.isHovering(
            (this.x + this.width - 14).roundToInt(),
            (this.daddy.daddy.gui.y + this.daddy.y + this.y - this.daddy.daddy.scrollTranslate.y + 9.5 + 3.0).roundToInt(),
            (this.x + this.width - 3.5).roundToInt(),
            (this.daddy.daddy.gui.y + this.daddy.y + this.y - this.daddy.daddy.scrollTranslate.y + 39.5 + 3.0 + 30).roundToInt(),
            mouseX, mouseY
        ) && MouseUtil.isHovering(
            (this.daddy.daddy.gui.x + 41).roundToInt(),
            (this.daddy.daddy.gui.y + 25 + 25.5).roundToInt(),
            (this.daddy.daddy.gui.x + 45.0 + 190.0 + 5.0 + 191.0).roundToInt(),
            (this.daddy.daddy.gui.y + 250.0).roundToInt(),
            mouseX, mouseY
        )
        val hoveringAlphaBar = MouseUtil.isHovering(
            this.x + 3,
            this.daddy.daddy.gui.y + this.daddy.y + this.y - this.daddy.daddy.scrollTranslate.y + 39.5 + 3.0 + 30 + 4,
            this.x + this.width - 18,
            this.daddy.daddy.gui.y + this.daddy.y + this.y - this.daddy.daddy.scrollTranslate.y + 39.5 + 3.0 + 30 + 4 + 11.5,
            mouseX, mouseY
        ) && MouseUtil.isHovering(
            (this.daddy.daddy.gui.x + 41).roundToInt(),
            (this.daddy.daddy.gui.y + 25 + 25.5).roundToInt(),
            (this.daddy.daddy.gui.x + 45.0 + 190.0 + 5.0 + 191.0).roundToInt(),
            (this.daddy.daddy.gui.y + 250.0).roundToInt(),
            mouseX, mouseY
        )
        when (button) {
            MouseUtil.ButtonType.LEFT.identifier -> {
                if (this.extended) {
                    if (hoveringHSBPicker) {
                        this.hsbPickerDragging = true
                    }
                    if (hoveringHueBar) {
                        this.hueSliderDragging = true
                    }
                    if (hoveringAlphaBar) {
                        this.alphaSliderDragging = true
                    }
                }
            }
            MouseUtil.ButtonType.RIGHT.identifier -> {
                if (hoveringPureColour) this.extended = !this.extended
            }
        }
    }

    override fun mouseReleased(mouseX: Int, mouseY: Int, state: Int) {
        this.hsbPickerDragging = false
        this.hueSliderDragging = false
        this.alphaSliderDragging = false
        Main.configManager.write(ModuleConfig())
    }

    override fun keyTyped(typedChar: Char, key: Int) {
        //Nothing todo.
    }

    override fun closed() {
        //Nothing todo.
    }

    override fun isVisible(): Boolean {
        return this.colourOption.dependency.invoke()
    }

    fun drawHSBRectangle(x: Double, y: Double, width: Double, height: Double) {
        //Background rectangle.
        RenderUtil.drawRect(
            x - 0.5,
            y - 0.5,
            width + 0.5,
            height + 0.5,
            Color.BLACK.rgb
        )
        //Saturation gradient rectangle.
        RenderUtil.drawGradientRect(
            x,
            y,
            width,
            height,
            ColorUtil.toColorRGB(Color.HSBtoRGB(this.hue, 1f, 1f), 255f).rgb,
            ColorUtil.toColorRGB(Color.HSBtoRGB(this.hue, 0f, 1f), 0f).rgb,
            true
        )
        //Brightness gradient rectangle.
        Gui.drawGradientRect(
            x.roundToInt(),
            y.roundToInt(),
            width.roundToInt(),
            height.roundToInt(),
            ColorUtil.toColorRGB(Color.HSBtoRGB(this.hue, 1f, 0f), 0f).rgb,
            ColorUtil.toColorRGB(Color.HSBtoRGB(this.hue, 1f, 0f), 255f).rgb
        )

        this.hsbPickerPointX = this.saturation * (width - x).toFloat()
        this.hsbPickerPointY = (1f - this.brightness) * (height - y).toFloat()
    }

    fun drawPoint() {
        RenderUtil.drawImage(
            ResourceLocation("${Main.name.lowercase()}/icons/other/point.png"),
            (this.x + 3).toFloat() + this.hsbPickerPointX - 7f,
            (this.daddy.daddy.gui.y + this.daddy.y + this.y - this.daddy.daddy.scrollTranslate.y + 9.5 + 3.0).toFloat() + this.hsbPickerPointY - 7f,
            16f,16f,
            Color(150, 150, 150, 180).rgb
        )
    }

    fun drawHueSlider(x: Int, y: Int, width: Int, height: Int) {
        //Background rectangle
        RenderUtil.drawRect(
            x - 0.5,
            y - 0.5,
            width + 0.5,
            height + 0.5,
            Color.BLACK.rgb
        )

        //Hue bar
        val calculatedHeight = height - y
        val gradientHeight = calculatedHeight / 5f
        var yPos = y
        var i = 0
        while (i.toFloat() < 5.0f) {
            val last = i.toFloat() == 4.0f
            Gui.drawGradientRect(
                x,
                yPos,
                width,
                (yPos + gradientHeight).roundToInt(),
                ColorUtil.getColor(Color.HSBtoRGB(1f - 0.2f * i.toFloat(), 1f, 1f)),
                ColorUtil.getColor(Color.HSBtoRGB(1f - 0.2f * (i + 1).toFloat(), 1f, 1f))
            )
            if (!last) {
                yPos += gradientHeight.toInt()
            }
            ++i
        }

        this.hueSliderRectangleY = (1 - this.hue) * (height - y).toFloat()

        val rectangleY = y + this.hueSliderRectangleY - 0.5
        val rectangleHeight = y + this.hueSliderRectangleY + 0.5

        RenderUtil.drawRect(
            x.toDouble(),
            rectangleY,
            width.toDouble(),
            rectangleHeight,
            Color(0, 0, 0, 180).rgb
        )
    }

    fun drawAlphaSlider(x: Double, y: Double, width: Double, height: Double) {
        //Background rectangle
        RenderUtil.drawRect(
            x - 0.5,
            y - 0.5,
            width + 0.5,
            height + 0.5,
            Color.BLACK.rgb
        )

        //Alpha bar
        RenderUtil.drawGradientRect(
            x,
            y,
            width,
            height,
            this.colourOption.value.rgb,
            Color(100, 100, 100).rgb,
            true
        )

        val xDelta = width - x

        this.alphaSliderRectangleX = ((this.alpha.toFloat() / 255) * xDelta).toFloat()

        val rectangleX = x + this.alphaSliderRectangleX - 0.5
        val rectangleWidth = x + this.alphaSliderRectangleX + 0.5

        RenderUtil.drawRect(
            rectangleX,
            y,
            rectangleWidth,
            height,
            Color(0, 0, 0, 180).rgb
        )
    }

    fun updateHSB(value: Int) {
        val hsb = ColorUtil.getHSBFromColor(value)

        this.hue = hsb[0]
        this.saturation = hsb[1]
        this.brightness = hsb[2]
    }

}