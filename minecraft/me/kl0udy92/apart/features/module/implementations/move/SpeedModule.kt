package me.kl0udy92.apart.features.module.implementations.move

import me.kl0udy92.apart.core.Main
import me.kl0udy92.apart.events.entities.player.MotionEvent
import me.kl0udy92.apart.events.network.PacketEvent
import me.kl0udy92.apart.features.module.Category
import me.kl0udy92.apart.features.module.Module
import me.kl0udy92.apart.features.module.annotation.ModuleData
import me.kl0udy92.apart.option.implementations.ArrayOption
import me.kl0udy92.apart.option.implementations.BooleanOption
import me.kl0udy92.apart.option.implementations.DoubleOption
import me.kl0udy92.apart.screen.notification.Notification
import me.kl0udy92.apart.screen.notification.NotificationType
import me.kl0udy92.apart.utils.entity.player.MoveUtil
import net.ayataka.eventapi.annotation.EventListener
import net.ayataka.eventapi.enumeration.EventDirection
import net.minecraft.network.play.server.S08PacketPlayerPosLook

@ModuleData("Speed", "加速", ["Bhop", "BunnyHop"], "Speed", Category.MOVE, "Let you move faster.")
class SpeedModule : Module() {

    val modeOption = ArrayOption("Mode", "Switch mode.", "NCP", arrayOf("NCP", "Custom"))
    val lagBackCheckOption = BooleanOption("Lag back Check", "Automatically disable when the player is lag back.", true)
    val motionOption = DoubleOption("Motion-Y", "Motion Y value.", 0.42, 0.1, 0.5, 0.02) {
        this.modeOption.value == "Custom"
    }
    val speedOption = DoubleOption("Speed", "Speed value.", 0.31, 0.1, 1.0, 0.02) {
        this.modeOption.value == "Custom"
    }

    @EventListener
    fun onMotion(event: MotionEvent) {
        this.suffix = this.modeOption.value
        when (this.modeOption.value) {
            "NCP" -> {
                if (MoveUtil.moving) {
                    if (mc.thePlayer.onGround) {
                        mc.thePlayer.motionY = 0.42
                    } else {
                        MoveUtil.strafe(MoveUtil.baseMoveSpeed)
                    }
                }
            }
            "Custom" -> {
                if (MoveUtil.moving) {
                    if (mc.thePlayer.onGround) {
                        mc.thePlayer.motionY = this.motionOption.value
                    } else {
                        MoveUtil.strafe(this.speedOption.value)
                    }
                }
            }
        }
    }

    @EventListener
    fun onPacket(event: PacketEvent) {
        when (event.direction) {
            EventDirection.INCOMING -> {
                if (this.lagBackCheckOption.value) {
                    if (event.packet is S08PacketPlayerPosLook) {
                        Main.notificationManager.notifications.add(
                            Notification(
                                "Lag back.",
                                "Detects that your lag back has automatically disabled.",
                                NotificationType.WARNING
                            )
                        )
                        this.toggle()
                    }
                }
            }
            EventDirection.OUTGOING -> {
                //Nothing todo.
            }
        }
    }

}