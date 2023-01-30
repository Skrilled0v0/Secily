package me.kl0udy92.apart.features.lua.api.functions.other

import me.kl0udy92.apart.core.Main
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.TwoArgFunction

class ChatLib: TwoArgFunction() {

    override fun call(name: LuaValue, env: LuaValue): LuaValue {
        val library = tableOf()

        val print = object : TwoArgFunction() {

            override fun call(message: LuaValue, prefix: LuaValue): LuaValue {
                Main.print(message.toString()) { prefix.toboolean() }
                return NIL
            }

        }

        library.set("print", print)

        env.set("chat", library)
        if (!env.get("package").isnil()) env.get("package").get("loaded").set("chat", library)

        return library
    }

}