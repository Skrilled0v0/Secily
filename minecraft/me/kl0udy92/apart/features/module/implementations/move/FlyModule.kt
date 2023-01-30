package me.kl0udy92.apart.features.module.implementations.move

import me.kl0udy92.apart.events.entities.player.UpdateEvent
import me.kl0udy92.apart.features.module.Category
import me.kl0udy92.apart.features.module.Module
import me.kl0udy92.apart.features.module.annotation.ModuleData
import me.kl0udy92.apart.option.implementations.ArrayOption
import me.kl0udy92.apart.option.implementations.DoubleOption
import me.kl0udy92.apart.utils.entity.player.MoveUtil
import net.ayataka.eventapi.annotation.EventListener
import net.minecraft.world.WorldSettings

/**
 * @author DeadAngels.
 * @since 2023/1/11
 **/
@ModuleData("Fly", "·ÉÐÐ", ["Flight"], "Fly", Category.MOVE, "Make you fly.")
class FlyModule: Module() {

    val modeOption = ArrayOption("Mode", "Switch mode.", "Motion", arrayOf("Motion", "Abilities", "AirJump"))

    override fun onDisable() {
        when (this.modeOption.value) {
            "Abilities" -> {
                if (mc.playerController.currentGameType != WorldSettings.GameType.CREATIVE && mc.playerController.currentGameType != WorldSettings.GameType.SPECTATOR) {
                    mc.thePlayer.capabilities.allowFlying = false
                    mc.thePlayer.capabilities.isFlying = false
                }
            }
        }
        super.onDisable()
    }

    @EventListener
    fun onUpdate(event: UpdateEvent) {
        this.suffix = this.modeOption.value
        when (this.modeOption.value) {
            "Abilities" -> {
                if (mc.playerController.currentGameType != WorldSettings.GameType.CREATIVE && mc.playerController.currentGameType != WorldSettings.GameType.SPECTATOR) {
                    mc.thePlayer.capabilities.allowFlying = true
                    mc.thePlayer.capabilities.isFlying = true
                }
            }
            "Motion" -> {
                mc.thePlayer.motionY = if (mc.gameSettings.keyBindJump.isKeyDown) 2.0 else if (mc.gameSettings.keyBindSneak.isKeyDown) -2.0 else 0.0
                if (MoveUtil.moving) MoveUtil.strafe()
            }
            "AirJump" -> {
                if (!mc.thePlayer.onGround) {
                    if (mc.gameSettings.keyBindJump.isKeyDown) {
                        mc.thePlayer.jump()
                    }
                }
            }
        }
    }

}