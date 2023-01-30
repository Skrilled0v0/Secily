package me.kl0udy92.apart.features.module.implementations.attack

import me.kl0udy92.apart.events.entities.player.UpdateEvent
import me.kl0udy92.apart.events.system.TickEvent
import me.kl0udy92.apart.features.module.Category
import me.kl0udy92.apart.features.module.Module
import me.kl0udy92.apart.features.module.annotation.ModuleData
import me.kl0udy92.apart.option.implementations.BooleanOption
import me.kl0udy92.apart.option.implementations.DoubleOption
import me.kl0udy92.apart.utils.render.MouseUtil
import me.kl0udy92.apart.utils.system.Stopwatch
import net.ayataka.eventapi.annotation.EventListener
import net.minecraft.client.settings.KeyBinding
import net.minecraft.util.MovingObjectPosition
import org.lwjgl.input.Mouse
import java.util.concurrent.ThreadLocalRandom

/**
 * @author DeadAngels.
 * @since 2023/1/17
 **/
@ModuleData("AutoClicker", "Á¬µãÆ÷", ["Clicker", "Hammer"], "Auto Clicker", Category.ATTACK, "Automatically clicks when you hold down left click.")
class AutoClickerModule: Module() {

    val ignoreFacingBlocksOption = BooleanOption("Ignore facing blocks", "Ignore facing blocks when you clicking.", true)

    val minCpsOption = DoubleOption("Min CPS", "Min CPS.", 8.0, 1.0, 20.0, 0.1)
    val maxCpsOption = DoubleOption("Max CPS", "Max CPS.", 9.0, 1.0, 20.0, 0.1)

    val stopWatch = Stopwatch()

    @EventListener
    fun onTick(event: TickEvent) {
        this.cpsComparator()
    }

    @EventListener
    fun onUpdate(event: UpdateEvent) {
        if (Mouse.isButtonDown(MouseUtil.ButtonType.LEFT.identifier)) {
            if (mc.currentScreen != null) return
            val clickKeycode = mc.gameSettings.keyBindAttack.keyCode
            if (mc.objectMouseOver == null || (mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && !this.ignoreFacingBlocksOption.value)) return else {
                KeyBinding.setKeyBindState(clickKeycode, true)
            }
            val randomizeCps = ThreadLocalRandom.current().nextDouble(this.minCpsOption.value, this.maxCpsOption.value).toLong()
            val clickDelay = 1000L / randomizeCps
            this.suffix = "${clickDelay}ms"
            if (this.stopWatch.elapsedWithReset(clickDelay)) {
                KeyBinding.setKeyBindState(clickKeycode, true)
                KeyBinding.onTick(clickKeycode)
            }else {
                KeyBinding.setKeyBindState(clickKeycode, false)
            }
        }else this.suffix = "0ms"
    }

    private fun cpsComparator() {
        if (this.minCpsOption.value >= this.maxCpsOption.max) {
            this.maxCpsOption.value = this.maxCpsOption.max
            this.minCpsOption.value = this.maxCpsOption.value - this.maxCpsOption.increment
        }
        if (this.minCpsOption.value >= this.maxCpsOption.value) this.maxCpsOption.value = this.minCpsOption.value + this.maxCpsOption.increment
    }
    
}