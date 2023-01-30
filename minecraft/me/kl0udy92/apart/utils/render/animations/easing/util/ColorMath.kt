package me.kl0udy92.apart.utils.render.animations.easing.util

object ColorMath {

    fun extractAlpha(color: Int): Int {
        return color shr 24 and 0xFF
    }

    fun extractRed(color: Int): Int {
        return color shr 16 and 0xFF
    }

    fun extractGreen(color: Int): Int {
        return color shr 8 and 0xFF
    }

    fun extractBlue(color: Int): Int {
        return color and 0xFF
    }

}