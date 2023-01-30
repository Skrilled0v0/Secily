package me.kl0udy92.apart.utils.render.animations.easing.bezier

open abstract class Bezier() {

    lateinit var startPoint: Point
    lateinit var endPoint: Point

    val start: Point = Point(0.0, 0.0)
    val end: Point = Point(1.0, 1.0)

    constructor(startPoint: Point, endPoint: Point) : this() {
        this.startPoint = startPoint
        this.endPoint = endPoint
    }

    abstract fun getValue(t: Double): Double

}