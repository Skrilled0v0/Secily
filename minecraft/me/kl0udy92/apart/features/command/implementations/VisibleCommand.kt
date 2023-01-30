package me.kl0udy92.apart.features.command.implementations

import me.kl0udy92.apart.core.Main
import me.kl0udy92.apart.features.command.AbstractCommand
import me.kl0udy92.apart.features.command.annotation.CommandData
import java.lang.Boolean
import kotlin.Array
import kotlin.String

@CommandData("Visible", "Set the visible of module.", "-visible <Module> <true/false>")
class VisibleCommand : AbstractCommand() {

    override fun execute(args: Array<String>) {
        if (args.size != 2) {
            Main.print(this.syntax) { true }
            return
        }

        val module = Main.moduleManager.getModuleByName(args[0])

        if (module != null) {
            module.visible = Boolean.parseBoolean(args[1])
            Main.print("Set the visible of ${module.name} to ${module.visible}.") { true }
        } else {
            Main.print("Unable to find module ${args[0]}.") { true }
        }
    }

}