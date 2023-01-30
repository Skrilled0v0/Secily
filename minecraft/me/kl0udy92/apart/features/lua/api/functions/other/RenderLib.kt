package me.kl0udy92.apart.features.lua.api.functions.other

import me.cubex2.ttfr.CFontRenderer
import me.kl0udy92.apart.core.Main
import me.kl0udy92.apart.screen.font.FontType
import me.kl0udy92.apart.utils.render.RenderUtil
import net.minecraft.client.Minecraft
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.FiveArgFunction
import org.luaj.vm2.lib.FourArgFunction
import org.luaj.vm2.lib.SixArgFunction
import org.luaj.vm2.lib.ThreeArgFunction
import org.luaj.vm2.lib.TwoArgFunction

class RenderLib: TwoArgFunction() {

    override fun call(name: LuaValue, env: LuaValue): LuaValue {
        val library = tableOf()

        val drawRect = object : FiveArgFunction() {

            override fun call(
                x: LuaValue,
                y: LuaValue,
                width: LuaValue,
                height: LuaValue,
                hex: LuaValue
            ): LuaValue {

                RenderUtil.drawRect(
                    x.todouble(),
                    y.todouble(),
                    width.todouble(),
                    height.todouble(),
                    hex.toint()
                )

                return NIL
            }

        }

        val drawString = object : SixArgFunction() {

            override fun call(
                type: LuaValue,
                size: LuaValue,
                string: LuaValue,
                x: LuaValue,
                y: LuaValue,
                hex: LuaValue
            ): LuaValue {

                if (type.toString() == "Minecraft") {
                    Minecraft.getMinecraft().fontRendererObj.drawString(
                        string.toString(),
                        x.todouble(),
                        y.todouble(),
                        hex.toint()
                    )
                }else {
                    Main.fontBuffer.find(type, size).drawString(
                        string.toString(),
                        x.todouble(),
                        y.todouble(),
                        hex.toint()
                    )
                }

                return NIL
            }

        }

        val drawStringWithShadow = object : SixArgFunction() {

            override fun call(
                type: LuaValue,
                size: LuaValue,
                string: LuaValue,
                x: LuaValue,
                y: LuaValue,
                hex: LuaValue
            ): LuaValue {

                if (type.toString() == "Minecraft") {
                    Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(
                        string.toString(),
                        x.todouble(),
                        y.todouble(),
                        hex.toint()
                    )
                }else {
                    Main.fontBuffer.find(type, size).drawStringWithShadow(
                        string.toString(),
                        x.todouble(),
                        y.todouble(),
                        hex.toint()
                    )
                }

                return NIL
            }

        }

        val drawStringWithOutline = object : SixArgFunction() {

            override fun call(
                type: LuaValue,
                size: LuaValue,
                string: LuaValue,
                x: LuaValue,
                y: LuaValue,
                hex: LuaValue
            ): LuaValue {

                if (type.toString() == "Minecraft") {
                    Minecraft.getMinecraft().fontRendererObj.drawStringWithOutlineWithoutColorcode(
                        string.toString(),
                        x.todouble(),
                        y.todouble(),
                        hex.toint()
                    )
                }else {
                    Main.fontBuffer.find(type, size).drawStringWithOutlineWithoutColorcode(
                        string.toString(),
                        x.todouble(),
                        y.todouble(),
                        hex.toint()
                    )
                }

                return NIL
            }

        }

        val getStringWidth = object : ThreeArgFunction() {

            override fun call(
                type: LuaValue,
                size: LuaValue,
                string: LuaValue
            ): LuaValue {

                return if (type.toString() == "Minecraft")
                    LuaValue.valueOf(Minecraft.getMinecraft().fontRendererObj.getStringWidth(string.toString()))
                else
                    LuaValue.valueOf(Main.fontBuffer.find(type, size).getStringWidth(string.toString()))
            }

        }

        val getStringHeight = object : FourArgFunction() {

            override fun call(
                type: LuaValue,
                size: LuaValue,
                string: LuaValue,
                chinese: LuaValue
            ): LuaValue {

                return if (type.toString() == "Minecraft")
                    LuaValue.valueOf(Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT)
                else
                    LuaValue.valueOf(Main.fontBuffer.find(type, size).getHeight(chinese.toboolean()))
            }

        }

        library.set("drawRect", drawRect)
        library.set("drawString", drawString)
        library.set("drawStringWithShadow", drawStringWithShadow)
        library.set("drawStringWithOutline", drawStringWithOutline)
        library.set("getStringWidth", getStringWidth)
        library.set("getStringHeight", getStringHeight)

        env.set("render", library)
        if (!env.get("package").isnil()) env.get("package").get("loaded").set("render", library)

        return library
    }

}