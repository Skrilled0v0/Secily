package me.kl0udy92.apart.features.module

import me.kl0udy92.apart.core.Main
import net.minecraft.util.ResourceLocation
import java.awt.Color

enum class Category(
    val identifier: String,
    val color: Color,
    val iconLocation: ResourceLocation,
    val iconLocation2: ResourceLocation
) {

    ATTACK(
        "Attack",
        Color(255, 82, 84),
        ResourceLocation("${Main.name.lowercase()}/icons/categories/dropdown/attack.png"),
        ResourceLocation("${Main.name.lowercase()}/icons/categories/imgui/attack.png")
    ),
    MOVE(
        "Move",
        Color(47, 203, 115),
        ResourceLocation("${Main.name.lowercase()}/icons/categories/dropdown/move.png"),
        ResourceLocation("${Main.name.lowercase()}/icons/categories/imgui/move.png")
    ),
    PLAYER(
        "Player",
        Color(142, 67, 174),
        ResourceLocation("${Main.name.lowercase()}/icons/categories/dropdown/player.png"),
        ResourceLocation("${Main.name.lowercase()}/icons/categories/imgui/player.png")
    ),
    WORLD(
        "World",
        Color(147, 105, 255),
        ResourceLocation("${Main.name.lowercase()}/icons/categories/dropdown/world.png"),
        ResourceLocation("${Main.name.lowercase()}/icons/categories/imgui/world.png")
    ),
    VISUAL(
        "Visual",
        Color(54, 0, 201),
        ResourceLocation("${Main.name.lowercase()}/icons/categories/dropdown/visual.png"),
        ResourceLocation("${Main.name.lowercase()}/icons/categories/imgui/visual.png")
    )

}