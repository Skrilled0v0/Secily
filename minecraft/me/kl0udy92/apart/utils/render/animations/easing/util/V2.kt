package me.kl0udy92.apart.utils.render.animations.easing.util

class V2 {

    var x: Double? = null
    var y: Double? = null

    constructor()

    constructor(x: Double, y: Double) {
        this.x = x
        this.y = y
    }

    constructor(x: Int, y: Int) {
        this.x = x.toDouble()
        this.y = y.toDouble()
    }

    constructor(x: Float, y: Float) {
        this.x = x.toDouble()
        this.y = y.toDouble()
    }

}