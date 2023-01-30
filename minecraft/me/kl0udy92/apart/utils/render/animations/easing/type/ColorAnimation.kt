package me.kl0udy92.apart.utils.render.animations.easing.type

import me.kl0udy92.apart.utils.render.animations.easing.Animation
import me.kl0udy92.apart.utils.render.animations.easing.util.ColorMath
import me.kl0udy92.apart.utils.render.animations.easing.util.Easing
import me.kl0udy92.apart.utils.render.animations.easing.util.Easings
import java.awt.Color

/**
 * Use if u need to animation the color value
 */
class ColorAnimation {

    val red: Animation = Animation()
    val green: Animation = Animation()
    val blue: Animation = Animation()
    val alpha: Animation = Animation()
    /**
     * Main method, use to animate color
     */
    /**
     * Main method, use to animate color
     */
    @JvmOverloads
    fun animate(colorTo: Int, duration: Double, safe: Boolean = false) {
        animate(colorTo, duration, Easings().NONE, safe)
    }

    /**
     * Main method, use to animate color
     */
    fun animate(colorTo: Int, duration: Double, easing: Easing, safe: Boolean) {
        red.animate(ColorMath.extractRed(colorTo).toDouble(), duration, easing, safe)
        green.animate(ColorMath.extractGreen(colorTo).toDouble(), duration, easing, safe)
        blue.animate(ColorMath.extractBlue(colorTo).toDouble(), duration, easing, safe)
        alpha.animate(ColorMath.extractAlpha(colorTo).toDouble(), duration, easing, safe)
    }

    /**
     * Main method, use to animate color
     */
    fun animate(colorTo: Color, duration: Double, easing: Easing, safe: Boolean) {
        animate(colorTo.rgb, duration, easing, safe)
    }

    /**
     * Main method, use to animate color
     */
    fun animate(colorTo: Color, duration: Double, safe: Boolean) {
        animate(colorTo.rgb, duration, Easings().NONE, safe)
    }

    /**
     * Main method, use to animate color
     */
    fun animate(colorTo: Color, duration: Double) {
        animate(colorTo.rgb, duration, false)
    }

    /**
     * Updates all colors
     *
     * @return all colors alive
     */
    fun update(): Boolean {
        return red.update() &&
                green.update() &&
                blue.update() &&
                alpha.update() || red.update() &&
                green.update() && blue.update()
    }

    /**
     * @return all colors alive
     */
    val isAlive: Boolean
        get() = red.isAlive() && green.isAlive() && blue.isAlive() && alpha.isAlive() || red.isAlive() && green.isAlive() && blue.isAlive()

    /**
     * @return all colors done
     */
    val isDone: Boolean
        get() = !isAlive

    /**
     * Build java.awt.Color
     *
     * @return java.awt.Color
     */
    val color: Color
        get() = Color(red.value.toInt(), green.value.toInt(), blue.value.toInt(), alpha.value.toInt())

    /**
     * return RGB color code
     *
     * @return java.awt.Color#getRGB()
     */
    val hex: Int
        get() = color.rgb
}