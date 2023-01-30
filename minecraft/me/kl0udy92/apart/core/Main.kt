package me.kl0udy92.apart.core

import me.kl0udy92.apart.config.ConfigManager
import me.kl0udy92.apart.features.command.CommandManager
import me.kl0udy92.apart.features.friends.FriendManager
import me.kl0udy92.apart.features.lua.LuaManager
import me.kl0udy92.apart.features.module.ModuleManager
import me.kl0udy92.apart.features.module.implementations.visual.ClickGUIModule
import me.kl0udy92.apart.screen.clickgui.style.astolfo.ClickGui
import me.kl0udy92.apart.screen.clickgui.style.imgui.ImGui
import me.kl0udy92.apart.screen.font.FontBuffer
import me.kl0udy92.apart.screen.menu.account.AccountManager
import me.kl0udy92.apart.screen.notification.NotificationManager
import me.kl0udy92.apart.utils.MinecraftInstance
import net.minecraft.util.ChatComponentText
import org.lwjgl.opengl.Display
import java.nio.file.Paths

object Main : MinecraftInstance() {

    val name = "Apart"
    val author = { "Kl0udy92" }
    val version = "1.0"
    val directory = Paths.get(mc.mcDataDir.absolutePath, this.name)

    lateinit var notificationManager: NotificationManager
    lateinit var accountManager: AccountManager
    lateinit var commandManager: CommandManager
    lateinit var configManager: ConfigManager
    lateinit var moduleManager: ModuleManager
    lateinit var friendManager: FriendManager
    lateinit var luaManager: LuaManager
    lateinit var fontBuffer: FontBuffer
    lateinit var clickGui: ClickGui
    lateinit var imgui: ImGui

    fun init() {
        Display.setTitle(this.name)
        if (!directory.toFile().exists()) directory.toFile().mkdirs()
        this.accountManager = AccountManager()
        this.notificationManager = NotificationManager()
        this.fontBuffer = FontBuffer()
        this.friendManager = FriendManager()
        this.commandManager = CommandManager()
        this.moduleManager = ModuleManager()
        this.luaManager = LuaManager()
        this.clickGui = ClickGui()
        this.imgui = ImGui()
        this.configManager = ConfigManager()
        this.configManager.readAll()
        if (this.moduleManager.getModuleByClass(ClickGUIModule().javaClass)!!.state)
            this.moduleManager.getModuleByClass(ClickGUIModule().javaClass)!!.state = false
    }

    fun shutdown() {
        this.configManager.writeAll()
    }

    fun print(msg: Any, prefix: () -> Boolean) {
        mc.ingameGUI.chatGUI.printChatMessage(ChatComponentText(if (prefix.invoke()) "[§l§4${this.name}§r] §7$msg" else "$msg"))
    }

}