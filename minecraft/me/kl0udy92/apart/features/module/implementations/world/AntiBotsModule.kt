package me.kl0udy92.apart.features.module.implementations.world

import me.kl0udy92.apart.events.entities.player.MotionEvent
import me.kl0udy92.apart.events.network.PacketEvent
import me.kl0udy92.apart.features.module.Category
import me.kl0udy92.apart.features.module.Module
import me.kl0udy92.apart.features.module.annotation.ModuleData
import me.kl0udy92.apart.option.implementations.ArrayOption
import me.kl0udy92.apart.utils.entity.player.PlayerUtil
import net.ayataka.eventapi.annotation.EventListener
import net.ayataka.eventapi.enumeration.EventDirection
import net.ayataka.eventapi.enumeration.EventState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.network.play.server.S07PacketRespawn

@ModuleData("AntiBots", "反假人", ["BotsRemover"], "Anti Bots", Category.WORLD, "Automatically remove world bots.")
class AntiBotsModule : Module() {

    val bots = mutableListOf<EntityPlayer>()

    val mode = ArrayOption("Mode", "Switch mode.", "Hypixel", arrayOf("Hypixel"))

    init {
        this.bots.clear()
    }

    @EventListener
    fun onMotion(event: MotionEvent) {
        when (event.state) {
            EventState.PRE -> {
                this.suffix = this.mode.value
                val strings = mutableListOf<String>()
                mc.thePlayer.sendQueue.playerInfoMap.forEach { strings.add(it.gameProfile.name) }
                mc.theWorld.playerEntities.filter {
                    it.ticksExisted >= 20 && !strings.contains(it.name) && !PlayerUtil.checkPing(it) && it !is EntityPlayer
                }.forEach { this.bots.add(it) }
            }
            EventState.POST -> { /*Nothing here.*/
            }
        }
    }

    @EventListener
    fun onPacket(event: PacketEvent) {
        when (event.direction) {
            EventDirection.INCOMING -> {
                if (event.packet is S07PacketRespawn) event.canceled = true
            }
            EventDirection.OUTGOING -> { /*Nothing here.*/
            }
        }
    }

}