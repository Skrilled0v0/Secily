package me.kl0udy92.apart.features.module.implementations.player

import me.kl0udy92.apart.events.entities.player.UpdateEvent
import me.kl0udy92.apart.events.network.PacketEvent
import me.kl0udy92.apart.features.module.Category
import me.kl0udy92.apart.features.module.Module
import me.kl0udy92.apart.features.module.annotation.ModuleData
import me.kl0udy92.apart.option.implementations.ArrayOption
import me.kl0udy92.apart.option.implementations.BooleanOption
import me.kl0udy92.apart.option.implementations.DoubleOption
import net.ayataka.eventapi.annotation.EventListener
import net.ayataka.eventapi.enumeration.EventDirection
import net.minecraft.init.Blocks
import net.minecraft.network.play.client.C07PacketPlayerDigging
import net.minecraft.util.BlockPos
import net.minecraft.util.EnumFacing

@ModuleData("SpeedMine", "快速挖掘", ["FastMine", "FastBreak"], "Speed Mine", Category.PLAYER, "Allows you fast mine.")
class SpeedMineModule : Module() {

    val modeOption = ArrayOption("Mode", "Switch mode.", "Packet", arrayOf("Damage", "Packet"))
    val speedOption = DoubleOption("Speed", "Break speed.", 1.4, 1.0, 2.0, 0.1) {
        this.modeOption.value == "Packet"
    }
    val damageOption = DoubleOption("Damage", "Block damage.", 0.8, 0.1, 1.0, 0.1) {
        this.modeOption.value == "Damage"
    }
    val noBlockHitDelayOption = BooleanOption("No Block-Hit Delay", "Cancel block-hit delay.", true)

    var destroying = false
    var damage = 0f
    var blockPos: BlockPos? = null
    var facing: EnumFacing? = null

    @EventListener
    fun onUpdate(event: UpdateEvent) {
        this.suffix = this.modeOption.value
        if (this.noBlockHitDelayOption.value) mc.playerController.blockHitDelay = 0
        if (!mc.playerController.extendedReach()) {
            when (this.modeOption.value.lowercase()) {
                "packet" -> {
                    if (this.destroying) {
                        val block = mc.theWorld.getBlockState(blockPos).block
                        this.damage = damage + (block.getPlayerRelativeBlockHardness(
                            mc.thePlayer,
                            mc.theWorld,
                            this.blockPos
                        ) * this.speedOption.value).toFloat()
                        if (this.damage >= 1f) {
                            mc.theWorld.setBlockState(blockPos, Blocks.air.defaultState, 11)
                            mc.netHandler.networkManager.sendPacketWithoutEvent(
                                C07PacketPlayerDigging(
                                    C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK,
                                    this.blockPos,
                                    this.facing
                                )
                            )
                            this.damage = 0f
                            this.destroying = false
                        }
                    }
                }
                "damage" -> {
                    if (mc.playerController.curBlockDamageMP >= this.damageOption.value) mc.playerController.curBlockDamageMP =
                        1f
                }
            }
        }
    }

    @EventListener
    fun onPacket(event: PacketEvent) {
        if (event.direction == EventDirection.OUTGOING) {
            if (this.modeOption.value == "Packet") {
                if (event.packet is C07PacketPlayerDigging && !mc.playerController.extendedReach()) {
                    val packet = event.packet as C07PacketPlayerDigging
                    if (packet.status == C07PacketPlayerDigging.Action.START_DESTROY_BLOCK) {
                        this.destroying = true
                        this.blockPos = packet.position
                        this.facing = packet.facing
                    } else if (packet.status == C07PacketPlayerDigging.Action.ABORT_DESTROY_BLOCK
                        || packet.status == C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK
                    ) {
                        this.destroying = false
                        this.blockPos = null
                        this.facing = null
                    }
                }
            }
        }
    }

}