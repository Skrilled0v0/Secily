package me.kl0udy92.apart.features.module.implementations.attack

import me.kl0udy92.apart.core.Main
import me.kl0udy92.apart.events.entities.player.UpdateEvent
import me.kl0udy92.apart.features.module.Category
import me.kl0udy92.apart.features.module.Module
import me.kl0udy92.apart.features.module.annotation.ModuleData
import me.kl0udy92.apart.option.implementations.ArrayOption
import me.kl0udy92.apart.option.implementations.BooleanOption
import me.kl0udy92.apart.option.implementations.DoubleOption
import me.kl0udy92.apart.utils.system.Stopwatch
import net.ayataka.eventapi.annotation.EventListener
import net.minecraft.entity.EntityLivingBase
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition

@ModuleData("Critcals", "刀刀暴击", ["Critcal"], "Critcals", Category.ATTACK, "Always critical hits.")
class CritcalsModule : Module() {

    val modeOption = ArrayOption("Mode", "Crit mode.", "Watchdoge", arrayOf("Watchdoge", "Packet"))
    val logOption = BooleanOption("Logger", "Log.", false)
    val delayOption = DoubleOption("Delay", "Crit delay.", 0.0, 0.0, 5.0, 0.1)
    val hurtTimeOption = DoubleOption("Hurt Time", "Target hurt time.", 0.0, 0.0, 10.0, 1.0)

    val stopWatch = Stopwatch()

    @EventListener
    fun onUpdate(event: UpdateEvent) {
        this.suffix = "${this.modeOption.value} ${this.delayOption.value * 1000}"
    }

    private fun canCrit(target: EntityLivingBase): Boolean {
        return mc.thePlayer.isCollidedVertically && !mc.thePlayer.isInWater && !mc.thePlayer.isInLava && !mc.thePlayer.isOnLadder && this.stopWatch.elapsedWithReset(
            this.delayOption.value.toLong() * 1000L
        ) && mc.thePlayer.onGround && target.hurtTime <= this.hurtTimeOption.value.toInt()
    }

    fun crit(targetEntity: EntityLivingBase) {
        if (this.canCrit(targetEntity)) {
            val player = mc.thePlayer
            when (this.modeOption.value) {
                "Packet" -> {
                    val offset = doubleArrayOf(0.03547687513000, 0.0068784511356, 0.0015423334700)
                    offset.forEach {
                        mc.netHandler.networkManager.sendPacketWithoutEvent(
                            C04PacketPlayerPosition(
                                player.posX,
                                player.posY + it,
                                player.posZ,
                                false
                            )
                        )
                    }
                }
                "Watchdoge" -> {
                    val offset = doubleArrayOf(0.013975468541, 0.012511000037193298, 0.05954835722479834)
                    offset.forEach {
                        mc.netHandler.networkManager.sendPacketWithoutEvent(
                            C04PacketPlayerPosition(
                                player.posX,
                                player.posY + it,
                                player.posZ,
                                false
                            )
                        )
                    }
                }
            }
            if (this.logOption.value) Main.print("Crit!") { true }
        }
    }

}