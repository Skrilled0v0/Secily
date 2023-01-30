package me.kl0udy92.apart.screen.notification

import me.kl0udy92.apart.core.Main
import me.kl0udy92.apart.utils.render.Palette
import net.minecraft.util.ResourceLocation
import java.awt.Color

enum class NotificationType(val color: Color, val location: ResourceLocation) {

    SUCCESS(Palette.HIGH_LIGHT_GREEN.color, ResourceLocation("${Main.name.lowercase()}/icons/notify/success.png")),
    ERROR(Palette.RED.color, ResourceLocation("${Main.name.lowercase()}/icons/notify/error.png")),
    WARNING(Palette.AMBER.color, ResourceLocation("${Main.name.lowercase()}/icons/notify/warning.png")),
    INFO(Palette.BLUE_GREY.color, ResourceLocation("${Main.name.lowercase()}/icons/notify/info.png"))

}