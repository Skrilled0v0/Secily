package me.kl0udy92.apart.utils.render

import me.kl0udy92.apart.core.Main
import me.kl0udy92.apart.features.module.implementations.visual.HUDModule
import java.awt.Color
import kotlin.math.ceil

object ColorUtil {

    val hudModule = Main.moduleManager.getModuleByClass(HUDModule().javaClass) as HUDModule

    fun getHUDColor(delay: Int): Int {
        when (this.hudModule.colorModeOption.value) {
            "Astolfo" -> {
                return this.getAstolfo(
                    (delay * this.hudModule.delayOption.value).toInt(),
                    (this.hudModule.saturationOption.value / 100).toFloat(),
                    (this.hudModule.brightnessOption.value / 100).toFloat()
                )!!.rgb
            }
            "Rainbow" -> {
                return this.getRainbow(
                    (delay * this.hudModule.delayOption.value).toInt(),
                    (this.hudModule.saturationOption.value / 100).toFloat(),
                    (this.hudModule.brightnessOption.value / 100).toFloat()
                )!!.rgb
            }
            "Fade" -> {
                return this.fadeBetween(
                    this.hudModule.colourOption.value.rgb,
                    this.hudModule.fadeColourOption.value.rgb,
                    (System.currentTimeMillis() + delay * this.hudModule.delayOption.value.toInt() * 100L) % 2000L / 1000.0f
                )
            }
        }
        return this.hudModule.colourOption.value.rgb
    }

    fun getAstolfo(delay: Int, saturation: Float, brightness: Float): Color? {
        var state = ceil((System.currentTimeMillis() + delay).toDouble()) / this.hudModule.changeSpeedOption.value
        state %= 360
        return Color.getHSBColor(
            if ((state / 360.0).toFloat() < 0.5) -(state / 360.0).toFloat() else (state / 360.0).toFloat(),
            saturation,
            brightness
        )
    }

    fun getRainbow(delay: Int, saturation: Float, brightness: Float): Color? {
        var state = ceil((System.currentTimeMillis() + delay) / this.hudModule.changeSpeedOption.value)
        state %= 360
        return Color.getHSBColor((state / 360.0f).toFloat(), saturation, brightness)
    }

    fun fadeBetween(startColor: Int, endColor: Int, progress: Float): Int {
        var progress = progress
        if (progress > 1.0f) {
            progress = 1.0f - progress % 1.0f
        }
        return fadeTo(startColor, endColor, progress)
    }

    fun fadeBetween(startColor: Int, endColor: Int): Int {
        return fadeBetween(startColor, endColor, System.currentTimeMillis() % 2000L / 1000.0f)
    }

    fun fadeTo(startColor: Int, endColor: Int, progress: Float): Int {
        val invert = 1.0f - progress
        val r = ((startColor shr 16 and 0xFF) * invert + (endColor shr 16 and 0xFF) * progress).toInt()
        val g = ((startColor shr 8 and 0xFF) * invert + (endColor shr 8 and 0xFF) * progress).toInt()
        val b = ((startColor and 0xFF) * invert + (endColor and 0xFF) * progress).toInt()
        val a = ((startColor shr 24 and 0xFF) * invert + (endColor shr 24 and 0xFF) * progress).toInt()
        return a and 0xFF shl 24 or (r and 0xFF shl 16) or (g and 0xFF shl 8) or (b and 0xFF)
    }

    fun reAlpha(color: Color, alpha: Double): Color {
        return Color(
            color.red,
            color.green,
            color.blue,
            alpha.toInt()
        )
    }

    fun getHealthColor(health: Float, maxHealth: Float): Color {
        val fractions = floatArrayOf(0.0f, 0.5f, 1.0f)
        val colors = arrayOf(Color(108, 0, 0), Color(255, 51, 0), Color.GREEN)
        val progress = health / maxHealth
        return this.blendColors(fractions, colors, progress).brighter()
    }

    fun blendColors(fractions: FloatArray, colors: Array<Color?>, progress: Float): Color {
        if (fractions.size == colors.size) {
            val indices = this.getFractionIndices(fractions, progress)
            val range = floatArrayOf(fractions[indices[0]], fractions[indices[1]])
            val colorRange = arrayOf(colors[indices[0]], colors[indices[1]])
            val max = range[1] - range[0]
            val value = progress - range[0]
            val weight = value / max
            return this.blend(
                colorRange[0]!!,
                colorRange[1]!!,
                (1.0f - weight).toDouble()
            )
        }
        throw IllegalArgumentException("Fractions and colours must have equal number of elements")
    }

    fun getFractionIndices(fractions: FloatArray, progress: Float): IntArray {
        var startPoint: Int
        val range = IntArray(2)
        startPoint = 0
        while (startPoint < fractions.size && fractions[startPoint] <= progress) {
            ++startPoint
        }
        if (startPoint >= fractions.size) {
            startPoint = fractions.size - 1
        }
        range[0] = startPoint - 1
        range[1] = startPoint
        return range
    }

    fun blend(color1: Color, color2: Color, ratio: Double): Color {
        val r = ratio.toFloat()
        val ir = 1.0f - r
        val rgb1 = color1.getColorComponents(FloatArray(3))
        val rgb2 = color2.getColorComponents(FloatArray(3))
        var red = rgb1[0] * r + rgb2[0] * ir
        var green = rgb1[1] * r + rgb2[1] * ir
        var blue = rgb1[2] * r + rgb2[2] * ir
        if (red < 0.0f) {
            red = 0.0f
        } else if (red > 255.0f) {
            red = 255.0f
        }
        if (green < 0.0f) {
            green = 0.0f
        } else if (green > 255.0f) {
            green = 255.0f
        }
        if (blue < 0.0f) {
            blue = 0.0f
        } else if (blue > 255.0f) {
            blue = 255.0f
        }
        return Color(red, green, blue)
    }

    fun convertRGB(rgb: Int): FloatArray {
        val a = (rgb shr 24 and 0xFF) / 255.0f
        val r = (rgb shr 16 and 0xFF) / 255.0f
        val g = (rgb shr 8 and 0xFF) / 255.0f
        val b = (rgb and 0xFF) / 255.0f
        return floatArrayOf(r, g, b, a)
    }

    fun toColorRGB(rgb: Int, alpha: Float): Color {
        val rgba = this.convertRGB(rgb)
        return Color(rgba[0], rgba[1], rgba[2], alpha / 255f)
    }

    fun getColor(hex: Int): Int {
        val r = hex shr 16 and 0xFF
        val g = hex shr 8 and 0xFF
        val b = hex and 0xFF
        val a = 255
        return r and 0xFF shl 16 or (g and 0xFF shl 8) or (b and 0xFF) or (a and 0xFF shl 24)
    }

    fun getHSBFromColor(hex: Int): FloatArray {
        val r = hex shr 16 and 0xFF
        val g = hex shr 8 and 0xFF
        val b = hex and 0xFF
        return Color.RGBtoHSB(r, g, b, null)
    }

}