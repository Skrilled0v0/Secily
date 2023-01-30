package me.kl0udy92.apart.features.module.implementations.move

import me.kl0udy92.apart.events.entities.player.MotionEvent
import me.kl0udy92.apart.features.module.Category
import me.kl0udy92.apart.features.module.Module
import me.kl0udy92.apart.features.module.annotation.ModuleData
import me.kl0udy92.apart.option.implementations.ArrayOption
import me.kl0udy92.apart.option.implementations.BooleanOption
import me.kl0udy92.apart.option.implementations.DoubleOption
import me.kl0udy92.apart.utils.entity.player.PlayerUtil
import me.kl0udy92.apart.utils.system.Stopwatch
import net.ayataka.eventapi.annotation.EventListener
import net.ayataka.eventapi.enumeration.EventState
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition

/**
 * @author DeadAngels.
 * @since 2023/1/21
 **/
@ModuleData("Pullback", "Ðé¿ÕÀ­»Ø", ["AntiVoid", "AntiFall"], "Pullback", Category.MOVE)
class PullbackModule: Module() {

    val modeOption = ArrayOption("Mode", "Switch mode.", "Packet", arrayOf("Packet", "Vanilla", "Position"))
    val voidOnly = BooleanOption("Void Only", "Void only.", true)
    val fallDistanceOption = DoubleOption("Fall Distance", "Max fall distance.", 5.0, 1.0, 10.0, 0.1)
    val delayOption = DoubleOption("Delay", "Stopwatch delay.", 3000.0, 0.0, 3000.0, 100.0)

    val stopWatch = Stopwatch()

    @EventListener
    fun onMotion(event: MotionEvent) {
        this.suffix = this.modeOption.value
        when (event.state) {
            EventState.PRE -> {
                if (this.stopWatch.elapsed(this.delayOption.value.toLong()) && mc.thePlayer.fallDistance > this.fallDistanceOption.value && !mc.thePlayer.capabilities.isFlying && (!PlayerUtil.blockUnder || !this.voidOnly.value)) {
                    when (this.modeOption.value) {
                        "Packet" -> {
                            mc.netHandler.networkManager.sendPacketWithoutEvent(
                                C04PacketPlayerPosition(
                                    mc.thePlayer.posX,
                                    mc.thePlayer.posY + this.fallDistanceOption.value,
                                    mc.thePlayer.posZ,
                                    false
                                )
                            )
                        }
                        "Vanilla" -> {
                            event.posY += this.fallDistanceOption.value
                        }
                        "Position" -> {
                            mc.thePlayer.posY += this.fallDistanceOption.value
                        }
                    }
                    this.stopWatch.reset()
                }
            }
            EventState.POST -> {
                //Nothing todo.
            }
        }
    }

}