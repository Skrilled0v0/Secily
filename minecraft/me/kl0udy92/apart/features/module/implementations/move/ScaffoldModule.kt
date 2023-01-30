package me.kl0udy92.apart.features.module.implementations.move

import me.kl0udy92.apart.core.Main
import me.kl0udy92.apart.events.entities.player.MotionEvent
import me.kl0udy92.apart.events.visual.Render2DEvent
import me.kl0udy92.apart.features.module.Category
import me.kl0udy92.apart.features.module.Module
import me.kl0udy92.apart.features.module.annotation.ModuleData
import me.kl0udy92.apart.features.module.implementations.visual.HUDModule
import me.kl0udy92.apart.option.implementations.ArrayOption
import me.kl0udy92.apart.option.implementations.BooleanOption
import me.kl0udy92.apart.option.implementations.DoubleOption
import me.kl0udy92.apart.utils.entity.player.MoveUtil
import me.kl0udy92.apart.utils.entity.player.PlayerUtil
import me.kl0udy92.apart.utils.entity.player.RotationUtil
import me.kl0udy92.apart.utils.render.RenderUtil
import me.kl0udy92.apart.utils.render.animations.easing.Animation
import me.kl0udy92.apart.utils.render.animations.easing.util.Easings
import me.kl0udy92.apart.utils.system.Stopwatch
import me.kl0udy92.apart.utils.system.world.BlockData
import me.kl0udy92.apart.utils.system.world.BlockUtil
import net.ayataka.eventapi.annotation.EventListener
import net.ayataka.eventapi.enumeration.EventState
import net.minecraft.block.Block
import net.minecraft.block.BlockAir
import net.minecraft.init.Blocks
import net.minecraft.item.Item
import net.minecraft.item.ItemBlock
import net.minecraft.network.play.client.C0APacketAnimation
import net.minecraft.util.BlockPos
import net.minecraft.util.EnumFacing
import org.lwjgl.input.Keyboard
import java.awt.Color

/**
 * @author DeadAngels.
 * @since 2023/1/21
 **/
@ModuleData("Scaffold", "×Ô¶¯´îÂ·", ["BlockFly"], "Scaffold", Category.MOVE, "Automatically place blocks under your feet.")
class ScaffoldModule: Module() {

    val placeDelayOption = DoubleOption("Place Delay", "Place the delay of the block.", 0.0, 0.0, 1000.0, 10.0)
    val placeMethodOption = ArrayOption("Place Method", "Switch place blocks method.", "Post", arrayOf("Pre", "Post"))
    val swingOption = BooleanOption("Swing", "Swing item when block placed.", true)
    val towerOption = BooleanOption("Tower", "Tower.", true)
    val towerMoveOption = BooleanOption("Tower Move", "Make you move when towering.", true) {
        this.towerOption.value
    }
    val noSprintOption = BooleanOption("No Sprint", "No sprint when block placed", true)
    val rotationsViewableOption = BooleanOption("Rotations Viewable", "Make rotations visible.", true)

    val counterOption = BooleanOption("Counter", "Blocks counter.", true)
    val textCounterOption = BooleanOption("Text", "Displays counters in text.", true) {
        this.counterOption.value
    }
    val barCounterOption = BooleanOption("Bar", "Displays counters in bar.", true) {
        this.counterOption.value
    }

    val invalidBlocks: MutableList<Block>
        get() = mutableListOf(Blocks.redstone_wire, Blocks.tallgrass, Blocks.redstone_torch, Blocks.enchanting_table, Blocks.furnace, Blocks.carpet, Blocks.crafting_table, Blocks.trapped_chest, Blocks.chest, Blocks.dispenser, Blocks.air, Blocks.water, Blocks.lava, Blocks.flowing_water, Blocks.flowing_lava, Blocks.sand, Blocks.snow_layer, Blocks.torch, Blocks.anvil, Blocks.jukebox, Blocks.stone_button, Blocks.wooden_button, Blocks.lever, Blocks.noteblock, Blocks.stone_pressure_plate, Blocks.light_weighted_pressure_plate, Blocks.wooden_pressure_plate, Blocks.heavy_weighted_pressure_plate, Blocks.stone_slab, Blocks.wooden_slab, Blocks.stone_slab2, Blocks.red_mushroom, Blocks.brown_mushroom, Blocks.yellow_flower, Blocks.red_flower, Blocks.anvil, Blocks.glass_pane, Blocks.stained_glass_pane, Blocks.iron_bars, Blocks.cactus, Blocks.ladder, Blocks.web, Blocks.gravel)

