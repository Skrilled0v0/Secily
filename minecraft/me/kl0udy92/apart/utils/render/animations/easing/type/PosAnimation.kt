package me.kl0udy92.apart.utils.render.animations.easing.type

import me.kl0udy92.apart.utils.render.animations.easing.Animation
import me.kl0udy92.apart.utils.render.animations.easing.util.Easing
import me.kl0udy92.apart.utils.render.animations.easing.util.Easings
import me.kl0udy92.apart.utils.render.animations.easing.util.V2

/**
 * Use if u need to animation the position
 */
class PosAnimation(val x: Animation = Animation(), val y: Animation = Animation()) {

    /**
     * Main method, use to animate position
     */
    fun animate(posTo: V2, duration: Double) {
        this.x.animate(posTo.x!!, duration, false)
        this.y.animate(posTo.y!!, duration, false)
    }

    /**
     * Main method, use to animate position
     */
    fun animate(posTo: V2, duration: Double, safe: Boolean) {
        this.x.animate(posTo.x!!, duration, Easings().NONE, safe)
        this.y.animate(posTo.y!!, duration, Easings().NONE, safe)
    }

    /**
     * Main method, use to animate position
     */
    fun animate(posTo: V2, duration: Double, easing: Easing, safe: Boolean) {
        this.x.animate(posTo.x!!, duration, easing, safe)
        this.y.animate(posTo.y!!, duration, easing, safe)
    }

    /**
     * Updates all coordinates
     * @return all v2s alive
     */
    fun update(): Boolean {
        return this.x.update() && this.y.update()
    }

    /**
     * Get position
     * @return positions via utility V2 class
     */
    fun getPosition(): V2 {
        return V2(this.x.value, this.y.value)
    }

    /**
     * @return all coordinates alive
     */
    fun isAlive(): Boolean {
        return this.x.isAlive() && this.y.isAlive()
    }

    /**
     * @return all coordinates done
     */
    fun isDone(): Boolean {
        return !isAlive()
    }

}