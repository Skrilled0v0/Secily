package me.kl0udy92.apart.utils.entity.player.inventory

import me.kl0udy92.apart.utils.MinecraftInstance
import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.item.ItemArmor
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemSword
import net.minecraft.item.ItemTool

object ItemUtil: MinecraftInstance() {

    fun isBestArmor(stack: ItemStack, type: ArmorType): Boolean {
        val prot: Float = this.getProtectionLevel(stack)
        val armorType = when (type) {
            ArmorType.HELMET -> type.identifier
            ArmorType.CHEST_PLACE -> type.identifier
            ArmorType.LEGGINGS -> type.identifier
            ArmorType.BOOTS -> type.identifier
        }
        if (!stack.unlocalizedName.contains(armorType)) {
            return false
        }
        for (i in 5..44) {
            val itemStack = mc.thePlayer.inventoryContainer.getSlot(i).stack
            if (!mc.thePlayer.inventoryContainer.getSlot(i).hasStack || this.getProtectionLevel(itemStack) <= prot || !itemStack.unlocalizedName.contains(armorType)) continue
            return false
        }
        return true
    }

    fun getProtectionLevel(stack: ItemStack): Float {
        var protectionLevel = 0.0f
        if (stack.item is ItemArmor) {
            val itemArmor = stack.item as ItemArmor
            protectionLevel =
                (protectionLevel.toDouble() + (itemArmor.damageReduceAmount.toDouble() + ((100 - itemArmor.damageReduceAmount) * EnchantmentHelper.getEnchantmentLevel(
                    Enchantment.protection.effectId,
                    stack
                )).toDouble() * 0.0075)).toFloat()
            protectionLevel = (protectionLevel.toDouble() + EnchantmentHelper.getEnchantmentLevel(
                Enchantment.blastProtection.effectId,
                stack
            ).toDouble() / 100.0).toFloat()
            protectionLevel = (protectionLevel.toDouble() + EnchantmentHelper.getEnchantmentLevel(
                Enchantment.fireProtection.effectId,
                stack
            ).toDouble() / 100.0).toFloat()
            protectionLevel =
                (protectionLevel.toDouble() + EnchantmentHelper.getEnchantmentLevel(Enchantment.thorns.effectId, stack)
                    .toDouble() / 100.0).toFloat()
            protectionLevel = (protectionLevel.toDouble() + EnchantmentHelper.getEnchantmentLevel(
                Enchantment.unbreaking.effectId,
                stack
            ).toDouble() / 50.0).toFloat()
            protectionLevel = (protectionLevel.toDouble() + EnchantmentHelper.getEnchantmentLevel(
                Enchantment.featherFalling.effectId,
                stack
            ).toDouble() / 100.0).toFloat()
        }
        return protectionLevel
    }

    fun getItemDamage(itemStack: ItemStack): Float {
        if (itemStack.item is ItemSword) {
            var damage = 4.0 + (itemStack.item as ItemSword).damageVsEntity
            damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, itemStack) * 1.25
            damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, itemStack) * 0.5
            return damage.toFloat()
        }
        if (itemStack.item is ItemTool) {
            var damage = (itemStack.item as ItemTool).damageVsEntity.toDouble()
            damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, itemStack) * 1.25
            damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, itemStack) * 0.5
            return damage.toFloat()
        }
        return 1.0f
    }

    enum class ArmorType(val identifier: String, val number: Int) {
        HELMET("helmet", 1),
        CHEST_PLACE("chestplate", 2),
        LEGGINGS("leggings", 3),
        BOOTS("boots", 4);
    }

    fun getArmorTypeFromNumber(number: Int): ArmorType? {
        when(number) {
            1-> return ArmorType.HELMET
            2 -> return ArmorType.CHEST_PLACE
            3 -> return ArmorType.LEGGINGS
            4 -> return ArmorType.BOOTS
        }
        return null
    }

}