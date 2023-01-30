package me.kl0udy92.apart.features.module.implementations.attack

import me.kl0udy92.apart.core.Main
import me.kl0udy92.apart.events.entities.player.MotionEvent
import me.kl0udy92.apart.events.system.TickEvent
import me.kl0udy92.apart.features.module.Category
import me.kl0udy92.apart.features.module.Module
import me.kl0udy92.apart.features.module.annotation.ModuleData
import me.kl0udy92.apart.option.implementations.ArrayOption
import me.kl0udy92.apart.option.implementations.BooleanOption
import me.kl0udy92.apart.option.implementations.DoubleOption
import me.kl0udy92.apart.option.implementations.MultiBooleanOption
import me.kl0udy92.apart.screen.notification.Notification
import me.kl0udy92.apart.screen.notification.NotificationType
import me.kl0udy92.apart.utils.entity.EntitiesUtil
import me.kl0udy92.apart.utils.entity.player.RotationUtil
import me.kl0udy92.apart.utils.system.Stopwatch
import net.ayataka.eventapi.annotation.EventListener
import net.ayataka.eventapi.enumeration.EventState
import net.minecraft.client.settings.KeyBinding
import net.minecraft.entity.EntityLivingBase
import net.minecraft.item.ItemSword
import net.minecraft.network.play.client.C02PacketUseEntity
import net.minecraft.network.play.client.C07PacketPlayerDigging
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement
import net.minecraft.util.BlockPos
import net.minecraft.util.EnumFacing
import org.lwjgl.input.Keyboard
import java.util.concurrent.ThreadLocalRandom
import kotlin.streams.toList

@ModuleData(
    "KillAura",
    "杀戮光环",
    ["Aura", "ka"],
    "Kill Aura",
    Category.ATTACK,
    "Automatically attack surrounding entities.",
    Keyboard.KEY_R
)
class KillAuraModule : Module() {

    val modeOption = ArrayOption("Mode", "Switch attack mode.", "Switch", arrayOf("Single", "Switch", "Multi"))
    val attackMethodOption = ArrayOption("Attack Method", "Switch attack method.", "Post", arrayOf("Pre", "Post"))
    val sortModeOption =
        ArrayOption("Sort Mode", "Switch sorted mode.", "Health", arrayOf("Health", "Distance", "Hurt Time", "Armor"))
    val minApsOption = DoubleOption("Min APS", "Min attack per second.", 12.0, 1.0, 20.0, 0.1)
    val maxApsOption = DoubleOption("Max APS", "Max attack per second.", 12.0, 1.0, 20.0, 0.1)
    val throughWallOption = BooleanOption("Through Wall", "Through wall attack.", true)
    val distanceOption = DoubleOption("Distance", "Attack distance.", 4.0, 3.0, 6.0, 0.1)
    val maxTargetsOption = DoubleOption("Max Targets", "Multi max targets.", 10.0, 2.0, 10.0, 1.0) { this.modeOption.value == "Multi" }
    val switchDelayOption = DoubleOption(
        "Switch Delay",
        "Switch target delay when the mode is switch.",
        1500.0,
        0.0,
        3000.0,
        100.0
    ) { this.modeOption.value == "Switch" }

    val autoBlockOption = BooleanOption("Auto Block", "Automatic block when the target is not empty.", true)
    val autoBlockModeOption = ArrayOption("Auto Block Mode", "Block mode.", "Packet", arrayOf("Packet", "Key")) {
        this.autoBlockOption.value
    }

    val targetsOption = MultiBooleanOption("Targets", "Choose your targets.", arrayOf(
        BooleanOption("Players", "Attack players?", true),
        BooleanOption("Animals", "Attack animals?", false),
        BooleanOption("Mobs", "Attack mobs?", false),
        BooleanOption("Villagers", "Attack villagers?", true),
        BooleanOption("Invisibles", "Attack invisibles?", true),
        BooleanOption("Friends", "Attack friends?", true),
        BooleanOption("Teams", "Attack teams?", true)
    ))

