package me.kl0udy92.apart.utils.render.animations.easing.bezier.list

import me.kl0udy92.apart.utils.render.animations.easing.bezier.Bezier
import me.kl0udy92.apart.utils.render.animations.easing.bezier.Point

class CubicBezier : Bezier() {

    override fun getValue(t: Double): Double {
        val dt = 1.0 - t
        val dt2 = dt * dt
        val t2 = t * t

        val temp: Point = this.startPoint.copy()

        return this.start.copy()
            .scale(dt2, dt)
            .add(temp.scale(3 * dt2 * t))
            .add(temp.set(this.endPoint).scale(3 * dt * t2))
            .add(temp.set(this.end).scale(t2 * t))
            .y
    }

    class Builder {
        private var bezier = CubicBezier()

        constructor(bezier: CubicBezier) {
            this.bezier = bezier
        }

        constructor() {}

        fun startPoint(point: Point): Builder {
            bezier.startPoint = point

            return this
        }

        fun endPoint(point: Point): Builder {
            bezier.endPoint = point
            return this
        }

        fun build(): CubicBezier {
            return bezier
        }
    }

}