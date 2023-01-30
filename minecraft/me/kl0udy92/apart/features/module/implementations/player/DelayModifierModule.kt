package me.kl0udy92.apart.features.module.implementations.player

import me.kl0udy92.apart.events.system.TickEvent
import me.kl0udy92.apart.features.module.Category
import me.kl0udy92.apart.features.module.Module
import me.kl0udy92.apart.features.module.annotation.ModuleData
import me.kl0udy92.apart.option.implementations.BooleanOption
import me.kl0udy92.apart.option.implementations.DoubleOption
import me.kl0udy92.apart.option.implementations.MultiBooleanOption
import net.ayataka.eventapi.annotation.EventListener
import net.minecraft.item.ItemBlock
import net.minecraft.item.ItemMonsterPlacer
import net.minecraft.item.ItemPotion

@ModuleData("DelayModifier", "间隔更改", ["NoDelay", "DelayRemover"], "Delay Modifier", Category.PLAYER, "Modify some niggers.")
class DelayModifierModule : Module() {

    val delayFilterOption = MultiBooleanOption("Delay Filter", "Remove some niggers...", arrayOf(
        BooleanOption("Left clicks", "Remove left clicks delay.", true),
        BooleanOption("Right clicks", "Modify right clicks delay.", true),
        BooleanOption("Jump ticks", "Remove left ticks delay.", true)
    ))

    val filterOption = MultiBooleanOption("Item Filter", "Only right clicks.", arrayOf(
        BooleanOption("Spawn Eggs", "Spawn Eggs.", true),
        BooleanOption("Potions", "Potions.", true),
        BooleanOption("Blocks", "Blocks.", true)
    )) { this.delayFilterOption.find("Right clicks")!!.value }

    val rightClicksDelay = DoubleOption("Right Delay", "Only right clicks.", 0.0, 0.0, 4.0, 1.0) { this.delayFilterOption.find("Right clicks")!!.value }

    val jumpTicksDelay = DoubleOption("Jump Ticks", "Only jump ticks.", 0.0, 0.0, 10.0, 1.0) { this.delayFilterOption.find("Jump ticks")!!.value }

    @EventListener
    fun onTick(event: TickEvent) {
        if (this.delayFilterOption.find("Left clicks")!!.value) mc.leftClickCounter = 0
        if (this.delayFilterOption.find("Right clicks")!!.value) {
            if (mc.thePlayer != null) {
                if ((mc.thePlayer.heldItem.item is ItemPotion && this.filterOption.find("Potions")!!.value)
                    || (mc.thePlayer.heldItem.item is ItemBlock && this.filterOption.find("Blocks")!!.value)
                    || (mc.thePlayer.heldItem.item is ItemMonsterPlacer && this.filterOption.find("Spawn Eggs")!!.value)
                ) {
                    mc.rightClickDelayTimer = mc.rightClickDelayTimer.coerceAtMost(this.rightClicksDelay.value.toInt())
                }
            }
        }
    }

}