    val autoDisableOption = BooleanOption("Auto Disable", "Automatically disable when u death.", true)
    val rotationsModeOption = ArrayOption("Rotations Mode", "Switch rotations mode.", "NCP", arrayOf("NCP", "Unknown", "Randomize"))
    val rotationsViewableOption = BooleanOption("Rotations Viewable", "Make rotations visible.", true)

    private val attackStopwatch = Stopwatch()
    private val switchStopwatch = Stopwatch()
    val target: EntityLivingBase?
        get() {
            if (this.targets.isEmpty()) return null
            return if (this.modeOption.value == "Single") this.targets[0] else this.targets[this.index]
        }
    var targets = mutableListOf<EntityLivingBase>()
    var index: Int = 0
    var blocking = false
        set(value) {
            if (value) {
                mc.playerController.syncCurrentPlayItem()
                when (autoBlockModeOption.value) {
                    "Packet" -> {
                        mc.netHandler.networkManager.sendPacketWithoutEvent(C08PacketPlayerBlockPlacement(mc.thePlayer.currentEquippedItem))
                        mc.thePlayer.itemInUseCount = mc.thePlayer.inventory.getCurrentItem().maxItemUseDuration
                    }
                    "Key" -> KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.keyCode, true)
                }
            }else {
                mc.playerController.syncCurrentPlayItem()
                when (autoBlockModeOption.value) {
                    "Packet" -> {
                        mc.netHandler.networkManager.sendPacketWithoutEvent(
                            C07PacketPlayerDigging(
                                C07PacketPlayerDigging.Action.RELEASE_USE_ITEM,
                                BlockPos.ORIGIN,
                                EnumFacing.DOWN
                            )
                        )
                        mc.thePlayer.itemInUseCount = 0
                    }
                    "Key" -> KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.keyCode, false)
                }
            }
            field = value
        }

    override fun onEnable() {
        this.targets.clear()
        this.index = 0
        super.onEnable()
    }

    override fun onDisable() {
        this.targets.clear()
        this.index = 0
        if (this.blocking) this.blocking = false
        super.onDisable()
    }

    @EventListener
    fun onTick(event: TickEvent) {
        this.apsComparator()
    }
    
    @EventListener
    fun onMotion(event: MotionEvent) {
        when (event.state) {
            EventState.PRE -> {
                this.suffix = "${this.modeOption.value} ${this.attackMethodOption.value}"
                if (mc.thePlayer.isDead && autoDisableOption.value) {
                    Main.notificationManager.notifications.add(
                        Notification(
                            "Aura Death", "Aura disabled due to death.",
                            NotificationType.WARNING
                        )
                    )
                    this.state = false
                }
                this.targets = if (this.modeOption.value == "Multi") EntitiesUtil.getEntityLivingBasesNearbyWithFilter(
                    this.distanceOption.value.toFloat(),
                    this.targetsOption.find("Players")!!.value,
                    this.targetsOption.find("Animals")!!.value,
                    this.targetsOption.find("Mobs")!!.value,
                    this.targetsOption.find("Villagers")!!.value,
                    this.targetsOption.find("Invisibles")!!.value,
                    this.targetsOption.find("Friends")!!.value,
                    this.targetsOption.find("Teams")!!.value,
                    this.throughWallOption.value
                ).take(this.maxTargetsOption.value.toInt()).toMutableList() else EntitiesUtil.getEntityLivingBasesNearbyWithFilter(
                    this.distanceOption.value.toFloat(),
                    this.targetsOption.find("Players")!!.value,
                    this.targetsOption.find("Animals")!!.value,
                    this.targetsOption.find("Mobs")!!.value,
                    this.targetsOption.find("Villagers")!!.value,
                    this.targetsOption.find("Invisibles")!!.value,
                    this.targetsOption.find("Friends")!!.value,
                    this.targetsOption.find("Teams")!!.value,
                    this.throughWallOption.value
                )
                this.sortTargets()
                this.changeTarget()

                if (this.target != null) {
                    if (this.attackMethodOption.value == "Pre") if (this.shouldAttack) {
                        if (this.modeOption.value != "Multi") this.attack(this.target!!)
                        else if (this.targets.isNotEmpty()) this.targets.forEach { this.attack(it) }
                    }
                    if (this.shouldBlock) this.blocking = true
                    val value = when (this.rotationsModeOption.value) {
                        "NCP" -> RotationUtil.getRotationsFromEntity(this.target!!)
                        "Unknown" -> RotationUtil.getRotations2(this.target!!)
                        else -> RotationUtil.getRotationsRandomize(this.target!!)
                    }
                    event.yaw = value[0]
                    event.pitch = value[1]
                    if (this.rotationsViewableOption.value) {
                        mc.thePlayer.renderYawOffset = value[0]
                        mc.thePlayer.rotationYawHead = value[0]
                        mc.thePlayer.rotationPitchHead = value[1]
                    }
                }else {
                    if (this.blocking) this.blocking = false
                }
            }
            EventState.POST -> {
                if (this.target != null) {
                    if (this.attackMethodOption.value == "Post") if (this.shouldAttack) {
                        if (this.modeOption.value != "Multi") this.attack(this.target!!)
                        else if (this.targets.isNotEmpty()) this.targets.forEach { this.attack(it) }
                    }
                    if (this.shouldBlock) this.blocking = true
                    val value = when (this.rotationsModeOption.value) {
                        "NCP" -> RotationUtil.getRotationsFromEntity(this.target!!)
                        "Unknown" -> RotationUtil.getRotations2(this.target!!)
                        else -> RotationUtil.getRotationsRandomize(this.target!!)
                    }
                    event.yaw = value[0]
                    event.pitch = value[1]
                    if (this.rotationsViewableOption.value) {
                        mc.thePlayer.renderYawOffset = value[0]
                        mc.thePlayer.rotationYawHead = value[0]
                        mc.thePlayer.rotationPitchHead = value[1]
                    }
                }else {
                    if (this.blocking) this.blocking = false
                }
            }
        }
    }

    private val shouldChange: Boolean
        get() {
            return this.switchStopwatch.elapsedWithReset(this.switchDelayOption.value.toLong())
        }

    private val shouldAttack: Boolean
        get() {
            val randomizeAps = ThreadLocalRandom.current().nextDouble(this.minApsOption.value, this.maxApsOption.value).toLong()
            return this.attackStopwatch.elapsedWithReset(1000L / randomizeAps)
        }

    private val shouldBlock: Boolean
        get() {
            return this.autoBlockOption.value && mc.thePlayer.heldItem != null && mc.thePlayer.heldItem.item is ItemSword && !this.blocking
        }

    private fun changeTarget() {
        if (this.targets.size >= this.index && this.shouldChange) {
            ++this.index
        }
        if (this.targets.size <= this.index) {
            this.index = 0
        }
    }

    private fun sortTargets() {
        when (sortModeOption.value) {
            "Health" -> this.targets.sortBy { it.health.toDouble() }
            "Distance" -> this.targets.sortBy { mc.thePlayer.getDistanceToEntity(it).toDouble() }
            "Hurt Time" -> this.targets.sortBy { it.hurtTime.toDouble() }
            "Armor" -> this.targets.sortBy { it.totalArmorValue.toDouble() }
        }
    }

    fun attack(entity: EntityLivingBase) {
        val critcals = (Main.moduleManager.getModuleByClass(CritcalsModule().javaClass) as CritcalsModule)
        if (critcals.state) critcals.crit(entity)
        mc.thePlayer.swingItem()
        mc.netHandler.networkManager.sendPacket(
            C02PacketUseEntity(
                entity,
                C02PacketUseEntity.Action.ATTACK
            )
        )
    }

    private fun apsComparator() {
        if (this.minApsOption.value >= this.maxApsOption.max) {
            this.maxApsOption.value = this.maxApsOption.max
            this.minApsOption.value = this.maxApsOption.value - this.maxApsOption.increment
        }
        if (this.minApsOption.value >= this.maxApsOption.value) this.maxApsOption.value = this.minApsOption.value + this.maxApsOption.increment
    }

}