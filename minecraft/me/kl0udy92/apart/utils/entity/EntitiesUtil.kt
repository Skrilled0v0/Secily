package me.kl0udy92.apart.utils.entity

import me.kl0udy92.apart.core.Main
import me.kl0udy92.apart.features.module.implementations.world.AntiBotsModule
import me.kl0udy92.apart.utils.MinecraftInstance
import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.item.EntityArmorStand
import net.minecraft.entity.monster.EntityMob
import net.minecraft.entity.monster.EntitySlime
import net.minecraft.entity.passive.EntityAnimal
import net.minecraft.entity.passive.EntitySquid
import net.minecraft.entity.passive.EntityVillager
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemArmor
import net.minecraft.item.ItemStack
import net.minecraft.util.DamageSource
import java.util.stream.Collectors
import kotlin.streams.toList

object EntitiesUtil : MinecraftInstance() {

    fun getEntitiesNearby(distance: Float): List<Entity> {
        return mc.theWorld.getLoadedEntityList().stream()
            .filter { entity -> mc.thePlayer.getDistanceToEntity(entity) <= distance && entity.isEntityAlive }
            .toList()
    }

    fun getEntityLivingBasesNearby(distance: Float): MutableList<EntityLivingBase> {
        val bases = mutableListOf<EntityLivingBase>()
        this.getEntitiesNearby(distance).filterIsInstance<EntityLivingBase>().forEach { bases.add(it) }
        return bases
    }

    fun getEntityLivingBasesNearbyWithFilter(
        distance: Float,
        players: Boolean,
        animals: Boolean,
        mobs: Boolean,
        villagers: Boolean,
        invisibles: Boolean,
        friends: Boolean,
        teams: Boolean,
        walls: Boolean
    ): MutableList<EntityLivingBase> {
        return this.getEntityLivingBasesNearby(distance)
            .filter { !this.filter(it, distance, players, animals, mobs, villagers, invisibles, friends, teams, walls) }
            .toMutableList()
    }

    fun filter(
        entityLivingBase: EntityLivingBase,
        distance: Float,
        players: Boolean,
        animals: Boolean,
        mobs: Boolean,
        villagers: Boolean,
        invisibles: Boolean,
        friends: Boolean,
        teams: Boolean,
        walls: Boolean
    ): Boolean {
        return entityLivingBase == mc.thePlayer
                || mc.thePlayer.getDistanceToEntity(entityLivingBase) > distance
                || Main.friendManager.friends.contains(Main.friendManager.getFriendByName(entityLivingBase.name)) && !friends
                || entityLivingBase.isDead
                || !entityLivingBase.canEntityBeSeen(mc.thePlayer) && !walls
                || entityLivingBase is EntityArmorStand
                || entityLivingBase is EntityPlayer && ((Main.moduleManager.getModuleByClass(AntiBotsModule().javaClass)) as AntiBotsModule).bots.contains(
            entityLivingBase
        ) && !players
                || entityLivingBase is EntityVillager && !villagers
                || entityLivingBase is EntityAnimal && !animals
                || entityLivingBase is EntitySquid && !animals
                || entityLivingBase is EntityMob && !mobs
                || entityLivingBase is EntitySlime && !mobs
                || this.isOnSameTeam(entityLivingBase) && !teams
                || entityLivingBase.isInvisible && !invisibles
    }

    fun isOnGround(entity: Entity, height: Double): Boolean {
        return mc.theWorld.getCollidingBoundingBoxes(entity, entity.entityBoundingBox.offset(0.0, -height, 0.0))
            .isNotEmpty()
    }

    fun isOnSameTeam(entity: EntityLivingBase): Boolean {
        return if (entity.team != null && mc.thePlayer.team != null) {
            entity.team == mc.thePlayer.team
        } else false
    }

    fun getArmorStrength(itemStack: ItemStack): Double {
        if (itemStack.item !is ItemArmor) return (-1).toDouble()
        var damageReduction = (itemStack.item as ItemArmor).damageReduceAmount.toFloat()
        val enchantments = EnchantmentHelper.getEnchantments(itemStack)
        if (enchantments.containsKey(Enchantment.protection.effectId)) {
            val level = enchantments[Enchantment.protection.effectId] as Int
            damageReduction += Enchantment.protection.calcModifierDamage(level, DamageSource.generic).toFloat()
        }
        return damageReduction.toDouble()
    }

}