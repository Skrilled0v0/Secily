package me.kl0udy92.apart.features.command.implementations

import me.kl0udy92.apart.core.Main
import me.kl0udy92.apart.features.command.AbstractCommand
import me.kl0udy92.apart.features.command.annotation.CommandData

@CommandData("Toggle", "Toggle module state.", "-toggle <Module>")
class ToggleCommand : AbstractCommand() {

    override fun execute(args: Array<String>) {
        if (args.size != 1) {
            Main.print(this.syntax) { true }
            return
        }

        val module = Main.moduleManager.getModuleByName(args[0])

        if (module != null) {
            module.toggle()
            Main.print("${module.name} toggled.") { true }
        } else {
            Main.print("Unable to find module ${args[0]}.") { true }
        }
    }

}