package me.kl0udy92.apart.utils.system.packet

import me.kl0udy92.apart.utils.MinecraftInstance
import net.minecraft.network.Packet

/**
 * @author DeadAngels.
 * @since 2023/1/13
 **/
object PacketUtil: MinecraftInstance() {

    fun sendPacket(input: Packet<*>, eventable: () -> Boolean) {
        if (eventable.invoke())
            mc.netHandler.networkManager.sendPacket(input)
        else
            mc.netHandler.networkManager.sendPacketWithoutEvent(input)
    }

    fun sendPacketWithDelayed(input: Packet<*>, delay: Long, eventable: () -> Boolean) {
        object : Thread("Send Packet Delayed Thread") {

            override fun run() {
                sleep(delay)
                sendPacket(input, eventable)
                super.run()
            }

        }.start()
    }

}