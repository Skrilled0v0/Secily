package me.kl0udy92.apart.features.module.implementations.attack

import me.kl0udy92.apart.events.entities.player.MotionEvent
import me.kl0udy92.apart.features.module.Category
import me.kl0udy92.apart.features.module.Module
import me.kl0udy92.apart.features.module.annotation.ModuleData
import me.kl0udy92.apart.option.implementations.ArrayOption
import me.kl0udy92.apart.option.implementations.BooleanOption
import me.kl0udy92.apart.option.implementations.DoubleOption
import me.kl0udy92.apart.utils.entity.EntitiesUtil
import me.kl0udy92.apart.utils.entity.player.RotationUtil
import me.kl0udy92.apart.utils.system.Stopwatch
import net.ayataka.eventapi.annotation.EventListener
import net.ayataka.eventapi.enumeration.EventState
import net.minecraft.entity.projectile.EntityFireball
import net.minecraft.network.play.client.C02PacketUseEntity
import net.minecraft.network.play.client.C0APacketAnimation

@ModuleData(
    "AntiFireballs",
    "火球弹反",
    ["AntiFireball"],
    "Anti Fireballs",
    Category.ATTACK,
    "When the fireball appears at a certain distance, it will automatically attack it."
)
class AntiFireballsModule : Module() {

    private val attackMethodOption =
        ArrayOption("Attack Method", "Switch attack method.", "Packet", arrayOf("Packet", "Normal"))
    private val swingOption = BooleanOption("Swing", "Item swing.", true)
    private val delayOption = DoubleOption("Delay", "Attack delay.", 200.0, 0.0, 500.0, 10.0)
    private val distanceOption = DoubleOption("Distance", "Attack distance.", 4.0, 3.0, 6.0, 0.1)

    private val stopWatch = Stopwatch()

    @EventListener
    fun onMotion(event: MotionEvent) {
        this.suffix = this.delayOption.value
        if (event.state == EventState.PRE) {
            EntitiesUtil.getEntitiesNearby(this.distanceOption.value.toFloat())
                .filter { it is EntityFireball && this.stopWatch.elapsedWithReset(this.delayOption.value.toLong()) }
                .forEach {
                    val values = RotationUtil.getRotationsFromEntity(it)
                    event.yaw = values[0]
                    event.pitch = values[1]
                    mc.thePlayer.renderYawOffset = values[0]
                    mc.thePlayer.rotationYawHead = values[1]
                    when (this.attackMethodOption.value) {
                        "Packet" -> {
                            mc.netHandler.networkManager.sendPacketWithoutEvent(
                                C02PacketUseEntity(
                                    it,
                                    C02PacketUseEntity.Action.ATTACK
                                )
                            )
                        }
                        else -> {
                            mc.playerController.attackEntity(mc.thePlayer, it)
                        }
                    }
                    if (this.swingOption.value)
                        mc.thePlayer.swingItem()
                    else
                        mc.netHandler.networkManager.sendPacketWithoutEvent(
                            C0APacketAnimation()
                        )
                }
        }
    }

}