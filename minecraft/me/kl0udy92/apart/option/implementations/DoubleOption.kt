package me.kl0udy92.apart.option.implementations

import me.kl0udy92.apart.option.Option

class DoubleOption : Option<Double> {

    val min: Double
    val max: Double
    val increment: Double

    constructor(
        name: String,
        description: String,
        value: Double,
        min: Double,
        max: Double,
        increment: Double,
        dependency: () -> Boolean
    ) : super(name, description, value, dependency) {
        this.min = min
        this.max = max
        this.increment = increment
    }

    constructor(name: String, description: String, value: Double, min: Double, max: Double, increment: Double) : super(
        name,
        description,
        value
    ) {
        this.min = min
        this.max = max
        this.increment = increment
    }
}