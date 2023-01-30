package me.kl0udy92.apart.utils.entity.player

import me.kl0udy92.apart.utils.MinecraftInstance
import net.minecraft.block.Block
import net.minecraft.block.BlockAir
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemBucketMilk
import net.minecraft.item.ItemFood
import net.minecraft.item.ItemPotion
import net.minecraft.potion.Potion
import net.minecraft.potion.PotionEffect
import net.minecraft.util.BlockPos

object PlayerUtil : MinecraftInstance() {

    val underBlock: Block
        get() {
            return mc.theWorld.getBlockState(
                BlockPos(
                    mc.thePlayer.posX,
                    mc.thePlayer.posY - 1,
                    mc.thePlayer.posZ,
                )
            ).block
        }

    val blockUnder: Boolean
        get() {
            for (i in (mc.thePlayer.posY - 1.0).toInt() downTo 1) {
                val pos = BlockPos(mc.thePlayer.posX, i.toDouble(), mc.thePlayer.posZ)
                if (mc.theWorld.getBlockState(pos).block !is BlockAir) {
                    return true
                }
            }
            return false
        }

    val usingFood: Boolean
        get() {
            val usingItem = mc.thePlayer.itemInUse.item
            return mc.thePlayer.isUsingItem && (usingItem is ItemFood || usingItem is ItemBucketMilk || usingItem is ItemPotion)
        }

    val jumpBoostModifier: Int
        get() {
            val effect = mc.thePlayer.getActivePotionEffect(Potion.jump)
            return if (effect != null) effect.amplifier + 1 else 0
        }

    val speedModifier: Int
        get() {
            val effect = mc.thePlayer.getActivePotionEffect(Potion.moveSpeed)
            return if (effect != null) effect.amplifier + 1 else 0
        }

    fun isOnGround(height: Double): Boolean {
        return mc.theWorld.getCollidingBoundingBoxes(
            mc.thePlayer,
            mc.thePlayer.entityBoundingBox.offset(0.0, -height, 0.0)
        ).isNotEmpty()
    }

    fun checkPing(player: EntityPlayer): Boolean {
        return mc.netHandler.getPlayerInfo(player.uniqueID) != null && mc.netHandler
            .getPlayerInfo(player.uniqueID).responseTime >= 1
    }

}