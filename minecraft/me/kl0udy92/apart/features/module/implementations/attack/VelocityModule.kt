package me.kl0udy92.apart.features.module.implementations.attack

import me.kl0udy92.apart.events.entities.player.UpdateEvent
import me.kl0udy92.apart.events.network.PacketEvent
import me.kl0udy92.apart.features.module.Category
import me.kl0udy92.apart.features.module.Module
import me.kl0udy92.apart.features.module.annotation.ModuleData
import me.kl0udy92.apart.option.implementations.BooleanOption
import me.kl0udy92.apart.option.implementations.DoubleOption
import net.ayataka.eventapi.annotation.EventListener
import net.ayataka.eventapi.enumeration.EventDirection
import net.minecraft.network.play.server.S12PacketEntityVelocity
import net.minecraft.network.play.server.S27PacketExplosion

@ModuleData("Velocity", "反击退", ["AntiKB", "kb", "knockback"], "Velocity", Category.ATTACK, "Anti knock back.")
class VelocityModule : Module() {

    val horizontalOption = DoubleOption("Horizontal", "Horizontal value.", 0.0, 0.0, 100.0, 1.0)
    val verticalOption = DoubleOption("Vertical", "Vertical value.", 0.0, 0.0, 100.0, 1.0)
    val explosionOtion = BooleanOption("Explosion", "Explosion proof knockback.", true)

    @EventListener
    fun onUpdate(event: UpdateEvent) {
        this.suffix =
            if (this.horizontalOption.value == 0.0 && this.verticalOption.value == 0.0) "Cancel" else "${this.horizontalOption.value}% ${this.verticalOption.value}%"
    }

    @EventListener
    fun onPacket(event: PacketEvent) {
        when (event.direction) {
            EventDirection.INCOMING -> {
                if (event.packet is S12PacketEntityVelocity) {
                    if ((event.packet as S12PacketEntityVelocity).entityID == mc.thePlayer.entityId) {
                        if (!(this.horizontalOption.value == 0.0 && this.verticalOption.value == 0.0)) {
                            (event.packet as S12PacketEntityVelocity).motionX =
                                ((event.packet as S12PacketEntityVelocity).motionX * this.horizontalOption.value.toInt() / 100)
                            (event.packet as S12PacketEntityVelocity).motionY =
                                ((event.packet as S12PacketEntityVelocity).motionY * this.verticalOption.value.toInt() / 100)
                            (event.packet as S12PacketEntityVelocity).motionZ =
                                ((event.packet as S12PacketEntityVelocity).motionZ * this.horizontalOption.value.toInt() / 100)
                        } else event.canceled = true
                    }
                }
                if (event.packet is S27PacketExplosion && this.explosionOtion.value) {
                    if (!(this.horizontalOption.value == 0.0 && this.verticalOption.value == 0.0)) {
                        (event.packet as S27PacketExplosion).x =
                            ((event.packet as S27PacketExplosion).x * this.horizontalOption.value.toInt() / 100)
                        (event.packet as S27PacketExplosion).y =
                            ((event.packet as S27PacketExplosion).y * this.horizontalOption.value.toInt() / 100)
                        (event.packet as S27PacketExplosion).z =
                            ((event.packet as S27PacketExplosion).z * this.horizontalOption.value.toInt() / 100)
                    } else event.canceled = true
                }
            }
            EventDirection.OUTGOING -> { /*Nothing here.*/
            }
        }
    }

}