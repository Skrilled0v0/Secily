package me.kl0udy92.apart.features.command.implementations

import me.kl0udy92.apart.config.implementations.FriendsConfig
import me.kl0udy92.apart.core.Main
import me.kl0udy92.apart.features.command.AbstractCommand
import me.kl0udy92.apart.features.command.annotation.CommandData

@CommandData("friends", "Show all your friends.", "-friends/-friends clear")
class FriendsCommand : AbstractCommand() {

    override fun execute(args: Array<String>) {
        if (args.isNotEmpty()) {
            if (args[0].equals("clear", ignoreCase = true)) {
                Main.friendManager.friends.clear()
                Main.configManager.write(FriendsConfig())
                Main.print("Clear!") { true }
                return
            }
            Main.print(this.syntax) { true }
            return
        }
        Main.print("Friends:") { true }
        Main.friendManager.friends.forEach { it.name }
    }

}