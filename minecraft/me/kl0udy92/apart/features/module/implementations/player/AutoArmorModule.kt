package me.kl0udy92.apart.features.module.implementations.player

import me.kl0udy92.apart.events.entities.player.UpdateEvent
import me.kl0udy92.apart.features.module.Category
import me.kl0udy92.apart.features.module.Module
import me.kl0udy92.apart.features.module.annotation.ModuleData
import me.kl0udy92.apart.option.implementations.BooleanOption
import me.kl0udy92.apart.option.implementations.DoubleOption
import me.kl0udy92.apart.utils.entity.player.inventory.InventoryUtil
import me.kl0udy92.apart.utils.entity.player.inventory.ItemUtil
import me.kl0udy92.apart.utils.render.MouseUtil
import me.kl0udy92.apart.utils.system.Stopwatch
import net.ayataka.eventapi.annotation.EventListener
import net.minecraft.client.gui.inventory.GuiInventory

@ModuleData("AutoArmor", "×Ô¶¯´©¿ø¼×", ["ar"], "Auto Armor", Category.PLAYER, "Automatic sorting armor.")
class AutoArmorModule: Module() {

    val onlyInventoryOpenOption = BooleanOption("Only Inventory Open", "Sorting armor only when opening inventory.", true)
    val delayOption = DoubleOption("Delay", "Sorting delay.", 1.0, 0.0, 5.0, 0.1)

    val stopWatch = Stopwatch()

    @EventListener
    fun onUpdate(event: UpdateEvent) {
        val delay = this.delayOption.value * 1000
        this.suffix = delay
        if (this.onlyInventoryOpenOption.value && mc.currentScreen !is GuiInventory) return

        if (!this.stopWatch.elapsedWithReset(delay.toLong())) return

        for (type in 1..4) {
            if (mc.thePlayer.inventoryContainer.getSlot(4 + type).hasStack) {
                val itemStack = mc.thePlayer.inventoryContainer.getSlot(4 + type).stack
                if (ItemUtil.isBestArmor(itemStack, ItemUtil.getArmorTypeFromNumber(type)!!)) continue
                InventoryUtil.windowClick(4 + type, MouseUtil.ButtonType.RIGHT, InventoryUtil.ModeType.DROP)
            }
            for (i in 9..44) {
                if (!mc.thePlayer.inventoryContainer.getSlot(i).hasStack) continue
                val itemStack = mc.thePlayer.inventoryContainer.getSlot(i).stack
                if (!ItemUtil.isBestArmor(itemStack, ItemUtil.getArmorTypeFromNumber(type)!!) || ItemUtil.getProtectionLevel(itemStack) <= 0.0f) continue
                InventoryUtil.windowClick(i, MouseUtil.ButtonType.LEFT, InventoryUtil.ModeType.SHIFT_LEFT)
            }
        }
    }

}