package me.kl0udy92.apart.utils.system.world

import me.kl0udy92.apart.utils.system.MathUtil
import net.minecraft.util.EnumFacing
import net.minecraft.util.Vec3
import java.util.concurrent.ThreadLocalRandom

/**
 * @author DeadAngels.
 * @since 2023/1/21
 **/
object BlockUtil {

    fun getVec3FromBlockData(blockData: BlockData): Vec3 {
        val pos = blockData.pos
        val face = blockData.face
        var x = (pos.x + 0.5f).toDouble()
        var y = (pos.y + 0.5f).toDouble()
        var z = (pos.z + 0.5f).toDouble()

        x += face.frontOffsetX / 2.0
        z += face.frontOffsetZ / 2.0
        y += face.frontOffsetY / 2.0
        if (face == EnumFacing.UP || face == EnumFacing.DOWN) {
            x += MathUtil.randomDouble(0.3, -0.3)
            z += MathUtil.randomDouble(0.3, -0.3)
        } else {
            y += MathUtil.randomDouble(0.49, 0.5)
        }
        if (face == EnumFacing.WEST || face == EnumFacing.EAST) {
            z += MathUtil.randomDouble(0.3, -0.3)
        }
        if (face == EnumFacing.SOUTH || face == EnumFacing.NORTH) {
            x += MathUtil.randomDouble(0.3, -0.3)
        }
        return Vec3(x, y, z)
    }

}