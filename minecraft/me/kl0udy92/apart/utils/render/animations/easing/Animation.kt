package me.kl0udy92.apart.utils.render.animations.easing

import me.kl0udy92.apart.utils.render.animations.easing.bezier.Bezier
import me.kl0udy92.apart.utils.render.animations.easing.util.Easing
import me.kl0udy92.apart.utils.render.animations.easing.util.Easings

/**
 * Main class
 */
class Animation {

    /**
     * System.currentTimeMillis() from last animation start
     */
    private var start: Long = 0

    /**
     * Last/Current animate method duration
     */
    private var duration = 0.0

    /**
     * Value from which animation is started
     */
    private var fromValue = 0.0

    /**
     * Value to which animation goes
     */
    private var toValue = 0.0

    /**
     * Returns current animation value (better usage: getValue())
     */
    var value = 0.0

    /**
     * Animation type
     */
    private var easing: Easing? = null

    /**
     * Custom easing
     */
    private var bezier: Bezier? = null

    /**
     * Animation type
     */
    private var type: AnimationType? = null

    /**
     * Outputs animation things
     */
    private val debug = false

    /**
     * Main method, use to animate value to something.
     *
     * @param valueTo  toValue, value to which animation will go
     * @param duration duration, with which animation will animate
     */
    fun animate(valueTo: Double, duration: Double): Animation {
        return animate(valueTo, duration, Easings().NONE, false)
    }

    /**
     * Main method, use to animate value to something.
     *
     * @param valueTo  toValue, value to which animation will go
     * @param duration duration, with which animation will animate
     * @param easing   animation type, like formula for animation
     */
    fun animate(valueTo: Double, duration: Double, easing: Easing): Animation {
        return animate(valueTo, duration, easing, false)
    }

    /**
     * Main method, use to animate value to something.
     *
     * @param valueTo  toValue, value to which animation will go
     * @param duration duration, with which animation will animate
     * @param bezier   custom easing instance, like formula of easing
     */
    fun animate(valueTo: Double, duration: Double, bezier: Bezier): Animation {
        return animate(valueTo, duration, bezier, false)
    }

    /**
     * Main method, use to animate value to something.
     *
     * @param valueTo  toValue, value to which animation will go
     * @param duration duration, with which animation will animate
     * @param safe     means will it update when animation isAlive or with the same targetValue
     */
    fun animate(valueTo: Double, duration: Double, safe: Boolean): Animation {
        return animate(valueTo, duration, Easings().NONE, safe)
    }

    /**
     * Main method, use to animate value to something
     *
     * @param valueTo  toValue, value to which animation will go
     * @param duration duration, with which animation will animate
     * @param easing   animation type, like formula for animation
     * @param safe     means will it update when animation isAlive or with the same targetValue
     */
    fun animate(valueTo: Double, duration: Double, easing: Easing, safe: Boolean): Animation {
        if (check(safe, valueTo)) {
            if (this.debug) println("Animate cancelled due to target val equals from val")
            return this
        }
        this.type = AnimationType.EASING
        this.easing = easing
        this.duration = duration * 1000
        this.start = System.currentTimeMillis()
        this.fromValue = this.value
        this.toValue = valueTo
        if (this.debug) System.out.println(
            """
            ${
                """#animate {
    to value: ${this.toValue}
    from value: ${this.value}
    duration: ${this.duration}"""
            }
            }
            """.trimIndent()
        )
        return this
    }

    /**
     * Main method, use to animate value to something
     *
     * @param valueTo  toValue, value to which animation will go
     * @param duration duration, with which animation will animate
     * @param bezier   custom easing instance, like formula of easing
     * @param safe     means will it update when animation isAlive or with the same targetValue
     */
    fun animate(valueTo: Double, duration: Double, bezier: Bezier, safe: Boolean): Animation {
        if (check(safe, valueTo)) {
            if (this.debug) println("Animate cancelled due to target val equals from val")
            return this
        }
        this.type = AnimationType.BEZIER
        this.bezier = bezier
        this.duration = duration * 1000
        this.start = System.currentTimeMillis()
        this.fromValue = this.value
        this.toValue = valueTo
        if (this.debug) System.out.println(
            """
            ${
                """#animate {
    to value: ${this.toValue}
    from value: ${this.value}
    duration: ${this.duration}
    type: ${this.type!!.name}"""
            }
            }
            """.trimIndent()
        )
        return this
    }

    /**
     * Important method, use to update value. If u won't update animation, animation won't work.
     *
     * @return returns if animation isAlive()
     */
    fun update(): Boolean {
        val alive = isAlive()
        if (alive) {
            if (this.type?.equals(AnimationType.BEZIER)!!) {
                this.value = interpolate(this.fromValue, this.toValue, this.bezier!!.getValue(calculatePart()))
            } else {
                this.value = interpolate(this.fromValue, this.toValue, this.easing!!.ease(calculatePart()))
            }
        } else {
            this.start = 0
            this.value = this.toValue
        }
        return alive
    }

    /**
     * Use if u want check if animation is animating
     *
     * @return returns if animation is animating
     */
    fun isAlive(): Boolean {
        return !isDone()
    }

    /**
     * Use if u want check if animation is not animating
     *
     * @return returns if animation is animating
     */
    fun isDone(): Boolean {
        return calculatePart() >= 1.0
    }

    /**
     * Use if u want to get the current part of animation (from 0.0 to 1.0, like 0% and 100%)
     *
     * @return returns animation part
     */
    fun calculatePart(): Double {
        return (System.currentTimeMillis() - this.start).toDouble() / this.duration
    }

    fun check(safe: Boolean, valueTo: Double): Boolean {
        return safe && isAlive() && (valueTo == this.fromValue || valueTo == this.toValue || valueTo == this.value)
    }

    /**
     * Basic interpolation formula
     */
    fun interpolate(start: Double, end: Double, pct: Double): Double {
        return start + (end - start) * pct
    }

}