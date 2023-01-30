package me.kl0udy92.apart.features.module.implementations.move

import me.kl0udy92.apart.core.Main
import me.kl0udy92.apart.events.entities.player.PlayerStateEvent
import me.kl0udy92.apart.events.entities.player.UpdateEvent
import me.kl0udy92.apart.features.module.Category
import me.kl0udy92.apart.features.module.Module
import me.kl0udy92.apart.features.module.annotation.ModuleData
import me.kl0udy92.apart.option.implementations.ArrayOption
import me.kl0udy92.apart.option.implementations.BooleanOption
import me.kl0udy92.apart.utils.entity.player.MoveUtil
import net.ayataka.eventapi.annotation.EventListener
import net.minecraft.client.settings.KeyBinding
import net.minecraft.potion.Potion

@ModuleData("Sprint", "强制疾跑", ["Run", "RunningMan"], "Sprint", Category.MOVE, "Make you sprint when you move.")
class SprintModule : Module() {

    val modeOption = ArrayOption("Mode", "Switch mode.", "Key", arrayOf("Key", "Set"))
    val multiDirectionalOption = BooleanOption("Multi-Directional", "Allows you to sprint when moving left and right or backwards.", true) {
        this.modeOption.value == "Set"
    }

    private val canSprint: Boolean
        get() {
            return when (this.modeOption.value) {
                "Key" -> MoveUtil.moving
                "Set" -> mc.thePlayer.movementInput.moveForward >= 0.8f ||
                        (this.multiDirectionalOption.value && MoveUtil.moving) &&
                        (mc.thePlayer.foodStats.foodLevel > 6.0f || mc.thePlayer.capabilities.allowFlying) &&
                        !mc.thePlayer.isPotionActive(Potion.blindness) &&
                        !mc.thePlayer.isCollidedHorizontally &&
                        !mc.thePlayer.isSneaking
                else -> false
            }
        }

    @EventListener
    fun onUpdate(event: UpdateEvent) {
        if (!this.canSprint) return
        if (this.modeOption.value == "Key") KeyBinding.setKeyBindState(mc.gameSettings.keyBindSprint, true)
    }

    @EventListener
    fun onPlayerState(event: PlayerStateEvent) {
        if (!this.canSprint) return
        if (this.modeOption.value == "Set") {
            event.sprinting = true
            mc.thePlayer.isSprinting = true
        }
    }

}