    val validBlocks: MutableList<Block>
        get() = mutableListOf(Blocks.air, Blocks.water, Blocks.flowing_water, Blocks.lava, Blocks.flowing_lava)

    val mostBlocksSlotFromHotbar: Int
        get() {
            var blockSlot = -1
            var blockCount = 0
            for (slot in 0..8) {
                val itemStack = mc.thePlayer.inventory.mainInventory[slot]
                if (itemStack != null) {
                    val stackSize = itemStack.stackSize
                    if (this.isValidItem(itemStack.item) && stackSize > blockCount) {
                        blockCount = stackSize
                        blockSlot = slot
                    }
                }
            }

            return blockSlot
        }

    val blocksCount: Int
        get() {
            var blocksCount = 0

            for (slot in 8 .. 44) {
                val itemStack = mc.thePlayer.inventoryContainer.getSlot(slot).stack
                if (itemStack != null) {
                    val stackSize = itemStack.stackSize
                    if (this.isValidItem(itemStack.item)) blocksCount += stackSize
                }
            }

            return blocksCount
        }

    val placeStopwatch = Stopwatch()
    val counterBarAnimation = Animation()

    @EventListener
    fun onMotion(event: MotionEvent) {
        val blockData = if (this.getBlockDataFromBlockPos(
                BlockPos(
                    mc.thePlayer.posX,
                    mc.thePlayer.posY - 1.0,
                    mc.thePlayer.posZ
                )
            ) != null) this.getBlockDataFromBlockPos(
            BlockPos(
                mc.thePlayer.posX,
                mc.thePlayer.posY - 1.0,
                mc.thePlayer.posZ
            )
        ) else this.getBlockDataFromBlockPos(
            BlockPos(
                mc.thePlayer.posX,
                mc.thePlayer.posY - 1.0,
                mc.thePlayer.posZ
            ).down()
        )
        if (this.mostBlocksSlotFromHotbar == -1) return
        when (event.state) {
            EventState.PRE -> {
                if (blockData == null) return
                if (!this.noSprintOption.value) mc.thePlayer.isSprinting = false
                if (this.towerOption.value) {
                    if (Keyboard.isKeyDown(mc.gameSettings.keyBindJump.keyCode) && PlayerUtil.underBlock !is BlockAir && PlayerUtil.isOnGround(2.0)) {
                        if (!this.towerMoveOption.value && MoveUtil.moving) {
                            mc.thePlayer.motionX = 0.0
                            mc.thePlayer.motionZ = 0.0
                        }
                        mc.thePlayer.motionY = 0.41982
                    }
                }

                val hitVec = BlockUtil.getVec3FromBlockData(blockData)

                val rotations = RotationUtil.getRotationsFromBlockPos(blockData.pos)
                event.yaw = rotations[0]
                event.pitch = rotations[1]
                if (this.rotationsViewableOption.value) {
                    mc.thePlayer.rotationYawHead = rotations[0]
                    mc.thePlayer.renderYawOffset = rotations[0]
                    mc.thePlayer.rotationPitchHead = rotations[1]
                }
                if (this.placeMethodOption.value == "Pre") {
                    if (!this.placeStopwatch.elapsed(this.placeDelayOption.value.toLong())) return
                    mc.thePlayer.inventory.currentItem = this.mostBlocksSlotFromHotbar
                    if (mc.playerController.onPlayerRightClick(
                            mc.thePlayer,
                            mc.theWorld,
                            mc.thePlayer.heldItem,
                            blockData.pos,
                            blockData.face,
                            hitVec
                        )
                    ) {
                        when (this.swingOption.value) {
                            true -> mc.thePlayer.swingItem()
                            false -> mc.netHandler.networkManager.sendPacketWithoutEvent(C0APacketAnimation())
                        }
                    }
                    this.placeStopwatch.reset()
                }
            }
            EventState.POST -> {
                val hitVec = BlockUtil.getVec3FromBlockData(blockData!!)
                if (this.placeMethodOption.value == "Post") {
                    if (!this.placeStopwatch.elapsed(this.placeDelayOption.value.toLong())) return
                    mc.thePlayer.inventory.currentItem = this.mostBlocksSlotFromHotbar
                    if (mc.playerController.onPlayerRightClick(
                            mc.thePlayer,
                            mc.theWorld,
                            mc.thePlayer.heldItem,
                            blockData.pos,
                            blockData.face,
                            hitVec
                        )
                    ) {
                        when (this.swingOption.value) {
                            true -> mc.thePlayer.swingItem()
                            false -> mc.netHandler.networkManager.sendPacketWithoutEvent(C0APacketAnimation())
                        }
                    }
                    this.placeStopwatch.reset()
                }
            }
        }
    }

