package me.kl0udy92.apart.features.command.implementations

import me.kl0udy92.apart.core.Main
import me.kl0udy92.apart.features.command.AbstractCommand
import me.kl0udy92.apart.features.command.annotation.CommandData
import org.lwjgl.input.Keyboard

@CommandData("Binds", "Show every module's bound keys.", "-binds/-binds clear")
class BindsCommand : AbstractCommand() {

    override fun execute(args: Array<String>) {
        if (args.isNotEmpty()) {
            if (args[0].equals("clear", ignoreCase = true)) {
                Main.moduleManager.modules
                    .forEach { module -> module.keyCode = Keyboard.KEY_NONE }
                Main.print("Clear!") { true }
                return
            }
            Main.print(this.syntax) { true }
            return
        }

        Main.print("Bound key of modules:") { true }
        Main.moduleManager.modules
            .filter { module -> module.keyCode != Keyboard.KEY_NONE }
            .forEach { module ->
                Main.print("${module.name} -> ${Keyboard.getKeyName(module.keyCode)}") { true }
            }
    }

}