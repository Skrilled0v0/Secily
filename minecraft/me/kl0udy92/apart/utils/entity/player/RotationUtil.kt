package me.kl0udy92.apart.utils.entity.player

import me.kl0udy92.apart.utils.MinecraftInstance
import me.kl0udy92.apart.utils.system.MathUtil
import net.minecraft.entity.Entity
import net.minecraft.entity.projectile.EntitySnowball
import net.minecraft.util.BlockPos
import net.minecraft.util.MathHelper
import kotlin.math.atan2
import kotlin.reflect.jvm.internal.impl.builtins.StandardNames.FqNames.target


object RotationUtil : MinecraftInstance() {

    fun getRotations(x: Double, y: Double, z: Double): FloatArray {
        val xDiff = x - mc.thePlayer.posX
        val yDiff = y - (mc.thePlayer.posY + mc.thePlayer.eyeHeight)
        val zDiff = z - mc.thePlayer.posZ
        val dist = MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff).toDouble()
        val yaw = Math.toDegrees(atan2(zDiff, xDiff)).toFloat() - 90.0f
        val pitch = -Math.toDegrees(atan2(yDiff, dist)).toFloat()
        return floatArrayOf(yaw, pitch)
    }

    fun getRotations2(entity: Entity): FloatArray {
        val xDiff = entity.posX - mc.thePlayer.posX
        val yDiff = entity.posY - mc.thePlayer.posY - 0.2
        val zDiff = entity.posZ - mc.thePlayer.posZ
        val dist = MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff).toDouble()
        val yaw = (atan2(zDiff, xDiff) * 180 / Math.PI).toFloat() - 90f
        val pitch = (-atan2(yDiff, dist) * 180 / Math.PI).toFloat()
        return floatArrayOf(
            mc.thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float(yaw - mc.thePlayer.rotationYaw),
            mc.thePlayer.rotationPitch + MathHelper.wrapAngleTo180_float(pitch - mc.thePlayer.rotationPitch)
        )
    }

    fun getRotationsRandomize(entity: Entity): FloatArray {
        val xDiff = entity.posX - mc.thePlayer.posX
        val yDiff = entity.posY - mc.thePlayer.posY - 0.2
        val zDiff = entity.posZ - mc.thePlayer.posZ
        val dist = MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff).toDouble()
        val yaw = (atan2(zDiff, xDiff) * 180 / Math.PI).toFloat() - 90f
        val pitch = (-atan2(yDiff, dist) * 180 / Math.PI).toFloat()
        return floatArrayOf(
            mc.thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float(yaw - mc.thePlayer.rotationYaw) + MathUtil.randomFloat(5F, 10F),
            mc.thePlayer.rotationPitch + MathHelper.wrapAngleTo180_float(pitch - mc.thePlayer.rotationPitch) + MathUtil.randomFloat(5F, 10F)
        )
    }
    
    fun getRotationsFromEntity(entity: Entity): FloatArray {
        return this.getRotations(entity.posX, entity.posY + entity.eyeHeight - 0.4, entity.posZ)
    }

    fun getRotationsFromBlockPos(blockPos: BlockPos): FloatArray {
        val tempEntity = EntitySnowball(mc.theWorld)
        tempEntity.posX = blockPos.x + 0.5
        tempEntity.posY = blockPos.y + 0.5
        tempEntity.posZ = blockPos.z + 0.5
        return this.getRotationsFromEntity(tempEntity)
    }

}