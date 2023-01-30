package me.kl0udy92.apart.utils.render.particles

import net.minecraft.block.Block
import net.minecraft.client.Minecraft
import net.minecraft.entity.EntityLivingBase
import net.minecraft.util.BlockPos

class Location {

    var x: Double
    var y: Double
    var z: Double
    var yaw: Float
    var pitch: Float

    constructor(x: Double, y: Double, z: Double, yaw: Float, pitch: Float) {
        this.x = x
        this.y = y
        this.z = z
        this.yaw = yaw
        this.pitch = pitch
    }

    constructor(x: Double, y: Double, z: Double) {
        this.x = x
        this.y = y
        this.z = z
        yaw = 0.0f
        pitch = 0.0f
    }

    constructor(pos: BlockPos) {
        x = pos.x.toDouble()
        y = pos.y.toDouble()
        z = pos.z.toDouble()
        yaw = 0.0f
        pitch = 0.0f
    }

    constructor(x: Int, y: Int, z: Int) {
        this.x = x.toDouble()
        this.y = y.toDouble()
        this.z = z.toDouble()
        yaw = 0.0f
        pitch = 0.0f
    }

    constructor(entity: EntityLivingBase) {
        x = entity.posX
        y = entity.posY
        z = entity.posZ
        yaw = 0.0f
        pitch = 0.0f
    }

    fun add(x: Int, y: Int, z: Int): Location {
        this.x += x.toDouble()
        this.y += y.toDouble()
        this.z += z.toDouble()
        return this
    }

    fun add(x: Double, y: Double, z: Double): Location {
        this.x += x
        this.y += y
        this.z += z
        return this
    }

    fun subtract(x: Int, y: Int, z: Int): Location {
        this.x -= x.toDouble()
        this.y -= y.toDouble()
        this.z -= z.toDouble()
        return this
    }

    fun subtract(x: Double, y: Double, z: Double): Location {
        this.x -= x
        this.y -= y
        this.z -= z
        return this
    }

    val block: Block
        get() = Minecraft.getMinecraft().theWorld.getBlockState(toBlockPos()).block

    fun setX(x: Double): Location {
        this.x = x
        return this
    }

    fun setY(y: Double): Location {
        this.y = y
        return this
    }

    fun setZ(z: Double): Location {
        this.z = z
        return this
    }

    fun setYaw(yaw: Float): Location {
        this.yaw = yaw
        return this
    }

    fun setPitch(pitch: Float): Location {
        this.pitch = pitch
        return this
    }

    fun toBlockPos(): BlockPos {
        return BlockPos(x, y, z)
    }

    fun distanceTo(loc: Location): Double {
        val dx = loc.x - x
        val dz = loc.z - z
        val dy = loc.y - y
        return Math.sqrt(dx * dx + dy * dy + dz * dz)
    }

    fun distanceToY(loc: Location): Double {
        val dy = loc.y - y
        return Math.sqrt(dy * dy)
    }

    companion object {
        fun fromBlockPos(blockPos: BlockPos): Location {
            return Location(blockPos.x, blockPos.y, blockPos.z)
        }
    }
}
