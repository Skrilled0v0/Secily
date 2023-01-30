package me.kl0udy92.apart.features.module.implementations.move

import me.kl0udy92.apart.events.entities.player.UpdateEvent
import me.kl0udy92.apart.features.module.Category
import me.kl0udy92.apart.features.module.Module
import me.kl0udy92.apart.features.module.annotation.ModuleData
import me.kl0udy92.apart.option.implementations.BooleanOption
import me.kl0udy92.apart.option.implementations.DoubleOption
import me.kl0udy92.apart.utils.entity.player.MoveUtil
import net.ayataka.eventapi.annotation.EventListener

@ModuleData("Timer", "变速齿轮", ["Boost"], "Timer", Category.MOVE, "Change timer speed.")
class TimerModule : Module() {

    val speedOption = DoubleOption("Speed", "Timer speed.", 2.0, 0.1, 3.0, 0.1)
    val onlyMoveOption = BooleanOption("Only Move", "Change the timer speed only while moving.", false)

    override fun onDisable() {
        mc.timer.timerSpeed = 1.0f
        super.onDisable()
    }

    @EventListener
    fun onUpdate(event: UpdateEvent) {
        this.suffix = this.speedOption.value
        if (this.onlyMoveOption.value) {
            if (MoveUtil.moving) mc.timer.timerSpeed = this.speedOption.value.toFloat() else mc.timer.timerSpeed =
                1f
        } else mc.timer.timerSpeed = this.speedOption.value.toFloat()
    }

}