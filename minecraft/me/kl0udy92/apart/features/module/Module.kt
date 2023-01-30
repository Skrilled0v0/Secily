package me.kl0udy92.apart.features.module

import me.kl0udy92.apart.config.implementations.ModuleConfig
import me.kl0udy92.apart.core.Main
import me.kl0udy92.apart.features.module.annotation.ModuleData
import me.kl0udy92.apart.features.module.implementations.visual.HUDModule
import me.kl0udy92.apart.option.Option
import me.kl0udy92.apart.option.implementations.*
import me.kl0udy92.apart.screen.notification.Notification
import me.kl0udy92.apart.screen.notification.NotificationType
import me.kl0udy92.apart.utils.MinecraftInstance
import me.kl0udy92.apart.utils.render.animations.easing.type.PosAnimation
import net.ayataka.eventapi.EventManager
import net.minecraft.client.Minecraft
import net.minecraft.util.EnumChatFormatting
import org.lwjgl.input.Keyboard

open class Module {

    val mc = Minecraft.getMinecraft()

    val name: String
    val chineseName: String
    val aliases: Array<String>
    val namebreak: String
    val description: String

    var suffix: Any? = ""
        set(value) {
            val string: String = value.toString()
            field = if (string.isEmpty()) string
            else "${EnumChatFormatting.GRAY}$string"
        }

    val category: Category

    var keyCode: Int

    var visible: Boolean

    val posAnimation: PosAnimation

    val options: MutableList<Option<*>>

    constructor() : super() {
        val moduleData = this.javaClass.getAnnotation(ModuleData::class.java)
        this.name = moduleData.name
        this.chineseName = moduleData.chineseName
        this.aliases = moduleData.aliases
        this.namebreak = moduleData.namebreak
        this.description = moduleData.description

        this.category = moduleData.category

        this.keyCode = moduleData.keyCode
        this.visible = moduleData.visible

        this.posAnimation = PosAnimation()

        this.options = mutableListOf()
    }

    constructor(name: String, chineseName: String, namebreak: String, description: String, category: Category) : super() {

        this.name = name
        this.chineseName = chineseName
        this.aliases = arrayOf("")
        this.namebreak = namebreak
        this.description = description
        this.category = category

        this.keyCode = Keyboard.KEY_NONE
        this.visible = true

        this.posAnimation = PosAnimation()

        this.options = mutableListOf()
    }

    var state = false
        set(value) {
            field = if (value) {
                this.onEnable()
                true
            } else {
                this.onDisable()
                false
            }
        }

    fun toggle() {
        state = !state
    }

    open fun onEnable() {
        Main.configManager.write(ModuleConfig())
        EventManager.register(this)
        if (this.visible && mc.thePlayer != null && mc.theWorld != null && (Main.moduleManager.getModuleByClass(
                HUDModule().javaClass
            ) as HUDModule).notificationsFilter.find("Toggle")!!.value) Main.notificationManager.notifications.add(
            Notification(
                "Module Toggled",
                "${this.name} was enabled.",
                NotificationType.SUCCESS
            )
        )
    }

    open fun onDisable() {
        Main.configManager.write(ModuleConfig())
        EventManager.unregister(this)
        if (this.visible && mc.thePlayer != null && mc.theWorld != null && (Main.moduleManager.getModuleByClass(
                HUDModule().javaClass
            ) as HUDModule).notificationsFilter.find("Toggle")!!.value) Main.notificationManager.notifications.add(
            Notification(
                "Module Toggled",
                "${this.name} was disabled.",
                NotificationType.ERROR
            )
        )
    }

    fun optionsFinder() {
        this.javaClass.declaredFields.filter {
            it.type.isAssignableFrom(Option::class.java) || it.type.isAssignableFrom(
                BooleanOption::class.java
            ) || it.type.isAssignableFrom(DoubleOption::class.java) || it.type.isAssignableFrom(ArrayOption::class.java) || it.type.isAssignableFrom(
                StringOption::class.java
            ) || it.type.isAssignableFrom(MultiBooleanOption::class.java) || it.type.isAssignableFrom(ColourOption::class.java)
        }.forEach {
            if (!it.isAccessible) it.isAccessible = true
            kotlin.runCatching { this.options.add(it.get(this) as Option<*>) }
                .onFailure { exception -> exception.printStackTrace() }
        }
    }

    fun getOption(name: String): Option<*>? {
        this.options.forEach {
            if (it.name.equals(name, true)) return it
        }
        return null
    }

}