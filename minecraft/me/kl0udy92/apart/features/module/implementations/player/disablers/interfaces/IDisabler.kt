package me.kl0udy92.apart.features.module.implementations.player.disablers.interfaces

import me.kl0udy92.apart.events.entities.player.MotionEvent
import me.kl0udy92.apart.events.entities.player.UpdateEvent
import me.kl0udy92.apart.events.network.PacketEvent
import me.kl0udy92.apart.events.network.WorldLoadEvent

/**
 * @author DeadAngels.
 * @since 2023/1/28
 **/
interface IDisabler {

    fun onEnable()

    fun onDisable()

    fun onWorldLoad(event: WorldLoadEvent)

    fun onUpdate(event: UpdateEvent)

    fun onMotion(event: MotionEvent)

    fun onPacket(event: PacketEvent)

}