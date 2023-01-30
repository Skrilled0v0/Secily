package me.kl0udy92.apart.screen.menu.account

import com.mojang.authlib.Agent
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication
import net.minecraft.client.Minecraft
import net.minecraft.util.EnumChatFormatting
import net.minecraft.util.Session
import java.net.Proxy

class AccountLoginThread(
    val username: String, val password: String, var status: String = "${EnumChatFormatting.GRAY}Static."
) : Thread("Account Login Thread") {

    private fun createSession(username: String, password: String): Session? {
        val service = YggdrasilAuthenticationService(Proxy.NO_PROXY, "")
        val auth = service.createUserAuthentication(Agent.MINECRAFT) as YggdrasilUserAuthentication
        auth.setUsername(username)
        auth.setPassword(password)
        kotlin.runCatching {
            auth.logIn()
            return Session(
                auth.selectedProfile.name,
                auth.selectedProfile.id.toString(),
                auth.authenticatedToken,
                "mojang"
            )
        }.onFailure { it.printStackTrace() }
        return null
    }

    override fun run() {
        if (password == "") {
            Minecraft.getMinecraft().session = Session(username.replace("&", "\u00a7"), "", "", "mojang")
            status = "${EnumChatFormatting.GREEN}Set username to $username"
            return
        }
        status = "${EnumChatFormatting.AQUA}Authenticating..."
        val auth = this.createSession(username, password)
        if (auth == null) {
            status = "${EnumChatFormatting.RED}Failed"
        } else {
            status = "${EnumChatFormatting.GREEN}Logged into ${auth.username}"
            Minecraft.getMinecraft().session = auth
        }
        super.run()
    }

}