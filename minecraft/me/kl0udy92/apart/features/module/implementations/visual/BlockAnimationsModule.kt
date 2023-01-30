package me.kl0udy92.apart.features.module.implementations.visual

import me.kl0udy92.apart.events.entities.player.UpdateEvent
import me.kl0udy92.apart.features.module.Category
import me.kl0udy92.apart.features.module.Module
import me.kl0udy92.apart.features.module.annotation.ModuleData
import me.kl0udy92.apart.option.implementations.ArrayOption
import me.kl0udy92.apart.option.implementations.BooleanOption
import net.ayataka.eventapi.annotation.EventListener

@ModuleData("BlockAnimations", "防砍动画", ["Animations", "anim"], "Block Animations", Category.VISUAL, "Block animations.")
class BlockAnimationsModule : Module() {

    val blockHitOption = BooleanOption("Block Hit", "1.7 block hit.", true)
    val modeOption =
        ArrayOption("Mode", "Switch block-anim mode.", "Old", arrayOf("Old", "1.8", "Sigma", "Jello", "Exhibobo"))

    @EventListener
    fun onUpdate(event: UpdateEvent) {
        this.suffix = this.modeOption.value
    }

}