package me.kl0udy92.apart.utils.render.animations.arthimo

/**
 * Created by Arithmo on 4/11/2017 at 2:12 PM.
 */
object AnimationUtil {

    fun calculateCompensation(target: Float, current: Float, delta: Long, speed: Double): Float {
        var current = current
        var delta = delta
        val diff = current - target
        if (delta < 1) {
            delta = 1
        }
        if (delta > 1000) {
            delta = 16
        }
        if (diff > speed) {
            val xD = if (speed * delta / (1000 / 60) < 0.5) 0.5 else speed * delta / (1000 / 60)
            current -= xD.toFloat()
            if (current < target) {
                current = target
            }
        } else if (diff < -speed) {
            val xD = if (speed * delta / (1000 / 60) < 0.5) 0.5 else speed * delta / (1000 / 60)
            current += xD.toFloat()
            if (current > target) {
                current = target
            }
        } else {
            current = target
        }
        return current
    }

}