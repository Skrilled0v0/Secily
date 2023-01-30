package me.kl0udy92.apart.option.implementations

import me.kl0udy92.apart.option.Option
import java.awt.Color
import java.util.*

class MultiBooleanOption : Option<Array<BooleanOption>> {

    constructor(name: String, description: String, value: Array<BooleanOption>, dependency: () -> Boolean) : super(
        name,
        description,
        value,
        dependency
    )

    constructor(name: String, description: String, value: Array<BooleanOption>) : super(name, description, value)

    operator fun contains(string: String?): Boolean {
        return Arrays.stream(this.value)
            .anyMatch { option: BooleanOption -> option.name.equals(string, ignoreCase = true) }
    }

    fun find(key: String): BooleanOption? {
        this.value.filter { it.name.equals(key, true) }.forEach { return it }
        return null
    }

}