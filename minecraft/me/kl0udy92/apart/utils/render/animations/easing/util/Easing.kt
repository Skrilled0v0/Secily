package me.kl0udy92.apart.utils.render.animations.easing.util

fun interface Easing {
    /**
     * Easing's method
     *
     * @param value
     * @return animation formula
     */
    fun ease(value: Double): Double
}