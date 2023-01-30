package me.kl0udy92.apart.features.module.implementations.move

import me.kl0udy92.apart.events.entities.player.UpdateEvent
import me.kl0udy92.apart.features.module.Category
import me.kl0udy92.apart.features.module.Module
import me.kl0udy92.apart.features.module.annotation.ModuleData
import net.ayataka.eventapi.annotation.EventListener
import net.minecraft.client.gui.GuiChat
import net.minecraft.client.settings.KeyBinding
import org.lwjgl.input.Keyboard

@ModuleData("InventoryWalk", "±³°üÐÐ×ß", ["InventoryMove", "InvMove", "InvWalk", "GuiMove", "GuiWalk"], "Inventory Walk", Category.MOVE, "Enables you to move while opening the container.")
class InventoryWalkModule: Module() {

    private val keys = arrayOf(
        mc.gameSettings.keyBindForward,
        mc.gameSettings.keyBindBack,
        mc.gameSettings.keyBindLeft,
        mc.gameSettings.keyBindRight,
        mc.gameSettings.keyBindJump
    )

    @EventListener
    fun onUpdate(event: UpdateEvent) {
        if (mc.currentScreen != null && mc.currentScreen !is GuiChat) {
            this.keys.forEach { KeyBinding.setKeyBindState(it.keyCode, Keyboard.isKeyDown(it.keyCode)) }
        }
    }

}