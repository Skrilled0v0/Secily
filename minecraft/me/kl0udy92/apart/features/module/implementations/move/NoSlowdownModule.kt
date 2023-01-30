package me.kl0udy92.apart.features.module.implementations.move

import me.kl0udy92.apart.core.Main
import me.kl0udy92.apart.events.entities.player.MotionEvent
import me.kl0udy92.apart.events.entities.player.SlowdownEvent
import me.kl0udy92.apart.features.module.Category
import me.kl0udy92.apart.features.module.Module
import me.kl0udy92.apart.features.module.annotation.ModuleData
import me.kl0udy92.apart.features.module.implementations.attack.KillAuraModule
import me.kl0udy92.apart.option.implementations.ArrayOption
import me.kl0udy92.apart.utils.entity.player.MoveUtil
import me.kl0udy92.apart.utils.entity.player.PlayerUtil
import net.ayataka.eventapi.annotation.EventListener
import net.ayataka.eventapi.enumeration.EventState
import net.minecraft.item.ItemSword
import net.minecraft.network.play.client.C07PacketPlayerDigging
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement
import net.minecraft.network.play.client.C09PacketHeldItemChange
import net.minecraft.util.BlockPos
import net.minecraft.util.EnumFacing

@ModuleData("NoSlowdown", "无减速", ["NoSlow"], "No Slowdown", Category.MOVE, "Let you no slowdown.")
class NoSlowdownModule : Module() {

    val modeOption = ArrayOption("Mode", "Switch mode.", "Always", arrayOf("Always", "Cancel", "NCP", "Watchdoge"))

    val blocking: Boolean
        get() = mc.thePlayer.heldItem != null && mc.thePlayer.heldItem.item is ItemSword && mc.thePlayer.isBlocking && !(Main.moduleManager.getModuleByClass(KillAuraModule().javaClass) as KillAuraModule).blocking

    @EventListener
    fun onSlowdown(event: SlowdownEvent) {
        when (this.modeOption.value) {
            "Always" -> {
                event.strafe = 1f
                event.forward = 1f
            }
            "Cancel" -> {
                event.canceled = true
            }
            "NCP" -> {
                event.canceled = true
            }
            "Watchdoge" -> {
                event.canceled = true
            }
        }
    }

    @EventListener
    fun onMotion(event: MotionEvent) {
        this.suffix = this.modeOption.value
        when (this.modeOption.value) {
            "Watchdoge" -> {
                if (mc.thePlayer.itemInUseDuration >= 1) {
                    if (PlayerUtil.usingFood) {
                        mc.netHandler.addToSendQueue(C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
                    }
                }
            }
            "NCP" -> {
                if (this.blocking && MoveUtil.moving && mc.thePlayer.onGround) {
                    when (event.state) {
                        EventState.PRE -> {
                            mc.playerController.syncCurrentPlayItem()
                            mc.netHandler.networkManager.sendPacketWithoutEvent(
                                C07PacketPlayerDigging(
                                    C07PacketPlayerDigging.Action.RELEASE_USE_ITEM,
                                    BlockPos.ORIGIN,
                                    EnumFacing.DOWN
                                )
                            )
                        }
                        EventState.POST -> {
                            mc.playerController.syncCurrentPlayItem()
                            mc.netHandler.networkManager.sendPacketWithoutEvent(C08PacketPlayerBlockPlacement(mc.thePlayer.currentEquippedItem))
                        }
                    }
                }
            }
        }
    }

}