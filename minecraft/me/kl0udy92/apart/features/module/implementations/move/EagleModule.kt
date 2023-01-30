package me.kl0udy92.apart.features.module.implementations.move

import me.kl0udy92.apart.events.entities.player.UpdateEvent
import me.kl0udy92.apart.features.module.Category
import me.kl0udy92.apart.features.module.Module
import me.kl0udy92.apart.features.module.annotation.ModuleData
import me.kl0udy92.apart.utils.entity.player.PlayerUtil
import net.ayataka.eventapi.annotation.EventListener
import net.minecraft.block.BlockAir
import net.minecraft.client.settings.KeyBinding
import org.lwjgl.input.Keyboard

/**
 * @author DeadAngels.
 * @since 2023/1/19
 **/
@ModuleData("Eagle", "±ßÔµÇ±ÐÐ", [""], "Eagle", Category.MOVE, "Makes you sneak when you're on the edge of the blocks.")
class EagleModule: Module() {

    override fun onDisable() {
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.keyCode, Keyboard.isKeyDown(mc.gameSettings.keyBindSneak.keyCode))
        super.onDisable()
    }

    @EventListener
    fun onUpdate(event: UpdateEvent) {
        if (mc.thePlayer.capabilities.isFlying) {
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.keyCode, Keyboard.isKeyDown(mc.gameSettings.keyBindSneak.keyCode))
            return
        }
        if (PlayerUtil.underBlock is BlockAir) {
            if (mc.thePlayer.onGround) {
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.keyCode, true)
            }
        } else {
            if (mc.thePlayer.onGround) {
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.keyCode, false)
            }
        }
    }

}