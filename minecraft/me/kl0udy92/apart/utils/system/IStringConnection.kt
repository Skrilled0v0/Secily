package me.kl0udy92.apart.utils.system

@FunctionalInterface
interface IStringConnection {

    fun connect(key: String, value: String): String

}