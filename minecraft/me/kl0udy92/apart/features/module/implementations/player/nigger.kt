package me.kl0udy92.apart.features.module.implementations.player

import me.kl0udy92.apart.core.Main
import me.kl0udy92.apart.events.network.PacketEvent
import me.kl0udy92.apart.features.module.Category
import me.kl0udy92.apart.features.module.Module
import me.kl0udy92.apart.features.module.annotation.ModuleData
import me.kl0udy92.apart.option.implementations.BooleanOption
import me.kl0udy92.apart.option.implementations.ColourOption
import me.kl0udy92.apart.option.implementations.MultiBooleanOption
import net.ayataka.eventapi.annotation.EventListener
import net.minecraft.network.play.client.C00PacketKeepAlive
import net.minecraft.network.play.client.C0CPacketInput
import net.minecraft.network.play.client.C0FPacketConfirmTransaction
import java.awt.Color

@ModuleData("nigger", "ºÚ¹í", ["e"], "nigger", Category.PLAYER)
class nigger : Module() {

    val multiTest = MultiBooleanOption(
        "Elements",
        "Elements.",
        arrayOf(
            BooleanOption("Toggle 1", "no", false),
            BooleanOption("Toggle 2", "no", true)
        )
    )
    val colourTest = ColourOption("Colour Test", "test", Color(255, 0, 0)) {
        this.multiTest.find("Toggle 1")!!.value
    }

    var lastPing = 0

    @EventListener
    fun onPacket(event: PacketEvent) {
        if (event.packet is C00PacketKeepAlive) {
            Main.print((event.packet as C00PacketKeepAlive).key) { false }
        }
    }

}