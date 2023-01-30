package me.kl0udy92.apart.utils.render.animations.easing.bezier

@Suppress("unused")
class Point(var x: Double, var y: Double) {

    constructor(point: Point) : this(point.x, point.y)

    fun copy(): Point {
        return Point(this)
    }

    fun scale(x: Double, y: Double): Point {
        this.x = this.x * x
        this.y = this.y * y

        return this
    }

    fun scale(scale: Double): Point {
        this.x = this.x * scale
        this.y = this.y * scale

        return this
    }

    fun add(x: Double, y: Double): Point {
        this.x += x
        this.y += y

        return this
    }

    fun add(point: Point): Point {
        this.x += point.x
        this.y += point.y

        return this
    }

    fun set(point: Point): Point {
        this.x = point.x
        this.y = point.y

        return this
    }

}