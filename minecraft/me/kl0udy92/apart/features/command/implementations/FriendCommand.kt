package me.kl0udy92.apart.features.command.implementations

import me.kl0udy92.apart.core.Main
import me.kl0udy92.apart.features.command.AbstractCommand
import me.kl0udy92.apart.features.command.annotation.CommandData
import me.kl0udy92.apart.features.friends.Friend
import me.kl0udy92.apart.screen.notification.Notification
import me.kl0udy92.apart.screen.notification.NotificationType

@CommandData("friend", "Add or remove friends.", "-friend add/remove <name>")
class FriendCommand : AbstractCommand() {

    override fun execute(args: Array<String>) {
        if (args.size != 2) {
            Main.print(this.syntax) { true }
            return
        }

        when (args[0].lowercase()) {
            "add" -> {
                Main.friendManager.addFriend(Friend(args[1]))
                Main.notificationManager.notifications.add(
                    Notification(
                        "Information",
                        "Added ${args[1]} to friends. ",
                        NotificationType.INFO
                    )
                )
            }
            "remove" -> {
                Main.friendManager.removeFriend(args[1])
                Main.notificationManager.notifications.add(
                    Notification(
                        "Information",
                        "Removed ${args[1]} from friends. ",
                        NotificationType.INFO
                    )
                )
            }
        }

    }

}