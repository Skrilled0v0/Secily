package me.kl0udy92.apart.features.module

import me.kl0udy92.apart.core.Main
import me.kl0udy92.apart.events.system.KeydownEvent
import me.kl0udy92.apart.features.lua.LuaManager
import net.ayataka.eventapi.EventManager
import net.ayataka.eventapi.annotation.EventListener
import net.ayataka.eventapi.enumeration.EventPriority
import org.reflections.Reflections

class ModuleManager {

    var modules = mutableListOf<Module>()

    init {
        Reflections("me.kl0udy92.${Main.name.lowercase()}.features.module.implementations").getSubTypesOf(Module::class.java)
            .forEach {
                runCatching {
                    this.modules.add(
                        it.getDeclaredConstructor().newInstance()
                    )
                }.onFailure { exception -> exception.printStackTrace() }
            }
        this.modules.forEach { it.optionsFinder() }
        EventManager.register(this)
    }

    @EventListener(EventPriority.HIGHEST)
    fun onKeydown(event: KeydownEvent) = this.modules.filter { it.keyCode == event.keyCode }.forEach { it.toggle() }

    fun getModuleByName(name: String): Module? {
        this.modules.forEach {
            if (it.name.equals(name, true)) return it
            if (it.aliases.isNotEmpty()) it.aliases.forEach { aliase ->
                if (aliase.equals(name, true)) return it
            }
        }
        return null
    }

    fun getModuleByClass(clazz: Class<*>) = this.modules.find { it.javaClass == clazz }

    fun getModulesInCategory(category: Category): MutableList<Module> {
        return this.modules.filter { it.category == category }.toMutableList()
    }

}