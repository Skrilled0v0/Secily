package me.kl0udy92.apart.features.module.implementations.player

import me.kl0udy92.apart.events.entities.player.MotionEvent
import me.kl0udy92.apart.events.entities.player.UpdateEvent
import me.kl0udy92.apart.events.network.PacketEvent
import me.kl0udy92.apart.events.network.WorldLoadEvent
import me.kl0udy92.apart.features.module.Category
import me.kl0udy92.apart.features.module.Module
import me.kl0udy92.apart.features.module.annotation.ModuleData
import me.kl0udy92.apart.features.module.implementations.player.disablers.AbstractDisablerMode
import me.kl0udy92.apart.features.module.implementations.player.disablers.implementations.BlocksMCDisabler
import me.kl0udy92.apart.option.implementations.ArrayOption
import net.ayataka.eventapi.annotation.EventListener

/**
 * @author DeadAngels.
 * @since 2023/1/28
 **/
@ModuleData("Disabler", "½ûÓÃÆ÷", ["Patcher"], "Disabler", Category.PLAYER, "Cancel anti-cheat.")
class DisablerModule: Module() {

    val modes = mutableListOf<AbstractDisablerMode>()

    val modeOption = ArrayOption("Mode", "Switch anti-cheat mode.", "BlocksMC", arrayOf("BlocksMC"))

    init {
        this.modes.add(BlocksMCDisabler(this))
    }

    override fun onEnable() {
        this.modes.find { this.modeOption.value == it.key }!!.onEnable()
        super.onEnable()
    }

    override fun onDisable() {
        this.modes.find { this.modeOption.value == it.key }!!.onDisable()
        super.onEnable()
    }

    @EventListener
    fun onWorldLoad(event: WorldLoadEvent) {
        this.modes.find { this.modeOption.value == it.key }!!.onWorldLoad(event)
    }

    @EventListener
    fun onUpdate(event: UpdateEvent) {
        this.suffix = this.modeOption.value
        this.modes.find { this.modeOption.value == it.key }!!.onUpdate(event)
    }

    @EventListener
    fun onMotion(event: MotionEvent) {
        this.modes.find { this.modeOption.value == it.key }!!.onMotion(event)
    }

    @EventListener
    fun onPacket(event: PacketEvent) {
        this.modes.find { this.modeOption.value == it.key }!!.onPacket(event)
    }

}