package me.kl0udy92.apart.features.command

import me.kl0udy92.apart.core.Main
import me.kl0udy92.apart.events.network.PacketEvent
import net.ayataka.eventapi.EventManager
import net.ayataka.eventapi.annotation.EventListener
import net.ayataka.eventapi.enumeration.EventDirection
import net.minecraft.network.play.client.C01PacketChatMessage
import org.reflections.Reflections

class CommandManager {

    var commands = mutableListOf<AbstractCommand>()

    init {
        this.commands.clear()
        Reflections("me.kl0udy92.${Main.name.lowercase()}.features.command.implementations").getSubTypesOf(
            AbstractCommand::class.java
        ).forEach {
            runCatching {
                this.commands.add(
                    it.getDeclaredConstructor().newInstance()
                )
            }.onFailure { exception -> exception.printStackTrace() }
        }
        EventManager.register(this)
    }

    @EventListener
    fun onPacket(event: PacketEvent) {
        if (event.direction == EventDirection.OUTGOING) {
            if (event.packet is C01PacketChatMessage) {
                if (this.onExecute((event.packet as C01PacketChatMessage).message)) event.canceled = true
            }
        }
    }

    fun getCommandByName(name: String): AbstractCommand? {
        this.commands.forEach {
            if (it.name.equals(name, true)) return it
        }
        return null
    }

    fun getCommandByClass(clazz: Class<Any>) = this.commands.find { it.javaClass == clazz }

    fun onExecute(string: String): Boolean {
        if (string.length > 1 && string.startsWith("-")) {
            val args = string.trim { it <= ' ' }.substring(1).split(" ".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()
            for (abstractCommand in this.commands) {
                if (!args[0].equals(abstractCommand.name, ignoreCase = true)) continue
                abstractCommand.execute(args.copyOfRange(1, args.size))
                break
            }
            return true
        }
        return false
    }

}