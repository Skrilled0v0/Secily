package me.kl0udy92.apart.features.lua.api.functions.other

import me.kl0udy92.apart.utils.entity.player.MoveUtil
import net.minecraft.client.Minecraft
import net.minecraft.client.settings.KeyBinding
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.OneArgFunction
import org.luaj.vm2.lib.TwoArgFunction
import org.luaj.vm2.lib.ZeroArgFunction

class PlayerLib: TwoArgFunction() {

    override fun call(name: LuaValue, env: LuaValue): LuaValue {
        val library = tableOf()

        val jump = object : ZeroArgFunction() {

            override fun call(): LuaValue {
                if (Minecraft.getMinecraft().theWorld != null && Minecraft.getMinecraft().thePlayer != null)
                    Minecraft.getMinecraft().thePlayer.jump()
                return NIL
            }

        }

        val moving = object : ZeroArgFunction() {

            override fun call(): LuaValue {
                return LuaValue.valueOf(MoveUtil.moving)
            }

        }

        val on_ground = object : ZeroArgFunction() {

            override fun call(): LuaValue {
                return if (Minecraft.getMinecraft().thePlayer != null)
                    LuaValue.valueOf(Minecraft.getMinecraft().thePlayer.onGround)
                else
                    NIL
            }

        }

        val health = object : ZeroArgFunction() {

            override fun call(): LuaValue {
                return if (Minecraft.getMinecraft().thePlayer != null)
                    LuaValue.valueOf(Minecraft.getMinecraft().thePlayer.health.toDouble())
                else
                    NIL
            }

        }

        val max_health = object : ZeroArgFunction() {

            override fun call(): LuaValue {
                return if (Minecraft.getMinecraft().thePlayer != null)
                    LuaValue.valueOf(Minecraft.getMinecraft().thePlayer.maxHealth.toDouble())
                else
                    NIL
            }

        }

        val armor = object : ZeroArgFunction() {

            override fun call(): LuaValue {
                return if (Minecraft.getMinecraft().thePlayer != null)
                    LuaValue.valueOf(Minecraft.getMinecraft().thePlayer.totalArmorValue)
                else
                    NIL
            }

        }

        val send_message = object : OneArgFunction() {

            override fun call(input: LuaValue): LuaValue {
                Minecraft.getMinecraft().thePlayer.sendChatMessage(input.toString())
                return NIL
            }

        }

        val fall_distance = object : OneArgFunction() {

            override fun call(distance: LuaValue): LuaValue {
                if (Minecraft.getMinecraft().thePlayer != null)
                    Minecraft.getMinecraft().thePlayer.fallDistance = distance.tofloat()
                return if (Minecraft.getMinecraft().thePlayer != null)
                    LuaValue.valueOf(Minecraft.getMinecraft().thePlayer.fallDistance.toDouble())
                else
                    NIL
            }

        }

        val name = object : ZeroArgFunction() {

            override fun call(): LuaValue {
                return if (Minecraft.getMinecraft().thePlayer != null)
                    LuaValue.valueOf(Minecraft.getMinecraft().thePlayer.name)
                else
                    NIL
            }

        }

        val hurt_time = object : ZeroArgFunction() {

            override fun call(): LuaValue {
                return if (Minecraft.getMinecraft().thePlayer != null)
                    LuaValue.valueOf(Minecraft.getMinecraft().thePlayer.hurtTime)
                else
                    NIL
            }

        }

        val food_level = object : ZeroArgFunction() {

            override fun call(): LuaValue {
                return if (Minecraft.getMinecraft().thePlayer != null)
                    LuaValue.valueOf(Minecraft.getMinecraft().thePlayer.foodStats.foodLevel)
                else
                    NIL
            }

        }

        val burning = object : ZeroArgFunction() {

            override fun call(): LuaValue {
                return if (Minecraft.getMinecraft().thePlayer != null)
                    LuaValue.valueOf(Minecraft.getMinecraft().thePlayer.isBurning)
                else
                    NIL
            }

        }

        val absorption = object : ZeroArgFunction() {

            override fun call(): LuaValue {
                return if (Minecraft.getMinecraft().thePlayer != null)
                    LuaValue.valueOf(Minecraft.getMinecraft().thePlayer.absorptionAmount.toDouble())
                else
                    NIL
            }

        }

        val left_click = object : ZeroArgFunction() {

            override fun call(): LuaValue {
                if (Minecraft.getMinecraft().thePlayer != null)
                    KeyBinding.setKeyBindState(
                        Minecraft.getMinecraft().gameSettings.keyBindAttack,
                        true
                    )
                return NIL
            }

        }

        val right_click = object : ZeroArgFunction() {

            override fun call(): LuaValue {
                if (Minecraft.getMinecraft().thePlayer != null)
                    KeyBinding.setKeyBindState(
                        Minecraft.getMinecraft().gameSettings.keyBindUseItem,
                        true
                    )
                return NIL
            }

        }

        val in_water = object : ZeroArgFunction() {

            override fun call(): LuaValue {
                return if (Minecraft.getMinecraft().thePlayer != null)
                    LuaValue.valueOf(Minecraft.getMinecraft().thePlayer.isInWater)
                else
                    NIL
            }

        }

        val in_lava = object : ZeroArgFunction() {

            override fun call(): LuaValue {
                return if (Minecraft.getMinecraft().thePlayer != null)
                    LuaValue.valueOf(Minecraft.getMinecraft().thePlayer.isInLava)
                else
                    NIL
            }

        }

        val sneaking = object : ZeroArgFunction() {

            override fun call(): LuaValue {
                return if (Minecraft.getMinecraft().thePlayer != null)
                    LuaValue.valueOf(Minecraft.getMinecraft().thePlayer.isSneaking)
                else
                    NIL
            }

        }

        val dead = object : ZeroArgFunction() {

            override fun call(): LuaValue {
                return if (Minecraft.getMinecraft().thePlayer != null)
                    LuaValue.valueOf(Minecraft.getMinecraft().thePlayer.isDead)
                else
                    NIL
            }

        }

        val sprinting = object : ZeroArgFunction() {

            override fun call(): LuaValue {
                return if (Minecraft.getMinecraft().thePlayer != null)
                    LuaValue.valueOf(Minecraft.getMinecraft().thePlayer.isSprinting)
                else
                    NIL
            }

        }

        val riding = object : ZeroArgFunction() {

            override fun call(): LuaValue {
                return if (Minecraft.getMinecraft().thePlayer != null)
                    LuaValue.valueOf(Minecraft.getMinecraft().thePlayer.isRiding)
                else
                    NIL
            }

        }

        val on_ladder = object : ZeroArgFunction() {

            override fun call(): LuaValue {
                return if (Minecraft.getMinecraft().thePlayer != null)
                    LuaValue.valueOf(Minecraft.getMinecraft().thePlayer.isOnLadder)
                else
                    NIL
            }

        }

        val collided_vertically = object : ZeroArgFunction() {

            override fun call(): LuaValue {
                return if (Minecraft.getMinecraft().thePlayer != null)
                    LuaValue.valueOf(Minecraft.getMinecraft().thePlayer.isCollidedVertically)
                else
                    NIL
            }

        }

        val collided_horizontally = object : ZeroArgFunction() {

            override fun call(): LuaValue {
                return if (Minecraft.getMinecraft().thePlayer != null)
                    LuaValue.valueOf(Minecraft.getMinecraft().thePlayer.isCollidedHorizontally)
                else
                    NIL
            }

        }

        val ticks_existed = object : ZeroArgFunction() {

            override fun call(): LuaValue {
                return if (Minecraft.getMinecraft().thePlayer != null)
                    LuaValue.valueOf(Minecraft.getMinecraft().thePlayer.ticksExisted)
                else
                    NIL
            }

        }

        val x = object : ZeroArgFunction() {

            override fun call(): LuaValue {
                return if (Minecraft.getMinecraft().thePlayer != null)
                    LuaValue.valueOf(Minecraft.getMinecraft().thePlayer.posX)
                else
                    NIL
            }

        }

        val y = object : ZeroArgFunction() {

            override fun call(): LuaValue {
                return if (Minecraft.getMinecraft().thePlayer != null)
                    LuaValue.valueOf(Minecraft.getMinecraft().thePlayer.posY)
                else
                    NIL
            }

        }

        val z = object : ZeroArgFunction() {

            override fun call(): LuaValue {
                return if (Minecraft.getMinecraft().thePlayer != null)
                    LuaValue.valueOf(Minecraft.getMinecraft().thePlayer.posZ)
                else
                    NIL
            }

        }

        val timer_speed = object : OneArgFunction() {

            override fun call(speed: LuaValue): LuaValue {
                Minecraft.getMinecraft().timer.timerSpeed = speed.tofloat()
                return LuaValue.valueOf(Minecraft.getMinecraft().timer.timerSpeed.toDouble())
            }

        }

        val yaw = object : OneArgFunction() {

            override fun call(yaw: LuaValue): LuaValue {
                Minecraft.getMinecraft().thePlayer.rotationYaw = yaw.tofloat()
                return LuaValue.valueOf(Minecraft.getMinecraft().thePlayer.rotationYaw.toDouble())
            }

        }

        val pitch = object : OneArgFunction() {

            override fun call(pitch: LuaValue): LuaValue {
                Minecraft.getMinecraft().thePlayer.rotationPitch = pitch.tofloat()
                return LuaValue.valueOf(Minecraft.getMinecraft().thePlayer.rotationPitch.toDouble())
            }

        }

        val motion_x = object : OneArgFunction() {

            override fun call(input: LuaValue): LuaValue {
                Minecraft.getMinecraft().thePlayer.motionX = input.todouble()
                return LuaValue.valueOf(Minecraft.getMinecraft().thePlayer.motionX)
            }

        }

        val motion_y = object : OneArgFunction() {

            override fun call(input: LuaValue): LuaValue {
                Minecraft.getMinecraft().thePlayer.motionY = input.todouble()
                return LuaValue.valueOf(Minecraft.getMinecraft().thePlayer.motionY)
            }

        }

        val motion_z = object : OneArgFunction() {

            override fun call(input: LuaValue): LuaValue {
                Minecraft.getMinecraft().thePlayer.motionZ = input.todouble()
                return LuaValue.valueOf(Minecraft.getMinecraft().thePlayer.motionZ)
            }

        }

        val item_in_use_count = object : OneArgFunction() {

            override fun call(input: LuaValue): LuaValue {
                Minecraft.getMinecraft().thePlayer.itemInUseCount = input.toint()
                return LuaValue.valueOf(Minecraft.getMinecraft().thePlayer.itemInUseCount)
            }

        }

        val eating = object : ZeroArgFunction() {

            override fun call(): LuaValue {
                return if (Minecraft.getMinecraft().thePlayer != null)
                    LuaValue.valueOf(Minecraft.getMinecraft().thePlayer.isEating)
                else
                    NIL
            }

        }

        library.set("jump", jump)
        library.set("moving", moving)
        library.set("on_ground", on_ground)
        library.set("health", health)
        library.set("max_health", max_health)
        library.set("armor", armor)
        library.set("send_message", send_message)
        library.set("fall_distance", fall_distance)
        library.set("name", name)
        library.set("hurt_time", hurt_time)
        library.set("food_level", food_level)
        library.set("burning", burning)
        library.set("absorption", absorption)
        library.set("left_click", left_click)
        library.set("right_click", right_click)
        library.set("in_water", in_water)
        library.set("in_lava", in_lava)
        library.set("sneaking", sneaking)
        library.set("dead", dead)
        library.set("sprinting", sprinting)
        library.set("riding", riding)
        library.set("on_ladder", on_ladder)
        library.set("collided_vertically", collided_vertically)
        library.set("collided_horizontally", collided_horizontally)
        library.set("ticks_existed", ticks_existed)
        library.set("x", x)
        library.set("y", y)
        library.set("y", z)
        library.set("timer_speed", timer_speed)
        library.set("yaw", yaw)
        library.set("pitch", pitch)
        library.set("motion_x", motion_x)
        library.set("motion_y", motion_y)
        library.set("motion_z", motion_z)
        library.set("item_in_use_count", item_in_use_count)
        library.set("eating", eating)

        env.set("player", library)
        if (!env.get("package").isnil()) env.get("package").get("loaded").set("player", library)

        return library
    }

}