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
@ModuleData("Hitboxes", "Åö×²Ïä", ["Hitbox"], "Hitboxes", Category.ATTACK, "Increase the hit box.")
class HitboxesModule: Module() {

    val sizeOption = DoubleOption("Size", "Hit box size.", 0.4, 0.1, 1.0, 0.1)
    val chanceOption = DoubleOption("Chance", "Increase chance.", 100.0, 1.0, 100.0, 1.0)

    @EventListener
    fun onUpdate(event: UpdateEvent) {
        this.suffix = this.sizeOption.value
    }

}