package me.kl0udy92.apart.utils.render.animations.arthimo

import kotlin.math.abs

/**
 * Created by cool1 on 4/9/2017.
 */
class Translate(var x: Float, var y: Float) {

    private var lastMS: Long

    init {
        lastMS = System.currentTimeMillis()
    }

    fun interpolate(targetX: Float, targetY: Float) {
        val currentMS = System.currentTimeMillis()
        val delta = currentMS - lastMS //16.66666
        this.lastMS = currentMS
        val deltaX = abs(targetX - x) * 0.51
        val deltaY = abs(targetY - y) * 0.51
        this.x = AnimationUtil.calculateCompensation(targetX, x, delta, deltaX)
        this.y = AnimationUtil.calculateCompensation(targetY, y, delta, deltaY)
    }

    fun interpolate(targetX: Float, targetY: Float, speed: Double) {
        val currentMS = System.currentTimeMillis()
        val delta = currentMS - lastMS //16.66666
        this.lastMS = currentMS
        var deltaX = 0.0
        var deltaY = 0.0
        if (speed != 0.0) {
            deltaX = abs(targetX - x) * 0.35f / (10 / speed)
            deltaY = abs(targetY - y) * 0.35f / (10 / speed)
        }
        this.x = AnimationUtil.calculateCompensation(targetX, x, delta, deltaX)
        this.y = AnimationUtil.calculateCompensation(targetY, y, delta, deltaY)
    }

}