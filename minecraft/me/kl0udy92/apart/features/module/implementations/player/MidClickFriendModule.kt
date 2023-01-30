package me.kl0udy92.apart.features.module.implementations.player

import me.kl0udy92.apart.core.Main
import me.kl0udy92.apart.events.system.MouseClickEvent
import me.kl0udy92.apart.features.friends.Friend
import me.kl0udy92.apart.features.module.Category
import me.kl0udy92.apart.features.module.Module
import me.kl0udy92.apart.features.module.annotation.ModuleData
import me.kl0udy92.apart.screen.notification.Notification
import me.kl0udy92.apart.screen.notification.NotificationType
import net.ayataka.eventapi.annotation.EventListener
import net.ayataka.eventapi.enumeration.EventClickType

@ModuleData(
    "MidClickFriend",
    "中键加好友",
    ["MCF", "MiddleClickFriend"],
    "Mid Click Friend",
    Category.PLAYER,
    "Add or remove friends when the middle mouse button is pressed."
)
class MidClickFriendModule : Module() {

    @EventListener
    fun onMouseClick(event: MouseClickEvent) {
        if (event.type == EventClickType.MIDDLE) {
            if (mc.objectMouseOver != null && mc.objectMouseOver.entityHit != null) {
                val entity = mc.objectMouseOver.entityHit
                if (Main.friendManager.getFriendByName(entity.name) != null) {
                    Main.friendManager.removeFriend(entity.name)
                    //Variance.getInstance().printMessage("Removed " + entity.getName() + " from friends.");
                    Main.notificationManager.notifications.add(
                        Notification(
                            "Information",
                            "Removed ${entity.name} from friends.",
                            NotificationType.INFO
                        )
                    )
                } else {
                    Main.friendManager.addFriend(Friend(entity.name))
                    //Variance.getInstance().printMessage("Added " + entity.getName() + " to friends.");
                    Main.notificationManager.notifications.add(
                        Notification(
                            "Information",
                            "Added ${entity.name} to friends.",
                            NotificationType.INFO
                        )
                    )
                }
            }
        }
    }

}