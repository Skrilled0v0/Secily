package me.kl0udy92.apart.features.command

import me.kl0udy92.apart.features.command.annotation.CommandData
import me.kl0udy92.apart.features.command.interfaces.ICommand

abstract class AbstractCommand : ICommand {

    val name: String
    val usage: String
    val syntax: String

    init {
        val commandData = this.javaClass.getAnnotation(CommandData::class.java)

        this.name = commandData.name
        this.usage = commandData.usage
        this.syntax = commandData.syntax
    }

}