package me.kl0udy92.apart.features.module.implementations.world

import me.kl0udy92.apart.events.entities.player.UpdateEvent
import me.kl0udy92.apart.events.network.PacketEvent
import me.kl0udy92.apart.features.module.Category
import me.kl0udy92.apart.features.module.Module
import me.kl0udy92.apart.features.module.annotation.ModuleData
import me.kl0udy92.apart.option.implementations.ArrayOption
import me.kl0udy92.apart.option.implementations.DoubleOption
import me.kl0udy92.apart.utils.system.packet.enums.EnumEffects
import net.ayataka.eventapi.annotation.EventListener
import net.ayataka.eventapi.enumeration.EventDirection
import net.minecraft.network.play.server.S2BPacketChangeGameState

@ModuleData("Weather", "天气", ["weatherchanger"], "Weather", Category.WORLD, "Allows you to control the weather.")
class WeatherModule : Module() {

    val modeOption = ArrayOption("Mode", "Switch mode.", "Snowy", arrayOf("Clear", "Rainy", "Thunder", "Snowy"))
    val strengthOption = DoubleOption("Strength", "Weather strength.", 1.0, 0.0, 1.0, 0.1)

    override fun onDisable() {
        if (mc.theWorld != null) {
            mc.theWorld.setRainStrength(0f)
            mc.theWorld.setThunderStrength(0f)
        }
        super.onDisable()
    }

    @EventListener
    fun onUpdate(event: UpdateEvent) {
        this.suffix = this.modeOption.value
        when (this.modeOption.value) {
            "Clear" -> {
                mc.theWorld.setRainStrength(0f)
                mc.theWorld.setThunderStrength(0f)
            }
            "Rainy" -> {
                mc.theWorld.setRainStrength(this.strengthOption.value.toFloat())
                mc.theWorld.setThunderStrength(0f)
            }
            else -> {
                mc.theWorld.setRainStrength(this.strengthOption.value.toFloat())
                mc.theWorld.setThunderStrength(this.strengthOption.value.toFloat())
            }
        }
    }

    @EventListener
    fun onPacket(event: PacketEvent) {
        if (event.direction == EventDirection.INCOMING) {
            if (event.packet is S2BPacketChangeGameState) {
                val packet = event.packet as S2BPacketChangeGameState
                if (packet.gameState == EnumEffects.RAIN_LEVEL_CHANGE.identifier || packet.gameState == EnumEffects.THUNDER_LEVEL_CHANGE.identifier) {
                    event.cancel()
                }
            }
        }
    }

}