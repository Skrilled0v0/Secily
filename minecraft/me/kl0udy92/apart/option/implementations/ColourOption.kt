package me.kl0udy92.apart.option.implementations

import me.kl0udy92.apart.option.Option
import java.awt.Color

class ColourOption: Option<Color> {

    var alpha: Int = 255

    constructor(name: String, description: String, value: Color, dependency: () -> Boolean) : super(
        name,
        description,
        value,
        dependency
    )

    constructor(name: String, description: String, value: Color) : super(name, description, value)

}