package me.kl0udy92.apart.screen.clickgui.style.astolfo.components.implementations.options

import me.kl0udy92.apart.config.implementations.ModuleConfig
import me.kl0udy92.apart.core.Main
import me.kl0udy92.apart.option.implementations.ColourOption
import me.kl0udy92.apart.screen.clickgui.style.astolfo.components.AbstractComponent
import me.kl0udy92.apart.screen.clickgui.style.astolfo.frames.AbstractFrame
import me.kl0udy92.apart.screen.clickgui.style.astolfo.frames.implementations.CategoryFrame
import me.kl0udy92.apart.utils.render.ColorUtil
import me.kl0udy92.apart.utils.render.MouseUtil
import me.kl0udy92.apart.utils.render.RenderUtil
import net.minecraft.client.gui.Gui
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.util.ResourceLocation
import oh.yalan.NativeClass
import org.lwjgl.opengl.GL11
import java.awt.Color
import kotlin.math.roundToInt

@NativeClass
class ColourOptionComponent(
    val colourOption: ColourOption,
    abstractFrame: AbstractFrame, x: Double, y: Double, width: Double, height: Double,
    private val categoryFrame: CategoryFrame = abstractFrame as CategoryFrame,
    var hue: Float = 0f, var saturation: Float = 0f, var brightness: Float = 0f, var alpha: Int = colourOption.alpha,
    var hsbPickerPointX: Float = 0f, var hsbPickerPointY: Float = 0f,
    var hueSliderRectangleY: Float = 0f, var alphaSliderRectangleX: Float = 0f,
    var hsbPickerDragging: Boolean = false, var hueSliderDragging: Boolean = false, var alphaSliderDragging: Boolean = false
) : AbstractComponent(abstractFrame, x, y, width, height, 80.0) {

    private fun drawOptionDescription(mouseX: Int, mouseY: Int) {
        GlStateManager.pushMatrix()
        GL11.glDisable(GL11.GL_SCISSOR_TEST)
        val hovered = MouseUtil.isHovered(
            this.categoryFrame.x + this.x + 1.5,
            (this.categoryFrame.y + this.y),
            this.width - 1.5,
            this.height - 0.1, mouseX, mouseY
        )
        if (hovered) {
            val sr = ScaledResolution(mc)
            val descriptionWidth =
                (sr.scaledWidth * 2) - mc.fontRendererObj.getStringWidth(this.colourOption.description)
            val descriptionHeight = (sr.scaledHeight * 2) - mc.fontRendererObj.FONT_HEIGHT
            GlStateManager.scale(0.5, 0.5, 0.5)
            mc.fontRendererObj.drawStringWithOutline(
                this.colourOption.description,
                descriptionWidth,
                descriptionHeight,
                -1
            )
        }
        GL11.glEnable(GL11.GL_SCISSOR_TEST)
        GlStateManager.popMatrix()
    }

    override fun init() {
        this.updateHSB(this.colourOption.value.rgb)
        this.alpha = this.colourOption.alpha
    }

    override fun render(mouseX: Int, mouseY: Int) {
        RenderUtil.drawRect(
            this.categoryFrame.x + this.x + 1.5,
            (this.categoryFrame.y + this.y),
            this.categoryFrame.x + this.x + this.width - 1.5,
            this.categoryFrame.y + this.y + this.offset + 0.1, Color(5,5,5).rgb
        )
        Main.fontBuffer.SF16.drawStringWithOutline(
            this.colourOption.name,
            this.categoryFrame.x + this.x + 2,
            this.categoryFrame.y + this.y + 5,
            -1
        )

        RenderUtil.drawRect(
            this.categoryFrame.x + this.x + this.width - 10,
            this.categoryFrame.y + this.y + this.height - 10,
            this.categoryFrame.x + this.x + this.width - 4,
            this.categoryFrame.y + this.y + this.height - 4, this.colourOption.value.rgb
        )

        this.drawHSBRectangle(
            this.categoryFrame.x + this.x + 2,
            this.categoryFrame.y + this.y + this.height,
            this.categoryFrame.x + this.x + this.width - 18,
            this.categoryFrame.y + this.y + this.offset - 20
        )

        this.drawPoint()

        this.drawHueSlider(
            (this.categoryFrame.x + this.x + this.width - 14).toInt(),
            (this.categoryFrame.y + this.y + this.height).toInt(),
            (this.categoryFrame.x + this.x + this.width - 3.5).toInt(),
            (this.categoryFrame.y + this.y + this.offset - 20).toInt()
        )

        this.drawAlphaSlider(
            this.categoryFrame.x + this.x + 2,
            this.categoryFrame.y + this.y + this.offset - 20 + 4,
            this.categoryFrame.x + this.x + this.width - 18,
            this.categoryFrame.y + this.y + this.offset - 20 + 4 + 11.5
        )

        this.drawOptionDescription(mouseX, mouseY)
    }

    override fun update(mouseX: Int, mouseY: Int) {
        if (this.hsbPickerDragging) {
            val xDelta = (this.categoryFrame.x + this.x + this.width - 18) - (this.categoryFrame.x + this.x + 2)
            var xDif = (mouseX - (this.categoryFrame.x + this.x + 2))
            if (xDif < 0.0) xDif = 0.0 else if (xDif > 90.0) xDif = 90.0
            this.hsbPickerPointX = xDif.toFloat()
            this.saturation = (xDif / xDelta).toFloat()
            val yDelta = (this.categoryFrame.y + this.y + this.offset - 20) - (this.categoryFrame.y + this.y + this.height)
            var yDif = (mouseY - (this.categoryFrame.y + this.y + this.height))
            if (yDif < 0.0) yDif = 0.0 else if (yDif > 45.0) yDif = 45.0
            this.hsbPickerPointY = yDif.toFloat()
            this.brightness = 1 - (yDif / yDelta).toFloat()
            this.colourOption.value = ColorUtil.toColorRGB(Color.HSBtoRGB(this.hue, this.saturation, this.brightness), this.alpha.toFloat())
        }

        if (this.hueSliderDragging) {
            val yDelta = (this.categoryFrame.y + this.y + this.offset - 20) - (this.categoryFrame.y + this.y + this.height)
            var yDif = (mouseY - (this.categoryFrame.y + this.y + this.height))
            if (yDif < 0.0) yDif = 0.0 else if (yDif > 45.0) yDif = 45.0
            this.hue = (1 - (yDif / yDelta)).toFloat()
            this.colourOption.value = ColorUtil.toColorRGB(Color.HSBtoRGB(this.hue, this.saturation, this.brightness), this.alpha.toFloat())
        }

        if (this.alphaSliderDragging) {
            val xDelta = (this.categoryFrame.x + this.x + this.width - 18) - (this.categoryFrame.x + this.x + 2)
            var xDif = (mouseX - (this.categoryFrame.x + this.x + 2))
            if (xDif < 0.0) xDif = 0.0 else if (xDif > 90.0) xDif = 90.0
            this.alpha = (255 * (xDif / xDelta)).toInt()
            this.colourOption.alpha = this.alpha
            this.colourOption.value = ColorUtil.toColorRGB(Color.HSBtoRGB(this.hue, this.saturation, this.brightness), this.alpha.toFloat())
        }
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int, button: Int) {
        val hoveringHSBPicker = MouseUtil.isHovering(
            this.categoryFrame.x + this.x + 2,
            this.categoryFrame.y + this.y + this.height,
            this.categoryFrame.x + this.x + this.width - 18,
            this.categoryFrame.y + this.y + this.offset - 20,
            mouseX, mouseY
        )
        val hoveringHueBar = MouseUtil.isHovering(
            (this.categoryFrame.x + this.x + this.width - 14).toInt(),
            (this.categoryFrame.y + this.y + this.height).toInt(),
            (this.categoryFrame.x + this.x + this.width - 3.5).toInt(),
            (this.categoryFrame.y + this.y + this.offset - 20).toInt(),
            mouseX, mouseY
        )
        val hoveringAlphaBar = MouseUtil.isHovering(
            this.categoryFrame.x + this.x + 2,
            this.categoryFrame.y + this.y + this.offset - 20 + 4,
            this.categoryFrame.x + this.x + this.width - 14,
            this.categoryFrame.y + this.y + this.offset - 20 + 4 + 11.5,
            mouseX, mouseY
        )
        if (button == MouseUtil.ButtonType.LEFT.identifier) {
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
            (this.categoryFrame.x + this.x + 2).toFloat() + this.hsbPickerPointX - 7f,
            (this.categoryFrame.y + this.y + this.height).toFloat() + this.hsbPickerPointY - 7f,
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

    override fun isVisible(): Boolean {
        return this.colourOption.dependency.invoke()
    }

}