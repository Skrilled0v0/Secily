package me.kl0udy92.apart.features.command.implementations

import me.kl0udy92.apart.core.Main
import me.kl0udy92.apart.features.command.AbstractCommand
import me.kl0udy92.apart.features.command.annotation.CommandData

@CommandData("Help", "Show every command's usage & syntax.", "-help")
class HelpCommand : AbstractCommand() {

    override fun execute(args: Array<String>) {
        Main.print("Command usage and syntax list:") { true }
        Main.commandManager.commands.forEach { command ->
            Main.print("${command.name} -> ${command.usage}") { true }
            Main.print("${command.name} -> ${command.syntax}") { true }
        }
    }

}