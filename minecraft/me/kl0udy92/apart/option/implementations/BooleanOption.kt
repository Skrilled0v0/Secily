package me.kl0udy92.apart.option.implementations

import me.kl0udy92.apart.option.Option

class BooleanOption : Option<Boolean> {

    constructor(name: String, description: String, value: Boolean, dependency: () -> Boolean) : super(
        name,
        description,
        value,
        dependency
    )

    constructor(name: String, description: String, value: Boolean) : super(name, description, value)

}