    @EventListener
    fun onRender2D(event: Render2DEvent) {
        val width = event.scaledResolution.scaledWidth_double
        val height = event.scaledResolution.scaledHeight_double
        val SF18 = Main.fontBuffer.SF18
        val hud = (Main.moduleManager.getModuleByClass(HUDModule().javaClass) as HUDModule)

        if (!this.counterOption.value) return

        if (this.textCounterOption.value) {
            when (hud.fontOption.value) {
                true -> {
                    SF18.drawCenteredStringWithOutline(
                        this.blocksCount.toString(),
                        width / 2, height / 2 + 30, -1
                    )
                }
                false -> {
                    mc.fontRendererObj.drawCenteredStringWithOutline(
                        this.blocksCount.toString(),
                        width / 2, height / 2 + 30, -1
                    )
                }
            }
        }

        if (this.barCounterOption.value) {
            val maxBlocks = 64.0
            val blocksCount = this.blocksCount.toDouble()
            var percentage = blocksCount / maxBlocks
            percentage = 1.0.coerceAtMost(percentage)
            val barWidth = 100.0

            this.counterBarAnimation.update()

            RenderUtil.drawRect(
                width / 2 - barWidth / 2 - 0.5,
                height / 2 + 50 - 0.5,
                (width / 2 - barWidth / 2) + barWidth + 0.5,
                height / 2 + 50 + 10 + 0.5,
                Color.BLACK.rgb
            )

            this.counterBarAnimation.animate(percentage * barWidth, 0.3, Easings().EXPO_OUT, true)

            RenderUtil.drawGradientRect(
                width / 2 - barWidth / 2,
                height / 2 + 50,
                (width / 2 - barWidth / 2) + this.counterBarAnimation.value,
                height / 2 + 50 + 10,
                hud.fadeColourOption.value.rgb,
                hud.colourOption.value.rgb,
                true
            )
        }
    }

