package me.kl0udy92.apart.events.network

import net.ayataka.eventapi.Event
import net.ayataka.eventapi.enumeration.EventDirection
import net.ayataka.eventapi.interfaces.Cancellable
import net.minecraft.network.Packet

class PacketEvent(var packet: Packet<*>, var direction: EventDirection) : Event(), Cancellable {

    override var canceled = false

    fun cancel() {
        this.canceled = true
    }

}