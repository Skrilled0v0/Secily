package me.kl0udy92.apart.features.module.implementations.player

import me.kl0udy92.apart.events.network.PacketEvent
import me.kl0udy92.apart.features.module.Category
import me.kl0udy92.apart.features.module.Module
import me.kl0udy92.apart.features.module.annotation.ModuleData
import me.kl0udy92.apart.option.implementations.BooleanOption
import me.kl0udy92.apart.utils.entity.player.inventory.ItemUtil
import me.kl0udy92.apart.utils.render.MouseUtil
import net.ayataka.eventapi.annotation.EventListener
import net.ayataka.eventapi.enumeration.EventDirection
import net.minecraft.network.play.client.C02PacketUseEntity
import org.lwjgl.input.Mouse

/**
 * @author DeadAngels.
 * @since 2023/1/18
 **/
@ModuleData("AutoTools", "自动切换工具", ["AutoTool"], "Auto Tools", Category.PLAYER, "Automatically select the best tool/weapon for the current action from hotbar.")
class AutoToolsModule: Module() {
    
    val weaponOption = BooleanOption("Weapon", "Automatically choose your weapon when you fight.", true)

    val bestWeaponSlotFromHotbar: Int
        get() {
            val originalSlot = mc.thePlayer.inventory.currentItem
            var weaponSlot = -1
            var weaponDamage = 1.0f
            for (slot in 0..8) {
                mc.thePlayer.inventory.currentItem = slot
                val itemStack = mc.thePlayer.inventory.mainInventory[slot]
                if (itemStack != null) {
                    val damage = ItemUtil.getItemDamage(itemStack)
                    if (damage > weaponDamage) {
                        weaponDamage = damage
                        weaponSlot = slot
                    }
                }
            }

            return if (weaponSlot != -1) {
                weaponSlot
            } else originalSlot
        }

    val bestToolSlotFromHotbar: Int
        get() {
            val block = if (mc.objectMouseOver != null && mc.objectMouseOver.blockPos != null) mc.theWorld.getBlockState(mc.objectMouseOver.blockPos).block else null

            val originalSlot = mc.thePlayer.inventory.currentItem
            var toolSlot = -1
            var toolStrength = 1.0f
            if (block != null) {
                for (slot in 0..8) {
                    val itemStack = mc.thePlayer.inventory.mainInventory[slot]
                    if (itemStack != null && itemStack.getStrVsBlock(block) > toolStrength) {
                        toolStrength = itemStack.getStrVsBlock(block)
                        toolSlot = slot
                    }
                }
            }

            return if (toolSlot != -1) {
                toolSlot
            } else originalSlot
        }

    @EventListener
    fun onPacket(event: PacketEvent) {
        when (event.direction) {
            EventDirection.INCOMING -> {
                //Nothing todo.
            }
            EventDirection.OUTGOING -> {
                if (this.weaponOption.value) {
                    if (event.packet is C02PacketUseEntity) {
                        if ((event.packet as C02PacketUseEntity).action == C02PacketUseEntity.Action.ATTACK) {
                            if (mc.currentScreen != null) return
                            mc.thePlayer.inventory.currentItem = this.bestWeaponSlotFromHotbar
                            mc.playerController.updateController()
                        }
                    }
                }

                if (Mouse.isButtonDown(MouseUtil.ButtonType.LEFT.identifier)) {
                    if (mc.currentScreen != null) return
                    mc.thePlayer.inventory.currentItem = this.bestToolSlotFromHotbar
                    mc.playerController.updateController()
                }
            }
        }
    }
    
}