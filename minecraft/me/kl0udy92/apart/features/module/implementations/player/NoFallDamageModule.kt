package me.kl0udy92.apart.features.module.implementations.player

import me.kl0udy92.apart.events.entities.player.MotionEvent
import me.kl0udy92.apart.events.network.PacketEvent
import me.kl0udy92.apart.features.module.Category
import me.kl0udy92.apart.features.module.Module
import me.kl0udy92.apart.features.module.annotation.ModuleData
import me.kl0udy92.apart.option.implementations.ArrayOption
import me.kl0udy92.apart.option.implementations.BooleanOption
import me.kl0udy92.apart.option.implementations.DoubleOption
import net.ayataka.eventapi.annotation.EventListener
import net.ayataka.eventapi.enumeration.EventDirection
import net.ayataka.eventapi.enumeration.EventState
import net.minecraft.network.play.client.C03PacketPlayer

@ModuleData("NoFallDamage", "ÎÞµôÂäÉËº¦", ["NoFall"], "No Fall Damage", Category.PLAYER, "Cancel falling damage.")
class NoFallDamageModule: Module() {

    val modeOption = ArrayOption("Mode", "Switch mode.", "Vanilla", arrayOf("Vanilla", "Packet", "C03 Ground"))
    val methodOption = ArrayOption("Method", "Switch method.", "Pre", arrayOf("Pre", "Post")) {
        this.modeOption.value == "Vanilla" || this.modeOption.value == "Packet"
    }
    val fallDistanceOption = DoubleOption("Fall Distance", "Min fall distance.", 3.0, 0.1, 7.0, 0.1)
    val resetFallDistanceOption = BooleanOption("Reset Fall Distance", "Reset player fall distance.", true)

    @EventListener
    fun onMotion(event: MotionEvent) {
        this.suffix = if (this.modeOption.value == "Vanilla" || this.modeOption.value == "Packet") "${this.modeOption.value} ${this.methodOption.value}" else this.modeOption.value
        if (mc.thePlayer.fallDistance > this.fallDistanceOption.value) {
            when (event.state) {
                EventState.PRE -> {
                    if (this.methodOption.value == "Post") return
                    when (this.modeOption.value) {
                        "Vanilla" -> {
                            event.ground = true
                            if (this.resetFallDistanceOption.value) mc.thePlayer.fallDistance = 0f
                        }
                        "Packet" -> {
                            mc.netHandler.networkManager.sendPacketWithoutEvent(C03PacketPlayer(true))
                            if (this.resetFallDistanceOption.value) mc.thePlayer.fallDistance = 0f
                        }
                    }
                }
                EventState.POST -> {
                    if (this.methodOption.value == "Pre") return
                    when (this.modeOption.value) {
                        "Vanilla" -> {
                            event.ground = true
                            if (this.resetFallDistanceOption.value) mc.thePlayer.fallDistance = 0f
                        }
                        "Packet" -> {
                            mc.netHandler.networkManager.sendPacketWithoutEvent(C03PacketPlayer(true))
                            if (this.resetFallDistanceOption.value) mc.thePlayer.fallDistance = 0f
                        }
                    }
                }
            }
        }
    }

    @EventListener
    fun onPacket(event: PacketEvent) {
        if (event.direction == EventDirection.OUTGOING) {
            if (event.packet is C03PacketPlayer) {
                if (this.modeOption.value == "C03 Ground") {
                    if (mc.thePlayer.fallDistance > this.fallDistanceOption.value) {
                        (event.packet as C03PacketPlayer).isOnGround = true
                        if (this.resetFallDistanceOption.value) mc.thePlayer.fallDistance = 0f
                    }
                }
            }
        }
    }

}