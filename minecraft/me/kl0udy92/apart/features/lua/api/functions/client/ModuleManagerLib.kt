package me.kl0udy92.apart.features.lua.api.functions.client

import me.kl0udy92.apart.core.Main
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.OneArgFunction
import org.luaj.vm2.lib.TwoArgFunction

/**
 * @author DeadAngels.
 * @since 2023/1/12
 **/
class ModuleManagerLib: TwoArgFunction() {

    override fun call(name: LuaValue, env: LuaValue): LuaValue {
        val library = tableOf()

        val get_state = object : OneArgFunction() {

            override fun call(name: LuaValue): LuaValue {
                return if (Main.moduleManager.getModuleByName(name.toString()) != null)
                    LuaValue.valueOf(Main.moduleManager.getModuleByName(name.toString())!!.state)
                else {
                    System.err.println("[Lua] Please check the toggle parameter 'module_name' in lua script exists!")
                    NIL
                }
            }

        }

        val toggle = object : OneArgFunction() {

            override fun call(name: LuaValue): LuaValue {

                if (Main.moduleManager.getModuleByName(name.toString()) != null)
                    Main.moduleManager.getModuleByName(name.toString())!!.toggle()
                else System.err.println("[Lua] Please check the toggle parameter 'module_name' in lua script exists!")

                return NIL
            }

        }

        library.set("get_state", get_state)
        library.set("toggle", toggle)

        env.set("module_manager", library)
        if (!env.get("package").isnil()) env.get("package").get("loaded").set("module_manager", library)

        return library
    }

}