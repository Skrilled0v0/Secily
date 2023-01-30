package me.kl0udy92.apart.features.lua.api

import me.kl0udy92.apart.features.lua.api.functions.client.ModuleManagerLib
import me.kl0udy92.apart.features.lua.api.functions.other.ChatLib
import me.kl0udy92.apart.features.lua.api.functions.other.PlayerLib
import me.kl0udy92.apart.features.lua.api.functions.other.RenderLib
import org.luaj.vm2.Globals
import org.luaj.vm2.LoadState
import org.luaj.vm2.compiler.LuaC
import org.luaj.vm2.lib.Bit32Lib
import org.luaj.vm2.lib.CoroutineLib
import org.luaj.vm2.lib.PackageLib
import org.luaj.vm2.lib.TableLib
import org.luaj.vm2.lib.jse.*

object VM {

    fun enchantedGlobals(): Globals {
        val globals = Globals()
        globals.load(JseBaseLib())
        globals.load(PackageLib())
        globals.load(Bit32Lib())
        globals.load(TableLib())
        globals.load(JseStringLib())
        globals.load(CoroutineLib())
        globals.load(JseMathLib())
        globals.load(JseIoLib())
        globals.load(JseOsLib())
        globals.load(LuajavaLib())
        globals.load(RenderLib())
        globals.load(ChatLib())
        globals.load(PlayerLib())
        globals.load(ModuleManagerLib())
        LoadState.install(globals)
        LuaC.install(globals)
        return globals
    }

}