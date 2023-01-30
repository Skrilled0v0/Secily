package me.kl0udy92.apart.utils.entity.player

import me.kl0udy92.apart.utils.MinecraftInstance
import net.minecraft.potion.Potion
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

object MoveUtil : MinecraftInstance() {

    val moving: Boolean
        get() = mc.thePlayer != null && (mc.thePlayer.movementInput.moveForward != 0f || mc.thePlayer.movementInput.moveStrafe != 0f)

    val direction: Float
        get() {
            // start with our current yaw
            var yaw = mc.thePlayer.rotationYaw
            var strafe = 45f
            // add 180 to the yaw to strafe backwards
            if (mc.thePlayer.moveForward < 0) {
                // invert our strafe to -45
                strafe = -45f
                yaw += 180f
            }
            if (mc.thePlayer.moveStrafing > 0) {
                // subtract 45 to strafe left forward
                yaw -= strafe
                // subtract an additional 45 if we do not press W in order to get to -90
                if (mc.thePlayer.moveForward == 0f) {
                    yaw -= 45f
                }
            } else if (mc.thePlayer.moveStrafing < 0) {
                // add 45 to strafe right forward
                yaw += strafe
                // add 45 if we do not press W in order to get to 90
                if (mc.thePlayer.moveForward == 0f) {
                    yaw += 45f
                }
            }
            return yaw
        }

    val currentSpeed: Double
        get() {
            return sqrt(mc.thePlayer.motionX * mc.thePlayer.motionX + mc.thePlayer.motionZ * mc.thePlayer.motionZ)
        }

    val baseMoveSpeed: Double
        get() {
            var baseSpeed = 0.2875
            if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
                baseSpeed *= 1.0 + 0.2 * PlayerUtil.speedModifier
            }
            return baseSpeed
        }

    fun strafe() {
        this.strafe(this.currentSpeed)
    }

    fun strafe(speed: Double) {
        val yaw = Math.toRadians(this.direction.toDouble())
        mc.thePlayer.motionZ = cos(yaw) * speed
        mc.thePlayer.motionX = -sin(yaw) * speed
    }

}