package me.kl0udy92.apart.utils.entity.player.inventory

import me.kl0udy92.apart.utils.MinecraftInstance
import me.kl0udy92.apart.utils.render.MouseUtil
import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemSword
import net.minecraft.item.ItemTool

object InventoryUtil: MinecraftInstance() {

    fun windowClick(slot: Int, buttonIn: Int, mode: Int) {
        mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot, buttonIn, mode, mc.thePlayer)
    }

    fun windowClick(slot: Int, buttonIn: MouseUtil.ButtonType, mode: ModeType) {
        mc.playerController.windowClick(
            mc.thePlayer.inventoryContainer.windowId,
            slot,
            buttonIn.identifier,
            mode.identifier,
            mc.thePlayer
        )
    }

    enum class ModeType(val identifier: Int) {
        LEFT(0), RIGHT(0), SHIFT_LEFT(1), MIDDLE(3), DROP(4), DOUBLE_CLICK_WITHOUT_SHIFT(6);
    }

}