    private fun getBlockDataFromBlockPos(pos: BlockPos): BlockData? {
        return if (!this.validBlocks.contains(mc.theWorld.getBlockState(pos.add(0, -1, 0)).block)) {
            BlockData(pos.add(0, -1, 0), EnumFacing.UP)
        } else if (!this.validBlocks.contains(mc.theWorld.getBlockState(pos.add(-1, 0, 0)).block)) {
            BlockData(pos.add(-1, 0, 0), EnumFacing.EAST)
        } else if (!this.validBlocks.contains(mc.theWorld.getBlockState(pos.add(1, 0, 0)).block)) {
            BlockData(pos.add(1, 0, 0), EnumFacing.WEST)
        } else if (!this.validBlocks.contains(mc.theWorld.getBlockState(pos.add(0, 0, -1)).block)) {
            BlockData(pos.add(0, 0, -1), EnumFacing.SOUTH)
        } else if (!this.validBlocks.contains(mc.theWorld.getBlockState(pos.add(0, 0, 1)).block)) {
            BlockData(pos.add(0, 0, 1), EnumFacing.NORTH)
        } else {
            val add = pos.add(-1, 0, 0)
            if (!this.validBlocks.contains(mc.theWorld.getBlockState(add.add(-1, 0, 0)).block)) {
                BlockData(add.add(-1, 0, 0), EnumFacing.EAST)
            } else if (!this.validBlocks.contains(mc.theWorld.getBlockState(add.add(1, 0, 0)).block)) {
                BlockData(add.add(1, 0, 0), EnumFacing.WEST)
            } else if (!this.validBlocks.contains(mc.theWorld.getBlockState(add.add(0, 0, -1)).block)) {
                BlockData(add.add(0, 0, -1), EnumFacing.SOUTH)
            } else if (!this.validBlocks.contains(mc.theWorld.getBlockState(add.add(0, 0, 1)).block)) {
                BlockData(add.add(0, 0, 1), EnumFacing.NORTH)
            } else {
                val add2 = pos.add(1, 0, 0)
                if (!this.validBlocks.contains(mc.theWorld.getBlockState(add2.add(-1, 0, 0)).block)) {
                    BlockData(add2.add(-1, 0, 0), EnumFacing.EAST)
                } else if (!this.validBlocks.contains(mc.theWorld.getBlockState(add2.add(1, 0, 0)).block)) {
                    BlockData(add2.add(1, 0, 0), EnumFacing.WEST)
                } else if (!this.validBlocks.contains(mc.theWorld.getBlockState(add2.add(0, 0, -1)).block)) {
                    BlockData(add2.add(0, 0, -1), EnumFacing.SOUTH)
                } else if (!this.validBlocks.contains(mc.theWorld.getBlockState(add2.add(0, 0, 1)).block)) {
                    BlockData(add2.add(0, 0, 1), EnumFacing.NORTH)
                } else {
                    val add3 = pos.add(0, 0, -1)
                    if (!this.validBlocks.contains(mc.theWorld.getBlockState(add3.add(-1, 0, 0)).block)) {
                        BlockData(add3.add(-1, 0, 0), EnumFacing.EAST)
                    } else if (!this.validBlocks.contains(mc.theWorld.getBlockState(add3.add(1, 0, 0)).block)) {
                        BlockData(add3.add(1, 0, 0), EnumFacing.WEST)
                    } else if (!this.validBlocks.contains(mc.theWorld.getBlockState(add3.add(0, 0, -1)).block)) {
                        BlockData(add3.add(0, 0, -1), EnumFacing.SOUTH)
                    } else if (!this.validBlocks.contains(mc.theWorld.getBlockState(add3.add(0, 0, 1)).block)) {
                        BlockData(add3.add(0, 0, 1), EnumFacing.NORTH)
                    } else {
                        val add4 = pos.add(0, 0, 1)
                        if (!this.validBlocks.contains(mc.theWorld.getBlockState(add4.add(-1, 0, 0)).block)) {
                            BlockData(add4.add(-1, 0, 0), EnumFacing.EAST)
                        } else if (!this.validBlocks.contains(mc.theWorld.getBlockState(add4.add(1, 0, 0)).block)) {
                            BlockData(add4.add(1, 0, 0), EnumFacing.WEST)
                        } else if (!this.validBlocks.contains(mc.theWorld.getBlockState(add4.add(0, 0, -1)).block)) {
                            BlockData(add4.add(0, 0, -1), EnumFacing.SOUTH)
                        } else {
                            if (!this.validBlocks.contains(mc.theWorld.getBlockState(add4.add(0, 0, 1)).block)) BlockData(
                                add4.add(0, 0, 1),
                                EnumFacing.NORTH
                            ) else null
                        }
                    }
                }
            }
        }
    }

    private fun isValidItem(item: Item): Boolean {
        return if (item is ItemBlock) !this.invalidBlocks.contains(item.block) else false
    }

}