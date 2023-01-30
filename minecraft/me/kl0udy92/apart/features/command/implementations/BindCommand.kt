package me.kl0udy92.apart.features.command.implementations

import me.kl0udy92.apart.core.Main
import me.kl0udy92.apart.features.command.AbstractCommand
import me.kl0udy92.apart.features.command.annotation.CommandData
import me.kl0udy92.apart.screen.notification.Notification
import me.kl0udy92.apart.screen.notification.NotificationType
import org.lwjgl.input.Keyboard

@CommandData("Bind", "Bind module key.", "-bind <Module> <key>")
class BindCommand : AbstractCommand() {

    override fun execute(args: Array<String>) {
        if (args.size != 2) {
            Main.print(this.syntax) { true }
            return
        }

        val module = Main.moduleManager.getModuleByName(args[0])

        if (module != null) {
            val keyCode = Keyboard.getKeyIndex(args[1].uppercase())
            module.keyCode = keyCode
            Main.notificationManager.notifications.add(
                Notification(
                    "Information",
                    "Set the key of ${module.name} to ${Keyboard.getKeyName(keyCode)}.",
                    NotificationType.INFO
                )
            )
        } else {
            Main.notificationManager.notifications.add(
                Notification(
                    "Information",
                    "Unable to find module ${args[0]}.",
                    NotificationType.INFO
                )
            )
        }

    }

}