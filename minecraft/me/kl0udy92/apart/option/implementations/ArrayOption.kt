package me.kl0udy92.apart.option.implementations

import me.kl0udy92.apart.option.Option
import java.util.*

class ArrayOption : Option<String> {

    val values: Array<String>

    constructor(
        name: String,
        description: String,
        value: String,
        values: Array<String>,
        dependency: () -> Boolean
    ) : super(name, description, value, dependency) {
        this.values = values
    }

    constructor(name: String, description: String, value: String, values: Array<String>) : super(
        name,
        description,
        value
    ) {
        this.values = values
    }

    operator fun contains(string: String?): Boolean {
        return Arrays.stream(values).anyMatch { s: String -> s.equals(string, ignoreCase = true) }
    }

}