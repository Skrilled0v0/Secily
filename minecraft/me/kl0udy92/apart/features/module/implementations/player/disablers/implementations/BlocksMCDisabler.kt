package me.kl0udy92.apart.features.module.implementations.player.disablers.implementations

import me.kl0udy92.apart.events.entities.player.MotionEvent
import me.kl0udy92.apart.events.entities.player.UpdateEvent
import me.kl0udy92.apart.events.network.PacketEvent
import me.kl0udy92.apart.events.network.WorldLoadEvent
import me.kl0udy92.apart.features.module.implementations.player.DisablerModule
import me.kl0udy92.apart.features.module.implementations.player.disablers.AbstractDisablerMode
import me.kl0udy92.apart.utils.system.Stopwatch
import me.kl0udy92.apart.utils.system.packet.PacketUtil
import net.minecraft.network.Packet
import net.minecraft.network.play.client.C00PacketKeepAlive
import net.minecraft.network.play.client.C0BPacketEntityAction
import oh.yalan.NativeClass

/**
 * @author DeadAngels.
 * @since 2023/1/28
 **/
@NativeClass
class BlocksMCDisabler(daddy: DisablerModule): AbstractDisablerMode("BlocksMC", daddy) {

    private val C00Packets = mutableListOf<Packet<*>>()
    private val C00Stopwatch = Stopwatch()

    override fun onEnable() {
        this.C00Packets.clear()
        this.C00Stopwatch.reset()
    }

    override fun onDisable() {
        //Nothing todo.
    }

    override fun onWorldLoad(event: WorldLoadEvent) {
        this.C00Packets.clear()
        this.C00Stopwatch.reset()
    }

    override fun onUpdate(event: UpdateEvent) {
        if (C00Packets.isNotEmpty() && C00Stopwatch.elapsedWithReset(1337)) {
            PacketUtil.sendPacket(this.C00Packets.removeFirst()) { true }
        }
    }

    override fun onMotion(event: MotionEvent) {
        //Nothing todo.
    }

    override fun onPacket(event: PacketEvent) {
        if (event.packet is C0BPacketEntityAction) {
            val packet = event.packet as C0BPacketEntityAction
            if (packet.action == C0BPacketEntityAction.Action.START_SPRINTING ||
                packet.action == C0BPacketEntityAction.Action.STOP_SPRINTING) {
                event.cancel()
            }
        }
        if (event.packet is C00PacketKeepAlive) {
            this.C00Packets.add(event.packet)
            event.cancel()
        }
    }

}