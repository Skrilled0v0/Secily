package me.kl0udy92.apart.option

open class Option<T>(
    val name: String,
    val description: String,
    var value: T,
    var dependency: () -> Boolean
) {

    constructor(name: String, description: String, value: T) : this(name, description, value, { true })

}