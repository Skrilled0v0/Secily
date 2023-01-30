package me.kl0udy92.apart.features.module.implementations.world

import me.kl0udy92.apart.events.entities.player.UpdateEvent
import me.kl0udy92.apart.events.network.PacketEvent
import me.kl0udy92.apart.features.module.Category
import me.kl0udy92.apart.features.module.Module
import me.kl0udy92.apart.features.module.annotation.ModuleData
import me.kl0udy92.apart.option.implementations.DoubleOption
import net.ayataka.eventapi.annotation.EventListener
import net.ayataka.eventapi.enumeration.EventDirection
import net.minecraft.network.play.server.S03PacketTimeUpdate

@ModuleData(
    "TimeChanger",
    "时间更改器",
    ["worldtime", "time"],
    "Time Changer",
    Category.WORLD,
    "Change world time on client-side."
)
class TimeChangerModule : Module() {

    val timeOption = DoubleOption("Time", "Time value.", 14000.0, 0.0, 24000.0, 1000.0)

    @EventListener
    fun onUpdate(event: UpdateEvent) {
        this.suffix = this.timeOption.value
        mc.theWorld.worldTime = this.timeOption.value.toLong()
        mc.theWorld.totalWorldTime = this.timeOption.value.toLong()
    }

    @EventListener
    fun onPacket(event: PacketEvent) {
        if (event.direction == EventDirection.INCOMING) {
            if (event.packet is S03PacketTimeUpdate) event.cancel()
        }
    }

}