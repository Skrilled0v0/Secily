package me.kl0udy92.apart.option.implementations

import me.kl0udy92.apart.option.Option
import java.awt.Color

class StringOption : Option<String> {

    constructor(name: String, description: String, value: String, dependency: () -> Boolean) : super(
        name,
        description,
        value,
        dependency
    )

    constructor(name: String, description: String, value: String) : super(name, description, value)

}