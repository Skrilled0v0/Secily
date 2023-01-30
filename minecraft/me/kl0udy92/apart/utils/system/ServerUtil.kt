package me.kl0udy92.apart.utils.system

import me.kl0udy92.apart.screen.menu.GuiMainMenu
import me.kl0udy92.apart.utils.MinecraftInstance
import net.minecraft.client.gui.GuiMultiplayer
import net.minecraft.client.multiplayer.GuiConnecting
import net.minecraft.client.multiplayer.ServerData

object ServerUtil: MinecraftInstance() {

    lateinit var serverData: ServerData
    var connectedTime = 0L

    fun connectToLastServer() {
        mc.displayGuiScreen(GuiConnecting(GuiMultiplayer(GuiMainMenu()), mc, serverData))
    }

    fun getRemoteIp(): String {
        var serverIp: String = "Singleplayer"
        if (mc.theWorld.isRemote) {
            val serverData = mc.currentServerData
            if (serverData != null) serverIp = serverData.serverIP
        }
        return serverIp
    }

}