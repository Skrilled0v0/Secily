package me.kl0udy92.apart.features.module.implementations.attack

import me.kl0udy92.apart.events.entities.player.UpdateEvent
import me.kl0udy92.apart.features.module.Category
import me.kl0udy92.apart.features.module.Module
import me.kl0udy92.apart.features.module.annotation.ModuleData
import me.kl0udy92.apart.option.implementations.DoubleOption
import net.ayataka.eventapi.annotation.EventListener

/**
 * @author DeadAngels.
 * @since 2023/1/19
 **/
@ModuleData("Reach", "Ôö¼Ó¹¥»÷¾àÀë", [""], "Reach", Category.ATTACK, "Increases your reach.")
class ReachModule: Module() {

    val rangeOption = DoubleOption("Range", "Range.", 4.0, 3.0, 6.0, 0.1)
    val blockRangeOption = DoubleOption("Block Range", "Block range.", 4.0, 3.0, 8.0, 0.1)
    val chanceOption = DoubleOption("Chance", "Increase chance.", 100.0, 1.0, 100.0, 1.0)

    @EventListener
    fun onUpdate(event: UpdateEvent) {
        this.suffix = this.rangeOption.value
    }

}