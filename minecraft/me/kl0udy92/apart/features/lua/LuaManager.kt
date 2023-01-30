package me.kl0udy92.apart.features.lua

import me.kl0udy92.apart.core.Main
import me.kl0udy92.apart.features.lua.api.VM
import me.kl0udy92.apart.features.module.Category
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.jse.CoerceJavaToLua
import java.nio.file.Paths


class LuaManager {

    val scripts = mutableListOf<LuaScript>()
    private val directory = Paths.get(Main.directory.toString(), "scripts")

    init {
        this.scripts.clear()
        if (!this.directory.toFile().exists()) this.directory.toFile().mkdirs()
        if (this.directory.toFile().listFiles()!!.isNotEmpty()) {
            this.directory.toFile().listFiles()!!.filter { it.name.endsWith(".lua") }.forEach {
                Main.moduleManager.modules.add(this.addAndGet(it.absolutePath))
            }
        }
    }

    fun addAndGet(input: String): LuaScript {

        val globals = VM.enchantedGlobals()

        globals.loadfile(input).invoke()

        val name = globals.get("module_name").invoke().toString()
        val chineseName = globals.get("chinese_name").invoke().toString()
        val namebreak = globals.get("name_break").invoke().toString()
        val description = globals.get("description").invoke().toString()
        val category_invoked = globals.get("category").invoke().toString()
        val category = Category.values().find { category_invoked == it.identifier }

        val lua = LuaScript(globals, name, chineseName, namebreak, description, category!!)

        val LuaParams = arrayOf<LuaValue>(
            CoerceJavaToLua.coerce(lua.options)
        )

        // FIXME: 2023/1/8 optionsµÄdependencyÊ§Ð§

        if (!globals.get("options").isnil()) globals.get("options").invoke(LuaValue.varargsOf(LuaParams));

        this.scripts.add(lua)

        return lua

    }

}