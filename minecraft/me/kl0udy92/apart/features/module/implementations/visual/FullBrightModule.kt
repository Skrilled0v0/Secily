package me.kl0udy92.apart.features.module.implementations.visual

import me.kl0udy92.apart.events.entities.player.UpdateEvent
import me.kl0udy92.apart.features.module.Category
import me.kl0udy92.apart.features.module.Module
import me.kl0udy92.apart.features.module.annotation.ModuleData
import me.kl0udy92.apart.option.implementations.ArrayOption
import net.ayataka.eventapi.annotation.EventListener
import net.minecraft.potion.Potion
import net.minecraft.potion.PotionEffect

@ModuleData("FullBright", "านสำ", ["Brightness"], "Full Bright", Category.VISUAL, "Make you bright.")
class FullBrightModule: Module() {

    private val modeOption = ArrayOption("Mode", "Switch mode.", "Potion", arrayOf("Gamma", "Potion"))
    private var normalGamma = -1f;

    override fun onEnable() {
        this.normalGamma = mc.gameSettings.gammaSetting;
        super.onEnable()
    }

    @EventListener
    fun onUpdate(event: UpdateEvent) {
        this.suffix = this.modeOption.value
        when (this.modeOption.value) {
            "Gamma" -> mc.gameSettings.gammaSetting = 1000f
            "Potion" -> mc.thePlayer.addPotionEffect(PotionEffect(Potion.nightVision.id, 99999, 10))
        }
    }

    override fun onDisable() {
        if (mc.theWorld != null) {
            when (this.modeOption.value) {
                "Gamma" -> mc.gameSettings.gammaSetting = this.normalGamma
                "Potion" -> mc.thePlayer.removePotionEffect(Potion.nightVision.id)
            }
        }
        super.onDisable()
    }

}