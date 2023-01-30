package me.kl0udy92.apart.utils.render.animations.easing.util

@Suppress("unused")
class Easings {

    val c1 = 1.70158
    val c2 = c1 * 1.525
    val c3 = c1 + 1.0
    val c4 = 2.0 * Math.PI / 3.0
    val c5 = 2.0 * Math.PI / 4.5

    public val NONE = Easing { value -> value }
    val QUAD_IN = powIn(2)
    val QUAD_OUT = powOut(2)
    val QUAD_BOTH = powBoth(2.0)

    val CUBIC_IN = powIn(3)
    val CUBIC_OUT = powOut(3)
    val CUBIC_BOTH = powBoth(3.0)

    val QUART_IN = powIn(4)
    val QUART_OUT = powOut(4)
    val QUART_BOTH = powBoth(4.0)

    val QUINT_IN = powIn(5)
    val QUINT_OUT = powOut(5)
    val QUINT_BOTH = powBoth(5.0)

    val SINE_IN = Easing { value -> 1.0 - Math.cos(value * Math.PI / 2.0) }
    val SINE_OUT = Easing { value -> Math.sin(value * Math.PI / 2.0) }
    val SINE_BOTH = Easing { value -> -(Math.cos(Math.PI * value) - 1.0) / 2.0 }

    val CIRC_IN = Easing { value -> 1.0 - Math.sqrt(1.0 - Math.pow(value, 2.0)) }
    val CIRC_OUT = Easing { value -> Math.sqrt(1.0 - Math.pow(value - 1.0, 2.0)) }
    val CIRC_BOTH = Easing { value ->
        if (value < 0.5) {
            return@Easing (1.0 - Math.sqrt(1.0 - Math.pow(2.0 * value, 2.0))) / 2.0
        } else {
            return@Easing (Math.sqrt(1.0 - Math.pow(-2.0 * value + 2.0, 2.0)) + 1.0) / 2.0
        }
    }

    val ELASTIC_IN = Easing { value ->
        if (value === 0.0 || value === 1.0) {
            return@Easing value
        } else {
            return@Easing Math.pow(-2.0, 10.0 * value - 10.0) * Math.sin((value * 10.0 - 10.75) * c4)
        }
    }
    val ELASTIC_OUT = Easing { value ->
        if (value === 0.0 || value === 1.0) {
            return@Easing value
        } else {
            return@Easing Math.pow(2.0, -10.0 * value) * Math.sin((value * 10.0 - 0.75) * c4) + 1.0
        }
    }
    val ELASTIC_BOTH = Easing { value ->
        if (value === 0.0 || value === 1.0) {
            return@Easing value
        } else if (value < 0.5) {
            return@Easing -(Math.pow(2.0, 20.0 * value - 10.0) * Math.sin((20.0 * value - 11.125) * c5)) / 2.0
        } else {
            return@Easing Math.pow(2.0, -20.0 * value + 10.0) * Math.sin((20.0 * value - 11.125) * c5) / 2.0 + 1.0
        }
    }
    val EXPO_IN = Easing { value ->
        if (value !== 0.0) {
            return@Easing Math.pow(2.0, 10.0 * value - 10.0)
        } else {
            return@Easing value
        }
    }
    val EXPO_OUT = Easing { value ->
        if (value !== 1.0) {
            return@Easing 1.0 - Math.pow(2.0, -10.0 * value)
        } else {
            return@Easing value
        }
    }
    val EXPO_BOTH = Easing { value ->
        if (value === 0.0 || value === 1.0) {
            return@Easing value
        } else if (value < 0.5) {
            return@Easing Math.pow(2.0, 20.0 * value - 10.0) / 2.0
        } else {
            return@Easing (2.0 - Math.pow(2.0, -20.0 * value + 10)) / 2.0
        }
    }
    val BACK_IN = Easing { value -> c3 * Math.pow(value, 3.0) - c1 * Math.pow(value, 2.0) }
    val BACK_OUT =
        Easing { value -> 1.0 + c3 * Math.pow(value - 1.0, 3.0) + c1 * Math.pow(value - 1.0, 2.0) }
    val BACK_BOTH = Easing { value ->
        if (value < 0.5) {
            return@Easing Math.pow(2.0 * value, 2.0) * ((c2 + 1.0) * 2.0 * value - c2) / 2.0
        } else {
            return@Easing (Math.pow(2.0 * value - 2.0, 2.0) * ((c2 + 1.0) * (value * 2.0 - 2.0) + c2) + 2.0) / 2.0
        }
    }
    val BOUNCE_OUT = Easing { x ->
        val n1 = 7.5625
        val d1 = 2.75
        if (x < 1.0 / d1) {
            return@Easing n1 * Math.pow(x, 2.0)
        } else if (x < 2.0 / d1) {
            return@Easing n1 * Math.pow(x - 1.5 / d1, 2.0) + 0.75
        } else if (x < 2.5 / d1) {
            return@Easing n1 * Math.pow(x - 2.25 / d1, 2.0) + 0.9375
        } else {
            return@Easing n1 * Math.pow(x - 2.625 / d1, 2.0) + 0.984375
        }
    }
    val BOUNCE_IN = Easing { value -> 1.0 - BOUNCE_OUT.ease(1.0 - value) }
    val BOUNCE_BOTH = Easing { value ->
        if (value < 0.5) {
            return@Easing (1 - BOUNCE_OUT.ease(1.0 - 2.0 * value)) / 2.0
        } else {
            return@Easing (1 + BOUNCE_OUT.ease(2.0 * value - 1.0)) / 2.0
        }
    }

    private fun Easings() {}

    fun powIn(n: Double): Easing {
        return Easing { value -> Math.pow(value, n) }
    }

    fun powIn(n: Int): Easing {
        return powIn(n.toDouble())
    }

    fun powOut(n: Double): Easing {
        return Easing { value -> 1.0 - Math.pow(1.0 - value, n) }
    }

    fun powOut(n: Int): Easing {
        return powOut(n.toDouble())
    }

    fun powBoth(n: Double): Easing {
        return Easing { value ->
            if (value < 0.5) {
                return@Easing Math.pow(2.0, n - 1) * Math.pow(value, n)
            } else {
                return@Easing 1.0 - Math.pow(-2.0 * value + 2.0, n) / 2.0
            }
        }
    }

}