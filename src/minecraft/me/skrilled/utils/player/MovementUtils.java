/*
 *eclipse
 *Code by SkrilledSense
 *20230106
 */
package me.skrilled.utils.player;

import me.skrilled.utils.IMC;

public class MovementUtils implements IMC {

    public static double getSpeed() {
        return Math.sqrt(mc.thePlayer.motionX * mc.thePlayer.motionX + mc.thePlayer.motionZ * mc.thePlayer.motionZ);
    }

    public static void setSpeed(double speed) {
        mc.thePlayer.motionX = -Math.sin(getDirection()) * speed;
        mc.thePlayer.motionZ = Math.cos(getDirection()) * speed;
    }

    public static float getDirection() {
        float yaw = mc.thePlayer.rotationYaw;
        if (mc.thePlayer.moveForward < 0.0f) {
            yaw += 180.0f;
        }
        float forward = 1.0f;
        if (mc.thePlayer.moveForward < 0.0f) {
            forward = -0.5f;
        } else if (mc.thePlayer.moveForward > 0.0f) {
            forward = 0.5f;
        }
        if (mc.thePlayer.moveStrafing > 0.0f) {
            yaw -= 90.0f * forward;
        }
        if (mc.thePlayer.moveStrafing < 0.0f) {
            yaw += 90.0f * forward;
        }
        return yaw * 0.017453292f;
    }
}
